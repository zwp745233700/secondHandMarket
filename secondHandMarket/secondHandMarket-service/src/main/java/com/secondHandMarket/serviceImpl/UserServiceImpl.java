package com.secondHandMarket.serviceImpl;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.secondHandMarket.common.pojo.SecondMarketResult;
import com.secondHandMarket.common.util.IDUtils;
import com.secondHandMarket.common.util.SFTPUtil;
import com.secondHandMarket.mapper.UserMapper;
import com.secondHandMarket.pojo.User;
import com.secondHandMarket.pojo.UserExample;
import com.secondHandMarket.pojo.UserExample.Criteria;
import com.secondHandMarket.service.UserService;

/**  
* Description: TODO
* @author Kanject  
* @date 2018年4月21日
*/ 
@Service
public class UserServiceImpl implements UserService {

	@Value("${SFTP_ADDRESS}")
	private String SFTP_ADDRESS;
	@Value("${SFTP_PORT}")
	private Integer SFTP_PORT;
	@Value("${SFTP_USERNAME}")
	private String SFTP_USERNAME;
	@Value("${SFTP_PASSWORD}")
	private String SFTP_PASSWORD;
	@Value("${SFTP_BASE_PATH}")
	private String SFTP_BASE_PATH;
	@Value("${IMAGE_BASE_URL}")
	private String IMAGE_BASE_URL;
	@Autowired
	UserMapper userMapper;
	

	/**  
	* Description: 根据数据类型检查数据是否有重复
	* @author Kanject  
	*/ 
	@Override
	public SecondMarketResult checkData(String param, int type) {
		UserExample userExample = new UserExample();
		Criteria criteria = userExample.createCriteria();
		//根据数据类型检查数据
		//1、2、3、4分别代表username、mobile、email、identity
		if(1 == type) {
			criteria.andUsernameEqualTo(param);
		} else if (2 == type) {
			criteria.andMobileEqualTo(param);
		} else if (3 == type) {
			criteria.andEmailEqualTo(param);
		} else if (4 == type) {
			criteria.andIdentityEqualTo(param);
		}
		
		List<User> list = userMapper.selectByExample(userExample);
		for (User user : list) {
			System.out.println(user);
		}
		
		if(list == null || list.isEmpty()) {
			return SecondMarketResult.ok(false);//没有重复，返回false
		}
		return SecondMarketResult.ok(true);//有重复，返回true
	}

