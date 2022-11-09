package cn.mingc.mybatis.datasource.pooled;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;

public class PooledConnection implements InvocationHandler {

    private Connection realConnection;

    private PooledDataSource dataSource;

    public PooledConnection(Connection realConnection, PooledDataSource dataSource) {
        this.realConnection = realConnection;
        this.dataSource = dataSource;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        if ("close".equals(method.getName())) {
            this.dataSource.pushConnection(this);
            return null;
        } else {
            return method.invoke(this.realConnection, args);
        }
    }

    public Connection getRealConnection() {
        return realConnection;
    }
}
