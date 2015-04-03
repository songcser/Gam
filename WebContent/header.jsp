<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<div class="navbar navbar-inverse navbar-fixed-top " role="navigation">
  　<div class="navbar-header">
  　    <a href="##" class="navbar-brand">友哈</a>
  　</div>
    <ul id="navul" class="nav navbar-nav ">
       <li ><a href="toMain.do">首页</a></li>
       <li><a href="operatorManage.do">账号管理</a></li>
      <li><a href="recommend.do">推荐管理</a></li>
      <li><a href="showManage.do">节目单管理</a></li>
      <li><a href="moment.do">瞬间管理</a></li>
      <li><a href="chartlet.do">贴图管理</a></li>
      <li><a href="publishManage.do">发布管理</a></li>
      <li><a href="#">通知管理</a></li>
     </ul>
     <!-- <form action="##" class="navbar-form navbar-left" rol="search">
        <div class="form-group">
           <input type="text" class="form-control" placeholder="ff" />
        </div>
        <button type="submit" class="btn btn-default">ff</button>
     </form>
      -->
</div>
<script type="text/javascript">
$(document).ready(function(){ 
	var url = window.location.href;
	if(url.indexOf("toMain.do")>0){
		$('ul li:eq(0)').addClass("active");
	}
	else if(url.indexOf("operatorManage.do")>0){
		$('ul li:eq(1)').addClass("active");
	}
	else if(url.indexOf("recommend.do")>0){
        $('ul li:eq(2)').addClass("active");
    }
	else if(url.indexOf("showManage.do")>0){
        $('ul li:eq(3)').addClass("active");
    }
	else if(url.indexOf("moment.do")>0){
        $('ul li:eq(4)').addClass("active");
    }
	else if(url.indexOf("chartlet.do")>0){
        $('ul li:eq(5)').addClass("active");
    }
	else if(url.indexOf("publishManage.do")>0){
        $('ul li:eq(6)').addClass("active");
    }
}) ;
</script>