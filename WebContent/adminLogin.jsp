<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>史塔克后台管理-管理员登录</title>
	<link rel="stylesheet" href="../css/pintuer.css">
	<link rel="stylesheet" href="../css/admin.css">
	<script src="../js/jquery.js"></script>
	<script src="../js/pintuer.js"></script>
	<script src="../js/respond.js"></script>
	<script src="../js/admin.js"></script>
	<link type="image/x-icon" href="${webIcon }" rel="shortcut icon" />
	<link href="${webIcon }" rel="bookmark icon" />
	<script language="javascript">
		if (top != self) {
			if (top.location != self.location)
				top.location = self.location;
		}
	
		function change() {
			document.getElementById("code").src = "RandCodeServlet?sc="
					+ new Date().getTime();
		};
	
		function CheckEmpty() {
			
			if (fLogin.name.value == "") {
				//alert("please input username!");
				fLogin.UserName.focus();
				return;
			}
			if (fLogin.password.value == "") {
				//alert("please input password!");
				fLogin.PWD.focus();
				return;
			}
			//  if(fLogin.randcode.value==""){
			//        alert("please input idetifying code!");
			//        fLogin.IdentifyCode.focus();
			//       return;
			//   }
			fLogin.submit();
		};
		
		function callback(){
			alert("用户名或密码输入错误");
		}
	</script>
</head>
<body>
	<div class="container">
    <div class="line">
        <div class="xs6 xm4 xs3-move xm4-move">
            <br /><br />
            <div class="media media-y">
                <a href="http://www.uha.so" target="_blank"><img src="../images/LOGO-ok.png" class="radius" width="80px" height="80px" alt="后台管理系统" /></a>
            </div>
            <br /><br />
            <form action="./adminLogin.do" method="post" name="fLogin">
            <div class="panel">
                <div class="panel-head"><strong>登录史塔克后台管理系统</strong></div>
                <div class="panel-body" style="padding:30px;">
                <div class="form-group">
                <div class="field">
                    <div class="button-group button-group-justified radio">
                        <label class="button active"><input name="adminRole" value="1" checked="checked" type="radio"><span class="icon icon-check text-green"></span> 管理员</label>
                        <label class="button"><input name="adminRole" value="2" type="radio"><span class="icon icon-check text-green"></span> 运营人员</label>
                    </div>
                    </div>
                    </div>
                    <div class="form-group">
                        <div class="field field-icon-right">
                            <input type="text" class="input" name="name" placeholder="登录账号" data-validate="required:请填写账号,length#>=3:账号长度不符合要求" />
                            <span class="icon icon-user"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="field field-icon-right">
                            <input type="password" class="input" name="password" placeholder="登录密码" data-validate="required:请填写密码,length#>=5:密码长度不符合要求" />
                            <span class="icon icon-key"></span>
                        </div>
                    </div>
                    <c:if test="${error == true }">
                    <div class="text-red" >密码或用户名输入错误</div>
                    </c:if>
                    <!-- 
                    <div class="form-group">
                        <div class="field">
                            <input type="text" class="input" name="passcode" placeholder="填写右侧的验证码" data-validate="required:请填写右侧的验证码" />
                            <img src="./user/getRandCode"  width="80" height="32" id="code" class="passcode" />
                        </div>
                    </div>
                     -->
                </div>
                <div class="panel-foot text-center"><button class="button button-block bg-main text-big" onclick="CheckEmpty()">立即登录后台</button></div>
            </div>
            </form>
            
        </div>
    </div>
</div>
</body>
</html>