package io.github.rxcats.datasourceroute

import io.github.rxcats.datasourceroute.entity.CommonUser
import io.github.rxcats.datasourceroute.entity.User
import java.time.LocalDateTime

object TestData {
    fun commonUser() = CommonUser("1000001", "Guest1000001", 0, LocalDateTime.now())
    fun user() = User("1000001", "Guest1000001", LocalDateTime.now())
}