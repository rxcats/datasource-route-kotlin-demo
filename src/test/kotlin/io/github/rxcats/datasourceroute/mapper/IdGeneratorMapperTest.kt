package io.github.rxcats.datasourceroute.mapper

import io.github.rxcats.datasourceroute.DataSourceConfig
import io.github.rxcats.datasourceroute.TxManager
import io.github.rxcats.datasourceroute.entity.IdGenerator
import io.github.rxcats.datasourceroute.mapper.common.IdGeneratorMapper
import io.github.rxcats.datasourceroute.service.DbType
import io.github.rxcats.datasourceroute.service.QueryHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest

@EnableAutoConfiguration
@SpringBootTest(classes = [DataSourceConfig::class, IdGeneratorMapper::class, QueryHelper::class, TxManager::class])
class IdGeneratorMapperTest(
    @Autowired private val idGeneratorMapper: IdGeneratorMapper,
    @Autowired private val queryHelper: QueryHelper) {

    @Test
    fun generate() {
        val input = IdGenerator("test")
        val insert = queryHelper.execute(db = DbType.COMMON, cb = { idGeneratorMapper.generate(input) })
        assertThat(insert).isEqualTo(1)
        assertThat(input.idValue).isNotNull()
    }
}