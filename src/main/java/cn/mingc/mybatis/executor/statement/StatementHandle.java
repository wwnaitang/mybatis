package cn.mingc.mybatis.executor.statement;

import cn.mingc.mybatis.session.ResultHandle;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public interface StatementHandle {

    Statement prepare(Connection connection);

    void parameterize(Statement stmt);

    <T> List<T> query(Statement stmt, ResultHandle resultHandle);

}
