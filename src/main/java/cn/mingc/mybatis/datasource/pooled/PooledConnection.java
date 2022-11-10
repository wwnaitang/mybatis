package cn.mingc.mybatis.datasource.pooled;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

public class PooledConnection implements InvocationHandler {

    public static final String CLOSE_METHOD = "close";

    private Connection realConnection;

    private PooledDataSource dataSource;

    private boolean valid;

    private long createTime = 0;

    private long latestUsedTime = 0;

    public PooledConnection(Connection realConnection, PooledDataSource dataSource) {
        this.realConnection = realConnection;
        this.dataSource = dataSource;
        this.valid = true;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (CLOSE_METHOD.equals(method.getName())) {
            this.dataSource.pushConnection(this);
            return null;
        } else {
            if (!Object.class.equals(method.getDeclaringClass())) {
                checkConnection();
            }
            return method.invoke(this.realConnection, args);
        }
    }

    private void checkConnection() throws SQLException {
        if (!this.valid) {
            throw new SQLException("失效的链接");
        }
    }

    public void invalidate() {
        this.valid = false;
    }

    public boolean isValid() {
        return this.valid;
    }

    public Connection getRealConnection() {
        return realConnection;
    }

    public long getCheckoutTime() {
        return System.currentTimeMillis() - this.latestUsedTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLatestUsedTime() {
        return latestUsedTime;
    }

    public void setLatestUsedTime(long latestUsedTime) {
        this.latestUsedTime = latestUsedTime;
    }
}
