package com.tensquare.qa.client;

import com.tensquare.qa.client.impl.LabelClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Auther: ZAO
 * @Date: 2019/6/10 21:52
 * @Description:
 */

@FeignClient(value = "tensquare-base" ,fallback = LabelClientImpl.class)
public interface LabelClient {

    @RequestMapping(value="/label/{labelid}", method = RequestMethod.GET)
    public Result findById(@PathVariable("labelid") String labelid);
}