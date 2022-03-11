package io.github.rxcats.datasourceroute.mapper.common

import io.github.rxcats.datasourceroute.entity.CommonUser
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface CommonUserMapper {
    @Select("""
        SELECT user_id, nickname, shard_no, created_at FROM common_user WHERE user_id = #{userId}
    """)
    fun selectOne(userId: String): CommonUser?

    @Insert("""
        INSERT INTO common_user (user_id, nickname, shard_no, created_at) VALUES(#{userId}, #{nickname}, #{shardNo}, #{createdAt})
		ON DUPLICATE KEY UPDATE user_id = #{userId}
    """)
    fun insert(user: CommonUser): Int

    @Delete("""
        DELETE FROM common_user WHERE user_id = #{userId}
    """)
    fun delete(userId: String): Int

    @Delete("""
        DELETE FROM common_user
    """)
    fun deleteAll(): Int

    @Insert("""
        INSERT INTO common_user (user_id, nickname, shard_no, created_at, ooo) VALUES(#{userId}, #{nickname}, #{shardNo}, #{createdAt})
		ON DUPLICATE KEY UPDATE user_id = #{userId}
    """)
    fun errorInsert(user: CommonUser): Int
}