package io.github.rxcats.datasourceroute.config

import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.DefaultTransactionDefinition

@Component
class RoutingDataSourceTransactionManager(
    private val transactionManager: PlatformTransactionManager) : DefaultTransactionDefinition() {

    private lateinit var status: TransactionStatus

    fun start() {
        status = transactionManager.getTransaction(null)
    }

    fun completed() = status.isCompleted

    fun commit() {
        if (!status.isCompleted) {
            transactionManager.commit(status)
        }
    }

    fun rollback() {
        if (!status.isCompleted) {
            transactionManager.rollback(status)
        }
    }

    fun close() = rollback()
}