package cn.mingc.mybatis.binding;

import cn.mingc.mybatis.session.SqlSession;
import java.lang.reflect.Proxy;

public class MapperProxyFactory<T> {

    private Class<T> mapperInterface;

    public MapperProxyFactory(Class<T> clazz) {
        this.mapperInterface = clazz;
    }

    public T newInstance(SqlSession sqlSession) {
        T proxy = (T) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class[]{this.mapperInterface}, new MapperProxy(this.mapperInterface, sqlSession));
        return proxy;
    }

}
