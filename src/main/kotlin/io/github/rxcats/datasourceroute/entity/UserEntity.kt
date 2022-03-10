package io.github.rxcats.datasourceroute.entity

import java.time.LocalDateTime

data class CommonUser(
    var userId: String,
    var nickname: String,
    var shardNo: Int,
    var createdAt: LocalDateTime
)

data class User(
    var userId: String,
    var nickname: String,
    var createdAt: LocalDateTime
)

data class UserShardNo(
    var userId: String,
    var shardNo: Int
)

data class IdGenerator(
    var idType: String = "",
    var idValue: Long = 0
)