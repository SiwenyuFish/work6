package com.memo.user.service.Impl;

import com.memo.user.mapper.UserMapper;
import com.memo.user.pojo.User;
import com.memo.user.service.UserService;
import com.memo.common.util.SnowFlakeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    /**
     * 实现输出登录用户信息
     */
    @Override
    public User findByUserName(String username) {
        User user = userMapper.findByUserName(username);
        return user;
    }

    @Override
    public String findUsernameById(Long id){
        return  userMapper.findUsernameById(id);
    }


    @Override
    public void updateUserInfo(Long id, int count) {
        userMapper.updateUserInfo(id,count);
    }

    /**
     * 实现用户注册 将用户信息保存到数据库
     */
    @Override
    public void register(String username, String password) {
        userMapper.add(SnowFlakeUtil.getSnowFlakeId(),username,password);
    }


}
