package cn.mingc.mybatis.executor.statement;

import cn.mingc.mybatis.executor.Executor;
import cn.mingc.mybatis.mapping.BoundSql;
import cn.mingc.mybatis.mapping.MappedStatement;
import cn.mingc.mybatis.session.Configuration;
import cn.mingc.mybatis.session.ResultHandle;

public abstract class BaseStatementHandle implements StatementHandle {

    protected Configuration configuration;

    protected Executor executor;

    protected MappedStatement mappedStatement;

    protected Object parameters;

    protected ResultHandle resultHandle;

    protected BoundSql boundSql;

    public BaseStatementHandle(Configuration configuration, Executor executor,
            MappedStatement mappedStatement, Object parameters, ResultHandle resultHandle,
            BoundSql boundSql) {
        this.configuration = configuration;
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.parameters = parameters;
        this.resultHandle = resultHandle;
        this.boundSql = boundSql;
    }
}
