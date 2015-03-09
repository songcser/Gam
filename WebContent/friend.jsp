<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
.center {
	z-index: 100px;
	padding: 10px;
	border: 1px solid #ccc;
	display: -webkit-box;
	-webkit-box-orient: horizontal;
	-webkit-box-pack: center;
	-webkit-box-align: center;
	display: -moz-box;
	-moz-box-orient: horizontal;
	-moz-box-pack: center;
	-moz-box-align: center;
	display: -o-box;
	-o-box-orient: horizontal;
	-o-box-pack: center;
	-o-box-align: center;
	display: -ms-box;
	-ms-box-orient: horizontal;
	-ms-box-pack: center;
	-ms-box-align: center;
	display: box;
	box-orient: horizontal;
	box-pack: center;
	box-align: center;
}

* {
	margin: 0;
	padding: 0;
}

.stock {
	position: fixed;
	top: 0;
	left: 0;
	z-index: -1;
	height: 100%;
	width: 100%;
	filter: url(blur.svg#blur); /* FireFox, Chrome, Opera */
	-webkit-filter: blur(40px); /* Chrome, Opera */
	-moz-filter: blur(40px);
	-ms-filter: blur(20px);
	filter: blur(20px);
	filter: progid:DXImageTransform.Microsoft.Blur(PixelRadius=10, MakeShadow=false); /* IE6~IE9 */
}

#top {
	z-index: 100;
	width: 100%;
	height: 173px;
}

#topPic {
	position: fixed;
	top: 0px;
	left: 0px;
	width: 1125px;
	height: 173px;
}

#downloadPic {
	position: fixed;
	top: 39px;
	right: 44px;
	width: 265px;
	height: 98px;
}

#headPic {
	margin-top: 400px;
	margin-left: auto;
	margin-right: auto;
	width: 330px;
	height: 330px;
}

#headPic img {
	z-index: 100;
	width: 330px;
	height: 330px;
	border-radius: 50%;
	overflow: hidden;
}

#info {
	margin-top: 400px;
	margin-left: auto;
	margin-right: auto;
	margin-top: 14px;
	text-align: center;
}

#info p {
	text-align: center;
	font-size: 42px;
	font-family: '宋体', arial, georgia, verdana, helvetica, sans-serif;
}

#info p+p {
	font-size: 36px;
	font-family: '宋体', arial, georgia, verdana, helvetica, sans-serif;
}

#petPic {
	margin-top: 125px;
	text-align: center;
}

#petPic img {
	border-radius: 50%;
	overflow: hidden;
	width: 200px;
	height: 200px;
}

#petPic p {
	text-align: center;
	font-size: 42px;
	font-family: '宋体', arial, georgia, verdana, helvetica, sans-serif;
}

#petPic p+p {
	font-size: 36px;
	font-family: '宋体', arial, georgia, verdana, helvetica, sans-serif;
}

#doorplate {
	margin-top: 99px;
	margin-left: auto;
	margin-right: auto;
	text-align: center;
}

#doorplate p {
	font-size: 36px;
	font-family: '宋体', arial, georgia, verdana, helvetica, sans-serif;
}

#bottom {
	margin-top: 60px;
	height: 138px;
	text-align: center;
}

#bottom ul {
	text-align: center;
}

#bottom ul li {
	float: left;
	display: block;
	width: 49%;
	height: 138px;
	background: #F0F8FF;
	font-family: SimSun;
	z-index: 500;
	margin: 0 1px;
	text-align: center;
}

/* display block will make the link fill the whole area of LI */
#bottom ul li p {
	margin: 45px;
	display: block;
	font-weight: 700;
	height: 23px;
	text-decoration: none;
	color: #888;
	text-align: center;
	font-size: 48px;
}
</style>
</head>
<body>
	<img class="stock" src="images/user.png">
	<div id="top">
		<img alt="" src="images/top.png" id="topPic" />
		<a href=""><img src="images/download.png" id="downloadPic"></a>
	</div>
	<div id="headPic">
		<img alt="" src="images/user.png">
	</div>
	<div id="info">
		<p>ddsfsfsdfsf</p>
		<p>ddddddddddd</p>
	</div>
	<div id="petPic">
		<img src="http://localhost/pet.jpg" />
		<p>Pet Name</p>
		<p>种类年龄</p>
	</div>
	<div id="doorplate">
		<p>门牌号：12123323</p>
	</div>
	<div id="bottom">
		<ul>
			<li><p>ddd</p></li>
			<li><p>sss</p></li>
		</ul>
	</div>
</body>
</html>