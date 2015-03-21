<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>友哈-推荐管理</title>
<link rel="stylesheet" href="../css/bootstrap.css">
<style type="text/css">
body{
    margin-top:80px;
}
</style>
</head>
<body class="bg-blue-light " onload="init()">
    <%@ include file="header.jsp"%>
    <div class="container">
        <div class="center-block">
             <button class="btn bg-blue radius-rounded " onclick="createUser()">新建用户</button>
        </div>
        <div>
            <div id="articleListDiv"  ></div>
            <div id="paginationDiv" ></div>
        </div>
    </div>
    <script src="../js/jquery-1.11.2.min.js"></script>
    <script src="../js/main.js"></script>
    <script type="text/javascript">
    function init(){
    	var date = new Date();
        var currentDate = date.format("yyyy-MM-dd");
        var url = "/StarkPet/article/getArticlesByDate.do?userId=0&date="+currentDate+"&type=100&";
        showArticleList(url,0);
        //$("#filterDate").val(currentDate);
    }
    </script>
</body>
</html>