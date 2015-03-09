<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>UHA</title>
<link rel="stylesheet" type="text/css" href="../css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="../css/roll.css">
<script type="text/javascript" src="../js/jquery-1.11.2.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/respond.js"></script>
<style type="text/css">
* {
	margin: 0;
	padding: 0;
}

body{
    margin-bottom:200px;
}

#picDiv img {
	width: 100%;
	z-index: 10;
}

#userDiv {
	margin-top: -198px
}

#userHeadDiv {
	margin-top: -198px;
	padding-left: 54px;
	padding-right: 54px;
}

#userHeadDiv img {
	width: 129px;
	height: 129px;
}

#userHeadDiv p {
	padding-top: 20px;
	font-size: 36px;
	font-weight: bold;
}

#userHeadDiv div {
	margin-top: 40px;
	word-wrap: break-word;
}

p.content {
	font-size: 42px;
	line-height: 48px;
	color: #808080;
}

img.xiejiao {
	height: 198px;
	width: 100%;
}

div.pandc {
	margin-top: 34px;
	margin-bottom: 34px;
}

img.pc {
	width: 70px;
	height: 70px;
}

span.count {
	font-size: 48px;
	vertical-align: middle;
}

div.iconDiv {
	position: fixed;
	bottom: 0px;
	right: 0px;
	left: 0px;
	border: 1px;
	height: 157px;
	background-color: #808080;
}

#logo {
	margin-top: 21px;
	margin-bottom: 15px;
	margin-left: 52px;
	height: 120px;
	width: 120px;
}

#uhaWord {
	font-size: 45px;
	color: #ffffff;
}

#fanWord {
	font-size: 36px;
	color: #aaaaaa;
}

div.logoWord {
	margin-top: 15px;
	margin-left: 25px;
}

#download {
	margin-top: 30px;
	margin-right: 39px;
}

#download img {
	height: 97px;
}

#moreImg {
	position: absolute;
	right: 10px;
	top: 320px;
}

#showPicDiv {
	position: fixed;
	top: 0px;
	bottom: 0px;
	left: 0px;
	right: 0px;
	background-color: #000000;
	display: none;
}

#guidePic {
	position: fixed;
	top: 0px;
	bottom: 0px;
	left: 0px;
	right: 0px;
	background-color:#000;
	filter:alpha(opacity=70); 
    -moz-opacity:0.7; 
    opacity:0.7;
	display: none;
}

#guidePic img {
	width: 100%;
	height: 100%;
}

#pcicon{
    margin-top:50px;
    text-align:center;
}

#pcicon span:nth-of-type(1){
    margin-right:50px;
}
#pcicon img:nth-of-type(2){
    margin-left:50px;
}

#slidershow{
    margin-top:100px;    
}

#slidershow img{
    width:80%;
}
.divcenter{
    text-align:center;
    margin-left:auto;
    margin-right:auto;
}

</style>
<script type="text/javascript">
	function showPic() {
		$("#showPicDiv").css({
			display : "block"
		});
	}
	function closeImg(){
		$("#showPicDiv").css({
            display : "none"
        });
	}
	
	function closeGuide(){
		$("#guidePic").css({
            display : "none"
        });
	}

	function isWeiXin() {
		var ua = window.navigator.userAgent.toLowerCase();
		if (ua.match(/MicroMessenger/i) == 'micromessenger') {
			return true;
		} else {
			return false;
		}
	}

	function downloadApp() {
		window.location.href="http://www.uha.so/"; 
		/*
		if (!isWeiXin()) {
			window.location.href="http://www.uha.so/"; 
			
		} else {
			$("#guidePic").css({
                display : "block"
            });
		}*/
	}
</script>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-6 col-md-offset-3">
				<div id="picDiv">
					<a href="javascript:showPic()"><img alt="" src="${pictures.get(0) }" /></a> <a id="moreImg" href="javascript:showPic()"><img alt="" src="../images/morePic.png"></a>
				</div>
				<div id="userDiv">
					<div>
						<img class="xiejiao" src="../images/xiejiao.png" />
					</div>
					<div id="userHeadDiv">
						<img src="${user.getHeadUrl() }" class="img-circle">
						<p>${user.name }</p>
						<div id="contentDiv">
							<p class="content">${article.content }</p>
						</div>
					</div>
				</div>
				<div id="pcicon" class="media">
						<img  class="pc" src="../images/praise.png"><span class="count">${article.praiseCount }</span>
						<img  class="pc" src="../images/comment.png"><span class="count">${article.commentCount }</span>
				</div>
				<div class="iconDiv">
					<div>
						<img id="logo" class="pull-left" src="../images/LOGO-ok.png">
						<div class="logoWord pull-left">
							<p id="uhaWord">Uha</p>
							<p id="fanWord">宠物萌友社群</p>
						</div>
						<div id="download" class="pull-right">
							<a href="javascript:downloadApp()"><img src="../images/download2.png"></a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="showPicDiv" class="container-fluid">
		 <div class="row">
		 <div class="col-md-6 col-md-offset-3">
		 <div id="slidershow" class="carousel slide" data-ride="carousel">
		      <ol class="carousel-indicators">
		          <c:if test="${!empty pictures }">
                    <c:forEach items="${pictures }" var="p" varStatus="status">
                        <c:if test="${status.index == 0 }">
                            <li class="active" data-target="#slidershow" data-slide-to="${status.index}"></li>
                        </c:if>
                        <c:if test="${status.index != 0 }">
                            <li data-target="#slidershow" data-slide-to="${status.index }"></li>
                        </c:if>
                    </c:forEach>
                </c:if>
             </ol>
             <div class="carousel-inner text-center" >
                <c:if test="${!empty pictures }">
                    <c:forEach items="${pictures }" var="p" varStatus="status">
                        <c:if test="${status.index == 0 }">
                        <div class="item active text-center">
                            <a href="javascript:closeImg()"><img src="${p}" alt="" style="width:100%"></a>
                        </div>
                        </c:if>
                        <c:if test="${status.index != 0 }">
                        <div class="item text-center">
                            <a href="javascript:closeImg()"><img src="${p}" alt="" style="width:100%"></a>
                        </div>
                        </c:if>
                    </c:forEach>
                </c:if>
		         <a class="left carousel-control" href="#slidershow" role="button" data-slide="prev">
	                <span class="glyphicon glyphicon-chevron-left" ></span>
	            </a>
	            <a class="right carousel-control" href="#slidershow" role="button" data-slide="next">
	                <span class="glyphicon glyphicon-chevron-right" ></span>
	            </a>
		    </div>
		 </div>
		 </div>
		 </div>
	</div>
	<div id="guidePic">
		<a href="javascript:closeGuide()"><img alt="" src="../images/guide3.png"></a>
	</div>
</body>
</html>