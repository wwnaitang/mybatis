package cn.mingc.mybatis.binding;

import cn.mingc.mybatis.session.SqlSession;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapperProxyFactory<T> {

    private Class<T> mapperInterface;

    private Map<Method, MapperMethod> mapperMethodCache = new ConcurrentHashMap<>();

    public MapperProxyFactory(Class<T> clazz) {
        this.mapperInterface = clazz;
    }

    public T newInstance(SqlSession sqlSession) {
        T proxy = (T) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class[]{this.mapperInterface}, new MapperProxy(this.mapperInterface, sqlSession, this.mapperMethodCache));
        return proxy;
    }

}
