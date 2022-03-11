package io.github.rxcats.datasourceroute

import io.github.rxcats.datasourceroute.entity.CommonUser
import io.github.rxcats.datasourceroute.entity.User
import java.time.LocalDateTime

object TestData {
    fun commonUser() = CommonUser("1000001", "Guest1000001", 1, LocalDateTime.now())
    fun commonUser2() = CommonUser("1000002", "Guest1000002", 2, LocalDateTime.now())
    fun user() = User("1000001", "Guest1000001", LocalDateTime.now())
    fun user2() = User("1000002", "Guest1000002", LocalDateTime.now())
}