<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="image/ico" href="../images/favicon.ico" rel="shortcut icon" />
<link href="../images/favicon.ico" rel="bookmark icon" />
<title>Uha 首页</title>
<link rel="stylesheet" href="../css/bootstrap.css">
<script src="../js/jquery-1.11.2.min.js"></script>
<style type="text/css">
#head {
	padding-top: 100px;
	padding
	height: 800px;
	
}
</style>

</head>
<body class="bg-blue-light">
	<%@ include file="header.jsp"%>
	<form id='formFile' name='formFile' method="post" action="../user/modifyAdmin.do" target='frameFile' enctype="multipart/form-data">
		<input size="100" type="file" name="file" id="headFileId" style="display: none" onchange="addFile()" /> <input type="hidden" name="userId" value="${user.userId }">
	</form>
	<iframe id='frameFile' name='frameFile' style='display: none;'></iframe>
    <div class="row padding-left padding-right">
	<div id="head" class="col-md-3 col-lg-3" >
		<div class="panel  panel-success ">
			<div class="panel-heading ">
				<strong><c:if test="${user.role==1 }">超级管理员</c:if> <c:if test="${user.role==2 }">运营人员</c:if> </strong>
			</div>
			<div class="thumbnail center-block" >
				<a href="javascript:selectUserHead()"> <img src="${user.getHeadUrl()}" class="img-thumbnail" id="headPic" width="100px" height="100px" alt="...">
				</a>
				<div class="caption">
					<p class="text-center">${user.name }</p>
				</div>
			</div>
		</div>
	</div>
	<div class="col-md-9 col-lg-9 " style="padding-top: 100px;">
		<div class="panel panel-default panel-success">
			<div class="panel-heading ">
				<strong>最新用户</strong>
			</div>
		<div class="panel-body">
        <div class="media-inline " style="overflow:auto">
            
			<c:forEach items="${lastUser }" var="u">
				<div class="text-center padding-right padding-bottom" style="width:120px;display:table-cell">
					<a class="margin-right"  style="width:100px;height:100px" href="javascript:showUser(${u.userId })"> 
					<img src="${u.getHeadUrl()}" style="width:100px;height:100px" class="img-circle" alt="">
					</a>
					<div > ${u.name } </div>
			 </div>
			</c:forEach>
			</div>
			</div>
			<div class="panel-footer "></div>
		</div>
		<div>
			<div class="pull-left panel panel-default panel-success" style="width: 48%">
				<div class="panel-heading ">
					<strong>用户数量总览</strong>
				</div>
				<div class="panel-body">
					<ul class="list-group ">
						<li class="list-group-item">运营人员<span class="float-right badge">${operatiorCount }</span></li>
						<li class="list-group-item">模拟用户<span class="float-right badge">${simulationCount }</span></li>
						<li class="list-group-item">真实用户<span class="float-right badge">${normalCount }</span></li>
					</ul>
				</div>
				<div class="panel-footer "></div>
			</div>
			<div class="pull-right panel  panel-default panel-success" style="width: 48%; margin-left: 20px;">
				<div class="panel-heading ">
					<strong>推文数量总览</strong>
				</div>
				<div class="panel-body">
					<ul class="list-group ">
						<li class="list-group-item">用户发布<span class="float-right badge">${publishCount }</span></li>
						<li class="list-group-item">杂志<span class="float-right badge">${magazineCount }</span></li>
						<li class="list-group-item">精选<span class="float-right badge">${exquisiteCount }</span></li>
						<li class="list-group-item">活动推文<span class="float-right badge">${activityCount }</span></li>
						<li class="list-group-item">未审核推文<span class="float-right badge">${noAuditingCount }</span></li>
						<li class="list-group-item">被举报推文<span class="float-right badge">${reportCount }</span></li>
						<li class="list-group-item">已删除<span class="float-right badge">${deleteCount }</span></li>
					</ul>
				</div>
				<div class="panel-footer"></div>
			</div>
		</div>
	</div>
	</div>
	<script src="../js/jquery-1.11.2.min.js"></script>
	<script src="../js/bootstrap.min.js"></script>
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

    function selectUserHead() {
        $("#headFileId").click();
    }

    function callback(str) {
        $("#headPic").attr("src", str);
        $("#formFile").reset();
    }
    function showUser(userId){
        window.location.href="showUser.do?userId="+userId; 
    }
</script>
</body>
</html>