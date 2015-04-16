<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>友哈-通知管理</title>
<link type="image/ico" href="../images/favicon.ico" rel="shortcut icon" />
<link href="../images/favicon.ico" rel="bookmark icon" />
<link rel="stylesheet" href="../css/bootstrap.css">
<script src="../js/jquery-1.11.2.min.js"></script>
<style type="text/css">
body {
	margin-top: 50px;
	height: 100%;
}
</style>
</head>
<body class="bg-blue-light ">
	<%@ include file="header.jsp"%>
	<div class="container">
		<div class=" panel panel-default" style="width:80%;margin-top:10px;margin-left:auto;margin-right:auto;padding:auto">
			<div class="panel-body">
			<form id='systemform' name='formFile' method="post" class="form form-tips" action="../notice/systemNotice.do" target='frameFile' >
			 <input type="hidden" name="userId" value="1">
			 <input type="hidden" name="showFlag" value="0" id="showFlag">
			 <div class="margin-bottom">
			 <a class="btn btn-primary" data-toggle="collapse" href="#showDiv" aria-expanded="false" aria-controls="showDiv">节目单</a>
			 </div>
				<div id="showDiv" class="form-group collapse">
					 <select name="activityId" id="showSelect" class="form-control">
						<c:if test="${!empty showList }">
							<c:forEach items="${showList }" var="u">
									<option value="${u.activityId }" title="${u.type }">${u.subject }</option>
							</c:forEach>
						</c:if>
					</select>
				</div>
				<div class="form-group margin-big-top">
                    <label for="content">系统消息</label>
                    <textarea id="noticeContent" name="systemArticle" class="form-control" rows="4" cols="30" placeholder="1000字以内"></textarea>
                </div>
                <div class="form-group  padding-bottom">
                   <input class="btn bg-blue pull-right" type="button" onclick="publishNotice()" value="发布">
                </div>
                </form>
			</div>
		</div>
	</div>
	<iframe id='frameFile' name='frameFile' style='display: none;'></iframe>
	<script src="../js/bootstrap.js"></script>
	<script type="text/javascript" src="../js/main.js"></script>
	<script type="text/javascript">
	var showType = 0;
	$('#showDiv').on('hidden.bs.collapse', function () {
		$("#showFlag").val("0");
		});
	$('#showDiv').on('shown.bs.collapse', function () {
		$("#showFlag").val("1");
        });
    function publishNotice(){
    	if($("#noticeContent").val()==""){
    		alert("内容不能为空");
    		return;
    	}
    	$("#systemform").submit();
	}
    
    function callbackSystem(ret){
    	alert(ret);
    	$("#noticeContent").val("");
    }
	</script>
</body>
</html>