package com.tensquare.search;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

/**
 * @Auther: ZAO
 * @Date: 2019/6/7 20:02
 * @Description:
 */
@EnableEurekaClient
@SpringBootApplication
public class SearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);

    }
        @Bean
        public IdWorker idWorker(){
            return new IdWorker(1,1);
        }
    }

