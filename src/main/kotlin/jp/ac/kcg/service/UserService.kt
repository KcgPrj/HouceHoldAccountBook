package jp.ac.kcg.service

import jp.ac.kcg.domain.User
import org.springframework.stereotype.Service

@Service
interface UserService {

    fun findById(id: String): User?

    fun save(user: User)

    fun delete(user: User)
}
