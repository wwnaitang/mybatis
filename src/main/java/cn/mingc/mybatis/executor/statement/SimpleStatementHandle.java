package cn.mingc.mybatis.executor.statement;

import cn.mingc.mybatis.executor.Executor;
import cn.mingc.mybatis.mapping.BoundSql;
import cn.mingc.mybatis.mapping.MappedStatement;
import cn.mingc.mybatis.session.Configuration;
import cn.mingc.mybatis.session.ResultHandle;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SimpleStatementHandle extends BaseStatementHandle {

    public SimpleStatementHandle(Configuration configuration,
            Executor executor,
            MappedStatement mappedStatement, Object[] parameters,
            ResultHandle resultHandle, BoundSql boundSql) {
        super(configuration, executor, mappedStatement, parameters, resultHandle, boundSql);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        return connection.createStatement();
    }

    @Override
    public void parameterize(Statement stmt) throws SQLException {

    }

    @Override
    public <T> List<T> query(Statement stmt, ResultHandle resultHandle) throws SQLException {
        String sql = boundSql.getSql();
        stmt.execute(sql);
        return resultSetHandle.<T>handleResultSets(stmt);
    }
}
