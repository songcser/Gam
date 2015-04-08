<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="image/ico" href="../images/favicon.ico" rel="shortcut icon" />
<link href="../images/favicon.ico" rel="bookmark icon" />
<title>友哈-用户管理</title>
<link rel="stylesheet" href="../css/bootstrap.css">
<script src="../js/jquery-1.11.2.min.js"></script>
<style type="text/css">
body{
    margin-top:60px;
}
</style>
</head>
<body class="bg-blue-light ">
	<%@ include file="header.jsp"%>
	<div id="mainBody" class="container-fluid" >
		<div class="row" style="height:100%">
			<div class="col-sm-4 margin-left radius" style="height:90%;">
			 
                 <div class="margin-bottom">
                 <button class="btn bg-blue radius-rounded " onclick="createUser()">新建用户</button>
                 <a class="btn bg-green radius-rounded  pull-right margin-left" href="./markUser.do">标记用户</a>
                 <a class="btn bg-green radius-rounded pull-right margin-left" href="javascript:viewImportantUser()">重要用户</a>
                <a class="btn bg-green radius-rounded  pull-right margin-left" href="./operatorManage.do?view=all">查看所有</a>
                <a class="btn bg-green radius-rounded  pull-right margin-left" href="./operatorManage.do?view=self">查看自己</a>
                
                
                </div>
                <form id='searchNameForm' name='searchNameForm' method="post" action="searchByName.do" >
                 <div class="input-group margin-bottom">
                    <input type="text" id="searchName" name="name" class="form-control" placeholder="Search By Name">
                    <span class="input-group-btn">
                        <button class="btn btn-default" type="button" onclick="searchByName()">Go!</button>
                    </span>
                </div>
                 </form>
				<%@ include file="userList.jsp"%>
			</div>
			<div class="col-sm-6 margin-left border-left" id="articleList" style="height:100%;overflow:auto">
				<div id="addOperationDiv" style="width:70%">
                <%@ include file="createUser.jsp"%>
                </div>
                <div id="articlesDiv" style="display: none">
                    <div id="articleListDiv"  ></div>
                    <div id="paginationDiv" ></div>
                </div>
			</div>
		</div>
	</div>
	<%@ include file="noticeDialog.jsp"%>
	<%@ include file="commentDialog.jsp"%>
	<%@ include file="sliderShow.jsp" %>
	<%@ include file="selectUserList.jsp" %>
	<script src="../js/jquery-1.11.2.min.js"></script>
	<script src="../js/bootstrap.min.js"></script>
	<script src="../js/main.js"></script>
	<script type="text/javascript">
	var total = document.documentElement.clientHeight-80;
	document.getElementById("mainBody").style.height=total+"px";
	
	function searchByName(){
		//var name = $("#searchName").val();
		//window.location = "searchByName.do?name="+name;
		$("#searchNameForm").submit();
	}
	function viewImportantUser(){
        $("#userTable").find("tr").each(function(){
            var tdArr = $(this).children();
            var tdcon = tdArr.eq(3).html();
            if(tdcon!="重要用户"&&tdcon!="<strong>角色</strong>"){
                $(tdArr).remove();
            }
        });
    }
	function selectBack(){
		selectUserSubmit();
	}
	</script>
</body>
</html>