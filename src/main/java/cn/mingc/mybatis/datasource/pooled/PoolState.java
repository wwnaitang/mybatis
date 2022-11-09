package cn.mingc.mybatis.datasource.pooled;

import java.util.ArrayList;
import java.util.List;

public class PoolState {

    List<PooledConnection> activeConnections = new ArrayList<>();

    List<PooledConnection> idleConnections = new ArrayList<>();

}
