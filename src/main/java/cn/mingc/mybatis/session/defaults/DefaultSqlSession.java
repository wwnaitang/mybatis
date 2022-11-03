package cn.mingc.mybatis.session.defaults;

import cn.mingc.mybatis.mapping.Environment;
import cn.mingc.mybatis.mapping.MappedStatement;
import cn.mingc.mybatis.session.Configuration;
import cn.mingc.mybatis.session.SqlSession;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Object selectOne(String statementName, Object[] args) throws Exception {
        MappedStatement statement = this.configuration.getMappedStatement(statementName);
        Environment environment = this.configuration.getEnvironment();
        Connection connection = environment.getTransaction().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(statement.getSql());
        for (int i = 0; i < args.length; i++) {
            preparedStatement.setObject(i + 1, args[0]);
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        return this.resultSet2Obj(resultSet, Class.forName(statement.getResultType()));
    }

    @Override
    public <T> T getMapper(Class<T> clazz) {
        return this.configuration.getMapper(clazz, this);
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }

    private <T> T resultSet2Obj(ResultSet resultSet, Class<T> clazz) throws Exception {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        T obj = null;
        if (resultSet.next()) {
            obj = clazz.newInstance();
            for (int i = 1; i <= columnCount; i++) {
                Object value = resultSet.getObject(i);
                String columnName = metaData.getColumnName(i);
                String setMethodName = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                Method setMethod = clazz.getMethod(setMethodName, value.getClass());
                if (setMethod == null) {
                    continue;
                }
                setMethod.invoke(obj, value);
            }
        }
        return obj;
    }
}
