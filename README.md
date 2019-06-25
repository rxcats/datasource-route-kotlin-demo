# MyBatis DataSourceRouter example

## application.properties 설정 
```
# MyBatis Query 로깅 설정
logging.level.io.github.rxcats.datasourceroute.mapper=debug

# 공통 MySQL 설정
app.database.driver-class-name=com.mysql.cj.jdbc.Driver
app.database.mapper-path=classpath:mybatis/mapper/**/*.xml
app.database.auto-commit=true

# 유저 생성시 샤딩 타겟이 되는 DB index array 값
app.database.shard-targets=0,1

# commondb 설정
app.database.common.pool-name=commondb
app.database.common.connection-timeout=5000
app.database.common.idle-timeout=600000
app.database.common.maximum-pool-size=10
app.database.common.username=username
app.database.common.password=password
app.database.common.jdbc-url=jdbc:mysql://192.168.99.100:3306/commondb?useUnicode=true&useSSL=false&verifyServerCertificate=false&useLocalSessionState=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true

# userdb[0] 샤드0 설정
app.database.user[0].pool-name=user[0]
app.database.user[0].connection-timeout=5000
app.database.user[0].idle-timeout=600000
app.database.user[0].maximum-pool-size=10
app.database.user[0].username=username
app.database.user[0].password=password
app.database.user[0].jdbc-url=jdbc:mysql://192.168.99.100:3306/userdb0?useUnicode=true&useSSL=false&verifyServerCertificate=false&useLocalSessionState=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true

# userdb[1] 샤드1 설정
app.database.user[1].pool-name=user[1]
app.database.user[1].connection-timeout=5000
app.database.user[1].idle-timeout=600000
app.database.user[1].maximum-pool-size=10
app.database.user[1].username=username
app.database.user[1].password=password
app.database.user[1].jdbc-url=jdbc:mysql://192.168.99.100:3306/userdb1?useUnicode=true&useSSL=false&verifyServerCertificate=false&useLocalSessionState=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true
```

## DB Query 예제
```kotlin
@Service
class UserService(
    @Autowired private val commonUserMapper: CommonUserMapper,
    @Autowired private val queryHelper: QueryHelper) {
    
    fun select(userId: String): CommonUser = queryHelper.execute(db = DbType.COMMON, cb = { commonUserMapper.selectOne("1000001") })
}
```