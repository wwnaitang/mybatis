package cn.mingc.mybatis.session;

import cn.mingc.mybatis.builder.xml.XMLConfigBuilder;
import cn.mingc.mybatis.session.defaults.DefaultSqlSessionFactory;
import java.io.Reader;

public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Reader reader) {
        XMLConfigBuilder configBuilder = new XMLConfigBuilder(reader);
        return this.build(configBuilder.parse());
    }

    public SqlSessionFactory build(Configuration configuration) {
        return new DefaultSqlSessionFactory(configuration);
    }

}
