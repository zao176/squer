package com.tensquare.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User>{

    User findByMobile(String mobile);

    /**
     * 更新粉丝数
     * @param userid
     * @param x
     */
   @Modifying
   @Query("update  User u set  u.fanscount=u.fanscount+?2 where u.id=?1")
    void incFanscount(String userid, int x);

    /**
     * 更新点赞数
     * @param userid
     * @param x
     */
    @Modifying
    @Query("update  User u set  u.followcount=u.followcount+?2 where u.id=?1")
    void incFollowcount(String userid, int x);
}


