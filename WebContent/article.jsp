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
    width: 100% !important; 
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
    overflow:hide;
}
</style>
</head>
<body >
<div id="bodyMain" style="margin-bottom:70px;margin-top:15px" class="container " >

<article>
${content }
</article>
</div>
<script src="../js/jquery-1.11.2.min.js"></script>
<script type="text/javascript">
var current = $("#bodyMain") ;
$(current).find("img").each(function(i){ 
	$(this).attr("style","width: 100%"); 
	$(this).attr("style","height: auto"); 
});
$(current).find("iframe").load(function(i){ 
    //$(this).height("auto"); 
    $(this).width($(current).width()); 
});
</script>
</body>
</html>