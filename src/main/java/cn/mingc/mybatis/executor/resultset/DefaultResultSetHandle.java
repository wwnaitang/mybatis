package cn.mingc.mybatis.executor.resultset;

import cn.mingc.mybatis.executor.Executor;
import cn.mingc.mybatis.mapping.BoundSql;
import cn.mingc.mybatis.mapping.MappedStatement;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DefaultResultSetHandle implements ResultSetHandle {

    private Executor executor;

    private MappedStatement mappedStatement;

    private BoundSql boundSql;

    public DefaultResultSetHandle(Executor executor, MappedStatement mappedStatement,
            BoundSql boundSql) {
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.boundSql = boundSql;
    }

    @Override
    public <T> List<T> handleResultSets(Statement st) throws SQLException {
        try {
            ResultSet resultSet = st.getResultSet();
            Class<T> resultTypeClass = (Class<T>) Class.forName(boundSql.getResultType());
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<T> list = new ArrayList<>();
            while (resultSet.next()) {
                T obj  = resultTypeClass.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    Object value = resultSet.getObject(i);
                    String columnName = metaData.getColumnName(i);
                    String setMethodName = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                    Method setMethod = null;
                    if (value.getClass().equals(Timestamp.class)) {
                        setMethod = resultTypeClass.getMethod(setMethodName, Date.class);
                        value = new Date(((Timestamp) value).getTime());
                    } else {
                        setMethod = resultTypeClass.getMethod(setMethodName, value.getClass());
                    }
                    if (setMethod == null) {
                        continue;
                    }
                    setMethod.invoke(obj, value);
                }
                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            throw new SQLException("结果集处理失败，失败原因：" + e.getMessage(), e);
        }
    }
}
