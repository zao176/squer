package com.tensquare.article.service;

import com.tensquare.article.dao.CommentDao;
import com.tensquare.article.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.List;

/**
 * @Auther: ZAO
 * @Date: 2019/6/7 16:51
 * @Description:
 */
@Service
public class CommentService {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private IdWorker idWorker;

    /**
     * 增加评论
     * @param comment
     */
    public void add(Comment comment){
        comment.set_id(idWorker.nextId()+"");
        commentDao.save(comment);
    }

    /**
     * 查询评论
     * @param articleid
     * @return
     */
    public List<Comment> findByAriticleid(String articleid) {

        return commentDao.findByArticleid(articleid);
    }

    /**
     * 删除评论
     * @param commentId
     */
    public void deleteById(String commentId) {

       commentDao.deleteById(commentId);
    }
}
