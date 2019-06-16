package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.NoFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Auther: ZAO
 * @Date: 2019/6/11 10:30
 * @Description: 不喜欢列表数据访问层
 */
public interface NoFriendDao extends JpaRepository<NoFriend,String>, JpaSpecificationExecutor<NoFriend> {
}
