package com.tensquare.sms.listener;

import com.tensquare.sms.util.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Auther: ZAO
 * @Date: 2019/6/9 19:11
 * @Description:短信监听类
 */

@Component
@RabbitListener(queues = "sms")
public class SmsListener {

    @Autowired
    private  SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String templateCode;//模板编号

    @Value("${aliyun.sms.sign_name}")
    private String signName;//签名


    @RabbitHandler
    public void sendSms(Map<String,String> map){
        System.out.println("手机号："+map.get("mobile"));
        System.out.println("验证码："+map.get("code"));
        /*try {
            smsUtil.sendSms(map.get("mobile"),templateCode,signName,"{\"code\":"+ map.get("code") +"}");
        } catch (ClientException e) {
            e.printStackTrace();
        }*/
    }

}