package io.github.rxcats.datasourceroute.service

import io.github.rxcats.datasourceroute.Application
import io.github.rxcats.datasourceroute.TestData
import io.github.rxcats.datasourceroute.mapper.user.UserMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DuplicateKeyException

@SpringBootTest(classes = [Application::class])
class UserServiceTransactionTest {
    @Autowired
    private lateinit var mapper: UserMapper

    @Autowired
    private lateinit var service: UserService

    @BeforeEach
    fun before() {
        service.deleteAllUser(1)
    }

    @Test
    fun transactionCommitTest() {
        service.insertUserWithTransaction(1) {
            mapper.insert(TestData.user())
            mapper.insert(TestData.user2())
        }
    }

    @Test
    fun transactionRollbackTest() {
        Assertions.assertThrows(DuplicateKeyException::class.java) {
            service.insertUserWithTransaction(1) {
                mapper.insert(TestData.user())
                mapper.insert(TestData.user2())
                mapper.insertOnly(TestData.user())
            }
        }
    }

}