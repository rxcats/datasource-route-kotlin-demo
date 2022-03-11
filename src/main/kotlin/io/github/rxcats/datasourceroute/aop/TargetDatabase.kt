package io.github.rxcats.datasourceroute.aop

import io.github.rxcats.datasourceroute.service.query.DbType
import io.github.rxcats.datasourceroute.service.query.ParamType

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TargetDatabase(
    val db: DbType = DbType.COMMON,
    val paramType: ParamType = ParamType.USER_ID,
    val useTransaction: Boolean = false
)
