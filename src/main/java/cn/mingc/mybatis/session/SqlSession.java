package cn.mingc.mybatis.session;

public interface SqlSession {

    Object select(String statement, Object param);

    <T> T getMapper(Class<T> clazz);

}
