package jp.ac.kcg.service

import jp.ac.kcg.domain.User
import jp.ac.kcg.domain.UserPK
import jp.ac.kcg.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * ユーザーの操作に関するサービス
 */
@Service
interface  UserService {

    /**
     * clientIdとnameに一致するユーザーをDBから取得する
     */
    fun findUser(clientId: String, name: String): User?

    /**
     * ユーザーを新規に作成します
     * @throws UserAlreadyExistsException すでにユーザーが存在した時
     */
    @Throws(UserAlreadyExistsException::class)
    fun createUser(clientId: String, name: String): User

    /**
     * clientIdとnameに一致するユーザーをDBから取得。存在しなければ新規作成する
     */
    fun findUserOrCreate(clientId: String, name: String): User

}

@Transactional(readOnly = true)
open class UserServiceImpl: UserService {

    @Autowired
    lateinit var userRepo: UserRepository

    @Transactional(readOnly = false)
    override fun createUser(clientId: String, name: String): User {
        if(userRepo.exists(UserPK(clientId, name))) {
            throw UserAlreadyExistsException()
        }
        return userRepo.save(User(UserPK(clientId, name), name))
    }

    override fun findUser(clientId: String, name: String): User? {
        return userRepo.findOne(UserPK(clientId, name))
    }

    @Transactional(readOnly = false)
    override fun findUserOrCreate(clientId: String, name: String): User {
        if(userRepo.exists(UserPK(clientId, name))) {
            userRepo.findOne(UserPK(clientId, name))
        }
        return userRepo.save(User(UserPK(clientId, name), name))
    }

}

/**
 * すでにユーザーが存在しているのに作成した時にthrowされる例外
 */
class UserAlreadyExistsException: RuntimeException()
