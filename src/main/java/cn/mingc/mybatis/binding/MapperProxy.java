package cn.mingc.mybatis.binding;

import cn.mingc.mybatis.session.SqlSession;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MapperProxy<T> implements InvocationHandler {

    private Class<T> mapperInterface;

    private SqlSession sqlSession;

    public MapperProxy(Class<T> clazz, SqlSession sqlSession) {
        this.mapperInterface = clazz;
        this.sqlSession = sqlSession;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        return this.sqlSession.select(method.getName(), args == null || args.length == 0 ? null : args[0]);
    }
}
