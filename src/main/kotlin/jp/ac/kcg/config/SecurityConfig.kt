package jp.ac.kcg.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.OAuth2ClientContext
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.csrf.CsrfFilter
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.security.web.csrf.CsrfTokenRepository
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.filter.CompositeFilter
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.WebUtils
import java.util.*
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableOAuth2Client
open class SecurityConfig: WebSecurityConfigurerAdapter() {

    @Autowired
    internal lateinit var oauth2ClientContext: OAuth2ClientContext

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        //ログイン画面は常にアクセス許可 それ以外は要検証
        http.authorizeRequests()
                .antMatchers("/", "/login**", "/ico/favicon.ico", "/webjars**").permitAll()
                .anyRequest().authenticated()
                .and().logout().logoutSuccessUrl("/").permitAll()
                .and().csrf().csrfTokenRepository(csrfTokenRepository())
                .and().addFilterAfter(csrfHeaderFilter(), CsrfFilter::class.java)
                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter::class.java)
    }

    @Throws(Exception::class)
    override fun configure(web: WebSecurity?) {
        //静的リソースは常にアクセス許可
        web!!.ignoring()
                .antMatchers("/css/**", "/js/**", "/image/**", "/assets/**")
    }

    fun csrfHeaderFilter(): Filter = object : OncePerRequestFilter() {
        override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
            val csrf = request.getAttribute(CsrfToken::class.java.name) as CsrfToken?
            csrf?.let {
                var cookie: Cookie? = WebUtils.getCookie(request, "XSRF-TOKEN")
                val token: String? = it.token
                if (cookie == null || token != null && !token.equals(cookie.value)) {
                    cookie = Cookie("XSRF-TOKEN", token)
                    cookie.path = ("/")
                    response.addCookie(cookie)
                }
            }
            filterChain.doFilter(request, response)
        }
    }

    private fun csrfTokenRepository(): CsrfTokenRepository {
        val repository = HttpSessionCsrfTokenRepository()
        repository.setHeaderName("X-XSRF-TOKEN")
        return repository
    }

    private fun ssoFilter(): Filter {
        val filter = CompositeFilter();
        val filters = ArrayList<Filter>();

        filters.add(ssoFilter(facebook(), "/login/facebook"))
        filters.add(ssoFilter(github(), "/login/github"))
        filter.setFilters(filters)
        return filter
    }

    private fun ssoFilter(client: ClientResources, path: String): Filter {
        val filter = OAuth2ClientAuthenticationProcessingFilter(path)
        val template = OAuth2RestTemplate(client.client, oauth2ClientContext)
        filter.setRestTemplate(template)
        filter.setTokenServices(UserInfoTokenServices(client.resource.userInfoUri, client.client.clientId))
        return filter
    }


    @Bean
    @ConfigurationProperties("facebook")
    open internal fun facebook() = ClientResources()

    @Bean
    @ConfigurationProperties("github")
    open internal fun github() = ClientResources()


    @Bean
    open fun oauth2ClientFilterRegistration(filter: OAuth2ClientContextFilter): FilterRegistrationBean {
        val registration = FilterRegistrationBean()
        registration.filter = filter
        registration.order = -100
        return registration
    }

    internal class ClientResources {
        val client: AuthorizationCodeResourceDetails = AuthorizationCodeResourceDetails()
        val resource: ResourceServerProperties = ResourceServerProperties()
    }
}
