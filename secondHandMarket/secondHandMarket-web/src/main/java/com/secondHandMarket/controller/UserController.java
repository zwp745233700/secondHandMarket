package com.secondHandMarket.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.secondHandMarket.common.pojo.SecondMarketResult;
import com.secondHandMarket.common.util.ExceptionUtil;
import com.secondHandMarket.dto.LoginDto;
import com.secondHandMarket.pojo.User;
import com.secondHandMarket.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Description: 用户的相关操作
 * 
 * @author Kanject
 * @date 2018年4月20日
 */
@Controller
@RequestMapping("/user")
@Api(tags = "UserController", description = "用户的相关操作")
@EnableSwagger2
public class UserController {

	@Autowired
	UserService userService;

	/**
	 * Description: 表单校验接口
	 * @author Kanject
	 */
	@RequestMapping(value = "/check/{param}/{type}", method = RequestMethod.GET)
	@ResponseBody
	public Object checkData(@PathVariable String param, @PathVariable int type, String callback) {
		try {
			SecondMarketResult result = userService.checkData(param, type);
			if (StringUtils.isNotBlank(callback)) {
				// 请求为jsonp调用，需要支持
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * Description: 注册接口
	 * @author Kanject
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(httpMethod = "POST", value = "注册接口", notes = "参数：用户名username、手机号mobile、密码password、邮箱email、身份证identity")
	public SecondMarketResult register(@RequestBody User user) {
		try {
			return userService.register(user);
		} catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, ExceptionUtil.getStackTrace(e));
		}

	}

	/**
	 * Description: 登录接口
	 * @author Kanject
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(httpMethod = "POST", value = "登录接口", notes = "参数：用户名username、密码password")
	public SecondMarketResult login(@RequestBody LoginDto login,HttpServletRequest request, HttpServletResponse response) {
		//获取到验证码
	//	String code1=(String) request.getSession().getAttribute("code");
	//	System.out.println(code1);
	//	System.out.println(login.getCode());
		//if(login.getCode().equals(code1)){
			try {
				return userService.login(login.getUsername(), login.getPassword(), request, response);
			} catch (Exception e) {
				e.printStackTrace();
				return SecondMarketResult.build(500, ExceptionUtil.getStackTrace(e));
			}
	/*	}else{
			return SecondMarketResult.build(500, "验证码错误:"+"输入的验证码："+login.getCode()+";生成的验证码："+code1);
		}*/
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(httpMethod = "POST", value = "测试验证码登陆", notes = "参数：用户名username、密码password、验证码code")
	public SecondMarketResult test(@RequestBody LoginDto login,HttpServletRequest request, HttpServletResponse response) {
		//获取到验证码
		String code1=(String) request.getSession().getAttribute("code");
		System.out.println(code1);
		System.out.println(login.getCode());
		if(login.getCode().equals(code1)){
			try {
				return userService.login(login.getUsername(), login.getPassword(), request, response);
			} catch (Exception e) {
				e.printStackTrace();
				return SecondMarketResult.build(500, ExceptionUtil.getStackTrace(e));
			}
		}else{
			return SecondMarketResult.build(500, "验证码错误:"+"输入的验证码："+login.getCode()+";生成的验证码："+code1);
		}
	}

	/**
	 * Description: 注销接口
	 * @author Kanject
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(httpMethod = "GET", value = "注销接口", notes = "无参数")
	public SecondMarketResult cancel(HttpServletRequest request, HttpServletResponse response) {
		try {
			return userService.cancel(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}



	/**
	 * Description: 修改密码接口
	 * @author Kanject
	 */
	@RequestMapping(value = "/modifyPassword", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(httpMethod = "POST", value = "修改密码接口", notes = "参数：原密码oldPwd、新密码newPwd")
	public SecondMarketResult modifyPassword(HttpServletRequest request, HttpServletResponse response, @RequestParam String oldPwd, @RequestParam String newPwd) {
		try {
			return userService.modifyPassword(request, response, oldPwd, newPwd);
		} catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	/**
	 * Description: 修改用户信息接口
	 * @author Kanject
	 */
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(httpMethod = "POST", value = "修改用户信息接口", notes = "参数：用户名username、手机号mobile、邮箱email、身份证identity,用户头像uploadFile")
	public SecondMarketResult modify(User user,@RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile, HttpServletRequest request, HttpServletResponse response) {
		try {
			return userService.modify(user, uploadFile, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			return SecondMarketResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	//用户修改头像接口
	@RequestMapping(method=RequestMethod.POST,value="/POST/updImage/{userId}",produces = {"application/json"},consumes="multipart/*",headers="content-type=multipart/form-data")
	@ResponseBody
	@ApiOperation(httpMethod="POST",value="用户上传头像接口",notes="需要头像和用户id")
	public String updHeadImage(@ApiParam(value="上传的文件") MultipartFile uploadFile,final HttpServletRequest request,@PathVariable(value="userId") Integer userId){
		
		String result=userService.updHeadImage(uploadFile,userId);
		
		return result;
	}
	
	//图片验证码接口
	@RequestMapping(method=RequestMethod.GET,value="/code")
	@ResponseBody
	@ApiOperation(httpMethod="GET",value="图片验证码",notes="不需要参数")
	public void code(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		int width = 120;
		int height = 25;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);//创建一副内存图像
		Graphics g = image.getGraphics();//得到画笔
		
		g.setColor(Color.BLUE);
		g.drawRect(0, 0, width, height);//变现颜色
		g.setColor(Color.YELLOW);
		g.fillRect(1, 1, width-2, height-2);//背景色
		
		//划干扰线
		g.setColor(Color.GRAY);
		Random r = new Random();
		for(int i=0;i<9;i++)
			g.drawLine(r.nextInt(width), r.nextInt(height), r.nextInt(width), r.nextInt(height));
	
		//设置验证码
		g.setColor(Color.RED);
		g.setFont(new Font("宋体", Font.ITALIC|Font.BOLD, 19));
		//String base = "\u7684\u4e00\u4e86\u662f\u6211\u4e0d\u5728\u4eba\u4eec\u6709\u6765\u4ed6\u8fd9\u4e0a\u7740\u4e2a\u5730\u5230\u5927\u91cc\u8bf4\u5c31\u53bb\u5b50\u5f97\u4e5f\u548c\u90a3\u8981\u4e0b\u770b\u5929\u65f6\u8fc7\u51fa\u5c0f\u4e48\u8d77\u4f60\u90fd\u628a\u597d\u8fd8\u591a\u6ca1\u4e3a\u53c8\u53ef\u5bb6\u5b66\u53ea\u4ee5\u4e3b\u4f1a\u6837\u5e74\u60f3\u751f\u540c\u8001\u4e2d\u5341\u4ece\u81ea\u9762\u524d\u5934\u9053\u5b83\u540e\u7136\u8d70\u5f88\u50cf\u89c1\u4e24\u7528\u5979\u56fd\u52a8\u8fdb\u6210\u56de\u4ec0\u8fb9\u4f5c\u5bf9\u5f00\u800c\u5df1\u4e9b\u73b0\u5c71\u6c11\u5019\u7ecf\u53d1\u5de5\u5411\u4e8b\u547d\u7ed9\u957f\u6c34\u51e0\u4e49\u4e09\u58f0\u4e8e\u9ad8\u624b\u77e5\u7406\u773c\u5fd7\u70b9\u5fc3\u6218\u4e8c\u95ee\u4f46\u8eab\u65b9\u5b9e\u5403\u505a\u53eb\u5f53\u4f4f\u542c\u9769\u6253\u5462\u771f\u5168\u624d\u56db\u5df2\u6240\u654c\u4e4b\u6700\u5149\u4ea7\u60c5\u8def\u5206\u603b\u6761\u767d\u8bdd\u4e1c\u5e2d\u6b21\u4eb2\u5982\u88ab\u82b1\u53e3\u653e\u513f\u5e38\u6c14\u4e94\u7b2c\u4f7f\u5199\u519b\u5427\u6587\u8fd0\u518d\u679c\u600e\u5b9a\u8bb8\u5feb\u660e\u884c\u56e0\u522b\u98de\u5916\u6811\u7269\u6d3b\u90e8\u95e8\u65e0\u5f80\u8239\u671b\u65b0\u5e26\u961f\u5148\u529b\u5b8c\u5374\u7ad9\u4ee3\u5458\u673a\u66f4\u4e5d\u60a8\u6bcf\u98ce\u7ea7\u8ddf\u7b11\u554a\u5b69\u4e07\u5c11\u76f4\u610f\u591c\u6bd4\u9636\u8fde\u8f66\u91cd\u4fbf\u6597\u9a6c\u54ea\u5316\u592a\u6307\u53d8\u793e\u4f3c\u58eb\u8005\u5e72\u77f3\u6ee1\u65e5\u51b3\u767e\u539f\u62ff\u7fa4\u7a76\u5404\u516d\u672c\u601d\u89e3\u7acb\u6cb3\u6751\u516b\u96be\u65e9\u8bba\u5417\u6839\u5171\u8ba9\u76f8\u7814\u4eca\u5176\u4e66\u5750\u63a5\u5e94\u5173\u4fe1\u89c9\u6b65\u53cd\u5904\u8bb0\u5c06\u5343\u627e\u4e89\u9886\u6216\u5e08\u7ed3\u5757\u8dd1\u8c01\u8349\u8d8a\u5b57\u52a0\u811a\u7d27\u7231\u7b49\u4e60\u9635\u6015\u6708\u9752\u534a\u706b\u6cd5\u9898\u5efa\u8d76\u4f4d\u5531\u6d77\u4e03\u5973\u4efb\u4ef6\u611f\u51c6\u5f20\u56e2\u5c4b\u79bb\u8272\u8138\u7247\u79d1\u5012\u775b\u5229\u4e16\u521a\u4e14\u7531\u9001\u5207\u661f\u5bfc\u665a\u8868\u591f\u6574\u8ba4\u54cd\u96ea\u6d41\u672a\u573a\u8be5\u5e76\u5e95\u6df1\u523b\u5e73\u4f1f\u5fd9\u63d0\u786e\u8fd1\u4eae\u8f7b\u8bb2\u519c\u53e4\u9ed1\u544a\u754c\u62c9\u540d\u5440\u571f\u6e05\u9633\u7167\u529e\u53f2\u6539\u5386\u8f6c\u753b\u9020\u5634\u6b64\u6cbb\u5317\u5fc5\u670d\u96e8\u7a7f\u5185\u8bc6\u9a8c\u4f20\u4e1a\u83dc\u722c\u7761\u5174\u5f62\u91cf\u54b1\u89c2\u82e6\u4f53\u4f17\u901a\u51b2\u5408\u7834\u53cb\u5ea6\u672f\u996d\u516c\u65c1\u623f\u6781\u5357\u67aa\u8bfb\u6c99\u5c81\u7ebf\u91ce\u575a\u7a7a\u6536\u7b97\u81f3\u653f\u57ce\u52b3\u843d\u94b1\u7279\u56f4\u5f1f\u80dc\u6559\u70ed\u5c55\u5305\u6b4c\u7c7b\u6e10\u5f3a\u6570\u4e61\u547c\u6027\u97f3\u7b54\u54e5\u9645\u65e7\u795e\u5ea7\u7ae0\u5e2e\u5566\u53d7\u7cfb\u4ee4\u8df3\u975e\u4f55\u725b\u53d6\u5165\u5cb8\u6562\u6389\u5ffd\u79cd\u88c5\u9876\u6025\u6797\u505c\u606f\u53e5\u533a\u8863\u822c\u62a5\u53f6\u538b\u6162\u53d4\u80cc\u7ec6";
		int x = 20;
		String code="";
		for(int i=0;i<4;i++){
			int num=r.nextInt(10);
			code=code+num;
			g.drawString(num+"", x,20 );
			//g.drawString(base.charAt(r.nextInt(base.length()))+"", x, 20);
			x+=18;
		}
		//将验证码放到session中
		HttpSession session = request.getSession();
		session.setAttribute("code", code);
		
		response.setHeader("Expires", "-1");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		ImageIO.write(image, "jpg", response.getOutputStream());
	}
	
	
	@RequestMapping(value="/get/{userId}")
	@ResponseBody
	@ApiOperation(httpMethod="GET",value="查看用户信息",notes="需要用户id")
	public SecondMarketResult getUserInfo(@PathVariable(value="userId") Integer userId){
		
		return userService.getUserInfo(userId);
		
	}
}
