package jp.ac.kcg.domain

import java.io.Serializable
import javax.persistence.*

@Embeddable
data class ItemCategoryPK(
        /**
         * カテゴリーを所有するユーザー
         */
        @ManyToOne
        @JoinColumns(
                JoinColumn(name = "clientId", updatable = false, insertable = false, nullable = false),
                JoinColumn(name = "userName", updatable = false, insertable = false, nullable = false)
        )
        var user: User = User(),
        /**
         * カテゴリーの名前
         */
        @Column(updatable = true, nullable = false)
        var categoryName: String = ""
): Serializable

@Entity
@Table(indexes = arrayOf(Index(columnList = "clientId,userName")))
data class ItemCategory(
        @EmbeddedId
        var key: ItemCategoryPK = ItemCategoryPK()
)
