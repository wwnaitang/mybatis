package cn.mingc.mybatis.session.defaults;

import cn.mingc.mybatis.binding.MapperRegistry;
import cn.mingc.mybatis.session.SqlSession;
import cn.mingc.mybatis.session.SqlSessionFactory;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private MapperRegistry mapperRegistry;

    public DefaultSqlSessionFactory(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(this.mapperRegistry);
    }
}
