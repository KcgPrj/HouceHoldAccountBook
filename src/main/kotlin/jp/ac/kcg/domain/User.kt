package jp.ac.kcg.domain

import java.io.Serializable
import javax.persistence.*

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
        var userName: String = ""
): Serializable

/**
 * ユーザーのテーブル
 */
@Entity
@Table(indexes = arrayOf(Index(columnList = "screenName")))
data class User(
        @EmbeddedId
        @Column(updatable = false, nullable = false)
        var id: UserPK = UserPK(),
        /**
         * ユーザーの表示用の名前
         */
        @Column(updatable = true, nullable = false)
        var screenName: String = ""
)
