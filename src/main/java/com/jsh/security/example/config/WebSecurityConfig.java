package com.jsh.security.example.config;

import com.jsh.security.example.filter.CustomAuthenticationFilter;
import com.jsh.security.example.handler.CustomLoginSuccessHanlder;
import com.jsh.security.example.provider.CustomAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        log.info("4. 정적 자원에 대한 Security 설정을 적용하지 않음");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("3. Login Authorization 설정");
        http.csrf().disable().authorizeRequests()
                .antMatchers("/about").authenticated()  // /about 요청에 대해서는 로그인을 요구함
                .antMatchers("/admin").hasRole("ADMIN") // /admin 요청에 대해서는 ROLE_ADMIN 역할을 가지고 있어야 함
                .anyRequest().permitAll()                          // 나머지 요청에 대해서는 로그인을 요구하지 않음
                .and()
                .formLogin()                                        // login에 대한 설정
                .loginPage("/user/loginView")                       // 로그인 페이지를 제공하는 URL을 설정함
                .successForwardUrl("/inedx")                        // 로그인 성공 URL을 설정함
                .failureForwardUrl("/index")                        // 로그인 실패 URL을 설정함
                .permitAll()
                .and()
                .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); //login실행 전 login parameter에 대한 filter실행
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        //filter 할 url정보
        customAuthenticationFilter.setFilterProcessesUrl("/user/login");
        //login 성공 Handler 설정
        customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());
        customAuthenticationFilter.afterPropertiesSet();

        log.info("2. AuthenticationFilter 설정");
        
        return customAuthenticationFilter;
    }

    @Bean
    public CustomLoginSuccessHanlder customLoginSuccessHandler() {
        return new CustomLoginSuccessHanlder();
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(){
        return new CustomAuthenticationProvider(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider());
        log.info("1. Web Security Provider 설정");
    }
}
