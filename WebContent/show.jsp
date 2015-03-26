<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>友哈-节目单管理</title>
<link rel="stylesheet" href="../css/bootstrap.css">
<style type="text/css">
body {
	margin-top: 50px;
	height: 100%;
}
</style>
</head>
<body class="bg-blue-light ">
	<%@ include file="header.jsp"%>
	<div id="mainBody" class="container-fluid padding-top">
		<div class="row" style="height: 100%">
			<div class="col-lg-5" style="height: 100%;">
				<button class="btn bg-blue radius-rounded " onclick="createShow()">新建节目单</button>
				<div id="showList" class="margin-top" style="height: 95%; overflow: auto">
					<c:if test="${!empty showList }">
						<c:forEach items="${showList }" var="o">
							<div class="border bg-red-light radius media padding-top" style="overflow: auto">
								<div class="media-left padding-small">
									<a style="width: 400px; height: 250px" href="javascript:showArticles(${o.activityId })" class="thumbnail"> <img class="media-object" style="width: 100%; height: 100%"
										src="${o.getBannerPicUrl() }" alt=""></a>
								</div>
								<div class="media-body padding-top">
									<button style="width: 170px; height: 30px; margin-left: -15px; display: block" class="bg-green radius-rounded margin-bottom ">${o.subject }</button>
									<c:if test="${o.type==2 }">
										<button style="width: 140px; height: 30px; margin-left: -15px; display: block" class="bg-green radius-rounded margin-bottom ">可参加</button>
									</c:if>
									<c:if test="${o.type==3 }">
										<button style="width: 140px; height: 30px; margin-left: -15px; display: block" class="bg-green radius-rounded margin-bottom ">不可参加</button>
									</c:if>
									<button style="width: 110px; height: 30px; margin-left: -15px; display: block" class="bg-green radius-rounded margin-bottom ">${o.order }</button>
									<button style="width: 70px; height: 30px; margin-left: -15px; display: block" class="bg-green radius-rounded margin-bottom ">
										<span class="padding-small-left glyphicon glyphicon-picture"></span>
									</button>
									<button style="width: 50px; height: 30px; margin-left: -15px" class="bg-green radius-rounded margin-bottom ">
										<span class="padding-small-left glyphicon glyphicon-trash"></span>
									</button>
								</div>
							</div>
						</c:forEach>
					</c:if>
				</div>
			</div>
			<div class="col-lg-7">
				<div id="createShowId" style="width: 85%;  overflow: auto;"  class="center-block ">
					<%@ include file="createShow.jsp"%>
				</div>
				<div style="width: 85%; height: 95%; overflow: auto;" id="articleList" class="center-block ">
					<div id="articleListDiv"></div>
					<div id="paginationDiv"></div>
				</div>
			</div>
		</div>
	</div>
	<script src="../js/jquery-1.11.2.min.js"></script>
	<script src="../js/bootstrap.min.js"></script>
    <script src="../js/main.js"></script>
	<script type="text/javascript">
		var isAuditing = true;
		var total = document.documentElement.clientHeight - 70;
		document.getElementById("mainBody").style.height = total + "px";
		function callback(str) {
			if (str == '1') {
				alert('发布成功');
				location.reload();
			} else if (str == '2') {
				alert('图片不可用 发布失败');
			} else {
				alert('发布失败');
			}

		}
		function showArticles(activityId) {
			$("#createShowId").css({
                display : "none"
            });
			
			$("#articlesDiv").css({
				display : "block"
			});
			var url = "/StarkPet/article/getShowArticleList2.0.do?showId=" + activityId + "&userId=0&";
			showArticleList(url, 0);
			isShowHeader = true;
		}
	</script>
</body>
</html>