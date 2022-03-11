package io.github.rxcats.datasourceroute.aop

import io.github.rxcats.datasourceroute.config.RoutingDataSourceContextHolder
import io.github.rxcats.datasourceroute.config.RoutingDataSourceTransactionManager
import io.github.rxcats.datasourceroute.loggerK
import io.github.rxcats.datasourceroute.mapper.common.UserShardNoMapper
import io.github.rxcats.datasourceroute.service.query.DbType
import io.github.rxcats.datasourceroute.service.query.ParamType
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import java.lang.reflect.Method

@Aspect
class TargetDatabaseAdvice(
    private val userShardNoMapper: UserShardNoMapper,
    private val transactionManager: RoutingDataSourceTransactionManager
) {

    private val log by loggerK

    @Around("@annotation(io.github.rxcats.datasourceroute.aop.TargetDatabase)")
    fun process(pjp: ProceedingJoinPoint): Any? {
        val signature = pjp.signature as MethodSignature
        val annotation = signature.method.getAnnotation(TargetDatabase::class.java)
        val useTransaction = annotation.useTransaction

        log.info("signature: {}, db: {}, useTransaction: {}", signature, annotation.db, useTransaction)

        try {
            if (annotation.db == DbType.COMMON) {
                RoutingDataSourceContextHolder.set(DbType.COMMON.name)
            } else {
                if (annotation.paramType == ParamType.SHARD_NO) {
                    val shardNo = parseShardNo(signature.method, pjp.args)
                        ?: throw IllegalStateException("Parameter shardNo must be greater than zero")
                    RoutingDataSourceContextHolder.set(DbType.USER.shard(shardNo))
                } else {
                    val userId = parseUserId(signature.method, pjp.args)
                    if (userId.isNullOrBlank()) {
                        throw IllegalStateException("Parameter userId required")
                    }
                    RoutingDataSourceContextHolder.set(DbType.COMMON.name)

                    val userShardNo = userShardNoMapper.selectOne(userId)
                        ?: throw IllegalStateException("Cannot find userShardNo ($userId)")

                    RoutingDataSourceContextHolder.set(DbType.USER.shard(userShardNo.shardNo))
                }
            }

            if (useTransaction) {
                transactionManager.start()
            }

            val result = pjp.proceed()

            if (useTransaction) {
                transactionManager.commit()
            }

            return result
        } catch (t: Throwable) {
            if (useTransaction) {
                transactionManager.rollback()
            }
            t.printStackTrace()
            throw t
        } finally {
            RoutingDataSourceContextHolder.clear()
        }
    }

    private fun parseShardNo(method: Method, args: Array<Any>): Int? {
        for (i in 0..method.parameterCount) {
            if ("shardNo" == method.parameters[i].name) {
                return args[i] as Int
            }
        }
        return null
    }

    private fun parseUserId(method: Method, args: Array<Any>): String? {
        for (i in 0..method.parameterCount) {
            if ("userId" == method.parameters[i].name) {
                return args[i] as String
            }
        }
        return null
    }

}