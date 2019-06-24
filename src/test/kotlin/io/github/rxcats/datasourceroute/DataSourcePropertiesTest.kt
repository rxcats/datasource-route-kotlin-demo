package io.github.rxcats.datasourceroute

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [DataSourceConfig::class])
class DataSourcePropertiesTest(@Autowired private val properties: DataSourceProperties) {

    private val log = logger()

    @Test
    fun test() {
        log.info("{}", properties)
        assertThat(properties).isNotNull
    }

}