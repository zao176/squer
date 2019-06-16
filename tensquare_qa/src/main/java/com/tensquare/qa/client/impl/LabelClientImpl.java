package com.tensquare.qa.client.impl;

import com.tensquare.qa.client.LabelClient;
import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

/**
 * @Auther: ZAO
 * @Date: 2019/6/12 15:51
 * @Description:
 */

@Component
public class LabelClientImpl implements LabelClient {


    @Override
    public Result findById(String labelid) {
        return new Result(false, StatusCode.ACCESSERROR,"服务器启动失败，熔断器启动了");
    }
}
