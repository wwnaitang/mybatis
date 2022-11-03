package cn.mingc.mybatis.datasource;

import java.util.Properties;
import javax.sql.DataSource;

public interface DataSourceFactory {

    DataSource getDataSource();

    void setProperties(Properties properties);

}
