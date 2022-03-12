package io.github.rxcats.datasourceroute.dynamicsql

import io.github.rxcats.datasourceroute.dynamicsql.CommonUserDynamicSqlSupport.commonUser
import io.github.rxcats.datasourceroute.dynamicsql.CommonUserDynamicSqlSupport.createdAt
import io.github.rxcats.datasourceroute.dynamicsql.CommonUserDynamicSqlSupport.nickname
import io.github.rxcats.datasourceroute.dynamicsql.CommonUserDynamicSqlSupport.shardNo
import io.github.rxcats.datasourceroute.dynamicsql.CommonUserDynamicSqlSupport.userId
import org.mybatis.dynamic.sql.SqlColumn
import org.mybatis.dynamic.sql.util.kotlin.CountCompleter
import org.mybatis.dynamic.sql.util.kotlin.DeleteCompleter
import org.mybatis.dynamic.sql.util.kotlin.SelectCompleter
import org.mybatis.dynamic.sql.util.kotlin.UpdateCompleter
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.*

fun CommonUserDynamicSqlMapper.count(completer: CountCompleter) =
    countFrom(this::count, commonUser, completer)

fun CommonUserDynamicSqlMapper.deleteAll() =
    deleteFrom(this::delete, commonUser) {

    }

fun CommonUserDynamicSqlMapper.delete(completer: DeleteCompleter) =
    deleteFrom(this::delete, commonUser, completer)

fun CommonUserDynamicSqlMapper.insert(row: CommonUserRow) =
    insert(
        this::insert,
        row,
        commonUser
    ) {
        map(userId) toProperty "userId"
        map(nickname) toProperty "nickname"
        map(shardNo) toProperty "shardNo"
        map(createdAt) toProperty "createdAt"
    }

fun CommonUserDynamicSqlMapper.selectOne(completer: SelectCompleter) =
    selectOne(
        this::selectOne,
        listOf(
            userId,
            nickname,
            shardNo,
            createdAt
        ),
        commonUser,
        completer
    )

fun CommonUserDynamicSqlMapper.selectOne(vararg columns: SqlColumn<*>, completer: SelectCompleter) =
    selectOne(
        this::selectOne,
        columns.toList(),
        commonUser,
        completer
    )

fun CommonUserDynamicSqlMapper.select(completer: SelectCompleter) =
    selectList(
        this::selectList,
        listOf(
            userId,
            nickname,
            shardNo,
            createdAt
        ),
        commonUser,
        completer
    )

fun CommonUserDynamicSqlMapper.select(vararg columns: SqlColumn<*>, completer: SelectCompleter) =
    selectList(
        this::selectList,
        columns.toList(),
        commonUser,
        completer
    )

fun CommonUserDynamicSqlMapper.update(completer: UpdateCompleter) =
    update(
        this::update,
        commonUser,
        completer
    )

fun CommonUserDynamicSqlMapper.update(row: CommonUserRow) =
    update {
        set(nickname) equalToWhenPresent row::nickname
        set(shardNo) equalToWhenPresent row::shardNo
        set(createdAt) equalToWhenPresent row::createdAt
        where {
            userId isEqualTo row.userId!!
        }
    }