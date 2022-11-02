package cn.mingc.mybatis.binding;

import cn.mingc.mybatis.mapping.MappedStatement;
import cn.mingc.mybatis.mapping.SqlCommandType;
import cn.mingc.mybatis.session.Configuration;
import cn.mingc.mybatis.session.SqlSession;
import java.lang.reflect.Method;

public class MapperMethod {

    private SqlCommand sqlCommand;

    public MapperMethod(Configuration configuration, Class<?> mapperInterface, Method method) {
        this.sqlCommand = new SqlCommand(configuration, mapperInterface, method);
    }

    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result = null;
        switch (this.sqlCommand.type) {
            case SELECT:
                result = sqlSession.selectOne(this.sqlCommand.name, args);
                break;
            case DELETE:
                break;
            case UPDATE:
                break;
            case INSERT:
                break;
            default:
                throw new RuntimeException("Unknow statement name: " + this.sqlCommand.name);
        }
        return result;
    }

    public static class SqlCommand {

        private final String name;

        private final SqlCommandType type;

        public SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method) {
            String statementName = mapperInterface.getName() + '.' + method.getName();
            MappedStatement statement = configuration.getMappedStatement(statementName);
            this.name = statementName;
            this.type = statement.getCommandType();
        }

        public String getName() {
            return name;
        }

        public SqlCommandType getType() {
            return type;
        }
    }

}
