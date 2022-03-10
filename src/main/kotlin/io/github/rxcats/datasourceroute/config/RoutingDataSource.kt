package io.github.rxcats.datasourceroute.config

import io.github.rxcats.datasourceroute.loggerK
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource

class RoutingDataSource : AbstractRoutingDataSource() {
    companion object {
        private val log by loggerK
    }

    override fun determineCurrentLookupKey(): Any? {
        return RoutingDataSourceContextHolder.get()
    }

    fun close() {
        for ((dbname, ds) in this.resolvedDataSources) {
            try {
                log.info("RoutingDataSource {} close", dbname)
                ds.connection.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}