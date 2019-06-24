package io.github.rxcats.datasourceroute

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun getShardNo(userId: String, targets: ArrayList<Int>) = targets[Math.abs(userId.hashCode() % targets.size)]

inline fun <reified T> T.logger(): Logger = LoggerFactory.getLogger(T::class.java)