package io.github.rxcats.datasourceroute.service

import io.github.rxcats.datasourceroute.Application
import io.github.rxcats.datasourceroute.TestData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [Application::class])
class UserServiceShardByShardNoTest {
    @Autowired
    private lateinit var service: UserService

    @BeforeEach
    fun before() {
        service.deleteAllUser(1)
        service.insertUser(1, TestData.user())

        service.deleteAllUser(2)
        service.insertUser(2, TestData.user2())
    }

    @Test
    fun shardTest() {
        val user1 = service.getUserByShardNo(1, "1000001")
        assertThat(user1).isNotNull

        val user2 = service.getUserByShardNo(2, "1000002")
        assertThat(user2).isNotNull
    }

}