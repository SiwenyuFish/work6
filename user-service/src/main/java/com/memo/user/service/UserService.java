package com.memo.user.service;


import com.memo.user.pojo.User;

public interface UserService {

    User findByUserName(String username);

    void register(String username, String password);

    String findUsernameById(Long id);

    void updateUserInfo(Long id, int count);
}
