package cn.mingc.mybatis.mapping;

import cn.mingc.mybatis.session.TransactionIsolationLevel;
import cn.mingc.mybatis.transaction.Transaction;
import cn.mingc.mybatis.transaction.TransactionFactory;
import javax.sql.DataSource;

public class Environment {

    private String id;

    private TransactionFactory transactionFactory;

    private DataSource dataSource;

    public static class Builder {

        private Environment environment = new Environment();

        public Builder(String id) {
            this.environment.id = id;
        }

        public Builder transactionFactory(TransactionFactory transactionFactory) {
            this.environment.transactionFactory = transactionFactory;
            return this;
        }

        public Builder dataSource(DataSource dataSource) {
            this.environment.dataSource = dataSource;
            return this;
        }

        public Environment build() {
            return this.environment;
        }
    }

    public Transaction getTransaction() {
        return this.transactionFactory.newTransaction(dataSource, TransactionIsolationLevel.READ_COMMITTED, true);
    }
}
