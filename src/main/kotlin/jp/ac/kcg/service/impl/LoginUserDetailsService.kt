package jp.ac.kcg.service.impl

import jp.ac.kcg.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class LoginUserDetailsService : UserDetailsService {
    @Autowired
    lateinit var userRepository: UserRepository

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        var user = userRepository.findOne(username) ?: throw UsernameNotFoundException("The requested user is not found.")
        return LoginUserDetails(user)

    }
}
