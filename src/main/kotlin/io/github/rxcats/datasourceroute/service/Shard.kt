package io.github.rxcats.datasourceroute.service

import io.github.rxcats.datasourceroute.DataSourceProperties
import io.github.rxcats.datasourceroute.NotFoundUserException
import io.github.rxcats.datasourceroute.getShardNo
import io.github.rxcats.datasourceroute.mapper.common.UserShardNoMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ShardHelper(
    @Autowired private val queryHelper: QueryHelper,
    @Autowired private val cacheService: CacheService,
    @Autowired private val userShardNoMapper: UserShardNoMapper,
    @Autowired private val properties: DataSourceProperties) {

    fun newShardNo(userId: String): Int = getShardNo(userId, properties.shardTargets)

    fun getShardNo(userId: String): Int {
        val userShardNo = cacheService.getViaCache(cacheKey = CacheKey.USER_SHARD, userId = userId, cb = {
            queryHelper.execute(db = DbType.COMMON, cb = { userShardNoMapper.selectOne(userId) })
        }) ?: throw NotFoundUserException("UserShardNo is null (userId:$userId)")
        return userShardNo.shardNo
    }

}