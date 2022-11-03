package cn.mingc.mybatis.transaction;

import cn.mingc.mybatis.session.TransactionIsolationLevel;
import javax.sql.DataSource;

public interface TransactionFactory {

    Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit);

}
