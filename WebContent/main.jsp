<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>史塔克后台管理-首页</title>
<link rel="stylesheet" type="text/css" href="../css/styles.css">
<link rel="stylesheet" href="../css/pintuer.css">
<link type="image/x-icon" href="${webIcon }" rel="shortcut icon" />
<link href="${webIcon }" rel="bookmark icon" />
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<script src="../js/pintuer.js"></script>
<script src="../js/respond.js"></script>
<script src="../js/admin.js"></script>

<style type="text/css">


#head {
	padding-top: 100px;
	
	width: 300px;
	height: 800px;
	
}

</style>
<script type="text/javascript">
	function modifyUser() {
		$("#loginDIV").css({
			display : "block"
		});
		$("#mbDIV").css({
			display : "block"
		});
	}
	function addFile() {
		document.forms[0].submit();
	}
	
	function selectUserHead(){
		$("#headFileId").click();
	}
	
	function callback(str){
		$("#headPic").attr("src",str);
		$("#formFile").reset();
	}
</script>
</head>
<body>
    <div  style="height:100%">
	<ul id="header" >
		<li style="width: 70px"><a href="toMain.do">首页</a></li>
		<li><a href="operatorManage.do">账号管理</a></li>
		<li><a href="articleManage.do">推文管理</a></li>
		<li><a href="activityManage.do">活动管理</a></li>
		<li><a href="publishManage.do">发布管理</a></li>
		<li><a href="systemManage.do">系统管理</a></li>
	</ul>
   
		<form id='formFile' name='formFile' method="post" action="../user/modifyAdmin.do" target='frameFile' enctype="multipart/form-data">
			 <input size="100" type="file" name="file" id="headFileId" style="display:none" onchange="addFile()"/>
			 <input type="hidden" name="userId" value="${user.userId }" >
		</form>
		<iframe id='frameFile' name='frameFile' style='display: none;'></iframe>
	<div id="head" class="border-right float-left">
	   <div class="media media-y">
        <a href="javascript:selectUserHead()">
        <img src="${user.getHeadUrl()}" class="radius" id="headPic" width="80px" height="80px" alt="...">
       
        </a>
        <div class="media-body">
        <strong>${user.name }</strong>
            <c:if test="${user.role==1 }">超级管理员</c:if>
            <c:if test="${user.role==2 }">运营人员</c:if>
        </div>
        </div>
	</div>
    <div class="float-left margin-large" style="padding-top:100px;width:500px">
        <div class="panel border-red-light" style="width:100%">
            <div class="panel-head  border-red-light bg-red-light"><strong>用户数量总览</strong></div>
                <div class="panel-body">
                 <ul class="list-group list-striped">
                    <li>运营人员<span class="float-right badge">${operatiorCount }</span></li>
                    <li>模拟用户<span class="float-right badge">${simulationCount }</span></li>
                    <li>普通用户<span class="float-right badge">${normalCount }</span></li>
                </ul>
               </div>
            <div class="panel-foot border-red-light bg-red-light"></div>
        </div>
        <div class="panel border-red-light margin-top" style="width:100%">
            <div class="panel-head  border-red-light bg-red-light"><strong>推文数量总览</strong></div>
                <div class="panel-body">
                 <ul class="list-group list-striped">
                    <li>用户发布<span class="float-right badge">${publishCount }</span></li>
                    <li>杂志<span class="float-right badge">${magazineCount }</span></li>
                    <li>精选<span class="float-right badge">${exquisiteCount }</span></li>
                    <li>活动推文<span class="float-right badge">${activityCount }</span></li>
                    <li>未审核推文<span class="float-right badge">${noAuditingCount }</span></li>
                    <li>被举报推文<span class="float-right badge">${reportCount }</span></li>
                    <li>已删除<span class="float-right badge">${deleteCount }</span></li>
                </ul>
               </div>
            <div class="panel-foot border-red-light bg-red-light"></div>
        </div>
    </div>
     </div>
</body>
</html>