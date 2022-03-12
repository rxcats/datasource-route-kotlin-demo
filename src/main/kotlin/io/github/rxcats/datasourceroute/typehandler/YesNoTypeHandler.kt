package io.github.rxcats.datasourceroute.typehandler

import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.TypeHandler
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class YesNoTypeHandler : TypeHandler<Boolean> {
    override fun setParameter(ps: PreparedStatement, i: Int, parameter: Boolean, jdbcType: JdbcType) =
        ps.setString(i, if (parameter) "Y" else "N")

    override fun getResult(rs: ResultSet, columnName: String) =
        "Y" == rs.getString(columnName)

    override fun getResult(rs: ResultSet, columnIndex: Int) =
        "Y" == rs.getString(columnIndex)

    override fun getResult(cs: CallableStatement, columnIndex: Int) =
        "Y" == cs.getString(columnIndex)
}