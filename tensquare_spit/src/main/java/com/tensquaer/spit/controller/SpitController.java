package com.tensquaer.spit.controller;

import com.tensquaer.spit.pojo.Spit;
import com.tensquaer.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Auther: ZAO
 * @Date: 2019/6/6 17:33
 * @Description:
 */
@RestController
@CrossOrigin
@RequestMapping ("/spit")
public class SpitController {
    @Autowired
    private SpitService spitService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private HttpServletRequest request;

    /**
     * 增加吐槽
     * @param spit
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Spit spit) {
        Claims claims = (Claims) request.getAttribute("user_claims");
       if (claims==null){
           return new Result(false,StatusCode.ACCESSERROR,"无权访问");
       }
        spit.setUserid(claims.getId());
        spitService.add(spit);
        return Result.success("添加成功");
    }

    /**
     * 全部列表
     * @return
     */
    @GetMapping
    public Result findAll() {
        List<Spit> spitList = spitService.findAll();
        return Result.success("添加成功", spitList);
    }

    /**
     * 根据 spitId查询
     * @param spitId
     * @return
     */
    @GetMapping(value = "/{spitId}")
    public Result findById(@PathVariable String spitId) {
        Spit spit = spitService.findById(spitId);
        return Result.success("查询成功", spit);
    }

    /**
     * 修改吐槽
     * @param spit
     * @param spitId
     * @return
     */
    @PutMapping(value = "/{spitId}")
    public Result update(@RequestBody Spit spit, @PathVariable String spitId) {
        spit.set_id(spitId);
        spitService.update(spit);
        return Result.success("修改成功");
    }

    /**
     * 删除吐槽
     * @param spitId
     * @return
     */
    @DeleteMapping(value = "/{spitId}")
    public Result delete(@PathVariable String spitId) {

        spitService.deleteById(spitId);
        return Result.success("修改成功");
    }

    /**
     * 吐槽点赞
     * @param spitId
     */
    @PutMapping(value = "/thumbup/{spitId}")
    public Result updateThumbup(@PathVariable String spitId) {
        //判断用户是否点过赞
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims==null){
            return new Result(false,StatusCode.ACCESSERROR,"请先登录");
        }
        String userid=claims.getId();
        if (redisTemplate.opsForValue().get("thumbup_"+userid+"_"+spitId)!=null){
            return new Result(false, StatusCode.REPERROR,"你已经点过赞了");
        }
        spitService.updateThumbup(spitId);
        redisTemplate.opsForValue().set("thumbup_"+userid+"_"+spitId,1);
        return  Result.success("点赞成功");
    }
    /**
     * 根据上级ID查询吐槽列表
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value="/comment/{parentId}/{page}/{size}")
    public Result findByParentid(@PathVariable String parentid, @PathVariable int page, @PathVariable int size ){
        Page<Spit> pageList = spitService.findByParentid(parentid, page, size);
        return  Result.success("查询成功",new PageResult<Spit>(pageList.getTotalElements(), pageList.getContent() ));
    }
}