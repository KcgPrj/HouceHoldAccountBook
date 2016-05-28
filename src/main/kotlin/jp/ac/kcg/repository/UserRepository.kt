package jp.ac.kcg.repository

import jp.ac.kcg.domain.User
import jp.ac.kcg.domain.UserPK
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Userのリポジトリ
 */
interface UserRepository: JpaRepository<User, UserPK> {

    /**
     * screenNameでLIKE検索
     */
    fun findByScreenNameContaining(screenName: String): List<User>
}
