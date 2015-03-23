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
    margin-top:50px;
    height:100%;
}
</style>
</head>
<body class="bg-blue-light " onload="init()">
    <%@ include file="header.jsp"%>
    <div id="mainBody" class="container padding-top"  style="">
        <div style="width:80%;height:5%" class="center-block margin-bottom" >
             <button class="btn bg-blue radius-rounded " onclick="getAuditedRecommend()">已审核</button>
             <button class="btn bg-blue radius-rounded" onclick="getNoAuditedRecommend()">未审核</button>
             <button class="btn bg-blue radius-rounded" onclick="Test()">未审核</button>
        </div>
        <div style="width:80%;height:95%;overflow:auto" id="articleList" class="center-block ">
            <div id="articleListDiv"  ></div>
            <div id="paginationDiv" ></div>
        </div>
    </div>
    <%@ include file="commentDialog.jsp"%>
    <%@ include file="sliderShow.jsp" %>
    <script src="../js/jquery-1.11.2.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
    <script src="../js/main.js"></script>
    <script type="text/javascript">
    var total = document.documentElement.clientHeight-70;
    document.getElementById("mainBody").style.height=total+"px";
    
    function getNoAuditedRecommend(){
    	var url = "/StarkPet/article/getNoAuditingRecommendList2.0.do?userId=0";
    	getArticleList(url)
    }
    
    function getAuditedRecommend(){
    	init();
    }
    
    function init(){
    	//var date = new Date();
       // var currentDate = date.format("yyyy-MM-dd");
        //var url = "/StarkPet/article/getArticlesByDate.do?userId=0&date="+currentDate+"&type=100";
        var url = "/StarkPet/article/getRecommendList2.0.do?userId=0&";
        getArticleList(url);
        //$("#filterDate").val(currentDate);
    }
    
    function getArticleList(url){
    	showArticleList(url,0);
        isShowHeader = true;
    }
    
    function Test(){
    	 var url = "/StarkPet/notice/test.do?";
    	 getArticleList(url);
    }
    </script>
</body>
</html>