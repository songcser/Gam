<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
    <div class="row">
        <div class="col-md-5">
            <button class="btn bg-blue radius-rounded " onclick="createShow()">新建节目单</button>
        </div>
        <div class="col-md-6">
            <%@ include file="createShow.jsp"%>
        </div>
    </div>
</div>
<script src="../js/jquery-1.11.2.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script type="text/javascript">
        var isAuditing = true;
        var total = document.documentElement.clientHeight - 70;
        document.getElementById("mainBody").style.height = total + "px";
        function callback(str) {
            if (str == '1') {
                alert('发布成功');
                location.reload();
            }else if(str == '2'){
                alert('图片不可用 发布失败');
            } else {
                alert('发布失败');
            }
           
        }
</script>
</body>
</html>