package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.ArticleSearchService;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleSearchController {

    @Autowired
    private ArticleSearchService articleSearchService;

    /**
     *  增加文章
     * @param article
     * @return
     */
    @PostMapping
    public Result save(@RequestBody Article article){
        articleSearchService.add(article);
        return  Result.success("操作成功");
    }

    /**
     * 检索
     * @param keywords
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value="/search/{keywords}/{page}/{size}")
    public Result findByKeywordsLike(@PathVariable String keywords, @PathVariable int page, @PathVariable int size){
        Page<Article> articlePage = articleSearchService.findByKeywordsLike(keywords,page,size);
        return  Result.success("查询成功",
                new PageResult<Article>(articlePage.getTotalElements(), articlePage.getContent()));
    }
}