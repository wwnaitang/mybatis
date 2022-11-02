package cn.mingc.mybatis.mapping;

import java.util.Map;

public class MappedStatement {

    private String msid;

    private String sql;

    private String parameterType;

    private String resultType;

    private Map<Integer, String> parameter;

    private SqlCommandType commandType;

    public static class Builder {

        private MappedStatement statement;

        public Builder(String msid, SqlCommandType commandType, String sql) {
            statement = new MappedStatement();
            this.statement.msid = msid;
            this.statement.commandType = commandType;
            this.statement.sql = sql;
        }

        public Builder parameter(Map<Integer, String> parameter) {
            this.statement.parameter = parameter;
            return this;
        }

        public Builder parameterType(String parameterType) {
            this.statement.parameterType = parameterType;
            return this;
        }

        public Builder resultType(String resultType) {
            this.statement.resultType = resultType;
            return this;
        }

        public MappedStatement build() {
            assert this.statement.msid != null;
            assert this.statement.sql != null;
            assert this.statement.commandType != null;
            return this.statement;
        }
    }

    public String getMsid() {
        return msid;
    }

    public String getSql() {
        return sql;
    }

    public String getParameterType() {
        return parameterType;
    }

    public String getResultType() {
        return resultType;
    }

    public Map<Integer, String> getParameter() {
        return parameter;
    }

    public SqlCommandType getCommandType() {
        return commandType;
    }
}
