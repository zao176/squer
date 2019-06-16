package com.tensquare.friend.controller;

import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: ZAO
 * @Date: 2019/6/11 08:20
 * @Description:
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;
    @Autowired
    private HttpServletRequest request;



    /**
     *  添加好友
     * @param friendid 对方用户ID
     * @param type  1：喜欢 0：不喜欢
     * @return
     */
    @PostMapping(value = "/like/{friendid}/{type}")
    public Result addFriend(@PathVariable String friendid,@PathVariable String type){

        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims==null){
            return new Result(false, StatusCode.ACCESSERROR,"无权访问");
        }
        //喜欢
        if (type.equals("1")){

            if (friendService.addFriend(claims.getId(),friendid)==0){
                return new Result(false, StatusCode.REPERROR,"已经添加过此好友");
            }


        }else {//不喜欢
            friendService.addNoFriend(claims.getId(),friendid);//向不喜欢列表中添加记录
        }
        return Result.success("操作成功");
    }

    /**
     * 删除好友
     * @param friendid
     * @return
     */
    @DeleteMapping(value = "/{friendid}")
    public Result remove(@PathVariable String friendid ){
        Claims claims = (Claims) request.getAttribute("user_claims");
    if (null==claims){
        return  new Result(false,StatusCode.ACCESSERROR,"无权访问");

    }
    friendService.deleteFriend(claims.getId(),friendid);

    return   Result.success("删除成功") ;
    }


}
