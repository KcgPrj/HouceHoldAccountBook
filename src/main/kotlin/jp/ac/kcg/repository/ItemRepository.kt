package jp.ac.kcg.repository

import jp.ac.kcg.domain.Item
import jp.ac.kcg.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface ItemRepository: JpaRepository<Item, Long> {

    @Query("select i from Item i where i.user = :user and :before <= i.receiptDate and i.receiptDate <= :after")
    fun searchItems(@Param("user") user: User, @Param("before") before: LocalDate, @Param("after") after: LocalDate): List<Item>
}
