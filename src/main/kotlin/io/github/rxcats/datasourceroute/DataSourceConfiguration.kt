package io.github.rxcats.datasourceroute

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.rxcats.datasourceroute.entity.ConfigScanEntity
import org.apache.ibatis.session.ExecutorType
import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.SqlSessionTemplate
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.DefaultTransactionDefinition
import javax.sql.DataSource

@ConfigurationProperties("app.database")
class DataSourceProperties {
    lateinit var driverClassName: String
    lateinit var mapperPath: String
    lateinit var autoCommit: String
    lateinit var shardTargets: ArrayList<Int>
    val common = DatabaseProp()
    val user = ArrayList<DatabaseProp>()

    class DatabaseProp {
        lateinit var poolName: String
        lateinit var connectionTimeout: String
        lateinit var idleTimeout: String
        lateinit var maximumPoolSize: String
        lateinit var username: String
        lateinit var password: String
        lateinit var jdbcUrl: String
        override fun toString(): String = "DatabaseProp(poolName='$poolName', connectionTimeout='$connectionTimeout', idleTimeout='$idleTimeout', maximumPoolSize='$maximumPoolSize', username='$username', password='$password', jdbcUrl='$jdbcUrl')"


    }

    override fun toString(): String = "DataSourceProperties(driverClassName='$driverClassName', mapperPath='$mapperPath', autoCommit='$autoCommit', shardTargets=$shardTargets, common=$common, user=$user)"
}

@Configuration
@EnableConfigurationProperties(DataSourceProperties::class)
class DataSourceConfig(private val properties: DataSourceProperties) {

    private fun commonDatasource(): HikariDataSource {
        val config = HikariConfig()
        config.isAutoCommit = properties.autoCommit.toBoolean()
        config.driverClassName = properties.driverClassName
        config.poolName = properties.common.poolName
        config.connectionTimeout = properties.common.connectionTimeout.toLong()
        config.idleTimeout = properties.common.idleTimeout.toLong()
        config.maximumPoolSize = properties.common.maximumPoolSize.toInt()
        config.username = properties.common.username
        config.password = properties.common.password
        config.jdbcUrl = properties.common.jdbcUrl
        return HikariDataSource(config)
    }

    private fun userDatasourceList(): ArrayList<HikariDataSource> {
        return properties.user.map {
            val config = HikariConfig()
            config.isAutoCommit = properties.autoCommit.toBoolean()
            config.driverClassName = properties.driverClassName
            config.poolName = it.poolName
            config.connectionTimeout = it.connectionTimeout.toLong()
            config.idleTimeout = it.idleTimeout.toLong()
            config.maximumPoolSize = it.maximumPoolSize.toInt()
            config.username = it.username
            config.password = it.password
            config.jdbcUrl = it.jdbcUrl
            HikariDataSource(config)
        }.toCollection(ArrayList())
    }

    @Bean
    fun dataSource(): DataSource {
        val map = HashMap<Any, Any>()

        val common = commonDatasource()
        map["common"] = common

        var i = 0
        userDatasourceList().forEach {
            map["user$i"] = it
            i++
        }

        val router = DataSourceRouter()
        router.setDefaultTargetDataSource(common)
        router.setTargetDataSources(map)
        return router
    }

    @Bean
    fun sqlSessionFactory(dataSource: DataSource, context: ApplicationContext): SqlSessionFactory? {
        val bean = SqlSessionFactoryBean()
        bean.setDataSource(dataSource)
        bean.setTypeAliasesPackage(ConfigScanEntity::class.java.`package`.toString())
        bean.setMapperLocations(context.getResources(properties.mapperPath))

        val cnf = org.apache.ibatis.session.Configuration()
        cnf.databaseId = "mysql"
        cnf.isMapUnderscoreToCamelCase = true
        cnf.defaultExecutorType = ExecutorType.REUSE
        cnf.isCacheEnabled = false
        cnf.isUseGeneratedKeys = true
        cnf.isAggressiveLazyLoading = false
        bean.setConfiguration(cnf)

        return bean.`object`
    }

    @Bean
    fun sqlSessionTemplate(sqlSessionFactory: SqlSessionFactory): SqlSessionTemplate {
        return SqlSessionTemplate(sqlSessionFactory)
    }

    @Bean
    fun transactionManager(dataSource: DataSource): PlatformTransactionManager {
        val transactionManager = DataSourceTransactionManager(dataSource)
        transactionManager.isGlobalRollbackOnParticipationFailure = false
        return transactionManager
    }

}

object DataSourceContextHolder {
    private val context = ThreadLocal<String>()

    fun set(dbType: String) = context.set(dbType)

    fun get(): String? = context.get()

    fun clear() = context.remove()
}

class DataSourceRouter : AbstractRoutingDataSource() {
    override fun determineCurrentLookupKey(): Any? {
        return DataSourceContextHolder.get()
    }
}

@Scope("prototype")
@Component
class TxManager(private val transactionManager: PlatformTransactionManager) : DefaultTransactionDefinition() {

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