package cn.mingc.mybatis.transaction.jdbc;

import cn.mingc.mybatis.session.TransactionIsolationLevel;
import cn.mingc.mybatis.transaction.Transaction;
import cn.mingc.mybatis.transaction.TransactionFactory;
import javax.sql.DataSource;

public class JdbcTransactionFactory implements TransactionFactory {

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level,
            boolean autoCommit) {
        return new JdbcTransaction(dataSource, level, autoCommit);
    }
}
