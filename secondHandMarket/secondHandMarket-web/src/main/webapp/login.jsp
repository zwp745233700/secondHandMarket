<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>登陆界面</title>
</head>
<body>
	<form action="http://localhost:8080/secondHandMarket-web/user/login?username=1&password=11&code=11" method="post">
	
		用户名：<input type="text" name="username" /><br/>
		密码：<input type="text" name="password" /><br/>
		<input type="text" size="3" name="code"/><img id="img1" src="/secondHandMarket-web/user/code"/><a href="javascript:change()">看不清</a><br/>
	    	<input type="submit" value="登录"/>
	</form>
	
	<a href="http://localhost:8080/secondHandMarket-web/user/login?username=1&password=11&code=11" >提交</a>
	
    <script type="text/javascript">
    	function change(){
    		var imgObj = document.getElementById("img1");
    		imgObj.src="/secondHandMarket-web/user/code?time="+new Date().getTime();//浏览器发现地址没有变化，根本不发请求
    	}
    </script>
	
</body>
</html>