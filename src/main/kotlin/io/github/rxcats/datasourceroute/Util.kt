package io.github.rxcats.datasourceroute

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.zip.CRC32
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun getShardNo(userId: String, targets: List<Int>): Int {
    val crc = CRC32()
    crc.update(userId.toByteArray())
    val shard = crc.value % targets.size
    return targets[shard.toInt()]
}


class LoggerDelegate : ReadOnlyProperty<Any, Logger> {
    private lateinit var logger: Logger
    override fun getValue(thisRef: Any, property: KProperty<*>): Logger {
        if (!::logger.isInitialized) logger = LoggerFactory.getLogger(thisRef.javaClass.name.substringBefore("\$Companion"))
        return logger
    }
}

val loggerK: ReadOnlyProperty<Any, Logger> get() = LoggerDelegate()
