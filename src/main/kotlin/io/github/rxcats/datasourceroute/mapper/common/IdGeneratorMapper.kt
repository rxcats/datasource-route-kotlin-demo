package io.github.rxcats.datasourceroute.mapper.common

import io.github.rxcats.datasourceroute.entity.IdGenerator
import org.apache.ibatis.annotations.Mapper

@Mapper
interface IdGeneratorMapper {
    fun generate(idGenerator: IdGenerator): Int
}