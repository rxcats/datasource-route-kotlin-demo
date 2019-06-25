package io.github.rxcats.datasourceroute.mapper

import io.github.rxcats.datasourceroute.DataSourceConfig
import io.github.rxcats.datasourceroute.TestData
import io.github.rxcats.datasourceroute.TxManager
import io.github.rxcats.datasourceroute.mapper.user.UserMapper
import io.github.rxcats.datasourceroute.service.DbType
import io.github.rxcats.datasourceroute.service.QueryHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.dao.DuplicateKeyException

@EnableAutoConfiguration
@Import(DataSourceConfig::class)
@SpringBootTest(classes = [UserMapper::class, QueryHelper::class, TxManager::class])
class UserMapperTest(
    @Autowired private val userMapper: UserMapper,
    @Autowired private val queryHelper: QueryHelper) {

    @BeforeEach
    fun setUp() {
        queryHelper.execute(db = DbType.USER, shardNo = 0, cb = { userMapper.deleteAll() })
        queryHelper.execute(db = DbType.USER, shardNo = 1, cb = { userMapper.deleteAll() })
    }

    @Test
    fun insert() {
        val r0 = queryHelper.execute(db = DbType.USER, shardNo = 0, cb = { userMapper.insert(TestData.user()) })
        val r1 = queryHelper.execute(db = DbType.USER, shardNo = 1, cb = { userMapper.insert(TestData.user()) })

        assertThat(r0).isEqualTo(1)
        assertThat(r1).isEqualTo(1)
    }

    @Test
    fun insertDuplicateKeyException() {
        Assertions.assertThrows(DuplicateKeyException::class.java) {
            queryHelper.execute(db = DbType.USER, shardNo = 0, cb = { userMapper.insertOnly(TestData.user()) })
            queryHelper.execute(db = DbType.USER, shardNo = 0, cb = { userMapper.insertOnly(TestData.user()) })
        }
    }

    @Test
    fun insertDuplicateKeyExceptionTryCatch() {
        try {
            queryHelper.execute(db = DbType.USER, shardNo = 0, cb = { userMapper.insertOnly(TestData.user()) })
            queryHelper.execute(db = DbType.USER, shardNo = 0, cb = { userMapper.insertOnly(TestData.user()) })
        } catch (e: DuplicateKeyException) {
            assertThat(e).hasMessageContaining("Duplicate entry")
        }

    }

    @Test
    fun selectOne() {
        queryHelper.execute(db = DbType.USER, shardNo = 0, cb = { userMapper.insert(TestData.user()) })
        queryHelper.execute(db = DbType.USER, shardNo = 1, cb = { userMapper.insert(TestData.user()) })

        val r0 = queryHelper.execute(db = DbType.USER, shardNo = 0, cb = { userMapper.selectOne("1000001") })
        val r1 = queryHelper.execute(db = DbType.USER, shardNo = 1, cb = { userMapper.selectOne("1000001") })

        assertThat(r0).isNotNull
        assertThat(r0?.userId).isEqualTo("1000001")

        assertThat(r1).isNotNull
        assertThat(r1?.userId).isEqualTo("1000001")
    }

    @Test
    fun delete() {
        queryHelper.execute(db = DbType.USER, shardNo = 0, cb = { userMapper.insert(TestData.user()) })
        queryHelper.execute(db = DbType.USER, shardNo = 1, cb = { userMapper.insert(TestData.user()) })

        queryHelper.execute(db = DbType.USER, shardNo = 0, cb = { userMapper.delete("1000001") })
        queryHelper.execute(db = DbType.USER, shardNo = 1, cb = { userMapper.delete("1000001") })

        val r0 = queryHelper.execute(db = DbType.USER, shardNo = 0, cb = { userMapper.selectOne("1000001") })
        val r1 = queryHelper.execute(db = DbType.USER, shardNo = 1, cb = { userMapper.selectOne("1000001") })

        assertThat(r0).isNull()
        assertThat(r1).isNull()
    }

}