package jp.ac.kcg.controller.api.user

import jp.ac.kcg.domain.User
import jp.ac.kcg.service.UserNotFoundException
import jp.ac.kcg.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class UserRestController {

    @Autowired
    lateinit var userService: UserService

    /**
     * 認証されたユーザーの情報を返す
     * @throws InvalidAuthorizationInformationException 認証情報がOAuth2.0以外だった時
     * @throws UserNotFoundException ユーザーがヒットしなかった時
     */
    @GetMapping("/api/user/me")
    fun showUser(principal: Principal): UserModel{
        val auth = (principal as? OAuth2Authentication) ?: throw InvalidAuthorizationInformationException()
        val user = userService.findUser(auth.oAuth2Request.clientId, auth.userAuthentication.name) ?: throw UserNotFoundException()

        return UserModel(user.id.clientId, user.id.userName, user.screenName)
    }

    /**
     * clientIdとnameからユーザーを検索する。存在しない場合はnull
     */
    @GetMapping("/api/user/")
    fun findUser(@RequestParam("clientId") clientId: String, @RequestParam("name") name: String): UserModel? {
        val user = userService.findUser(clientId, name) ?: return null
        return UserModel(user.id.clientId, user.id.userName, user.screenName)
    }

    /**
     * 指定の文字列をスクリーンネームに含むユーザーのリストを検索する
     */
    @GetMapping("/api/user/search")
    fun findUserByScreenName(@RequestParam("query") query: String) =
            userService.searchUsersByScreenName(query).map {
                UserModel(it.id.clientId, it.id.userName, it.screenName)
            }

    /**
     * 自身の表示名を更新します
     */
    @PostMapping("/api/user/update_name")
    fun updateScreenName(@RequestParam("name") name: String, principal: Principal): User {
        val auth = (principal as? OAuth2Authentication) ?: throw InvalidAuthorizationInformationException()
        return userService.updateScreenName(auth.oAuth2Request.clientId, auth.userAuthentication.name, name)
    }
}

/**
 * 不正な認証情報の時にスローされる例外
 */
class InvalidAuthorizationInformationException: RuntimeException()
