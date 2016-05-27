package jp.ac.kcg.controller.screen.user

import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.security.Principal

@Controller
class TopPageController {

    @GetMapping("/")
    fun showTopPage(principal: Principal?, model: Model): String {
        model.addAttribute("principal", principal)
        (principal as? OAuth2Authentication)?.let {
            //TODO: 実際使う時のデモとしてコメントアウトしてある。 不要になったら消してね
            //service.findUserOrCreate(it.oAuth2Request.clientId, it.userAuthentication.name)
            println("hogehoge: " + it.oAuth2Request.clientId)
        }
        return "index"
    }
}
