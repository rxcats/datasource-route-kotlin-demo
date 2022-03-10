package io.github.rxcats.datasourceroute.service.query

import io.github.rxcats.datasourceroute.InvalidShardNoException

enum class DbType(private val nm: String) {
    COMMON("common"),
    USER("user"),
    ;

    fun shard(shardNo: Int): String {
        if (shardNo < 0) {
            throw InvalidShardNoException("shardNo must be greater than zero")
        }
        return USER.nm + shardNo
    }
}