	/**  
	* Description: 注册服务
	* @author Kanject  
	*/ 
	@Override
	public SecondMarketResult register(User user) {
		//校验数据
		//校验用户名、密码不能为空
		if(StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
			return SecondMarketResult.build(411, "用户名或密码不能为空");
		}
		//校验身份证不能为空
		if(StringUtils.isBlank(user.getIdentity())) {
			return SecondMarketResult.build(412, "身份证不能为空");
		}
		//校验数据是否重复,1、2、3、4分别代表username、mobile、email、identity
		//用户名
		SecondMarketResult result = checkData(user.getUsername(), 1);
		if((boolean) result.getData()) {
			return SecondMarketResult.build(413, "用户名重复");
		}
		//手机号
		result = checkData(user.getMobile(), 2);
		if((boolean) result.getData()) {
			return SecondMarketResult.build(414, "手机号重复");
		}
		//邮箱
		result = checkData(user.getEmail(), 3);
		if((boolean) result.getData()) {
			return SecondMarketResult.build(415, "邮箱重复");
		}
		//身份证
		result = checkData(user.getIdentity(), 4);
		if((boolean) result.getData()) {
			return SecondMarketResult.build(416, "身份证重复");
		}
		//生成盐
		Random r = new Random();  
        StringBuilder sb = new StringBuilder(16);  
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));  
        int len = sb.length();  
        if (len < 16) {  
            for (int i = 0; i < 16 - len; i++) {  
                sb.append("0");  
            }  
        }  
        String salt = sb.toString();
        user.setSalt(salt);
		//密码进行MD5加盐加密
		String newPassword = user.getPassword() + user.getSalt();
		user.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
		user.setHeadpic("http://112.74.40.115/secondHand/default.jpg");
		user.setReportStatus(0);
		//插入数据
		userMapper.insert(user);
		
		return SecondMarketResult.ok();
	}

	/**  
	* Description: 登录服务
	* 	根据用户名从数据库调出对应用户信息，校验密码是否正确？
	* 		错误：返回“用户名或密码错误”
	* 		正确：返回从数据库得到的user
	* 	生成token，并将用户信息存进session
	* @author Kanject  
	*/ 
	@Override
	public SecondMarketResult login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<User> list = userMapper.selectByExample(example);
		if(list == null || list.isEmpty()) {
			return SecondMarketResult.build(417, "用户名或密码错误");
		}
		User user = list.get(0);
		//对输入的密码进行加盐后再进行比对
		password = password + user.getSalt();
		if(!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
			return SecondMarketResult.build(417, "用户名或密码错误");
		}
		if(user.getReportStatus() !=null){
			if(user.getReportStatus()==1 ){
				return SecondMarketResult.build(419, "该账户已被举报，暂时禁止登陆");
			}
		}
		
		if(user.getHeadpic()==null || "".equals(user.getHeadpic())){
			user.setHeadpic("http://112.74.40.115/secondHand/default.jpg");
		}
		
		request.getSession().setAttribute("user", user);

		return SecondMarketResult.ok(user);
	}

	/**  
	* Description: 注销
	* @author Kanject  
	*/ 
	@Override
	public SecondMarketResult cancel(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);//false代表：不创建session对象，只是从request中获取。
		if(session == null) {
			return SecondMarketResult.ok();
		}
		session.removeAttribute("user");
		session.invalidate();
		
		return SecondMarketResult.ok();
	}

	/**  
	* Description: 修改用户信息服务
	* @author Kanject  
	*/ 
	@Override
	public SecondMarketResult modify(User user,MultipartFile uploadFile, HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否已登录
		User db_user = (User) request.getSession().getAttribute("user");
		if(db_user == null) {
			return SecondMarketResult.build(410, "未登录");
		}
		
		//校验数据
		//校验用户名不能为空
		/*if(StringUtils.isBlank(user.getUsername())) {
			return SecondMarketResult.build(400, "用户名不能为空");
		}
		//校验身份证不能为空
		if(StringUtils.isBlank(user.getIdentity())) {
			return SecondMarketResult.build(400, "身份证不能为空");
		}
		//校验数据是否重复,1、2、3、4分别代表username、mobile、email、identity
		//用户名
		SecondMarketResult result = checkData(user.getUsername(), 1);
		if((boolean) result.getData()) {
			return SecondMarketResult.build(400, "用户名重复");
		}
		//手机号
		result = checkData(user.getMobile(), 2);
		if((boolean) result.getData()) {
			return SecondMarketResult.build(400, "手机号重复");
		}
		//邮箱
		result = checkData(user.getEmail(), 3);
		if((boolean) result.getData()) {
			return SecondMarketResult.build(400, "邮箱重复");
		}
		//身份证
		result = checkData(user.getIdentity(), 4);
		if((boolean) result.getData()) {
			return SecondMarketResult.build(400, "身份证重复");
		}*/
		
		//修改信息
		db_user.setMobile(user.getMobile());
		db_user.setEmail(user.getEmail());
		db_user.setIdentity(user.getIdentity());
		//修改头像
		if(uploadFile != null && !("".equals(uploadFile))){
			try{
				String path = updHeadImage(uploadFile,db_user.getId());
				db_user.setHeadpic(path);
			}catch (Exception e) {
				return SecondMarketResult.build(500, "修改失败");
			}
		}
		userMapper.updateByPrimaryKey(db_user);
		return SecondMarketResult.ok();
	}

	/**  
	* Description: 修改密码服务
	* 	旧密码加盐加密后进行比对
	* 		错误：返回“原密码错误”；
	* 		正确：对新密码加盐加密，根据id对数据库进行修改
	* @author Kanject  
	*/ 
	@Override
	public SecondMarketResult modifyPassword(HttpServletRequest request, HttpServletResponse response,  String oldPwd,  String newPwd) {
		//判断用户是否已登录
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) {
			return SecondMarketResult.build(410, "未登录");
		}
		oldPwd = oldPwd + user.getSalt();
		if(!user.getPassword().equals(DigestUtils.md5DigestAsHex(oldPwd.getBytes()))) {
				return SecondMarketResult.build(418, "原密码错误");
		}
		
		newPwd = newPwd + user.getSalt();
		user.setPassword(DigestUtils.md5DigestAsHex(newPwd.getBytes()));
		userMapper.updateByPrimaryKey(user);
		
		return SecondMarketResult.ok();
	}

	
	public String updHeadImage(MultipartFile uploadFile, Integer userId) {
		
		//获取文件拓展名
		String originalFilename=uploadFile.getOriginalFilename();
		String ext=originalFilename.substring(originalFilename.lastIndexOf("."));
		//生成新的文件名
		String imageName=IDUtils.genImageName();
		
		//把图片上传到sftp服务器（图片服务器）
		String filePath="/secondHand/"+userId+"/head";//图片目录路径
		
		//sftp连接方式上传文件
		SFTPUtil sftp = new SFTPUtil(SFTP_USERNAME, SFTP_PASSWORD, SFTP_ADDRESS, SFTP_PORT);  
		try{
			sftp.login();
			sftp.upload(SFTP_BASE_PATH, filePath, imageName+ext, uploadFile.getInputStream());
			sftp.logout();  
		}catch (Exception e) {
			e.printStackTrace();
		}
		//可以访问到图片的url
		String path=IMAGE_BASE_URL + filePath + "/" + imageName + ext;
		
		return path;
	}

	@Override
	public SecondMarketResult getUserInfo(Integer userId) {
		try{
			User user = userMapper.selectByPrimaryKey(userId);
			
			if(user.getHeadpic()==null || "".equals(user.getHeadpic())){
				user.setHeadpic("http://119.29.194.197//secondHand/default.jpg");
			}
			
			return SecondMarketResult.ok(user);
		}catch (Exception e) {
			return SecondMarketResult.build(500, "查询用户信息失败");
		}
	}


}
