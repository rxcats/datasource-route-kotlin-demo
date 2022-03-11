package io.github.rxcats.datasourceroute.service

import io.github.rxcats.datasourceroute.aop.TargetDatabase
import io.github.rxcats.datasourceroute.entity.CommonUser
import io.github.rxcats.datasourceroute.mapper.common.CommonUserMapper
import io.github.rxcats.datasourceroute.service.query.DbType
import org.springframework.stereotype.Service

@Service
class CommonUserService(
    private val mapper: CommonUserMapper
) {

    @TargetDatabase(db = DbType.COMMON)
    fun getUser(userId: String): CommonUser? {
        return mapper.selectOne(userId)
    }

    @TargetDatabase(db = DbType.COMMON)
    fun deleteAllUser() {
        mapper.deleteAll()
    }

    @TargetDatabase(db = DbType.COMMON)
    fun insertUser(user: CommonUser) {
        mapper.insert(user)
    }

}