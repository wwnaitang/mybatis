package cn.mingc.mybatis.datasource.pooled;

import java.util.ArrayList;
import java.util.List;

public class PoolState {

    protected final List<PooledConnection> activeConnections = new ArrayList<>();

    protected final List<PooledConnection> idleConnections = new ArrayList<>();

    protected long checkoutTime = 0;

    protected long waitingCount = 0;

    protected long waitTime = 0;

    protected long badConnectionCount = 0;

}
