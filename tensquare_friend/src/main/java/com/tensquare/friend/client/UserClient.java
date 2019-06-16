package com.tensquare.friend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Auther: ZAO
 * @Date: 2019/6/12 16:53
 * @Description:
 */

@FeignClient("tensquare-user")
public interface UserClient {

    /**
     * 增加粉丝数
     *
     * @param userid
     * @param x
     */

    @PostMapping(value = "/user/incfans/{userid}/{x}")
    public void incFanscount(@PathVariable("userid") String userid, @PathVariable("x") int x) ;

    /**
     * 增加关注数
     *
     * @param userid
     * @param x
     */
    @PostMapping(value = "/user/incfollow/{userid}/{x}")
    public void incFollowcount(@PathVariable("userid") String userid, @PathVariable("x") int x);
}
