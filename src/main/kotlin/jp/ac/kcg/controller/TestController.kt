package jp.ac.kcg.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class TestController {

    @RequestMapping("/")
    @ResponseBody
    fun home() = "home"
}
