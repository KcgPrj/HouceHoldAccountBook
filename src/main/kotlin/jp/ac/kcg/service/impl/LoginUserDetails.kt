package jp.ac.kcg.service.impl

import jp.ac.kcg.domain.User
import org.springframework.security.core.authority.AuthorityUtils

class LoginUserDetails(user: User) :
        org.springframework.security.core.userdetails.User(
                user.userName,
                user.password,
                AuthorityUtils.createAuthorityList("ROLE_USER"))

