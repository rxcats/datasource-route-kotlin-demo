package io.github.rxcats.datasourceroute.service

import io.github.rxcats.datasourceroute.Application
import io.github.rxcats.datasourceroute.TestData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [Application::class])
class UserServiceShardByUserIdTest {
    @Autowired
    private lateinit var commonUserService: CommonUserService

    @Autowired
    private lateinit var service: UserService

    @Test
    fun shardTest() {
        TestData.commonUser()
        commonUserService.deleteAllUser()
        commonUserService.insertUser(TestData.commonUser())
        commonUserService.insertUser(TestData.commonUser2())

        service.insertUser(1, TestData.user())
        service.insertUser(2, TestData.user2())

        val user1 = service.getUser("1000001")
        assertThat(user1).isNotNull

        val user2 = service.getUser("1000002")
        assertThat(user2).isNotNull
    }

}