package com.tensquaer.spit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import util.IdWorker;
import util.JwtUtil;

/**
 * @Auther: ZAO
 * @Date: 2019/6/6 17:24
 * @Description:
 */
@EnableEurekaClient
@SpringBootApplication
public class SpitApplicatione {
    public static void main(String[] args) {
        SpringApplication.run(SpitApplicatione.class,args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }

    @Bean
   public JwtUtil jwtUtil(){
        return new JwtUtil();
    }
}
