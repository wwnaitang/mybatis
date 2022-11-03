package cn.mingc.mybatis.datasource.druid;

import cn.mingc.mybatis.datasource.DataSourceFactory;
import com.alibaba.druid.pool.DruidDataSource;
import java.util.Properties;
import javax.sql.DataSource;

public class DruidDataSourceFactory implements DataSourceFactory {

    private Properties properties;

    @Override
    public DataSource getDataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setDriverClassName(this.properties.getProperty("driver"));
        datasource.setUrl(this.properties.getProperty("url"));
        datasource.setUsername(this.properties.getProperty("username"));
        datasource.setPassword(this.properties.getProperty("password"));
        return datasource;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
