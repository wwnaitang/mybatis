package cn.mingc.mybatis.binding;

import cn.mingc.mybatis.session.SqlSession;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class MapperProxy<T> implements InvocationHandler {

    private Class<T> mapperInterface;

    private SqlSession sqlSession;

    private Map<Method, MapperMethod> mapperMethodCache;

    public MapperProxy(Class<T> clazz, SqlSession sqlSession, Map<Method, MapperMethod> cache) {
        this.mapperInterface = clazz;
        this.sqlSession = sqlSession;
        this.mapperMethodCache = cache;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        MapperMethod mapperMethod = this.cachedMapperMethod(method);
        return mapperMethod.execute(this.sqlSession, args);
    }

    private MapperMethod cachedMapperMethod(Method method) {
        MapperMethod mapperMethod = this.mapperMethodCache.get(method);
        if (mapperMethod == null) {
            mapperMethod = new MapperMethod(this.sqlSession.getConfiguration(), this.mapperInterface, method);
            this.mapperMethodCache.put(method, mapperMethod);
        }
        return mapperMethod;
    }
}
