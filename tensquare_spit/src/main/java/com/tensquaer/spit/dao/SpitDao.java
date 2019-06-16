package com.tensquaer.spit.dao;

import com.tensquaer.spit.pojo.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Auther: ZAO
 * @Date: 2019/6/6 17:29
 * @Description:吐槽数据访问层
 */
public interface SpitDao extends MongoRepository<Spit,String> {
    /**
     * 根据上级id查找吐槽列表(分页)
     * @param parentid
     * @param pageable
     * @return
     */
    public Page<Spit> findByParentid(String parentid,Pageable pageable);
}
