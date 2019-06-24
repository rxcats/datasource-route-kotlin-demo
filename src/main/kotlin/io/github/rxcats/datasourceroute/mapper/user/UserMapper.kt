package io.github.rxcats.datasourceroute.mapper.user

import io.github.rxcats.datasourceroute.entity.User
import org.apache.ibatis.annotations.Mapper

@Mapper
interface UserMapper {
    fun selectOne(userId: String): User

    fun insert(user: User): Int

    fun delete(userId: String): Int

    fun deleteAll()

    fun insertOnly(user: User): Int
}