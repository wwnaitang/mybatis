package cn.mingc.mybatis.executor;

import cn.mingc.mybatis.mapping.BoundSql;
import cn.mingc.mybatis.mapping.MappedStatement;
import cn.mingc.mybatis.session.Configuration;
import cn.mingc.mybatis.session.ResultHandle;
import cn.mingc.mybatis.transaction.Transaction;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public abstract class BaseExecutor implements Executor {

    protected final Logger logger = Logger.getLogger(this.getClass().getName());

    protected Configuration configuration;

    protected Transaction transaction;

    protected Executor wrapper;

    private boolean closed;

    public BaseExecutor(Configuration configuration, Transaction transaction) {
        this.configuration = configuration;
        this.transaction = transaction;
        this.wrapper = this;
    }

    @Override
    public Transaction getTransaction() {
        return this.transaction;
    }

    @Override
    public void commit(boolean required) throws SQLException {
        if (closed) {
            throw new SQLException("关闭的链接!");
        }
        if (required) {
            transaction.commit();
        }
    }

    @Override
    public void rollback(boolean required) throws SQLException {
        if (closed) {
            throw new SQLException("关闭的链接!");
        }
        if (required) {
            transaction.rollback();
        }
    }

    @Override
    public void close(boolean forceRollback) {
        try {
            try {
                rollback(forceRollback);
            } finally {
                transaction.close();
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        } finally {
            transaction = null;
            closed = true;
        }

    }

    @Override
    public <T> List<T> query(MappedStatement ms, Object[] parameters, ResultHandle resultHandle,
            BoundSql boundSql) throws SQLException {
        if (closed) {
            throw new SQLException("关闭的链接!");
        }
        return doQuery(ms, parameters, resultHandle, boundSql);
    }

    protected abstract <T> List<T> doQuery(MappedStatement ms, Object[] parameters, ResultHandle resultHandle,
            BoundSql boundSql) throws SQLException;
}
