package com.jsh.security.example.service;

import com.jsh.security.example.vo.UserVO;

public interface UserService {
    UserVO login(UserVO userVO);
    UserVO createUser(UserVO userVO);
    UserVO findUserByUserEmail(String userEmail);
}
