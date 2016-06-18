package jp.ac.kcg.service

import jp.ac.kcg.HouseHoldAccountBookApplication
import jp.ac.kcg.domain.*
import jp.ac.kcg.repository.ItemCategoryRepository
import jp.ac.kcg.repository.ItemRepository
import jp.ac.kcg.repository.UserRepository
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import java.time.LocalDate
import org.assertj.core.api.Assertions.*

@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(HouseHoldAccountBookApplication::class))
@WebAppConfiguration
class ItemServiceTests {

    @Autowired
    lateinit var itemService: ItemService
    @Autowired
    lateinit var userRepo: UserRepository
    @Autowired
    lateinit var itemRepo: ItemRepository
    @Autowired
    lateinit var categoryRepo: ItemCategoryRepository

    val user1 = User(UserPK("clientId", "user1"), "user1")
    val user2 = User(UserPK("clientId", "user2"), "user2")

    val categoryHoge = ItemCategory(ItemCategoryPK(user1, "hoge"))
    val categoryFuga = ItemCategory(ItemCategoryPK(user1, "fuga"))
    val categoryPiyo = ItemCategory(ItemCategoryPK(user1, "piyo"))
    val categoryHogera = ItemCategory(ItemCategoryPK(user1, "hogera"))

    @Before
    fun before() {
        userRepo.save(user1)
        userRepo.save(user2)

        categoryRepo.save(categoryHoge)
        categoryRepo.save(categoryFuga)
        categoryRepo.save(categoryPiyo)
        categoryRepo.save(categoryHogera)
    }

    @After
    fun after() {
        itemRepo.deleteAll()
        categoryRepo.deleteAll()
        userRepo.deleteAll()
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSearchException() {
        itemService.search(user1.id, LocalDate.of(2016, 5, 20), LocalDate.of(2016, 5, 19))
    }

    @Test
    fun testSearch() {

        val hoge = categoryRepo.findOne(categoryHoge.key)
        val user = userRepo.findOne(user1.id)
        //テスト用データ
        itemRepo.save(Item(receiptDate = LocalDate.of(2016, 5, 18), category = hoge, user = user, memo = "memo", receiptFrom = "hoge"))
        itemRepo.save(Item(receiptDate = LocalDate.of(2016, 5, 19), category = hoge, user = user, memo = "memo", receiptFrom = "hoge"))
        itemRepo.save(Item(receiptDate = LocalDate.of(2016, 5, 20), category = hoge, user = user, memo = "memo", receiptFrom = "hoge"))
        itemRepo.save(Item(receiptDate = LocalDate.of(2016, 6, 20), category = hoge, user = user, memo = "memo", receiptFrom = "hoge"))

        val search1 = itemService.search(user1.id, LocalDate.of(2016, 5, 20), LocalDate.of(2016, 5, 20))
        assertThat(search1.size).isEqualTo(1)

        val search2 = itemService.search(user1.id, LocalDate.of(2016, 5, 20))
        assertThat(search2.size).isEqualTo(1)
        assertThat(search1).isEqualTo(search2)

        val search3 = itemService.search(user1.id, LocalDate.of(2016, 5, 18), LocalDate.of(2016, 5, 20))
        assertThat(search3.size).isEqualTo(3)

        val search4 = itemService.search(user1.id, LocalDate.of(2016, 5, 19), LocalDate.of(2016, 5, 20))
        assertThat(search4.size).isEqualTo(2)

        val search5 = itemService.search(user1.id, LocalDate.of(2016, 5, 18), LocalDate.of(2016, 5, 19))
        assertThat(search5.size).isEqualTo(2)

    }
}
