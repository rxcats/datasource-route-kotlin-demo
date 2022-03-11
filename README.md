# MyBatis RoutingDataSource

## application.properties 설정 
```
# MyBatis Query 로깅 설정
logging.level.io.github.rxcats.datasourceroute.mapper=debug

# mybatis configuration
mybatis.configuration.map-underscore-to-camel-case=true

# 공통 MySQL 설정
app.database.driver-class-name=com.mysql.cj.jdbc.Driver
app.database.shard-targets=1,2

# 유저 생성시 샤딩 타겟이 되는 DB index array 값
app.database.shard-targets=0,1

# commondb
app.database.properties.common.pool-name=commondb
app.database.properties.common.connection-timeout=5000
app.database.properties.common.idle-timeout=600000
app.database.properties.common.maximum-pool-size=10
app.database.properties.common.username=root
app.database.properties.common.password=qwer1234
app.database.properties.common.jdbc-url=jdbc:mysql://localhost:3306/route_commondb

# userdb[1]
app.database.properties.user1.pool-name=user1
app.database.properties.user1.connection-timeout=5000
app.database.properties.user1.idle-timeout=600000
app.database.properties.user1.maximum-pool-size=10
app.database.properties.user1.username=root
app.database.properties.user1.password=qwer1234
app.database.properties.user1.jdbc-url=jdbc:mysql://localhost:3306/route_userdb1

# userdb[2]
app.database.properties.user2.pool-name=user2
app.database.properties.user2.connection-timeout=5000
app.database.properties.user2.idle-timeout=600000
app.database.properties.user2.maximum-pool-size=10
app.database.properties.user2.username=root
app.database.properties.user2.password=qwer1234
app.database.properties.user2.jdbc-url=jdbc:mysql://localhost:3306/route_userdb2
```

## DB Query 예제
```kotlin
@Service
class UserService {
    @Autowired
    private lateinit var commonUserMapper: CommonUserMapper
    
    @Autowired
    private lateinit var queryHelper: QueryHelper
    
    fun getCommonUser(userId: String): CommonUser? {
        return queryHelper.execute(db = DbType.COMMON) {
            commonUserMapper.selectOne(userId)
        }
    }
}
```

## AOP 이용 예제
```kotlin
@Service
class UserService {
    @Autowired
    private lateinit var commonUserMapper: CommonUserMapper

    @Autowired
    private lateinit var userMapper: UserMapper

    @TargetDatabase(db = DbType.COMMON)
    fun getCommonUser(userId: String): CommonUser? {
        return commonUserMapper.selectOne(userId)
    }

    @TargetDatabase(db = DbType.USER, paramType = ParamType.SHARD_NO)
    fun getUserByShardNo(shardNo: Int, userId: String): User? {
        return userMapper.selectOne(userId)
    }

    @TargetDatabase(db = DbType.USER, paramType = ParamType.USER_ID)
    fun getUserById(userId: String): User? {
        return userMapper.selectOne(userId)
    }
    
    // Rollback with duplicates
    @TargetDatabase(db = DbType.USER, paramType = ParamType.SHARD_NO, useTransaction = true)
    fun insertUserWithTransaction(shardNo: Int, user: User) {
        userMapper.insert(user)
        userMapper.insertOnly(user)
    }
}
```
