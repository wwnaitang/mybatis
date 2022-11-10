package cn.mingc.mybatis.mapping;

import cn.mingc.mybatis.session.Configuration;

public class MappedStatement {

    private Configuration configuration;

    private String msid;

    private SqlCommandType commandType;

    private BoundSql boundSql;

    public static class Builder {

        private MappedStatement statement;

        public Builder(Configuration configuration, String msid, SqlCommandType commandType, BoundSql boundSql) {
            statement = new MappedStatement();
            this.statement.configuration = configuration;
            this.statement.msid = msid;
            this.statement.commandType = commandType;
            this.statement.boundSql = boundSql;
        }

        public MappedStatement build() {
            assert this.statement.configuration != null;
            assert this.statement.msid != null;
            return this.statement;
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getMsid() {
        return msid;
    }

    public SqlCommandType getCommandType() {
        return commandType;
    }

    public BoundSql getBoundSql() {
        return boundSql;
    }
}
