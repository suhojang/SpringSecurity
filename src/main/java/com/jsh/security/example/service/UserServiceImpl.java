package com.jsh.security.example.service;

import com.jsh.security.example.repository.UserRepository;
import com.jsh.security.example.vo.UserVO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service("userService")
public class UserServiceImpl implements UserService{
    @NonNull
    private UserRepository userRepository;

    @Override
    public UserVO login(UserVO userVO) {
        log.info("UserServiceImpl login call");
        return userRepository.findByUserEmailAndUserPw(userVO.getUserEmail(), userVO.getUserPw());
    }

    @Override
    public UserVO createUser(UserVO userVO) {
        log.info("UserServiceImpl createUser call");
        return userRepository.save(userVO);
    }

    @Override
    public UserVO findUserByUserEmail(String userEmail) {
        log.info("UserServiceImpl findUserByUserEmail call");
        return userRepository.findByUserEmail(userEmail).get();
    }
}
