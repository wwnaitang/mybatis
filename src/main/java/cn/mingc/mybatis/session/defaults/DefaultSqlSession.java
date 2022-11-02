package cn.mingc.mybatis.session.defaults;

import cn.mingc.mybatis.mapping.MappedStatement;
import cn.mingc.mybatis.session.Configuration;
import cn.mingc.mybatis.session.SqlSession;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Object selectOne(String statementName, Object[] args) {
        MappedStatement statement = this.configuration.getMappedStatement(statementName);
        System.out.println(statement.getSql());
        return null;
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
