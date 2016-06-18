package jp.ac.kcg.service

import jp.ac.kcg.HouseHoldAccountBookApplication
import jp.ac.kcg.domain.UserPK
import jp.ac.kcg.repository.UserRepository
import org.assertj.core.api.Assertions.*
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(HouseHoldAccountBookApplication::class))
@WebAppConfiguration
class UserServiceTests {

    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var repo: UserRepository


    @After
    fun after() {
        repo.deleteAll()
    }

    @Test(expected = UserAlreadyExistsException::class)
    fun testFindUserException() {
        try {
            userService.createUser("a", "a")
        } catch(e: UserAlreadyExistsException) {
            fail("ここで例外はおかしい")
        }
        //2回目で例外が発生
        userService.createUser("a", "a")
    }

    @Test
    fun testCreateUser() {
        val a = userService.findUser("clientId", "name")
        assertThat(a).isNull()

        val user = userService.createUser("clientId", "name")
        assertThat(user).isNotNull()
        assertThat(user.id).isNotNull()
        assertThat(user.id.clientId).isEqualTo("clientId")
        assertThat(user.id.userName).isEqualTo("name")
        assertThat(user.screenName).isEqualTo("name")

        val b = userService.findUser("clientId", "name")
        assertThat(b).isNotNull().isEqualTo(user)
    }

    @Test
    fun testFindUser() {
        val user1 = userService.createUser("clientId", "name")
        val user2 = userService.createUser("clientId2", "name2")

        assertThat(userService.findUser("clientId", "name")).isEqualTo(user1)
        assertThat(userService.findUser("clientId2", "name2")).isEqualTo(user2)

        assertThat(userService.findUser("clientId", "name")).isNotEqualTo(user2)
        assertThat(userService.findUser("clientId3", "name3")).isNull()
    }

    @Test
    fun testFindOrCreate() {
        assertThat(userService.findUser("clientId", "name")).isNull()
        var user = userService.findUserOrCreate("clientId", "name")
        assertThat(userService.findUser("clientId", "name")).isEqualTo(user)

        //もう一度呼び出しても例外がでないか
        user = userService.findUserOrCreate("clientId", "name")
        assertThat(userService.findUser("clientId", "name")).isEqualTo(user)
    }

    //ちゃんと名前が更新できるか
    @Test
    fun testUpdateName() {
        val user = userService.createUser("clientId", "name")
        assertThat(user.screenName).isEqualTo("name")

        val updatedUser = userService.updateScreenName("clientId", "name", "hoge")
        assertThat(updatedUser.screenName).isEqualTo("hoge")

        val user2 = userService.findUser("clientId", "name")
        assertThat(user2?.screenName).isEqualTo("hoge")
    }

    @Test(expected = UserNotFoundException::class)
    fun testUpdateNameException() {
        userService.updateScreenName("clientId", "name", "hoge")
    }

    @Test
    fun testSearchByScreenName() {
        userService.createUser("clientId", "hoge")
        userService.createUser("clientId", "fuga")
        //一応名前を更新したケースも用意
        userService.createUser("clientId", "hoger")
        userService.updateScreenName("clientId", "hoger", "hogera")

        val list1 = userService.searchUsersByScreenName("g")
        assertThat(list1.size).isEqualTo(3)

        val list2 = userService.searchUsersByScreenName("u")
        assertThat(list2.size).isEqualTo(1)
        assertThat(list2.first().screenName).isEqualTo("fuga")

        val list3 = userService.searchUsersByScreenName("hoge")
        assertThat(list3.size).isEqualTo(2)
        assertThat(list3.map { it.screenName }).contains("hoge")
        assertThat(list3.map { it.screenName }).contains("hogera")
    }

    @Test
    fun testCreateIfNotExist() {
        val user = userService.findUser("clientId", "name")
        assertThat(user).isNull()

        userService.createIfNotExist("clientId", "name")
        userService.updateScreenName("clientId", "name", "hoge")

        val user2 = userService.findUser("clientId", "name")
        assertThat(user2).isNotNull()
        assertThat(user2?.id).isEqualTo(UserPK("clientId", "name"))
        assertThat(user2?.screenName).isEqualTo("hoge")

        userService.createIfNotExist("clientId", "name")
        val user3 = userService.findUser("clientId", "name")
        assertThat(user3?.screenName).isEqualTo("hoge")
    }
}
