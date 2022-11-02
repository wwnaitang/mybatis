package cn.mingc.mybatis.test.dao;

import cn.mingc.mybatis.io.Resources;
import cn.mingc.mybatis.session.SqlSession;
import cn.mingc.mybatis.session.SqlSessionFactory;
import cn.mingc.mybatis.session.SqlSessionFactoryBuilder;
import cn.mingc.mybatis.test.dao.dao.IUserMapper;
import cn.mingc.mybatis.test.dao.po.User;
import java.io.IOException;
import java.io.Reader;
import org.junit.Test;

public class ApiTest {

    @Test
    public void test_MapperProxyFactory() {
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader("mybatis-config.xml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        IUserMapper userMapper = sqlSession.getMapper(IUserMapper.class);
        userMapper.queryUserById("1002");
    }

}
