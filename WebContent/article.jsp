<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>${article.title }</title>
<link rel="stylesheet" href="../css/bootstrap.min.css">
<style type="text/css">
img{
    margin: 0px; 
    padding: 0px; 
    max-width: 100%; 
    height: auto !important; 
    box-sizing: border-box !important; 
    word-wrap: break-word !important; 
    visibility: visible !important;
}
ul{
    margin: 0px; 
    padding: 0px; 
}
li{
    list-style-type:none
}
div{
    max-width: 100%; 
}

iframe{
    margin: 0px; 
    padding: 0px; 
    width:700px !important;
    max-width: 100% !important; 
    height: auto !important; 
}

@media (min-width: 1200px) {
    body{
        background-color:#B0E2FF;
    }
    #bodyMain{
        width:700px;
        
    }
 }
 
@media ( max-width : 768px) {
    body{
        background-color:#fff
    }
    img{
        width: 100% !important; 
    }
}
</style>
</head>
<body style="">
<div id="bodyMain" style="padding-left:5px;padding-right:5px;padding-bottom:70px;padding-top:15px;background-color:#fff" class="container " >
<article>
 <c:if test="${content!=null }">
              ${content }
          </c:if>
          <c:if test="${content==null }">
              <c:forEach items="${pictures }" var="pic">
                    <div style="margin-top:10px">
                        <img style="max-width: 100% !important" alt="" src="${pic }" />
                    </div>
                </c:forEach>
          </c:if>
${content }
</article>
</div>
<script src="../js/jquery-1.11.2.min.js"></script>
<script type="text/javascript">

window.onload = function () {
	var u = navigator.userAgent;
	if (u.indexOf('Android') > -1 ||u.indexOf('iPhone') > -1||u.indexOf('Windows Phone') > -1) {//安卓手机
		var current = $("#bodyMain") ;
		//$(current).find("img").each(function(i){ 
		    //$(this).attr("style","width: 100%"); 
		    //$(this).attr("style","height: auto"); 
		//});
		$(current).find("iframe").load(function(i){ 
		    //$(this).height("auto"); 
		    //$(this).width($(current).width()); 
		});
	} 
}


</script>
</body>
</html>