package cn.mingc.mybatis.executor.resultset;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface ResultSetHandle {

    <T> List<T> handleResultSets(Statement st) throws SQLException;

}
