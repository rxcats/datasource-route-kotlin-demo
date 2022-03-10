package io.github.rxcats.datasourceroute.config

object RoutingDataSourceContextHolder {
    private val context = ThreadLocal<String>()

    fun set(dbType: String) = context.set(dbType)

    fun get(): String? = context.get()

    fun clear() = context.remove()
}