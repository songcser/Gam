<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>${article.title }</title>
<style type="text/css">
img{
    margin: 0px; 
    padding: 0px; 
    max-width: 100%; 
    height: auto !important; 
    box-sizing: border-box !important; 
    word-wrap: break-word !important; 
    width: 591px !important; 
    visibility: visible !important;
}
</style>
</head>
<body >
<div style="margin-bottom:70px" >

<article>
${content }
</article>
</div>

</body>
</html>