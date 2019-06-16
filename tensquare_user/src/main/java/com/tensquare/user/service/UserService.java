package com.tensquare.user.service;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class UserService {
	//打印日志
	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private IdWorker idWorker;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private BCryptPasswordEncoder encoder;
	/**
	 * 查询全部列表
	 *
	 * @return
	 */
	public List<User> findAll() {
		return userDao.findAll();
	}


	/**
	 * 条件查询+分页
	 *
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<User> findSearch(Map whereMap, int page, int size) {
		Specification<User> specification = createSpecification(whereMap);
		PageRequest pageRequest = PageRequest.of(page - 1, size);
		return userDao.findAll(specification, pageRequest);
	}


	/**
	 * 条件查询
	 *
	 * @param whereMap
	 * @return
	 */
	public List<User> findSearch(Map whereMap) {
		Specification<User> specification = createSpecification(whereMap);
		return userDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 *
	 * @param id
	 * @return
	 */
	public User findById(String id) {
		return userDao.findById(id).get();
	}

	/**
	 * 增加
	 *
	 * @param user
	 */
	public void add(User user) {
		user.setId(idWorker.nextId() + "");
		String encodePassword = encoder.encode(user.getPassword());
		user.setPassword(encodePassword);
		userDao.save(user);
	}

	/**
	 * 修改
	 *
	 * @param user
	 */
	public void update(User user) {
		userDao.save(user);
	}

	/**
	 * 删除
	 *
	 * @param id
	 */
	public void deleteById(String id) {
		userDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 *
	 * @param searchMap
	 * @return
	 */
	private Specification<User> createSpecification(Map searchMap) {

		return new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
				// ID
				if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
					predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
				}
				// 手机号码
				if (searchMap.get("mobile") != null && !"".equals(searchMap.get("mobile"))) {
					predicateList.add(cb.like(root.get("mobile").as(String.class), "%" + (String) searchMap.get("mobile") + "%"));
				}
			/*	// 密码
				if (searchMap.get("password") != null && !"".equals(searchMap.get("password"))) {
					predicateList.add(cb.like(root.get("password").as(String.class), "%" + (String) searchMap.get("password") + "%"));
				}*/
				// 昵称
				if (searchMap.get("nickname") != null && !"".equals(searchMap.get("nickname"))) {
					predicateList.add(cb.like(root.get("nickname").as(String.class), "%" + (String) searchMap.get("nickname") + "%"));
				}
				// 性别
				if (searchMap.get("sex") != null && !"".equals(searchMap.get("sex"))) {
					predicateList.add(cb.like(root.get("sex").as(String.class), "%" + (String) searchMap.get("sex") + "%"));
				}
				// 头像
				if (searchMap.get("avatar") != null && !"".equals(searchMap.get("avatar"))) {
					predicateList.add(cb.like(root.get("avatar").as(String.class), "%" + (String) searchMap.get("avatar") + "%"));
				}
				// E-Mail
				if (searchMap.get("email") != null && !"".equals(searchMap.get("email"))) {
					predicateList.add(cb.like(root.get("email").as(String.class), "%" + (String) searchMap.get("email") + "%"));
				}
				// 兴趣
				if (searchMap.get("interest") != null && !"".equals(searchMap.get("interest"))) {
					predicateList.add(cb.like(root.get("interest").as(String.class), "%" + (String) searchMap.get("interest") + "%"));
				}
				// 个性
				if (searchMap.get("personality") != null && !"".equals(searchMap.get("personality"))) {
					predicateList.add(cb.like(root.get("personality").as(String.class), "%" + (String) searchMap.get("personality") + "%"));
				}

				return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

	/**
	 * 发送验证码
	 *
	 * @param mobile
	 */
	public void sendsms(String mobile) {
		//1.判断手机号是否注册过了
		User user = userDao.findByMobile(mobile);
		if (null != user) {
			throw new RuntimeException("该手机已经注册过了，请直接登录");
		}
		//2. 判断是否已经发送过验证码了，验证码未失效.
		String key = "sms_code_" + mobile;
		String code = (String) redisTemplate.opsForValue().get(key);
		if (!StringUtils.isEmpty(code)) {
			throw new RuntimeException("验证码已发送过了,请注意查收!");
		}
		//3.生成6位数验证码
		code = RandomStringUtils.randomNumeric(6);
		// 4. 写入消息队列
		// 消息内容 map 手机号， 验证码
		Map<String, String> map = new HashMap<>();
		map.put("mobile", mobile);
		map.put("code", code);
		log.info("mobile={},code={}", mobile, code);//打印日志
		rabbitTemplate.convertAndSend("sms", map);//写入消息队列

		//5.保持到redis
		redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
	}

	/**
	 * 用戶註冊
	 *
	 * @param code
	 * @param user
	 */
	public void register(String code, User user) {
		//手机号不能为空
		if (StringUtils.isEmpty(user.getMobile())) {
			throw new RuntimeException("请输入手机号");
		}
		//判斷手機號碼是不是已經註冊了
		User olduser = userDao.findByMobile(user.getMobile());
		if (null != olduser) {
			throw new RuntimeException("该手机已经注册过了，请直接登录");
		}

		//判断redis中是不是有code
		String key = "sms_code_" + user.getMobile();
		String rediscode = (String) redisTemplate.opsForValue().get(key);
		if (StringUtils.isEmpty(rediscode)) {
			throw new RuntimeException("请先点击获取验证码!");
		}
		//存在判断是否一致
		if (!rediscode.equals(code)) {
			throw new RuntimeException("请输入正确的验证码!");
		}
		//验证码一致
		user.setId(idWorker.nextId() + "");
		user.setPassword(encoder.encode(user.getPassword()));//密码加密
		Date date = new Date();
		// 用户的初始化信息
		user.setFollowcount(0);//关注数
		user.setFanscount(0);//粉丝数
		user.setOnline(0L);//在线时长
		user.setRegdate(date);//注册日期
		user.setUpdatedate(date);//更新日期
		user.setLastdate(date);//最后登陆日期

		userDao.save(user);

	}

	/**
	 * 用戶登錄
	 *
	 * @param mobile
	 * @param password
	 * @return
	 */
	public User findByMobileAndPassword(String mobile, String password) {
		//1. 查询数据库中是否存在这个用户 mobile
		User user = userDao.findByMobile(mobile);
		//2. 如果不存在用户，返回null
    /*   if(StringUtils.isEmpty(user)){
       	return null;
	   }*/
		//3. 如果存在，校验密码
		if (user==null  || !encoder.matches(password, user.getPassword())) {
			return null;
		}
		/*if (!user.getPassword().equals(password)){
			return  null;
		}*/

		return user;
	}

	/*public static void main(String[] args) {
		String pwd = "admin";
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode(pwd));

		// 验证
		// 第一个参数：明文的密码，没加密的。
		// 第二个参数：加密后的密码
		boolean matches = encoder.matches(pwd, "$2a$10$hr.BSCx7NpscbPVfo5f.R.XogtvQQ.gFDsaaYpPZ/1OpuADY6n/s6");
		System.out.println(matches);
	}*/

	/**
	 * 更新粉丝数
	 * @param userid
	 * @param x
	 */
	@Transactional
	public void incFanscount(String userid, int x) {
		userDao.incFanscount(userid, x);
	}

	/**
	 * 更新点赞数
	 * @param userid
	 * @param x
	 */
	@Transactional
	public void incFollowcount(String userid, int x) {
		userDao.incFollowcount(userid, x);
	}

}