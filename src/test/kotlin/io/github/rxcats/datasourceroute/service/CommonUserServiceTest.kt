package io.github.rxcats.datasourceroute.service

import io.github.rxcats.datasourceroute.Application
import io.github.rxcats.datasourceroute.TestData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [Application::class])
class CommonUserServiceTest {

    @Autowired
    private lateinit var service: CommonUserService

    @BeforeEach
    fun before() {
        service.deleteAllUser()
        service.insertUser(TestData.commonUser())
    }

    @Test
    fun test() {
        val user = service.getUser("1000001")
        assertThat(user).isNotNull
    }

}