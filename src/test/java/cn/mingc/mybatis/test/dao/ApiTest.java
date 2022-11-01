package cn.mingc.mybatis.test.dao;

import cn.mingc.mybatis.binding.MapperProxyFactory;
import cn.mingc.mybatis.binding.MapperRegistry;
import cn.mingc.mybatis.session.SqlSession;
import cn.mingc.mybatis.session.SqlSessionFactory;
import cn.mingc.mybatis.session.defaults.DefaultSqlSessionFactory;
import cn.mingc.mybatis.test.dao.dao.IUserMapper;
import org.junit.Assert;
import org.junit.Test;

public class ApiTest {

    @Test
    public void test_MapperProxyFactory() {
        MapperRegistry mapperRegistry = new MapperRegistry();
        mapperRegistry.addMapper("cn.mingc.mybatis.test.dao");
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(mapperRegistry);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        IUserMapper userMapper = sqlSession.getMapper(IUserMapper.class);
        Integer userCount = userMapper.loadUserCount();
        String userName1 = userMapper.loadUserName("001");
        String userName2 = userMapper.loadUserName("002");

        Assert.assertEquals(userCount.intValue(), 102);
        Assert.assertEquals(userName1, "mingc");
        Assert.assertEquals(userName2, "zhangsan");
    }

}
