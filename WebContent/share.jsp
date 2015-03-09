<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
    <style type="text/css">
    
    * { 
            margin:0; 
            padding:0; 
        } 
        #top{
            width:100%;
            height:173px;
        }
        #topPic{
            position:fixed;
            width:1125px;
            height:173px;
        }
        #downloadPic{
            position:fixed;
            top:39px;
            right:44px;
            width:265px;
            height:98px;
        }
       
        #user{
            width:100%;
            height:240px;
        }
        
        #userPic{
            position:relative;
            left:42px;
            top:70px;
            float:left;
             width:170px;
            height:170px;
        }
        
        #userName{
            position:relative;
            top:70px;
            left:88px;
            font-weight:bold;
            font-size:42px;
            font-family:'宋体',arial,georgia,verdana,helvetica,sans-serif;
            color:#f00;
        }
        
        #tags{
            position:relative;
            top:146px;
            left:88px;
            
            font-weight:bold;
            font-size:32px;
            font-family:'宋体',arial,georgia,verdana,helvetica,sans-serif;
            color:#555;
        }
        #article{
            clear:both;
            widht:100%;
            
        }
        #article p{
            margin-top:66px;
            margin-left:42px;
            width:1040px;
            height:50px;
            font-size:55px;
             font-family:'宋体',arial,georgia,verdana,helvetica,sans-serif;
        }
        
        #picture{
            widht:100%;
        }
        
        #picture img{
            margin-top:70px;
            margin-left:42px;
            width:900px;
        }
        #picture p{
            margin-left:42px;
            margin-top:38px;
            margin-bottom:38px;
            width:450px;
            font-family:'宋体',arial,georgia,verdana,helvetica,sans-serif;
            color:#555;
            font-size:30px;
        }
        
        #praise{
            widht:100%;
        }
        
        #praise hr{
            width:100%;
        }
        
        #praise p{
            margin-top:28px;
            margin-left:42px;
             font-size:50px;
        }
        .praisePic{
             margin-top:37px;
            position:relative;
            left:32px;
            widht:100px;
            height:100px;
            margin-top:20px;
        }
        
    </style>
</head>
<body>
	<div id="top">
	    <img alt="" src="images/top.png" id="topPic">
	    <a href=""><img src="images/download.png" id="downloadPic"></a>
	</div>
	
	<div id="user">
	        <img alt="" src="${user.headPic}" id="userPic" >
	        <p id="userName">${user.name }</p>
	        
	</div>
	<div id="article">
	   <p >
	${article.content}
	</p>
	</div>
	<div id="picture">
	       <img alt=" " src="${picture }">
	       <br >
	       <p>${article.date }</p>
	</div>
	
	<div id="praise">
	<hr >
	<p>赞(12)</p>
	    <c:if test="${!empty praiseUser }">
                      <c:forEach items="${praiseUser }" var="pu"  >
                       <a href=""> <img alt="" src="${pu.headPic }" class="praisePic"></a>
                      </c:forEach>
        </c:if>
	</div>
</body>
</html>