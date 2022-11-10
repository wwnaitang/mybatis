package cn.mingc.mybatis.mapping;

import java.util.Map;

public class BoundSql {

    private String sql;

    private String parameterType;

    private String resultType;

    private Map<Integer, String> parameter;

    public BoundSql(String sql, String parameterType, String resultType,
            Map<Integer, String> parameter) {
        this.sql = sql;
        this.parameterType = parameterType;
        this.resultType = resultType;
        this.parameter = parameter;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public Map<Integer, String> getParameter() {
        return parameter;
    }

    public void setParameter(Map<Integer, String> parameter) {
        this.parameter = parameter;
    }
}
