package io.github.rxcats.datasourceroute.dynamicsql

import java.time.LocalDateTime

data class CommonUserRow(
    var userId: String? = null,
    var nickname: String? = null,
    var shardNo: Int? = null,
    var createdAt: LocalDateTime? = null
)
