package jp.ac.kcg.domain

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity

/**
 * ユーザーの主キー
 */
@Embeddable
data class UserPK (
        /**
         * ユーザーがログインしたクライアントのID
         */
        @Column(updatable = false, nullable = false)
        var clientId: String = "",
        /**
         * クライアントで使用されているユーザーのID
         */
        @Column(updatable = false, nullable = false)
        var name: String = ""
): Serializable

/**
 * ユーザーのテーブル
 */
@Entity
data class User(
        @EmbeddedId
        @Column(updatable = false, nullable = false)
        var id: UserPK = UserPK(),
        @Column(updatable = true, nullable = false)
        var screenName: String = ""
)
