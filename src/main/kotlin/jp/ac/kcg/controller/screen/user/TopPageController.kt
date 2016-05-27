package jp.ac.kcg.controller.screen.user

import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class TopPageController {

    @GetMapping("/")
    fun showTopPage(principal: OAuth2Authentication?, model: Model): String {
        model.addAttribute("principal", principal)
        principal?.let {
            //TODO: 実際使う時のデモとしてコメントアウトしてある。 不要になったら消してね
            //service.findUserOrCreate(it.oAuth2Request.clientId, it.userAuthentication.name)
        }
        return "index"
    }
}
