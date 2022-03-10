package io.github.rxcats.datasourceroute

import io.github.rxcats.datasourceroute.config.DataBaseProperties
import io.github.rxcats.datasourceroute.config.DataSourceConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [DataSourceConfig::class])
class DataBasePropertiesTest {

    @Autowired
    private lateinit var properties: DataBaseProperties

    @Test
    fun test() {
        assertThat(properties).isNotNull
    }

}