package cn.mingc.mybatis.datasource.unpooled;

import cn.mingc.mybatis.datasource.DataSourceFactory;
import java.util.Properties;
import javax.sql.DataSource;

public class UnpooledDataSourceFactory implements DataSourceFactory {

    protected Properties properties;

    @Override
    public DataSource getDataSource() {
        UnpooledDataSource dataSource = new UnpooledDataSource();
        dataSource.setDriver(properties.getProperty("driver"));
        dataSource.setUrl(properties.getProperty("url"));
        dataSource.setUsername(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));
        return dataSource;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
