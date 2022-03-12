package io.github.rxcats.datasourceroute.dynamicsql

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.ResultMap
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.SelectProvider
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider
import org.mybatis.dynamic.sql.util.SqlProviderAdapter
import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper
import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper
import org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper
import org.mybatis.dynamic.sql.util.mybatis3.CommonSelectMapper
import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper

@Mapper
interface CommonUserDynamicSqlMapper : CommonInsertMapper<CommonUserRow>,
    CommonCountMapper, CommonDeleteMapper, CommonUpdateMapper, CommonSelectMapper {

    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    @Results(
        id = "CommonUserResult",
        value = [
            Result(column = "user_id", property = "userId"),
            Result(column = "nickname", property = "nickname"),
            Result(column = "shard_no", property = "shardNo"),
            Result(column = "created_at", property = "createdAt")
        ]
    )
    fun selectOne(selectStatement: SelectStatementProvider): CommonUserRow?

    @ResultMap("CommonUserResult")
    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    fun selectList(selectStatement: SelectStatementProvider): List<CommonUserRow>
}