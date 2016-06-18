package jp.ac.kcg.config

import jp.ac.kcg.service.ItemServiceImpl
import jp.ac.kcg.service.UserServiceImpl
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer
import org.springframework.boot.web.servlet.ErrorPage
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus

@Configuration
open class BeanConfig {

    @Bean
    open fun containerCustomizer(): EmbeddedServletContainerCustomizer {
        return EmbeddedServletContainerCustomizer { container ->
            container.addErrorPages(ErrorPage(HttpStatus.FORBIDDEN, "/error/403.html"))
            container.addErrorPages(ErrorPage(HttpStatus.NOT_FOUND, "/error/404.html"))
            container.addErrorPages(ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500.html"))
        }
    }

    @Bean
    open fun userService() = UserServiceImpl()

    @Bean
    open fun itemService() = ItemServiceImpl()
}
