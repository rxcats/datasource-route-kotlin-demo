package io.github.rxcats.datasourceroute.dynamicsql

import io.github.rxcats.datasourceroute.Application
import io.github.rxcats.datasourceroute.dynamicsql.CommonUserDynamicSqlSupport.nickname
import io.github.rxcats.datasourceroute.dynamicsql.CommonUserDynamicSqlSupport.userId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest(classes = [Application::class])
class CommonUserDynamicSqlTest {

    @Autowired
    private lateinit var mapper: CommonUserDynamicSqlMapper

    @BeforeEach
    fun before() {
        mapper.deleteAll()
    }

    private fun prepareData(): CommonUserRow {
        val data = CommonUserRow(
            userId = "1000003",
            nickname = "1000003",
            shardNo = 1,
            createdAt = LocalDateTime.now()
        )
        mapper.insert(data)
        return data
    }

    @Test
    fun insertTest() {
        val data = CommonUserRow(
            userId = "1000003",
            nickname = "1000003",
            shardNo = 1,
            createdAt = LocalDateTime.now()
        )
        val row = mapper.insert(data)
        assertThat(row).isEqualTo(1)
    }

    @Test
    fun selectOneTest() {
        prepareData()
        val data = mapper.selectOne {
            where {
                userId isEqualTo "1000003"
            }
        }
        assertThat(data).isNotNull
    }

    @Test
    fun selectOneTargetFieldsTest() {
        prepareData()
        val data = mapper.selectOne(userId, nickname) {
            where {
                userId isEqualTo "1000003"
            }
        }
        assertThat(data).isNotNull
        assertThat(data?.shardNo).isNull()
        assertThat(data?.createdAt).isNull()
    }

    @Test
    fun selectListTest() {
        prepareData()
        val list = mapper.select {
            where {
                userId isIn listOf("1000001", "1000002", "1000003")
            }
        }
        assertThat(list).hasSizeGreaterThanOrEqualTo(1)
    }

    @Test
    fun selectListTargetFieldsTest() {
        prepareData()
        val list = mapper.select(userId, nickname) {
            where {
                userId isIn listOf("1000001", "1000002", "1000003")
            }
        }
        assertThat(list).hasSizeGreaterThanOrEqualTo(1)
        list.forEach {
            assertThat(it.shardNo).isNull()
            assertThat(it.createdAt).isNull()
        }
    }

    @Test
    fun countTest() {
        prepareData()
        val rows = mapper.count {
            where {
                userId isEqualTo "1000003"
            }
        }
        assertThat(rows).isEqualTo(1)
    }

    @Test
    fun updateTest() {
        val data = prepareData()

        val updatedData = data.copy(
            nickname = "U${data.nickname}",
            shardNo = null,
        )

        val row = mapper.update(updatedData)
        assertThat(row).isEqualTo(1)

        val after = mapper.selectOne {
            where {
                userId isEqualTo "1000003"
            }
        }
        assertThat(after).isNotNull
        assertThat(after?.nickname).isEqualTo("U${data.nickname}")
        assertThat(after?.shardNo).isEqualTo(1)
    }

    @Test
    fun deleteTest() {
        prepareData()
        val row = mapper.delete {
            where {
                userId isEqualTo "1000003"
            }
        }
        assertThat(row).isEqualTo(1)
    }
}