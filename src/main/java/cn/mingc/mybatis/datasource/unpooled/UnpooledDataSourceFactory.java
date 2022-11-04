package cn.mingc.mybatis.datasource.unpooled;

import cn.mingc.mybatis.datasource.DataSourceFactory;
import java.util.Properties;
import javax.sql.DataSource;

public class UnpooledDataSourceFactory implements DataSourceFactory {

    protected Properties properties;

    @Override
    public DataSource getDataSource() {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
