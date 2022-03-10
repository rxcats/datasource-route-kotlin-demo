package io.github.rxcats.datasourceroute.mapper

import io.github.rxcats.datasourceroute.Application
import io.github.rxcats.datasourceroute.entity.IdGenerator
import io.github.rxcats.datasourceroute.mapper.common.IdGeneratorMapper
import io.github.rxcats.datasourceroute.service.query.DbType
import io.github.rxcats.datasourceroute.service.query.QueryHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [Application::class])
class IdGeneratorMapperTest {
    @Autowired
    private lateinit var idGeneratorMapper: IdGeneratorMapper

    @Autowired
    private lateinit var queryHelper: QueryHelper

    @BeforeEach
    fun before() {
        queryHelper.execute(DbType.COMMON) {
            idGeneratorMapper.deleteAll()
        }
    }

    @Test
    fun generate() {
        val input = IdGenerator("test")
        val insert = queryHelper.execute(DbType.COMMON) {
            idGeneratorMapper.generate(input)
        }
        assertThat(insert).isEqualTo(1)
        assertThat(input.idValue).isNotNull
    }
}