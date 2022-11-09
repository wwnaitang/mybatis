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

    public static void main(String[] args) throws InterruptedException {
        new ApiTest().test_MapperProxyFactory();
    }

    @Test
    public void test_MapperProxyFactory() throws InterruptedException {
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader("mybatis-config.xml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        IUserMapper userMapper = sqlSession.getMapper(IUserMapper.class);
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new MyRunnable(userMapper, i));
            thread.start();
        }
    }

    class MyRunnable implements Runnable {
        private int i;
        private IUserMapper userMapper;

        public MyRunnable(IUserMapper userMapper, int i) {
            this.userMapper = userMapper;
            this.i = i;
        }

        @Override
        public void run() {
            System.out.println("run in Thread " + i);
            User user = userMapper.queryUserById("10001");
            System.out.println(user);
        }

    }

}
