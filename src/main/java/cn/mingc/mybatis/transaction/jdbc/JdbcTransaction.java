package cn.mingc.mybatis.transaction.jdbc;

import cn.mingc.mybatis.session.TransactionIsolationLevel;
import cn.mingc.mybatis.transaction.Transaction;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public class JdbcTransaction implements Transaction {

    private Connection connection;

    private DataSource dataSource;

    private TransactionIsolationLevel level;

    private boolean autoCommit;

    public JdbcTransaction(DataSource dataSource, TransactionIsolationLevel level,
            boolean autoCommit) {
        this.dataSource = dataSource;
        this.level = level;
        this.autoCommit = autoCommit;
    }

    @Override
    public Connection getConnection() throws SQLException {
        this.connection = this.dataSource.getConnection();
        this.connection.setTransactionIsolation(this.level.levelInt());
        this.connection.setAutoCommit(this.autoCommit);
        return this.connection;
    }

    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }

    @Override
    public void close() {

    }
}
