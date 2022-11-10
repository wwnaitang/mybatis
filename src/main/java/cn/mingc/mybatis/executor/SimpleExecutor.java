package cn.mingc.mybatis.executor;

import cn.mingc.mybatis.executor.statement.StatementHandle;
import cn.mingc.mybatis.mapping.BoundSql;
import cn.mingc.mybatis.mapping.MappedStatement;
import cn.mingc.mybatis.session.Configuration;
import cn.mingc.mybatis.session.ResultHandle;
import cn.mingc.mybatis.transaction.Transaction;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SimpleExecutor extends BaseExecutor {

    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    protected <T> List<T> doQuery(MappedStatement ms, Object[] parameters,
            ResultHandle resultHandle, BoundSql boundSql) throws SQLException {
        StatementHandle handle = configuration.newStatementHandle(this, ms, parameters, resultHandle, boundSql);
        Connection connection = transaction.getConnection();
        Statement stmt = handle.prepare(connection);
        handle.parameterize(stmt);
        return handle.query(stmt, resultHandle);
    }
}
