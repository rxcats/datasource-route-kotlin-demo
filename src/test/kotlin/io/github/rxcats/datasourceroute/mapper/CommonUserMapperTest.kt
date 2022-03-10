package io.github.rxcats.datasourceroute.mapper

import io.github.rxcats.datasourceroute.Application
import io.github.rxcats.datasourceroute.TestData
import io.github.rxcats.datasourceroute.mapper.common.CommonUserMapper
import io.github.rxcats.datasourceroute.service.query.DbType
import io.github.rxcats.datasourceroute.service.query.QueryHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.BadSqlGrammarException
import javax.annotation.Resource

@SpringBootTest(classes = [Application::class])
class CommonUserMapperTest {

    @Resource
    private lateinit var commonUserMapper: CommonUserMapper

    @Autowired
    private lateinit var queryHelper: QueryHelper

    @BeforeEach
    fun before() {
        queryHelper.execute(DbType.COMMON) {
            commonUserMapper.deleteAll()
        }
    }

    @Test
    fun crud() {
        queryHelper.execute(DbType.COMMON) {
            val insertCnt = commonUserMapper.insert(TestData.commonUser())
            assertThat(insertCnt).isEqualTo(1)

            val select = commonUserMapper.selectOne("1000001")
            assertThat(select).isNotNull
            assertThat(select.nickname).isEqualTo("Guest1000001")

            val deleteCnt = commonUserMapper.delete("1000001")
            assertThat(deleteCnt).isEqualTo(1)
        }
    }

    @Test
    fun txInsert() {
        queryHelper.executeTx(DbType.COMMON) {
            val insertCnt = commonUserMapper.insert(TestData.commonUser())
            assertThat(insertCnt).isEqualTo(1)

            val user = commonUserMapper.selectOne("1000001")
            assertThat(user).isNotNull
        }
    }

    @Test
    fun invalidInsertSql() {
        Assertions.assertThrows(BadSqlGrammarException::class.java) {
            queryHelper.execute(DbType.COMMON) {
                commonUserMapper.errorInsert(TestData.commonUser())
            }
        }
    }
}