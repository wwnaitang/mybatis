package cn.mingc.mybatis.datasource.pooled;

import cn.mingc.mybatis.datasource.unpooled.UnpooledDataSource;
import java.io.PrintWriter;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class PooledDataSource implements DataSource {

    private final UnpooledDataSource dataSource;

    private final PoolState pool;

    private static final int MAX_ACTIVE_CONNECTION_COUNT = 3;

    private static final int MAX_IDLE_CONNECTION_COUNT = 1;

    public PooledDataSource() {
        this.dataSource = new UnpooledDataSource();
        this.pool = new PoolState();
    }

    public PooledConnection popConnection(String username, String password) throws SQLException {
        PooledConnection connection = null;
        while (connection == null) {
            synchronized (pool) {
                if (pool.activeConnections.size() < MAX_ACTIVE_CONNECTION_COUNT) {
                    if (pool.idleConnections.size() > 0) {
                        connection = pool.idleConnections.remove(0);
                    } else {
                        connection = new PooledConnection(this.dataSource.getConnection(), this);
                    }
                    pool.activeConnections.add(connection);
                } else {
                    try {
                        pool.wait();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }
        return connection;
    }

    public void pushConnection(PooledConnection connection) throws SQLException {
        synchronized (pool) {
            pool.activeConnections.remove(connection);
            if (!connection.getRealConnection().getAutoCommit()) {
                connection.getRealConnection().rollback();
            }
            if (pool.idleConnections.size() < MAX_IDLE_CONNECTION_COUNT) {
                pool.idleConnections.add(connection);
            } else {
                connection.getRealConnection().close();
            }
            pool.notify();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.getConnection(this.dataSource.getUsername(), this.dataSource.getPassword());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        PooledConnection pooledConnection = this.popConnection(username, password);
        return (Connection) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { Connection.class }, pooledConnection);
    }

    @Override
    public <T> T unwrap(Class<T> clazz) throws SQLException {
        return this.dataSource.unwrap(clazz);
    }

    @Override
    public boolean isWrapperFor(Class<?> clazz) throws SQLException {
        return this.dataSource.isWrapperFor(clazz);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return this.dataSource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter printWriter) throws SQLException {
        this.dataSource.setLogWriter(printWriter);
    }

    @Override
    public void setLoginTimeout(int i) throws SQLException {
        this.dataSource.setLoginTimeout(i);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return this.dataSource.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.dataSource.getParentLogger();
    }

    public String getDriver() {
        return this.dataSource.getDriver();
    }

    public void setDriver(String driver) {
        this.dataSource.setDriver(driver);
    }

    public String getUrl() {
        return this.dataSource.getUrl();
    }

    public void setUrl(String url) {
        this.dataSource.setUrl(url);
    }

    public String getUsername() {
        return this.dataSource.getUsername();
    }

    public void setUsername(String username) {
        this.dataSource.setUsername(username);
    }

    public String getPassword() {
        return this.dataSource.getPassword();
    }

    public void setPassword(String password) {
        this.dataSource.setPassword(password);
    }
}
