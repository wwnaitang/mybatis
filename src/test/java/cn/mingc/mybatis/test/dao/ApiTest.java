package cn.mingc.mybatis.test.dao;

import cn.mingc.mybatis.datasource.pooled.PooledDataSource;
import cn.mingc.mybatis.io.Resources;
import cn.mingc.mybatis.session.SqlSession;
import cn.mingc.mybatis.session.SqlSessionFactory;
import cn.mingc.mybatis.session.SqlSessionFactoryBuilder;
import cn.mingc.mybatis.test.dao.dao.IUserMapper;
import cn.mingc.mybatis.test.dao.po.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;
import org.junit.Test;

public class ApiTest {

    private final Logger logger = Logger.getLogger(ApiTest.class.getName());

    @Test
    public void test_SqlSessionFactory() throws IOException {
        // 1. 从SqlSessionFactory中获取SqlSession
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 2. 获取映射器对象
        IUserMapper userDao = sqlSession.getMapper(IUserMapper.class);

        // 3. 测试验证
        for (int i = 0; i < 50; i++) {
            User user = userDao.queryUserById("10001");
            System.out.println(user);
        }
    }

    @Test
    public void test_pooled() throws SQLException, InterruptedException {
        PooledDataSource pooledDataSource = new PooledDataSource();
        pooledDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        pooledDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/mybatis?useUnicode=true");
        pooledDataSource.setUsername("root");
        pooledDataSource.setPassword("123456");
        // 持续获得链接
        while (true){
            Connection connection = pooledDataSource.getConnection();
            System.out.println(connection);
            Thread.sleep(1000);
            connection.close();
        }
    }


}
