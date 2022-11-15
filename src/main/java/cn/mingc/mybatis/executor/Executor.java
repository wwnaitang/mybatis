package cn.mingc.mybatis.executor;

import cn.mingc.mybatis.mapping.BoundSql;
import cn.mingc.mybatis.mapping.MappedStatement;
import cn.mingc.mybatis.session.ResultHandle;
import cn.mingc.mybatis.transaction.Transaction;
import java.sql.SQLException;
import java.util.List;

public interface Executor {

    ResultHandle NO_RESULT_HANDLER = null;

    Transaction getTransaction();

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceRollback);

    <T> List<T> query(MappedStatement ms, Object[] parameters, ResultHandle resultHandle, BoundSql boundSql)
            throws SQLException;

}
