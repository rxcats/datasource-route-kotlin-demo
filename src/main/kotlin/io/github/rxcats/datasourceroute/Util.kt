package io.github.rxcats.datasourceroute

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.abs

fun getShardNo(userId: String, targets: ArrayList<Int>) = targets[abs(userId.hashCode() % targets.size)]

inline fun <reified T> T.logger(): Logger {
    return if (T::class.isCompanion) {
        LoggerFactory.getLogger(T::class.java.enclosingClass)
    } else {
        LoggerFactory.getLogger(T::class.java)
    }
}