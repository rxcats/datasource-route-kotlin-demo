package io.github.rxcats.datasourceroute.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("app.database")
class DataBaseProperties(
    val driverClassName: String,
    val shardTargets: List<Int>,
    val properties: Map<String, DatabaseProp>
)