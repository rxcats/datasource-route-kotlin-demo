package io.github.rxcats.datasourceroute.service

import io.github.rxcats.datasourceroute.DataSourceContextHolder
import io.github.rxcats.datasourceroute.InvalidShardNoException
import io.github.rxcats.datasourceroute.TxManager
import org.springframework.stereotype.Service

enum class DbType(private val nm: String) {
    COMMON("common"),
    USER("user"),
    ;

    fun shard(shardNo: Int): String {
        if (shardNo < 0) {
            throw InvalidShardNoException("shardNo must be greater than or equal to zero")
        }
        return USER.nm + shardNo
    }
}

@Service
class QueryHelper(private val txManager: TxManager) {

    fun <T> execute(db: String, cb: () -> T?): T? {
        try {
            DataSourceContextHolder.set(db)
            return cb.invoke()
        } finally {
            DataSourceContextHolder.clear()
        }
    }

    fun <T> execute(db: DbType, shardNo: Int = -1, cb: () -> T?): T? {
        return if (db == DbType.COMMON) {
            execute(db.name, cb)
        } else {
            execute(db.shard(shardNo), cb)
        }
    }

    fun <T> executeTx(db: String, cb: () -> T?): T? {
        try {
            DataSourceContextHolder.set(db)
            txManager.start()
            val result = cb.invoke()
            txManager.commit()
            return result
        } finally {
            txManager.close()
            DataSourceContextHolder.clear()
        }
    }

    fun <T> executeTx(db: DbType, shardNo: Int = -1, cb: () -> T?): T? {
        return if (db == DbType.COMMON) {
            executeTx(db.name, cb)
        } else {
            executeTx(db.shard(shardNo), cb)
        }
    }
}