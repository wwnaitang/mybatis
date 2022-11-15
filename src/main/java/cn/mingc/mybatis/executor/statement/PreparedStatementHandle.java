package cn.mingc.mybatis.executor.statement;

import cn.mingc.mybatis.executor.Executor;
import cn.mingc.mybatis.mapping.BoundSql;
import cn.mingc.mybatis.mapping.MappedStatement;
import cn.mingc.mybatis.session.Configuration;
import cn.mingc.mybatis.session.ResultHandle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PreparedStatementHandle extends BaseStatementHandle {

    public PreparedStatementHandle(Configuration configuration,
            Executor executor,
            MappedStatement mappedStatement, Object[] parameters,
            ResultHandle resultHandle, BoundSql boundSql) {
        super(configuration, executor, mappedStatement, parameters, resultHandle, boundSql);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        String sql = boundSql.getSql();
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }

    @Override
    public void parameterize(Statement stmt) throws SQLException {
        PreparedStatement statement = (PreparedStatement) stmt;
        statement.setString(1, (String) parameters[0]);
    }

    @Override
    public <T> List<T> query(Statement stmt, ResultHandle resultHandle) throws SQLException {
        PreparedStatement statement = (PreparedStatement) stmt;
        statement.execute();
        return resultSetHandle.<T>handleResultSets(statement);
    }
}
