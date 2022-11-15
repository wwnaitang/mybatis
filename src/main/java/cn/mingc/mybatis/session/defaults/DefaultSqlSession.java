package cn.mingc.mybatis.session.defaults;

import cn.mingc.mybatis.executor.Executor;
import cn.mingc.mybatis.mapping.BoundSql;
import cn.mingc.mybatis.mapping.MappedStatement;
import cn.mingc.mybatis.session.Configuration;
import cn.mingc.mybatis.session.SqlSession;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public Object selectOne(String statementName, Object[] args) throws Exception {
        MappedStatement ms = this.configuration.getMappedStatement(statementName);
        BoundSql boundSql = ms.getBoundSql();
        return executor.query(ms, args, Executor.NO_RESULT_HANDLER, boundSql).get(0);
    }

    @Override
    public <T> T getMapper(Class<T> clazz) {
        return this.configuration.getMapper(clazz, this);
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }
}
