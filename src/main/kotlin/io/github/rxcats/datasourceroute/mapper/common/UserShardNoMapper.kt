package io.github.rxcats.datasourceroute.mapper.common

import io.github.rxcats.datasourceroute.entity.UserShardNo
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface UserShardNoMapper {
    @Select("SELECT userId, shardNo FROM tb_common_user WHERE userId = #{userId}")
    fun selectOne(userId: String): UserShardNo
}