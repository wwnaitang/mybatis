package cn.mingc.mybatis.session;

public interface SqlSession {

    Object selectOne(String statementName, Object[] args) throws Exception;

    <T> T getMapper(Class<T> clazz);

    Configuration getConfiguration();

}
