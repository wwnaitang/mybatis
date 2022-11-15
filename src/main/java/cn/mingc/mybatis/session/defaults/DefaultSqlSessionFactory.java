package cn.mingc.mybatis.session.defaults;

import cn.mingc.mybatis.executor.Executor;
import cn.mingc.mybatis.mapping.Environment;
import cn.mingc.mybatis.session.Configuration;
import cn.mingc.mybatis.session.SqlSession;
import cn.mingc.mybatis.session.SqlSessionFactory;
import cn.mingc.mybatis.transaction.Transaction;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        Environment environment = this.configuration.getEnvironment();
        Transaction transaction = environment.getTransaction();
        Executor executor = this.configuration.newExecutor(transaction);
        return new DefaultSqlSession(this.configuration, executor);
    }
}
