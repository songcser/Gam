<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>友哈-发布管理</title>
<link rel="stylesheet" href="../css/bootstrap.css">
<script src="../js/jquery-1.11.2.min.js"></script>
<style type="text/css">
body {
	margin-top: 80px;
}
</style>
</head>
<body class="bg-blue-light ">
	<%@ include file="header.jsp"%>
	<div class="container">
		<div id="createArticleDiv" class="center-block " style="width: 80%">
			<form id='articleFormFile' name='formFile' method="post" action="../article/addArticle.do" target='frameFile' enctype="multipart/form-data">
				<input type="hidden" id="userId" name="userId" value="${operations.get(0).userId }" />
				<div class="btn-group margin-bottom">
					<label class="btn btn-primary active"> <input type="radio" name="articleType" id="commonMode" autocomplete="off" value="0" checked onclick="selectCommonMode()">
						普通
					</label> <label class="btn btn-primary"> <input type="radio" name="articleType" id="recommendMode" autocomplete="off" value="12" onclick="selectRecommendMode()"> 推荐
					</label> <label class="btn btn-primary"> <input type="radio" name="articleType" id="showMode" autocomplete="off" value="9" onclick="selectShowMode()"> 节目单
					</label>
				</div>
				<div class="panel panel-default">
					<div class="panel-body">

						<div class="form-group" style="height: 90px">
							<div class="pull-left">
								<a style="width: 80px; height: 80px" class="thumbnail" href="javascript:selectUser()"> <img id="headImg" style="width: 70px; height: 70px"
									src="${operations.get(0).getHeadUrl() }" alt="" class="img-circle">
								</a>
							</div>
							<div id="userName" class="pull-left margin-left margin-top text-gray">${operations.get(0).name }</div>

						</div>

						<div id="showDiv" class="form-group collapse">
							<label for="activityId">节目单</label> 
							<select name="activityId" id="showSelect" class="form-control" >
								<c:if test="${!empty showList }">
									<c:forEach items="${showList }" var="u">
										<option value="${u.activityId }" title="${u.type }">${u.subject }</option>
									</c:forEach>
								</c:if>
							</select>
						</div>

						<div class="form-group">
							<label for="title">标题</label> <input type="text" class="form-control" name="title">
						</div>

						<div class="form-group">
							<label for="content">内容</label>
							<textarea id="articleContent" name="content" class="form-control" rows="3" cols="30" placeholder="1000字以内"></textarea>
						</div>

						<div class="form-group clearfix">
							<label for="reference">推文来源</label> <input type="text" class="form-control" id="reference" name="reference" size="50" />
						</div>
						<div>
							<%@ include file="previewPicture.jsp"%>
						</div>

						<div id="richTextDiv" class="form-group collapse">
							<label for="content">富文本</label>
							<%@ include file="ueditor.jsp"%>
						</div>

						<div style="width: 100%; clear: both">
							<div class="form-group  padding-bottom">
								<input class="btn bg-blue pull-right" type="button" onclick="publishArticle()" value="发布">
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	<iframe id='frameFile' name='frameFile' style='display: none;'></iframe>
	<%@ include file="selectUserList.jsp"%>
	<script src="../js/bootstrap.min.js"></script>
	<script type="text/javascript">
		var isClick = false;
		function selectCommonMode() {
			$('input[type="radio"]').parent('label').removeClass('active');
			$('input[type="radio"]:checked').parent('label').addClass('active');
			$('.collapse').collapse('hide');
		}

		function selectRecommendMode() {
			$('input[type="radio"]').parent('label').removeClass('active');
			$('input[type="radio"]:checked').parent('label').addClass('active');

			$('#richTextDiv').collapse('show');
			if (isClick) {
				$('#showDiv').collapse('hide');
			}
			isClick = true;
		}

		function selectShowMode() {
			$('input[type="radio"]').parent('label').removeClass('active');
			$('input[type="radio"]:checked').parent('label').addClass('active');
			$('#showDiv').collapse('show');
			var title = $("#showSelect").find('option:selected').attr("title");
			if(title=="2"){
                $('#richTextDiv').collapse('hide');
            }
            else if(title=="3"){
                $('#richTextDiv').collapse('show');
            }
			isClick = true;
		}
		function selectUser() {
			selectUserList();
		}
		function selectBack() {
			$("#userId").val(selectUserId[0]);
			$("#headImg").attr("src", selectUserHeadUrl[0]);
			$("#userName").html(selectUserName[0]);
		}

		function publishArticle() {
			if ($("#articleContent").val() == "") {
				alert("请输入内容");
				return;
			}
			if (getPicCount() < 1) {
				alert("请添加封面图片");
				return;
			}
			setRichTextContent();
			$("#articleFormFile").submit();
		}
		function callback(str, userId) {
			if (str == '1') {
				alert('发布成功');
			} else if (str == '2') {
				alert('图片不可用 发布失败');
			} else {
				alert('发布失败');
			}
			//$("#articleFormFile").reset();
			$("input[name='title']").val("");
			$("#articleContent").val("");
			$("input[name='reference']").val("");
			resetPreviewPicture();
			resetRichTextContent();
		}

		$(document).ready(function() {
			$('#showSelect').change(
				function() {
					var title = $(this).children('option:selected').attr("title");
					if(title=="2"){
						$('#richTextDiv').collapse('hide');
					}
					else if(title=="3"){
						$('#richTextDiv').collapse('show');
					}
				});
		});
	</script>
</body>
</html>