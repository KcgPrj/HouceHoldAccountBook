package jp.ac.kcg

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(HouseHoldAccountBookApplication::class))
@WebAppConfiguration
class HouseHoldAccountBookApplicationTests {

    @Autowired
    lateinit var context: WebApplicationContext

    lateinit var mock: MockMvc

    @Before
    fun setup() {
        mock = MockMvcBuilders
                .webAppContextSetup(context)
                .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
                .build()
    }

    @Test
    @WithMockUser(username = "test")
    fun topPageTest() {
        mock.perform(MockMvcRequestBuilders.get("/").with(csrf()))
                .andExpect(status().isOk)
                .andExpect(model().attributeExists("principal"))
    }

    @Test
    fun topPageTest2() {
        mock.perform(MockMvcRequestBuilders.get("/").with(csrf()))
                .andExpect(status().isOk)
                .andExpect(model().attributeDoesNotExist("principal"))
    }
}
