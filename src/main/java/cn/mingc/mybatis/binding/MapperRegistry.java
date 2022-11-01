package cn.mingc.mybatis.binding;

import cn.hutool.core.lang.ClassScanner;
import cn.mingc.mybatis.session.SqlSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapperRegistry {

    private Map<Class<?>, MapperProxyFactory<?>> knowMappers;

    public MapperRegistry() {
        this.knowMappers = new HashMap<>();
    }

    public <T> T getMapper(Class<T> clazz, SqlSession sqlSession) {
        MapperProxyFactory proxyFactory = knowMappers.get(clazz);
        return (T) proxyFactory.newInstance(sqlSession);
    }

    public void addMapper(String packageName) {
        Set<Class<?>> classSet = ClassScanner.scanPackage(packageName);
        for (Class<?> clazz : classSet) {
            this.addMapper(clazz);
        }
    }

    public void addMapper(Class<?> clazz) {
        if (!clazz.isInterface()) {
            return;
        }

        if (this.hasMapper(clazz)) {
            return;
        }

        this.knowMappers.put(clazz, new MapperProxyFactory<>(clazz));
    }

    public boolean hasMapper(Class<?> clazz) {
        return this.knowMappers.get(clazz) != null;
    }

}
