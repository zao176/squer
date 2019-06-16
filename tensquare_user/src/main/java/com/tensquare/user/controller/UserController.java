package com.tensquare.user.controller;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

	@Autowired
	private UserService userService;

@Autowired
private HttpServletRequest request;

@Autowired
private JwtUtil jwtUtil;
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true, StatusCode.OK,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param user
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
		userService.add(user);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 * 删除用户，必须拥有管理员权限，否则不能删除。
	 *
	 * 前后端约定：前端请求微服务时需要添加头信息Authorization ,内容为Bearer+空格+token
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
	/*	String authHeader = request.getHeader("Authorization");//获取头信息
		if (authHeader==null){
			return new Result(false,StatusCode.ACCESSERROR,"无权访问");
		}
		if (!authHeader.startsWith("Bearer ")){
			return new Result(false,StatusCode.ACCESSERROR,"无权访问");
		}
		String token = authHeader.substring(7);//提取token  "Bearer "有七位(0-6),所以从7开始截取
		Claims claims = jwtUtil.parseJWT(token);
		if (claims==null){
		return new Result(false,StatusCode.ACCESSERROR,"无权访问");
		}
		if (!"admin".equals(claims.get("roles"))){
		return new Result(false,StatusCode.ACCESSERROR,"无权访问");
		}*/

		Claims claims = (Claims) request.getAttribute("admin_claims");
		if (claims==null){
			return new Result(false,StatusCode.ACCESSERROR,"无权访问");
		}
		userService.deleteById(id);
		return Result.success("删除成功");
	}

    /**
     * 发送验证码
     * @param mobile
     * @return/sendsms/{mobile}
     */
    @PostMapping(value = "/sendsms/{mobile}")
    public  Result sendsms(@PathVariable String mobile) {
        userService.sendsms(mobile);
        return Result.success("验证码发送成功");

    }

    /**
     * 用户注册
     * @param code
     * @param user
     * @return
     */
    @PostMapping(value = "/register/{code}")
    public  Result register(@PathVariable String code,@RequestBody User user){
        userService.register(code,user);
        return Result.success("用户注册成功");
    }

    /**
     * 用户登录
     * @param loginMap
     * @return
     */
    @PostMapping(value = "/login")
    public  Result login(@RequestBody Map <String,String>loginMap){
        String mobile = loginMap.get("mobile");
        String password = loginMap.get("password");
       User  user= userService.findByMobileAndPassword(mobile,password);
       if (null==user){
           return new Result(false,StatusCode.LOGINERROR,"用戶名或者密码不正确");
       }
		String token = jwtUtil.createJWT(user.getId(), user.getNickname(), "user");
       Map map= new HashMap();
       map.put("token",token);
       map.put("name",user.getNickname());//昵称
       map.put("avatar",user.getAvatar());//头像
        return Result.success("登录成功",map);
    }

	/**
	 * 增加粉丝数
	 * @param userid
	 * @param x
	 */

    @PostMapping(value = "/incfans/{userid}/{x}")
	public  void  incFanscount(@PathVariable String userid,@PathVariable int x){
    	userService.incFanscount(userid,x);
	}

	/**
	 * 增加关注数
	 * @param userid
	 * @param x
	 */
	@PostMapping(value = "/incfollow/{userid}/{x}")
	public void incFollowcount(@PathVariable String userid,@PathVariable int x){
		userService.incFollowcount(userid,x);
	}
}
