package jp.ac.kcg.domain

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(indexes = arrayOf(Index(columnList = "id")))
data class Item(
        /**
         * Itemのサロゲートキー
         */
        @Id
        @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
        var id: Long = 0,
        /**
         * 支払いが発生した日付
         */
        @Column(updatable = true, nullable = false)
        var receiptDate: LocalDate = LocalDate.MIN,
        /**
         * 支払元
         */
        @Column(updatable = true, nullable = false)
        var receiptFrom: String = "",
        /**
         * 支払いについてのメモ
         */
        @Column(updatable = true, nullable = false)
        var memo: String = "",
        /**
         * カテゴリー
         */
        @ManyToOne
        @JoinColumns(
                JoinColumn(name = "clientId", updatable = false, insertable = false, nullable = false, referencedColumnName = "clientId"),
                JoinColumn(name = "userName", updatable = false, insertable = false, nullable = false, referencedColumnName = "userName"),
                JoinColumn(name = "categoryName", updatable = false, insertable = false, nullable = false, referencedColumnName = "categoryName")
        )
        var category: ItemCategory = ItemCategory(),
        /**
         * アイテムを所有するユーザー
         */
        @ManyToOne
        @JoinColumns(
                JoinColumn(name = "clientId", updatable = false, insertable = false, nullable = false, referencedColumnName = "clientId"),
                JoinColumn(name = "userName", updatable = false, insertable = false, nullable = false, referencedColumnName = "userName")
        )
        var user: User = User()
)
