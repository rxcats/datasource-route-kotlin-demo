package io.github.rxcats.datasourceroute.mapper

import io.github.rxcats.datasourceroute.DataSourceConfig
import io.github.rxcats.datasourceroute.TestData
import io.github.rxcats.datasourceroute.TxManager
import io.github.rxcats.datasourceroute.mapper.common.CommonUserMapper
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
import org.springframework.jdbc.BadSqlGrammarException

@EnableAutoConfiguration
@Import(DataSourceConfig::class)
@SpringBootTest(classes = [CommonUserMapper::class, QueryHelper::class, TxManager::class])
class CommonUserMapperTest(
    @Autowired private val commonUserMapper: CommonUserMapper,
    @Autowired private val queryHelper: QueryHelper) {

    @BeforeEach
    fun before() {
        queryHelper.execute(db = DbType.COMMON, cb = { commonUserMapper.deleteAll() })
    }

    @Test
    fun crud() {
        val insertCnt = queryHelper.execute(db = DbType.COMMON, cb = { commonUserMapper.insert(TestData.commonUser()) })
        assertThat(insertCnt).isEqualTo(1)

        val select = queryHelper.execute(db = DbType.COMMON, cb = { commonUserMapper.selectOne("1000001") })
        assertThat(select).isNotNull
        assertThat(select?.nickname).isEqualTo("Guest1000001")

        val deleteCnt = queryHelper.execute(db = DbType.COMMON, cb = { commonUserMapper.delete("1000001") })
        assertThat(deleteCnt).isEqualTo(1)
    }

    @Test
    fun txInsert() {
        val insertCnt = queryHelper.executeTx(db = DbType.COMMON, cb = { commonUserMapper.insert(TestData.commonUser()) })
        assertThat(insertCnt).isEqualTo(1)

        val user = queryHelper.execute(db = DbType.COMMON, cb = { commonUserMapper.selectOne("1000001") })
        assertThat(user).isNotNull
    }

    @Test
    fun invalidInsertSql() {
        Assertions.assertThrows(BadSqlGrammarException::class.java) {
            queryHelper.execute(db = DbType.COMMON, cb = {
                commonUserMapper.errorInsert(TestData.commonUser())
            })
        }
    }

}