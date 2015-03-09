<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>史塔克后台管理-发布管理</title>
<link rel="stylesheet" type="text/css" href="../css/styles.css">
<link rel="stylesheet" href="../css/pintuer.css">
 <link rel=stylesheet href="../ueditor/themes/default/css/ueditor.css" />
<link type="image/png" href="${webIcon }" rel="shortcut icon" />
<link href="${webIcon }" rel="bookmark icon" />
</head>
<body >
	<div style="height:100%">
		<ul id="header">
			<li style="width: 70px"><a href="toMain.do">首页</a></li>
			<li><a href="operatorManage.do">账号管理</a></li>
			<li><a href="articleManage.do">推文管理</a></li>
			<li><a href="activityManage.do">活动管理</a></li>
			<li><a href="publishManage.do">发布管理</a></li>
			<li><a href="systemManage.do">系统管理</a></li>
		</ul>
		<div style="width:80%">
		<form id="form" method="post" target="_blank" action="../article/richText.do">
		<script id="container" name="content" type="text/plain" >
        这里写你的初始化内容
        </script>
        <input type="submit" value="通过input的submit提交">
        
        </form>
        <input type="button" onclick="submit()" value="submit">
        </div>
	    <!-- 配置文件 -->
	    <script type="text/javascript" src="../ueditor/ueditor.config.js"></script>
	    <!-- 编辑器源码文件 -->
	    <script type="text/javascript" src="../ueditor/ueditor.all.js"></script>
	    <!-- 实例化编辑器 -->
	    <script type="text/javascript">
	        var ue = UE.getEditor('container');
	        function submit(){
	        	alert(ue.getContent());
	        }
	    </script>
	</div>
</body>
</html>