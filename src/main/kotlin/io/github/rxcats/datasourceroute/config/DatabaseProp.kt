package io.github.rxcats.datasourceroute.config

class DatabaseProp(
    var poolName: String,
    var connectionTimeout: Long,
    var idleTimeout: Long,
    var maximumPoolSize: Int,
    var username: String,
    var password: String,
    var jdbcUrl: String
)