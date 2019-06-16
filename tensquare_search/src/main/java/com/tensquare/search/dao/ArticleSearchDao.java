package com.tensquare.search.dao;

import com.tensquare.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Auther: ZAO
 * @Date: 2019/6/7 20:19
 * @Description:
 */
public interface ArticleSearchDao extends ElasticsearchRepository<Article,String> {


    /**
     * 检索
     *
     * @param
     * @return
     */
    public Page<Article> findByTitleOrContentLike(String title, String content, Pageable pageable);

}