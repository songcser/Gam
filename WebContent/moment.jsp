<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>友哈-瞬间管理</title>
<link rel="stylesheet" href="../css/bootstrap.css">
<style type="text/css">
body {
	margin-top: 50px;
	height: 100%;
}
</style>
</head>
<body class="bg-blue-light" onload="init()">
	<%@ include file="header.jsp"%>
	<div id="mainBody" class="container padding-top">
		<div style="width: 85%; height: 5%" class="center-block margin-bottom row">
			<div class="col-lg-6 col-md-6 ">
				<div class="input-group">
					<input type="date" id="dateSearchId" class="form-control" placeholder="Search for Time"> <span class="input-group-btn">
						<button class="btn btn-default" type="button" onclick="getByDate()">Go!</button>
					</span>
				</div>
			</div>
			<div class="col-lg-6 col-md-6 ">
				<button class="btn bg-blue radius-rounded pading-left" onclick="getAuditedMoment()">已审核</button>
				<button class="btn bg-blue radius-rounded " onclick="getNoAuditedMoment()">未审核</button>
				<button class="btn bg-blue radius-rounded " onclick="getDeleteList()">已删除</button>
			</div>
		</div>
		<div style="width: 85%; height: 95%; overflow: auto; padding-left: 15px" id="articleList" class="center-block ">
			<div id="articleListDiv"></div>
			<div id="paginationDiv"></div>
		</div>
	</div>
	<%@ include file="browseDialog.jsp"%>
    <%@ include file="auditingDialog.jsp"%>
    <%@ include file="commentDialog.jsp"%>
    <%@ include file="sliderShow.jsp"%>
    <%@ include file="selectUserList.jsp"%>
	<script src="../js/jquery-1.11.2.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
    <script src="../js/main.js"></script>
    <script type="text/javascript">
    var isAuditing = false;
    var total = document.documentElement.clientHeight - 70;
    document.getElementById("mainBody").style.height = total + "px";
    function init() {
        var url = "/StarkPet/article/getMomentList2.0.do?userId=0&";
        getArticleList(url);
    }
    function getArticleList(url) {
    	isShowHeader = true;
        showArticleList(url, 0);
    }
    function getAuditedMoment(){
    	init();
    }
    function getNoAuditedMoment(){
    	var url = "/StarkPet/article/getNoAuditingMomentList2.0.do?userId=0&";
    	showArticleList(url, 0);
    }
    function selectBack() {
        selectUserSubmit();
    }
    function getByDate(){
        isAuditing = true;
        var time = Date.parse($("#dateSearchId").val());
        
        if(!isNaN(time)){
            var date = new Date(time);
            var currentDate = date.format("yyyy-MM-dd");
            var url = "/StarkPet/article/getArticlesByDate.do?userId=0&date="+currentDate+"&type=0&";
            getArticleList(url);
        }
        else {
            alert("请输入日期");
        }
    }
    function getDeleteList(){
    	var url = "/StarkPet/article/getDeleteList.do?";
        showArticleList(url, 0);
    }
    function auditingBack() {

        var articleId = currentArticle.Id;
        var url = "/StarkPet/article/changeArticleType.do?articleId="+ articleId + "&type=" + 0;
        ajaxRequest(url, response);
    }
    function response(data){
    	if (data.result == 1) {
            if(isAuditing){
                $("#articleType"+currentArticle.Id).html(data.type);
            }
            else{
                $("#mediaMainId" + currentArticle.Id).remove();
            }
        } else {
            alert("失败了!!!");
        }
    }
    </script>
</body>
</html>