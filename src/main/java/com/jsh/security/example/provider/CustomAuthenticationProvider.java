package com.jsh.security.example.provider;

import com.jsh.security.example.vo.UserDetailsVO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Resource(name = "userDetailsService")
    private UserDetailsService userDetailsService;

    @NonNull
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token   = (UsernamePasswordAuthenticationToken) authentication;
        String userEmail    = token.getName();
        String userPw       = (String) token.getCredentials();

        UserDetailsVO userDetailsVO = (UserDetailsVO) userDetailsService.loadUserByUsername(userEmail);
        log.info("4. 전달 된 비밀번호와 조회 된 비밀번호를 비교");
        if (!passwordEncoder.matches(userPw, userDetailsVO.getPassword())){
            throw new BadCredentialsException(userDetailsVO.getUsername() + "Invalid password");
        }

        log.info("5. UsernamePasswordAuthenticationToken 정보 Return");

        return new UsernamePasswordAuthenticationToken(userDetailsVO, userPw, userDetailsVO.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
