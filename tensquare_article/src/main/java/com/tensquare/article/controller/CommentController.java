package com.tensquare.article.controller;

import com.tensquare.article.pojo.Comment;
import com.tensquare.article.service.CommentService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: ZAO
 * @Date: 2019/6/7 16:58
 * @Description:评论
 */

@RestController
@CrossOrigin
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 增加评论
     * @param comment
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Comment comment){
        commentService.add(comment);
        return Result.success("提交成功");
    }

    /**
     * 根据 ariticleid 查找评论
     * @param articleid
     * @return
     */
    @GetMapping("/article/{articleid}")
    public Result findByAriticleid(@PathVariable String articleid){
        List<Comment> commentList=commentService.findByAriticleid(articleid);
        return Result.success("查询成功",commentList);
    }

    /**
     * 删除评论
     * @param commentId
     * @return
     */
    @DeleteMapping("/{commentId}")
    public Result deleteById(@PathVariable String commentId){
        commentService.deleteById(commentId);
        return Result.success("删除成功");
    }
}
