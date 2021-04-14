#### Spring Security
+ What is Spring Security?
    + Spring Security는 Spring 기반의 Application의 보안(인증과 권한,인가 등)을 담당하는 Spring 하위 Framewrork이다.
      Spring Security는 '인증'과 '권한'에 대한 부분을 Filter 흐름에 따라 처리하고 있다.
      Filter는 Dispatcher Servlet으로 가기 전에 적용되므로 가장 먼저 URL 요청을 받지만 Interceptor는 Dispatcher와 Contoller사이에
      위치한다는 점에서 적용 시기의 차이가 있다.
      Spring Security는 보안과 관련해서 체계적으로 많은 옵션을 제공해주기 때문에 개발자 입장에서는 일일이 보안 관련 로직을 작성하지 않아도 된다는 장점이 있다.
      이러한 Spring Security의 아키텍처는 아래와 같다.

      ![Spring Security](https://github.com/suhojang/SpringSecurity/blob/master/Spring_security.png)

+ Spring Security 주요 모듈
  
  ![Spring Security_Module](https://github.com/suhojang/SpringSecurity/blob/master/Spring_security_module.png)
  + SecurityContextHolder: SecurityContextHolder는 보안 주체의 세부 정보를 포함하여 응용프로그램의 현재 보안 Context에 대한 세부 정보가 저장 된다.
    SecurityContextHolder는 기본적으로 SecurityContextHolder.MODE_INHERITABLETHREADLOCAL 방법과 SecurityContextHolder.MODE_THREADLOCAL 방법을 제공한다.

+ Dependency
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.20</version>
    <scope>provided</scope>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
  ```
+ Gradle
```groovy
implementation 'org.springframework.boot:spring-boot-starter-web'
implementation 'org.projectlombok:lombok:1.18.20'
testImplementation 'org.springframework.boot:spring-boot-starter-test'
```

+ Interceptor 생성
```java
ppackage com.jsh.interceptor.example.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoggerInterceptor implements HandlerInterceptor {
  private final Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    logger.info("[" + this.getClass().getName() + "] preHandle");
    return HandlerInterceptor.super.preHandle(request, response, handler);
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    logger.info("[" + this.getClass().getName() + "] postHandle");
    HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    logger.info("[" + this.getClass().getName() + "] afterCompletion");
    HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
  }
}
```

+ Configuration에 Interceptor 등록
```java
package com.jsh.interceptor.example.config;

import com.jsh.interceptor.example.interceptor.LoggerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LoggerInterceptor())
            .addPathPatterns("/*")              //include path
            .excludePathPatterns("/example");   //exclude path

    WebMvcConfigurer.super.addInterceptors(registry);
  }
}
```

+ Test를 위한 RestController 생성
```java
package com.jsh.interceptor.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class InterceptorController {
  private final Logger logger = LoggerFactory.getLogger(InterceptorController.class);

  @GetMapping("/home")
  public String home(){
    logger.info("home call");
    return "home call";
  }

  @GetMapping("/example")
  public String example(){
    logger.info("example call");
    return "example call";
  }
}
```

+ exclude path call
```groovy
2021-04-13 13:40:13.395  INFO 80560 --- [nio-8080-exec-5] c.j.i.e.c.InterceptorController          : example call
```
```groovy
Exclude Path Pattern을 감지하여 Interceptor가 catch 하지 않는다
```

+ include path call
```groovy
2021-04-13 13:41:40.880  INFO 80560 --- [nio-8080-exec-1] c.j.i.e.interceptor.LoggerInterceptor    : [com.jsh.interceptor.example.interceptor.LoggerInterceptor] preHandle
2021-04-13 13:41:40.880  INFO 80560 --- [nio-8080-exec-1] c.j.i.e.c.InterceptorController          : home call
2021-04-13 13:41:40.880  INFO 80560 --- [nio-8080-exec-1] c.j.i.e.interceptor.LoggerInterceptor    : [com.jsh.interceptor.example.interceptor.LoggerInterceptor] postHandle
2021-04-13 13:41:40.881  INFO 80560 --- [nio-8080-exec-1] c.j.i.e.interceptor.LoggerInterceptor    : [com.jsh.interceptor.example.interceptor.LoggerInterceptor] afterCompletion
```
```groovy
Include Path Pattern을 감지하여 Interceptor의 preHandle, postHandle, afterCompletion를 실행하는 것을 볼 수 있다.
```
