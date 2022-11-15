package cn.mingc.mybatis.executor.statement;

import cn.mingc.mybatis.executor.Executor;
import cn.mingc.mybatis.executor.resultset.ResultSetHandle;
import cn.mingc.mybatis.mapping.BoundSql;
import cn.mingc.mybatis.mapping.MappedStatement;
import cn.mingc.mybatis.session.Configuration;
import cn.mingc.mybatis.session.ResultHandle;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseStatementHandle implements StatementHandle {

    protected Configuration configuration;

    protected Executor executor;

    protected MappedStatement mappedStatement;

    protected Object[] parameters;

    protected ResultSetHandle resultSetHandle;

    protected BoundSql boundSql;

    public BaseStatementHandle(Configuration configuration, Executor executor,
            MappedStatement mappedStatement, Object[] parameters, ResultHandle resultHandle,
            BoundSql boundSql) {
        this.configuration = configuration;
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.parameters = parameters;
        this.resultSetHandle = configuration.newResultSetHandle(executor, mappedStatement, boundSql);
        this.boundSql = boundSql;
    }

    @Override
    public Statement prepare(Connection connection) throws SQLException {
        Statement statement = instantiateStatement(connection);
        statement.setQueryTimeout(350);
        statement.setFetchSize(10000);
        return statement;
    }

    protected abstract Statement instantiateStatement(Connection connection) throws SQLException;
}
