package io.github.rxcats.datasourceroute.service.query

import io.github.rxcats.datasourceroute.config.RoutingDataSourceContextHolder
import io.github.rxcats.datasourceroute.config.RoutingDataSourceTransactionManager
import org.springframework.stereotype.Component

@Component
class QueryHelper(
    private val transactionManager: RoutingDataSourceTransactionManager
) {

    fun <T> execute(db: String, cb: () -> T?): T? {
        try {
            RoutingDataSourceContextHolder.set(db)
            return cb.invoke()
        } finally {
            RoutingDataSourceContextHolder.clear()
        }
    }

    fun <T> execute(db: DbType, shardNo: Int = 0, cb: () -> T?): T? {
        return if (db == DbType.COMMON) {
            execute(db.name, cb)
        } else {
            execute(db.shard(shardNo), cb)
        }
    }

    fun <T> executeTx(db: String, cb: () -> T?): T? {
        try {
            RoutingDataSourceContextHolder.set(db)
            transactionManager.start()
            val result = cb.invoke()
            transactionManager.commit()
            return result
        } finally {
            transactionManager.close()
            RoutingDataSourceContextHolder.clear()
        }
    }

    fun <T> executeTx(db: DbType, shardNo: Int = 0, cb: () -> T?): T? {
        return if (db == DbType.COMMON) {
            executeTx(db.name, cb)
        } else {
            executeTx(db.shard(shardNo), cb)
        }
    }
}