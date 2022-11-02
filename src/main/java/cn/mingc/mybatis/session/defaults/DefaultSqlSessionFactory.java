package cn.mingc.mybatis.session.defaults;

import cn.mingc.mybatis.session.Configuration;
import cn.mingc.mybatis.session.SqlSession;
import cn.mingc.mybatis.session.SqlSessionFactory;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(this.configuration);
    }
}
