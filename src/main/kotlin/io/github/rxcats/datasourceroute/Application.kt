package io.github.rxcats.datasourceroute

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class, DataSourceTransactionManagerAutoConfiguration::class])
class DatasourceRouteKotlinDemoApplication

fun main(args: Array<String>) {
    runApplication<DatasourceRouteKotlinDemoApplication>(*args)
}
