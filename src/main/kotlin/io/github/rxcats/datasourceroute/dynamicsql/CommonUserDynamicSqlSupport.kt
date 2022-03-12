package io.github.rxcats.datasourceroute.dynamicsql

import org.mybatis.dynamic.sql.SqlTable
import java.time.LocalDateTime

import org.mybatis.dynamic.sql.util.kotlin.elements.column
import java.sql.JDBCType

object CommonUserDynamicSqlSupport {
    val commonUser = CommonUser()
    val userId = commonUser.userId
    val nickname = commonUser.nickname
    val shardNo = commonUser.shardNo
    val createdAt = commonUser.createdAt

    class CommonUser : SqlTable("common_user") {
        val userId = column<String>(name = "user_id", jdbcType = JDBCType.VARCHAR)
        val nickname = column<String>(name = "nickname", jdbcType = JDBCType.VARCHAR)
        val shardNo = column<Int>(name = "shard_no", jdbcType = JDBCType.INTEGER)
        val createdAt = column<LocalDateTime>(name = "created_at", jdbcType = JDBCType.TIMESTAMP)
    }
}