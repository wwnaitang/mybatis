package cn.mingc.mybatis.test.dao.dao;

import cn.mingc.mybatis.test.dao.po.User;

public interface IUserMapper {

    User queryUserById(String userId);

}
