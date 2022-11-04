package cn.mingc.mybatis.datasource.pooled;

import cn.mingc.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import javax.sql.DataSource;

public class PooledDataSourceFactory extends UnpooledDataSourceFactory {

    @Override
    public DataSource getDataSource() {
        return null;
    }
}
