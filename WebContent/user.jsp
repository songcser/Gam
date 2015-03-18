<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Uha 用户管理</title>
<link rel="stylesheet" href="../css/bootstrap.css">

<style type="text/css">
body{
    margin-top:80px;
}
</style>
</head>
<body class="bg-blue-light">
	<%@ include file="header.jsp"%>
	<div id="mainBody" class="container-fluid" >
		<div class="row" style="height:100%">
			<div class="col-sm-4 margin radius" style="height:100%;">
			     <div class="input-group margin-bottom">
                    <input type="text" class="form-control" placeholder="Search By Name">
                    <span class="input-group-btn">
                        <button class="btn btn-default" type="button" onclick="searchByName()">Go!</button>
                    </span>
                </div>
                
				<%@ include file="userList.jsp"%>
			</div>
			<div class="col-sm-6 " id="articleList" style="height:100%;overflow:auto">
				<div id="addOperationDiv" style="width:70%">
                <%@ include file="createUser.jsp"%>
                </div>
                <div id="articlesDiv" style="display: none">
                    <div id="articleListDiv" class="" ></div>
                    <div id="paginationDiv" class="button-group button-group-justified bg-mix" ></div>
                </div>
			</div>
		</div>
	</div>
	<script src="../js/jquery-1.11.2.min.js"></script>
	<script src="../js/bootstrap.min.js"></script>
	<script src="../js/main.js"></script>
	<script src="../js/content.js"></script>
	<script type="text/javascript">
	var total = document.documentElement.clientHeight-90;
	document.getElementById("mainBody").style.height=total+"px";
	
	function searchByName(){
		window.location = "searchByName.do";
	}
	</script>
</body>
</html>