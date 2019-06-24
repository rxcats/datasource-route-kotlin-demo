package io.github.rxcats.datasourceroute.mapper

import io.github.rxcats.datasourceroute.DataSourceConfig
import io.github.rxcats.datasourceroute.TestData
import io.github.rxcats.datasourceroute.TxManager
import io.github.rxcats.datasourceroute.mapper.common.CommonUserMapper
import io.github.rxcats.datasourceroute.mapper.common.UserShardNoMapper
import io.github.rxcats.datasourceroute.service.DbType
import io.github.rxcats.datasourceroute.service.QueryHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest

@EnableAutoConfiguration
@SpringBootTest(classes = [DataSourceConfig::class, UserShardNoMapper::class, CommonUserMapper::class, QueryHelper::class, TxManager::class])
class UserShardNoMapperTest(
    @Autowired private val userShardNoMapper: UserShardNoMapper,
    @Autowired private val commonUserMapper: CommonUserMapper,
    @Autowired private val queryHelper: QueryHelper) {

    @BeforeEach
    fun before() {
        queryHelper.execute(db = DbType.COMMON, cb = { commonUserMapper.deleteAll() })
    }

    @Test
    fun selectOne() {
        queryHelper.execute(db = DbType.COMMON, cb = { commonUserMapper.insert(TestData.commonUser()) })

        val userShardNo = queryHelper.execute(DbType.COMMON, cb = { userShardNoMapper.selectOne("1000001") })

        assertThat(userShardNo).isNotNull
        assertThat(userShardNo?.shardNo).isEqualTo(TestData.commonUser().shardNo)
    }
}