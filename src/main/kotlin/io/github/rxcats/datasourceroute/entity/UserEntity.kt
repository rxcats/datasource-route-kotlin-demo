package io.github.rxcats.datasourceroute.entity

import java.time.LocalDateTime

data class CommonUser(
    val userId: String,
    val nickname: String,
    val shardNo: Int,
    val createdAt: LocalDateTime
)

data class User(
    val userId: String,
    val nickname: String,
    val createdAt: LocalDateTime
)

data class UserShardNo(
    val userId: String,
    val shardNo: Int
)

data class IdGenerator(
    val idType: String,
    val idValue: String? = null
)