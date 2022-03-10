package io.github.rxcats.datasourceroute.mapper.common

import io.github.rxcats.datasourceroute.entity.IdGenerator
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.SelectKey

@Mapper
interface IdGeneratorMapper {
    @SelectKey(keyColumn = "id_value", keyProperty = "idValue", resultType = Long::class, before = false
        , statement=["SELECT id_value FROM key_generator WHERE id_type = #{idType}"])
    @Insert("""
        INSERT INTO key_generator (id_type, id_value) VALUES (#{idType}, 1)
        ON DUPLICATE KEY UPDATE id_value = id_value + 1
    """)
    fun generate(idGenerator: IdGenerator): Int

    @Delete("""
        DELETE FROM key_generator        
    """)
    fun deleteAll(): Int
}