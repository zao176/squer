package com.tensquaer.spit.service;

import com.tensquaer.spit.dao.SpitDao;
import com.tensquaer.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import util.IdWorker;

import java.util.Date;
import java.util.List;

/**
 * @Auther: ZAO
 * @Date: 2019/6/6 17:31
 * @Description:
 */
@Service
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查询全部
     * @return
     */
    public List<Spit> findAll(){
        return spitDao.findAll();
    }

    /**
     * 根据主键查询实体
     * @param spitId
     * @return
     */
    public Spit findById(String spitId){
        return spitDao.findById(spitId).get();
    }

    /**
     * 增加 发布吐槽（或吐槽评论）
     * @param spit
     */
    public  void  add(Spit spit){
        spit.set_id(idWorker.nextId()+"");
        spit.setPublishtime(new Date());//发布日期
        spit.setVisits(0);//浏览量
        spit.setShare(0);//分享数
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//回复数
        spit.setState("1");//状态

        //判断是评论还是吐槽,spit.getParentid()不为空则是评论
        if (!StringUtils.isEmpty(spit.getParentid())){
           // Query query, Update update, String collectionName
            Query query=new Query(Criteria.where("_id").is(spit.getParentid()));
            Update update=new Update();
            update.inc("thumbup",1);
            mongoTemplate.updateFirst(query,update,"spit");
        }
        spitDao.save(spit);
    }

    /**
     * 修改更新吐槽
     * @param spit
     */
    public void update(Spit spit) {
        spitDao.save(spit);
    }

    /**
     * 删除吐槽
     * @param spitId
     */
    public void deleteById(String spitId) {
        spitDao.deleteById(spitId);
    }

    /**
     * 点赞
     * MongoTemplate类来实现对某列的操作
     * @param spitId
     */
  /*  public void updateThumbup(String spitId) {
        Spit spit = spitDao.findById(spitId).get();
        if(spit.getThumbup()==null){
        spit.getThumbup()=0;
        }
        spit.setThumbup(spit.getThumbup()+1);
        spitDao.save(spit);

       // 以上方法虽然实现起来比较简单，但是执行效率并不高，
        //因为我只需要将点赞数加1就可以了，没必要查询出所有字段修改后再更新所有字段。
        //我们可以使用MongoTemplate类来实现对某列的操作。
    }*/
    public void updateThumbup(String spitId) {
        //用mongodb $inc
       // Query query :where 后的条件
       // Update update:update 中的set
       // String collectionName 表名
        Query query=new Query(Criteria.where("_id").is(spitId));
        Update update=new Update();
        update.inc("thumbup",1);
        mongoTemplate.updateFirst(query,update,"spit");

    }

    /**
     * 根据上级ID查询吐槽列表
     * @param parentid
     * @param page
     * @param size
     * @return
     */

    public Page<Spit> findByParentid(String parentid,int page,int size ){
        PageRequest pageRequest = PageRequest.of(page-1, size);
        return  spitDao.findByParentid(parentid,pageRequest);
    }


}
