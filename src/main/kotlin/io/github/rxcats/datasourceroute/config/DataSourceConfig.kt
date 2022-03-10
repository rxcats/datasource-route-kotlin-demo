package io.github.rxcats.datasourceroute.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.boot.autoconfigure.MybatisProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
@EnableConfigurationProperties(DataBaseProperties::class)
class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "mybatis")
    fun mybatisProperties(): MybatisProperties {
        return MybatisProperties()
    }

    @Bean(destroyMethod = "close")
    fun routingDataSource(dbProps: DataBaseProperties): RoutingDataSource {
        val dataSourceMap = hashMapOf<Any, Any>()
        val router = RoutingDataSource()
        for ((dbname, prop) in dbProps.properties) {
            val config = HikariConfig()
            config.driverClassName = dbProps.driverClassName
            config.jdbcUrl = prop.jdbcUrl
            config.username = prop.username
            config.password = prop.password
            config.poolName = prop.poolName
            config.maximumPoolSize = prop.maximumPoolSize
            config.connectionTimeout = prop.connectionTimeout
            config.idleTimeout = prop.idleTimeout
            dataSourceMap[dbname] = HikariDataSource(config)

            if (dbname == "common") {
                router.setDefaultTargetDataSource(dataSourceMap[dbname]!!)
            }
        }

        router.setTargetDataSources(dataSourceMap)

        return router
    }

    @Bean
    fun dataSource(routingDataSource: RoutingDataSource): DataSource {
        return LazyConnectionDataSourceProxy(routingDataSource)
    }

    @Bean
    fun sqlSessionFactory(dataSource: DataSource, mybatisProperties: MybatisProperties): SqlSessionFactory {
        val f = SqlSessionFactoryBean()
        f.setDataSource(dataSource)
        f.setConfigurationProperties(mybatisProperties.configurationProperties)
        f.setMapperLocations(*mybatisProperties.resolveMapperLocations())
        f.setTypeHandlersPackage(mybatisProperties.typeHandlersPackage)
        f.setTypeAliasesPackage(mybatisProperties.typeAliasesPackage)
        return f.`object`!!
    }

    @ConditionalOnMissingBean
    @Bean
    fun transactionManager(dataSource: DataSource): PlatformTransactionManager {
        val transactionManager = DataSourceTransactionManager(dataSource)
        transactionManager.isGlobalRollbackOnParticipationFailure = false
        return transactionManager
    }
}