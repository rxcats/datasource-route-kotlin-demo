package io.github.rxcats.datasourceroute.mapper.common

import io.github.rxcats.datasourceroute.entity.CommonUser
import org.apache.ibatis.annotations.Mapper

@Mapper
interface CommonUserMapper {
    fun selectOne(userId: String): CommonUser

    fun insert(user: CommonUser): Int

    fun delete(userId: String): Int

    fun deleteAll(): Int

    fun errorInsert(user: CommonUser): Int
}