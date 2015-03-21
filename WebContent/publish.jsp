<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>友哈-发布管理</title>
<link rel="stylesheet" href="../css/bootstrap.css">
<style type="text/css">
body {
	margin-top: 80px;
}
</style>
</head>
<body class="bg-blue-light ">
	<%@ include file="header.jsp"%>
	<div class="container">
		<div id="createArticleDiv" class="center-block panel panel-default" style="width: 80%">
			<div class="panel-body">
				<form id='articleFormFile' name='formFile' method="post" action="../article/addArticle.do" target='frameFile' enctype="multipart/form-data">
					<input type="hidden" name="articleType" id="articleType">
					<div class="form-group">
						<label for="userId">姓名</label> <select name="userId" class="form-control">
							<c:if test="${!empty operations }">
								<c:forEach items="${operations }" var="u">
									<option value="${u.userId }">${u.name }</option>
								</c:forEach>
							</c:if>
						</select>
					</div>
					<div class="form-group">
						<label for="title">标题</label> <input type="text" class="form-control" name="titles">
					</div>
					<div class="form-group">
						<label for="content">内容</label>
						<textarea id="articleContent" name="content" class="form-control" rows="3" cols="30" placeholder="500字以内"></textarea>
					</div>
					<div class="form-group">
						<label for="reference">推文来源</label> <input type="text" class="form-control" id="reference" name="reference" size="50" />
					</div>
					<%@ include file="previewPicture.jsp"%>
					<div style="width: 100%; clear: both">
						<div class="form-button text-right padding-bottom">
							<button class="button bg-green" type="button" onclick="publishArticle()">发布</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<script src="../js/jquery-1.11.2.min.js"></script>
</body>
</html>