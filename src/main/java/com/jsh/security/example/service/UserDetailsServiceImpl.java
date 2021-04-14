package com.jsh.security.example.service;

import com.jsh.security.example.exception.UserNotFoundException;
import com.jsh.security.example.repository.UserRepository;
import com.jsh.security.example.vo.UserDetailsVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@AllArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        log.info("3. User 정보 조회 후 UserDetailsVO 로 형변환");

        return userRepository.findByUserEmail(userEmail)
                .map(u -> new UserDetailsVO(u, Collections.singleton(new SimpleGrantedAuthority(u.getRole().getValue()))))
                .orElseThrow(() -> new UserNotFoundException(userEmail))
                ;
    }
}
