package jp.ac.kcg.service.impl

import jp.ac.kcg.domain.User
import jp.ac.kcg.repository.UserRepository
import jp.ac.kcg.service.UserService
import org.springframework.beans.factory.annotation.Autowired

class UserServiceImpl: UserService {

    @Autowired
    lateinit var repo: UserRepository

    override fun save(user: User) {
        repo.save(user)
    }

    override fun delete(user: User) {
        repo.delete(user)
    }

    override fun findById(id: String) = repo.findOne(id)


}
