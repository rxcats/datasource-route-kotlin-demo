package io.github.rxcats.datasourceroute.service

import io.github.rxcats.datasourceroute.aop.TargetDatabase
import io.github.rxcats.datasourceroute.entity.User
import io.github.rxcats.datasourceroute.mapper.user.UserMapper
import io.github.rxcats.datasourceroute.service.query.DbType
import io.github.rxcats.datasourceroute.service.query.ParamType
import org.springframework.stereotype.Service

@Service
class UserService(private val userMapper: UserMapper) {
    @TargetDatabase(db = DbType.USER)
    fun getUser(userId: String): User? {
        return userMapper.selectOne(userId)
    }

    @TargetDatabase(db = DbType.USER, paramType = ParamType.SHARD_NO)
    fun getUserByShardNo(shardNo: Int, userId: String): User? {
        return userMapper.selectOne(userId)
    }

    @TargetDatabase(db = DbType.USER, paramType = ParamType.SHARD_NO)
    fun insertUser(shardNo: Int, user: User) {
        userMapper.insert(user)
    }

    @TargetDatabase(db = DbType.USER, paramType = ParamType.SHARD_NO)
    fun deleteAllUser(shardNo: Int) {
        userMapper.deleteAll()
    }

    @TargetDatabase(db = DbType.USER, paramType = ParamType.SHARD_NO, useTransaction = true)
    fun insertUserWithTransaction(shardNo: Int, block: () -> Unit) {
        block()
    }

}