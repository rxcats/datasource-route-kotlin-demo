package io.github.rxcats.datasourceroute.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

enum class CacheKey(private val prefix: String, private val ttl: Long, private val desc: String) {
    USER("user", 300, ""),
    USER_SHARD("user_shard", 86400, ""),
    ;

    private val delimiter = ":"

    fun name(userId: String) = prefix + delimiter + userId
    fun ttl() = ttl
    fun desc() = desc
}

@Service
class CacheService {
    private val cacheTable = ConcurrentHashMap<String, Any>()
    private val cacheTimeInfo = ConcurrentHashMap<String, Long>()

    @Suppress("UNCHECKED_CAST")
    fun <T> getCache(cacheKey: CacheKey, userId: String): T? = cacheTable[cacheKey.name(userId)] as T?

    fun setCache(cacheKey: CacheKey, userId: String, data: Any, ttl: Long) {
        cacheTable[cacheKey.name(userId)] = data
        cacheTimeInfo[cacheKey.name(userId)] = System.currentTimeMillis() + ttl
    }

    fun removeCache(cacheKey: CacheKey, userId: String) {
        cacheTable.remove(cacheKey.name(userId))
        cacheTimeInfo.remove(cacheKey.name(userId))
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getViaCache(cacheKey: CacheKey, userId: String, cb: () -> T?, useCache: Boolean): T? {
        var data: T? = if (useCache) getCache<T>(cacheKey, userId) else null
        if (data == null) {
            data = cb.invoke()
            if (useCache && data != null) {
                setCache(cacheKey, userId, data, cacheKey.ttl())
            }
        }
        return data
    }

    fun <T> getViaCache(cacheKey: CacheKey, userId: String, cb: () -> T?): T? = getViaCache(cacheKey, userId, cb, true)

    @Scheduled(fixedDelay = 1000)
    fun clean() {
        val now = System.currentTimeMillis()

        val targets = cacheTimeInfo.entries
            .filter {
                it.value <= now
            }.map {
                it.key
            }.toCollection(ArrayList())

        targets.forEach {
            cacheTimeInfo.remove(it)
            cacheTable.remove(it)
        }
    }

}