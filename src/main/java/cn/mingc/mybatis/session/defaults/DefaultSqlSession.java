package cn.mingc.mybatis.session.defaults;

import cn.mingc.mybatis.binding.MapperRegistry;
import cn.mingc.mybatis.session.SqlSession;

public class DefaultSqlSession implements SqlSession {

    private MapperRegistry mapperRegistry;

    public DefaultSqlSession(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    public Object select(String statement, Object param) {
        if ("loadUserCount".equals(statement)) {
            return 102;
        } else if ("loadUserName".equals(statement)) {
            if ("001".equals(param)) {
                return "mingc";
            } else {
                return "zhangsan";
            }
        }
        return null;
    }

    @Override
    public <T> T getMapper(Class<T> clazz) {
        return mapperRegistry.getMapper(clazz, this);
    }
}
