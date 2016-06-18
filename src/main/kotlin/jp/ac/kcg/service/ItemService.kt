package jp.ac.kcg.service

import jp.ac.kcg.domain.Item
import jp.ac.kcg.domain.User
import jp.ac.kcg.domain.UserPK
import jp.ac.kcg.repository.ItemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
interface ItemService {

    /**
     * ユーザーのアイテムを年と月で絞り込みします。
     */
    fun search(user: UserPK, //検索するユーザー
               startMonth: LocalDate, //絞り込む年月日(含む)
               endMonth: LocalDate = startMonth //絞り込む年月日(含む)
    ): List<Item>
}

@Transactional(readOnly = true)
open class ItemServiceImpl: ItemService {

    @Autowired
    lateinit var itemRepository: ItemRepository

    override fun search(user: UserPK, startMonth: LocalDate, endMonth: LocalDate): List<Item> {
        if(startMonth.isAfter(endMonth)) {
            throw IllegalArgumentException("endMonth < startMonth")
        }

        return itemRepository.searchItems(User(user), startMonth, endMonth)
    }

}
