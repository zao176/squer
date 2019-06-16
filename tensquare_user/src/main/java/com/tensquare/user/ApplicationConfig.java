package com.tensquare.user;

import com.tensquare.user.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @Auther: ZAO
 * @Date: 2019/6/10 15:53
 * @Description:
 */
@Configuration
public class ApplicationConfig  extends WebMvcConfigurationSupport {
@Autowired
    private JwtInterceptor jwtInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor).
                //拦截路径
                addPathPatterns("/**").
                 // /admin/login
                // /user/login
                //过滤路径
                excludePathPatterns("/**/login");

    }




}
