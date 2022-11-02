package cn.mingc.mybatis.session;

import cn.mingc.mybatis.binding.MapperRegistry;
import cn.mingc.mybatis.mapping.MappedStatement;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private MapperRegistry mapperRegistry;

    private Map<String, MappedStatement> statementMap;

    public Configuration() {
        this.mapperRegistry = new MapperRegistry();
        this.statementMap = new HashMap<>();
    }

    public <T> T getMapper(Class<T> clazz, SqlSession sqlSession) {
        return this.mapperRegistry.getMapper(clazz, sqlSession);
    }

    public void addMapper(Class<?> clazz) {
        this.mapperRegistry.addMapper(clazz);
    }

    public void addMappedStatement(MappedStatement statement) {
        this.statementMap.put(statement.getMsid(), statement);
    }

    public MappedStatement getMappedStatement(String msid) {
        return this.statementMap.get(msid);
    }
}
