package jp.ac.kcg.controller.screen.user

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.security.Principal

@Controller
class TopPageController {

    @GetMapping("/")
    fun showTopPage(principal: Principal?, model: Model): String {
        model.addAttribute("principal", principal)
        return "index"
    }
}
