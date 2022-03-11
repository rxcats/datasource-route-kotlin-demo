package io.github.rxcats.datasourceroute.mapper.common

import io.github.rxcats.datasourceroute.entity.UserShardNo
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface UserShardNoMapper {
    @Select("SELECT user_id, shard_no FROM common_user WHERE user_id = #{userId}")
    fun selectOne(userId: String): UserShardNo?
}