package jp.ac.kcg.config

import org.springframework.beans.factory.annotation.Autowired
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebMvcSecurity
open class SecurityConfig: WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        //ログイン画面は常にアクセス許可 それ以外は要検証
        http.authorizeRequests()
                .antMatchers("/login", "/signup", "/ico/favicon.ico")
                .permitAll()
                .anyRequest()
                .authenticated()
        //ログインの設定
        http.formLogin()
                .loginProcessingUrl("/login")
                .loginPage("/login")
                .failureUrl("/login?error")
                .defaultSuccessUrl("/")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
        //ログアウトの設定
        http.logout()
                .logoutRequestMatcher(AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .permitAll()
    }

    @Throws(Exception::class)
    override fun configure(web: WebSecurity?) {
        //静的リソースは常にアクセス許可
        web!!.ignoring()
                .antMatchers("/css/**", "/js/**", "/image/**", "/assets/**")
    }
}

@Configuration
open class AuthenticationConfiguration : GlobalAuthenticationConfigurerAdapter() {
    @Autowired
    lateinit var userDetailsService: UserDetailsService

    @Bean
    open fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(10)
    }

    @Throws(Exception::class)
    override fun init(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
    }
}
