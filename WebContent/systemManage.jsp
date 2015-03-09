<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>史塔克后台管理-系统管理</title>
<link rel="stylesheet" type="text/css" href="../css/styles.css">
<link rel="stylesheet" href="../css/pintuer.css">
<link type="image/x-icon" href="${webIcon }" rel="shortcut icon" />
<link href="${webIcon }" rel="bookmark icon" />
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<script src="../js/pintuer.js"></script>
<script src="../js/respond.js"></script>
<script src="../js/admin.js"></script>
<script type="text/javascript">
function selectFrequency(){
    var obj = document.getElementById("frequency");
    var fre = obj.options[obj.selectedIndex].value;
    if("day"==fre){
        document.getElementById("dayDiv").style.display="block";
        document.getElementById("weekDiv").style.display="none";
        document.getElementById("monthDiv").style.display="none";
    }else if("week"==fre){
        document.getElementById("dayDiv").style.display="none";
        document.getElementById("weekDiv").style.display="block";
        document.getElementById("monthDiv").style.display="none";
    }else if("month"==fre){
        document.getElementById("dayDiv").style.display="none";
        document.getElementById("weekDiv").style.display="none";
        document.getElementById("monthDiv").style.display="block";
    }
}
</script>
</head>
<body>
	<div style="height: 100%">
		<ul id="header">
			<li style="width: 70px"><a href="toMain.do">首页</a></li>
			<li><a href="operatorManage.do">账号管理</a></li>
			<li><a href="articleManage.do">推文管理</a></li>
			<li><a href="activityManage.do">活动管理</a></li>
			<li><a href="publishManage.do">发布管理</a></li>
			<li><a href="systemManage.do">系统管理</a></li>
		</ul>
		<div id="fristDiv" class="  nav-navicon padding-large-top bg" style="width: 10%; height: 93%; float: left;">
			<ul id="subject" class="nav nav-pills nav-navicon border-main nav-menu ">
				<li><a href="javascript: showDiv('article');">备份管理</a></li>

			</ul>
		</div>
		<div id="thirdDiv" class="padding-large" style="width: 45%; height: 93%; float: left;">
		  <div class="panel">
		      <div class="panel-head  border-sub bg-sub"><strong>备份状态</strong></div>
            <div class="panel-body">
                <ul class="list-group list-striped">
                <c:if test="${backup.status=='0'}">
                    <li>运行状态：停止运行</li>
                </c:if>
                <c:if test="${backup.status=='1'}">
                    <li>运行状态：运行中</li>
                </c:if>
                    <li>开始时间：${backup.startDate }</li>
                    <li>最新备份时间：${backup.lastDate }</li>
                    <li>备份路径：${backup.path }</li>
                    <li>备份数量：${backup.saveCount }</li>
                <c:if test="${backup.frequency=='0'}">
                    <li>备份频率：每天一次</li>
                </c:if>
                <c:if test="${backup.frequency=='1'}">
                    <li>备份频率：每周一次</li>
                </c:if>
                <c:if test="${backup.frequency=='2'}">
                    <li>备份频率：每月一次</li>
                </c:if>
                    <li>备份日期：${backup.date }</li>
                    <li>备份时间：${backup.time }</li>
                </ul>
            </div>
          </div>
		</div>
		<div id="secondDiv" class="padding-large" style="width: 45%; height: 93%; float: left;">
		<form action="./backup.do">
		  <div class="form-group ">
			<div class="label">
				<label>更新备份</label>
			</div>
			<div class="field">
				<div class="button-group radio">
					<label class="button active"><input name="pintuer" value="yes" checked="checked" type="radio">
					   <span class="icon icon-check"></span>开始备份</label>
					<label class="button"><input name="pintuer" value="no" type="radio">
					   <span class="icon icon-times"></span> 停止备份</label>
				</div>
			</div>
		  </div>
		  <div class="form-group">
			<div class="label">
			<label for="backuppath">备份路径</label></div>
            <div class="field">
                <input type="text" class="input input-auto" id="backuppath" name="backuppath" size="30"  />
            </div>
          </div>
          <div class="form-group ">
            <div class="input-inline">
            <div class="label">
            <label for="frequency">备份频率</label></div>
            <div class="field">
                <select id="frequency" class="input input-auto" name="frequency" onchange="selectFrequency()">
                    <option value="day">每天</option>
                    <option value="week">每周</option>
                    <option value="month">每月</option>
                </select>
            </div>
            </div>
          </div>
          <div class="form-group">
            <div class="label">
            <label for="day">日期</label></div>
            <div class="field">
               <div id="dayDiv">
                   <select class="input input-auto" id="day" name="day">
                       <% for (int i = 1; i <= 100; i++) {%>
                       <option value="<%=i%>"><%=i%></option>
                       <%  } %>
                   </select>
               </div>
               <div id="weekDiv"  style="display: none">
                   <select id="week" class="input input-auto"  name="week">
                       <option value="Monday">Monday</option>
                       <option value="Tuesday">Tuesday</option>
                       <option value="Wednesday">Wednesday</option>
                       <option value="Thursday">Thursday</option>
                       <option value="Friday">Friday</option>
                       <option value="Saturday">Saturday</option>
                       <option value="Sunday">Sunday</option>
                   </select>
               </div>
               <div id="monthDiv"  style="display: none">
                   <select id="month" class="input input-auto" name="month" >
                       <% for (int i = 1; i < 31; i++) {%>
                       <option value="<%=i%>"><%=i%></option>
                       <% }%>
                   </select>
               </div>
            </div>
          </div>
          <div class="form-group">
            <div class="label">
            <label for="time">时间</label></div>
            <div class="field">
                <select id="hour" name="hour" class="input  input-auto">
                        <% for (int i = 0; i < 24; i++) { %>
                        <option value="<%=i%>"><%=i%></option>
                        <% } %>
                </select> : 
                <select id="minute" name="minute" class="input  input-auto">
                        <%for (int i = 0; i < 60; i++) { %>
                        <option value="<%=i%>"><%=i%></option>
                        <% }  %>
                </select>
            </div>
          </div>
          <div class="form-group">
            <div class="label">
            <label for="backuppath">保存个数</label></div>
            <div class="field">
                <input type="text" class="input input-auto" id="backuppath" name="backuppath" size="30"  />
            </div>
          </div>
          <div class="form-button"><button class="button" type="submit">提交</button></div>
          </form>
		</div>
		
	</div>
	
</body>
</html>