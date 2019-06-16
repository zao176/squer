package com.tensquare.sms;

import com.tensquare.sms.util.SmsUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

/**
 * @Auther: ZAO
 * @Date: 2019/6/9 14:20
 * @Description:
 */
@EnableEurekaClient
@SpringBootApplication
public class SmsApplication {
    public static void main(String[] args) {

        SpringApplication.run(SmsApplication.class,args);
    }
    @Bean
    public SmsUtil smsUtil(){
        return new SmsUtil();
    }
}

