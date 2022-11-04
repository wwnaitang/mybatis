package cn.mingc.mybatis.session;

import cn.mingc.mybatis.binding.MapperRegistry;
import cn.mingc.mybatis.datasource.druid.DruidDataSourceFactory;
import cn.mingc.mybatis.datasource.pooled.PooledDataSourceFactory;
import cn.mingc.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import cn.mingc.mybatis.mapping.Environment;
import cn.mingc.mybatis.mapping.MappedStatement;
import cn.mingc.mybatis.transaction.jdbc.JdbcTransactionFactory;
import cn.mingc.mybatis.type.TypeAliasRegistry;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private MapperRegistry mapperRegistry = new MapperRegistry();

    private Map<String, MappedStatement> statementMap = new HashMap<>();

    private Environment environment;

    private TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    public Configuration() {
        // transactionManager
        this.typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);

        // datasource
        this.typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
        this.typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);
        this.typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
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

    public TypeAliasRegistry getTypeAliasRegistry() {
        return this.typeAliasRegistry;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Environment getEnvironment() {
        return environment;
    }
}
