package jp.ac.kcg.service

import jp.ac.kcg.HouseHoldAccountBookApplication
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
        assertThat(user.id.name).isEqualTo("name")
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
}
