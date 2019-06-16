package com.tensquare.user.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: ZAO
 * @Date: 2019/6/10 15:46
 * @Description:Spring为我们提供了org.springframework.web.servlet.handler.HandlerInterceptorAdapter这个适配器，
 * 继承此类，可以非常方便的实现自己的拦截器。他有三个方法：
 * 分别实现预处理、后处理（调用了Service并返回ModelAndView，但未进行页面渲染）、返回处理（已经渲染了页面）
 * 在preHandle中，可以进行编码、安全控制等处理；
 * 在postHandle中，有机会修改ModelAndView；
 * 在afterCompletion中，可以根据ex是否为null判断是否发生了异常，进行日志记录。
 */

@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;


    @Override//ctrl+o
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("经过了拦截器");
       final String authHeader = request.getHeader("Authorization");
       if (authHeader!=null&&authHeader.startsWith("Bearer ")){
          final String token = authHeader.substring(7);// The part after "Bearer "
           Claims claims = jwtUtil.parseJWT(token);
           if (claims!=null){
               if ("admin".equals(claims.get("roles"))){
                   request.setAttribute("admin_claims",claims);
               }
               if ("user".equals(claims.get("roles"))){
                   request.setAttribute("user_claims",claims);
               }
           }
       }
      return true;
    }

}
