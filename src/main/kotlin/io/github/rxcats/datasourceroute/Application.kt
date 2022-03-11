package io.github.rxcats.datasourceroute

import io.github.rxcats.datasourceroute.aop.TargetDatabaseAdvice
import io.github.rxcats.datasourceroute.config.RoutingDataSourceTransactionManager
import io.github.rxcats.datasourceroute.mapper.common.UserShardNoMapper
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@MapperScan
@SpringBootApplication
class Application {
    @Bean
    fun targetDatabaseAdvice(
        userShardNoMapper: UserShardNoMapper,
        routingDataSourceTransactionManager: RoutingDataSourceTransactionManager
    ): TargetDatabaseAdvice {
        return TargetDatabaseAdvice(userShardNoMapper, routingDataSourceTransactionManager)
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
