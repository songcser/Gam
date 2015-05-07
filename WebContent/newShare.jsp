<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>友哈 这个世界我哈过</title>
<link rel="stylesheet" href="../css/bootstrap.css">
<style type="text/css">
* {
    padding:0px;
    margin:0px;
}
div.iconDiv {
    position: fixed;
    bottom: 0px;
    border: 1px;
    height: 157px;
    /*background-color: #808080;*/
    width:100%;
    margin:auto;
    padding:auto;
    background: rgba(0, 0, 0, 0.8);
}

.transparent {  
      filter:alpha(opacity=20);  
      -moz-opacity:0.2;  
      -khtml-opacity: 0.2;  
      opacity: 0.2;  
}
.transparent_no{
    filter:alpha(opacity=100);  
      -moz-opacity:1.0;  
      -khtml-opacity: 1.0;  
      opacity: 1.0;  
}

#downText p:nth-of-type(1){
    margin-top:30px;
    color:#fff;
    font-size:45px;
    line-height: 20px
}
#downText p:nth-of-type(2){
   color:#ddd;
   font-size:18px
}
#logImg{
    margin-top:10px;
}
#downloadDiv{
    margin-top:23px;
    margin-right:25px
}
@media ( min-width : 1200px) {
	#bodyMain {
		width: 700px;
	}
	div.iconDiv{
	   width:700px;
	   height:100px;
	}
}
 @media ( max-width : 768px) {
    div.iconDiv{
        height:60px;
    }
    #logImg{
        width:45px;
        height:45px;
        margin-left:13px;
        margin-top:8px;
    }
    #downloadImg{
        width:80px;
    }
    #downText p:nth-of-type(1){
        margin-top:16px;
        font-size:20px;
        line-height: 10px;
    }
    #downText p:nth-of-type(2){
        color:#ddd;
        font-size:15px;
        line-height: 8px;
    }
    #downloadDiv{
        margin-top:16px;
        margin-right:15px
    }
    #article img{
        width: 100% !important; 
    }
}
</style>
</head>
<body style="background-color:#B0E2FF">
	<div id="bodyMain" style="margin:auto;padding-left:0px;padding-right:0px;padding-bottom: 70px;background-color:#fff" >
		<div  style="padding-left:10px;padding-top:10px;width: 100%; height:70px">
			<div class="pull-left"  style="padding:0px;padding-top:0px">
				<img style="width: 60px; height: 60px" class="img-circle" alt="" src="${user.getHeadUrl() }">
			</div>
			<div class=" padding-left pull-left " style="padding-top:10px" >
				<p style="font-weight: bold; font-size: 18px;line-height: 10px;">${user.name }</p>
				<p style="font-size: 8px;padding-top:10px;line-height: 10px" id="date" ></p>
			</div>
		</div>
		<hr style="padding:0px;margin-top:10px"/>
		<article id="article"> 
		<c:if test="${type==0 }">
			<c:if test="${!empty pictures }">
				<c:forEach items="${pictures }" var="pic">
					<div class="margin">
						<img style="max-width: 100% !important" alt="" src="${pic }" />
					</div>
				</c:forEach>
			</c:if>
		</c:if> 
		<c:if test="${type==1 }">
		  <c:if test="${content!=null }">
		      <div style="margin-left:5px;margin-right:5px">${content }</div>
		  </c:if>
		  <c:if test="${content==null }">
		      <c:forEach items="${pictures }" var="pic">
                    <div class="margin">
                        <img style="max-width: 100% !important" alt="" src="${pic }" />
                    </div>
                </c:forEach>
		  </c:if>
		</c:if> 
		</article>
		<div class="iconDiv ">
            <div >
                <img id="logImg" class="pull-left" style="margin-left:15px" src="../images/LOGO-ok.png">
                <div id="downText" class=" pull-left margin-left">
                    <p  >Uha</p>
                    <p style="">这个世界我哈过</p>
                </div>
                <div id="downloadDiv" class="pull-right">
                    <a href="javascript:downloadApp()"><img id="downloadImg" src="../images/download3.png"></a>
                </div>
            </div>
        </div>
	</div>
<script src="../js/main.js"></script>
<script type="text/javascript">
var date = strToDate(""+${date});
var strDate = calculateDate(date);
var dateObj = document.getElementById("date");
dateObj.innerHTML=strDate;
window.onload = function () {
    var u = navigator.userAgent;
    if (u.indexOf('Android') > -1 ||u.indexOf('iPhone') > -1||u.indexOf('Windows Phone') > -1) {//安卓手机
        var current = $("#article") ;
        $(current).find("img").each(function(i){ 
            $(this).attr("style","width: 100%"); 
            $(this).attr("style","height: auto"); 
        });
        $(current).find("iframe").load(function(i){ 
            //$(this).height("auto"); 
            $(this).width($(current).width()); 
        });
    } 
    var current = $("#article") ;
    $(current).find("img").each(function(i){ 
        $(this).attr("style","width: 100%"); 
        $(this).attr("style","height: auto"); 
    });
    $(current).find("iframe").load(function(i){ 
        //$(this).height("auto"); 
        $(this).width($(current).width()); 
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
    //window.location.href="http://www.uha.so/"; 
    window.location.href="http://a.app.qq.com/o/simple.jsp?pkgname=com.starkpet.pa";
    /*
    if (!isWeiXin()) {
        window.location.href="http://www.uha.so/"; 
        
    } else {
        //$("#guidePic").css({
        //    display : "block"
        //});
        var u = navigator.userAgent;
        if (u.indexOf('Android') > -1 || u.indexOf('Linux') > -1) {//安卓手机
         window.location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.starkpet.pa";
        } else if (u.indexOf('iPhone') > -1) {//苹果手机
         window.location.href = "https://itunes.apple.com/cn/app/uha-chong-wu-meng-you-she-qun/id959345766?mt=8";
        } else if (u.indexOf('Windows Phone') > -1) {//winphone手机
        alert("winphone手机现不支持,谢谢");
        // window.location.href = "mobile/index.html";
        }

    }*/
}
</script>
</body>
</html>