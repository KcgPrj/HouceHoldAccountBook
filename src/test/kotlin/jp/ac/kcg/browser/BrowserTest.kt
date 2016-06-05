package jp.ac.kcg.browser

import com.codeborne.selenide.Selenide.*;
import com.codeborne.selenide.Condition.*;
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.WebDriverRunner
import jp.ac.kcg.HouseHoldAccountBookApplication
import org.junit.*
import org.junit.runner.RunWith
import org.openqa.selenium.By
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BrowserTest {

    @LocalServerPort
    var port: Int = -1

    @Before
    fun before() {
        Configuration.baseUrl = "http://localhost:$port/"
    }

    @After
    fun after() {
        close()
    }

    @Test
    fun testLogin() {
        navigator.open("/")
        Selenide.screenshot("TopPage")

        `$`(By.linkText("facebook")).doubleClick()
        Selenide.screenshot("login_facebook")
        navigator.open("/")

        `$`(By.linkText("google")).doubleClick()
        Selenide.screenshot("login_google")
        navigator.open("/")

        `$`(By.linkText("yahoo")).doubleClick()
        Selenide.screenshot("login_yahoo")
        navigator.open("/")
    }

    companion object {

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            Configuration.browser = WebDriverRunner.FIREFOX
        }
    }
}
