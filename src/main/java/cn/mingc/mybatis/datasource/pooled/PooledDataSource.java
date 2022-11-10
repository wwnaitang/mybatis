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

    private final Logger logger = Logger.getLogger(PooledDataSource.class.getName());

    private UnpooledDataSource dataSource;

    private final PoolState state;

    private long maxActiveConnectionSize = 10;

    private long maxIdleConnectionSize = 5;

    private long waitTimeOut = 5000;

    private long maxBadConnectionCount = 3;

    private long overTime = 5000;

    public PooledDataSource() {
        this.dataSource = new UnpooledDataSource();
        this.state = new PoolState();
    }

    /**
     * 回收链接
     * @param connection
     * @throws SQLException
     */
    public void pushConnection(PooledConnection connection) throws SQLException {
        logger.info("回收链接...");
        synchronized (this.state) {
            this.state.activeConnections.remove(connection);
            if (connection.isValid()) {
                logger.info("链接有效");
                this.state.checkoutTime += connection.getCheckoutTime();
                if (!connection.getRealConnection().getAutoCommit()) {
                    connection.getRealConnection().rollback();
                }
                if (this.state.idleConnections.size() < this.maxIdleConnectionSize) {
                    logger.info("空闲链接列表不足，链接放到空闲列表");
                    PooledConnection newConnection = new PooledConnection(
                            connection.getRealConnection(), this);
                    newConnection.setCreateTime(connection.getCreateTime());
                    newConnection.setLatestUsedTime(connection.getLatestUsedTime());
                    this.state.idleConnections.add(newConnection);
                    this.state.notify();
                } else {
                    logger.info("空闲链接列表已满，关闭链接");
                    connection.getRealConnection().close();
                    connection.invalidate();
                }
            } else {
                logger.warning("无效的链接");
                this.state.badConnectionCount++;
            }
        }
    }

    /**
     * 获取链接
     * @return
     */
    public PooledConnection popConnection(String username, String password) throws SQLException {
        logger.info("获取链接...");
        PooledConnection connection = null;
        boolean isWaiting = false;
        int localBadConnectionCount = 0;
        long localWaitedTime = 0;
        synchronized (this.state) {
            while (connection == null) {
                if (!this.state.idleConnections.isEmpty()) {
                    logger.info("存在空闲链接，直接获取");
                    connection = this.state.idleConnections.remove(0);
                } else {
                    if (this.state.activeConnections.size() < this.maxActiveConnectionSize) {
                        logger.info("不存在空闲链接且活跃链接列表未满，创建新的链接");
                        connection = new PooledConnection(this.dataSource.getConnection(username, password), this);
                    } else {
                        logger.info("无空闲链接且活跃链接列表已满，等待链接回收");
                        try {
                            if (!isWaiting) {
                                this.state.waitingCount++;
                            }
                            long waitStart = System.currentTimeMillis();
                            this.state.wait(this.waitTimeOut - localWaitedTime);
                            long waitTime = System.currentTimeMillis() - waitStart;
                            this.state.waitTime += waitTime;
                            localWaitedTime += waitTime;
                            if (localWaitedTime >= this.overTime) {
                                throw new SQLException("获取链接超时");
                            }
                            logger.info("等待时长：" + waitTime);
                        } catch (InterruptedException e) {
                            throw new SQLException(e.getMessage(), e);
                        }
                    }
                }

                if (connection != null) {
                    if (connection.isValid()) {
                        logger.info("获取链接成功");
                        connection.setLatestUsedTime(System.currentTimeMillis());
                        this.state.activeConnections.add(connection);
                    } else {
                        logger.warning("获取链接无效");
                        this.state.badConnectionCount++;
                        localBadConnectionCount++;
                        connection = null;
                        if (localBadConnectionCount > this.maxBadConnectionCount) {
                            throw new SQLException("无效的链接");
                        }
                    }
                }
            }
        }
        logger.info(connection.getRealConnection().toString());
        return connection;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.getConnection(this.dataSource.getUsername(), this.dataSource.getPassword());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return (Connection) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] {Connection.class}, this.popConnection(username, password));
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return this.dataSource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        this.dataSource.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        this.dataSource.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return this.dataSource.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.dataSource.getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return this.dataSource.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return this.dataSource.isWrapperFor(iface);
    }

    public void setDriverClassName(String driverClassName) {
        this.dataSource.setDriverClassName(driverClassName);
    }

    public void setUrl(String url) {
        this.dataSource.setUrl(url);
    }

    public void setUsername(String username) {
        this.dataSource.setUsername(username);
    }

    public void setPassword(String password) {
        this.dataSource.setPassword(password);
    }
}
