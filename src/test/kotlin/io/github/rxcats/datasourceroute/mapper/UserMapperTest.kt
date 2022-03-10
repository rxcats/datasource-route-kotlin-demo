package io.github.rxcats.datasourceroute.mapper

import io.github.rxcats.datasourceroute.Application
import io.github.rxcats.datasourceroute.TestData
import io.github.rxcats.datasourceroute.mapper.user.UserMapper
import io.github.rxcats.datasourceroute.service.query.DbType
import io.github.rxcats.datasourceroute.service.query.QueryHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DuplicateKeyException

@SpringBootTest(classes = [Application::class])
class UserMapperTest {

    @Autowired
    private lateinit var userMapper: UserMapper

    @Autowired
    private lateinit var queryHelper: QueryHelper

    @BeforeEach
    fun setUp() {
        queryHelper.execute(db = DbType.USER, shardNo = 1) {
            userMapper.deleteAll()
        }

        queryHelper.execute(db = DbType.USER, shardNo = 2) {
            userMapper.deleteAll()
        }
    }

    @Test
    fun insert() {
        val r1 = queryHelper.execute(db = DbType.USER, shardNo = 1) {
            userMapper.insert(TestData.user())
        }
        assertThat(r1).isEqualTo(1)

        val r2 = queryHelper.execute(db = DbType.USER, shardNo = 2) {
            userMapper.insert(TestData.user())
        }
        assertThat(r2).isEqualTo(1)
    }

    @Test
    fun insertDuplicateKeyException() {
        Assertions.assertThrows(DuplicateKeyException::class.java) {
            queryHelper.execute(db = DbType.USER, shardNo = 1) {
                userMapper.insertOnly(TestData.user())

                userMapper.insertOnly(TestData.user())
            }
        }
    }

    @Test
    fun insertDuplicateKeyExceptionTryCatch() {
        try {
            queryHelper.execute(db = DbType.USER, shardNo = 1) {
                userMapper.insertOnly(TestData.user())
                userMapper.insertOnly(TestData.user())
            }
        } catch (e: DuplicateKeyException) {
            assertThat(e).hasMessageContaining("Duplicate entry")
        }

    }

    @Test
    fun selectOne() {
        queryHelper.execute(db = DbType.USER, shardNo = 1) {
            userMapper.insert(TestData.user())
        }

        queryHelper.execute(db = DbType.USER, shardNo = 2) {
            userMapper.insert(TestData.user())
        }

        val r1 = queryHelper.execute(db = DbType.USER, shardNo = 1) {
            userMapper.selectOne("1000001")
        }

        val r2 = queryHelper.execute(db = DbType.USER, shardNo = 2) {
            userMapper.selectOne("1000001")
        }

        assertThat(r1).isNotNull
        assertThat(r1?.userId).isEqualTo("1000001")

        assertThat(r2).isNotNull
        assertThat(r2?.userId).isEqualTo("1000001")
    }

    @Test
    fun delete() {
        queryHelper.execute(db = DbType.USER, shardNo = 1) {
            userMapper.insert(TestData.user())
            userMapper.delete("1000001")
        }

        queryHelper.execute(db = DbType.USER, shardNo = 2) {
            userMapper.insert(TestData.user())
            userMapper.delete("1000001")
        }

        val r1 = queryHelper.execute(db = DbType.USER, shardNo = 1) {
            userMapper.selectOne("1000001")
        }

        val r2 = queryHelper.execute(db = DbType.USER, shardNo = 2) {
            userMapper.selectOne("1000001")
        }

        assertThat(r1).isNull()
        assertThat(r2).isNull()
    }

}