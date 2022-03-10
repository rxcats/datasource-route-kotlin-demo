package io.github.rxcats.datasourceroute.mapper.user

import io.github.rxcats.datasourceroute.entity.User
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface UserMapper {
    @Select("""
        SELECT user_id, nickname, created_at FROM user WHERE user_id = #{playerId}
    """)
    fun selectOne(userId: String): User

    @Insert("""
        INSERT INTO user (user_id, nickname, created_at) VALUES(#{userId}, #{nickname}, #{createdAt})
		ON DUPLICATE KEY UPDATE user_id = #{userId}
    """)
    fun insert(user: User): Int

    @Delete("""
        DELETE FROM user WHERE user_id = #{userId}
    """)
    fun delete(userId: String): Int

    @Delete("""
        DELETE FROM user
    """)
    fun deleteAll()

    @Insert("""
        INSERT INTO user (user_id, nickname, created_at) VALUES(#{userId}, #{nickname}, #{createdAt})
    """)
    fun insertOnly(user: User): Int
}