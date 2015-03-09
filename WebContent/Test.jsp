<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript">
    $(document).ready(function(){
        $("#add").click(function(){
          
            	var user = {
            		    userId:"212",
            		    name:"dddd",
            		    sex:"1",
            		    type:["dddd","dddd"],
            		    birthday:"2012-12-13",
            		}
          //  var user = {userName:userName,age:age};
            $.ajax({
                url:"/StarkPet/user/update",
                type:"post",
                data:JSON.stringify(user),
                success:function(a){
                    alert("status--->" + a.status + "----------result--->" + a.result );
                    
                },
                dataType: "json",
                contentType: "application/json",
            });
        });
        
        $("#addUser").click(function(){
            
        	var date = new Date();
            var user ={
            		headPic:"http://ww4.sinaimg.cn/crop.0.0.540.540.1024/e9182c14jw8ebl8fk1tc4j20f00f0my5.jpg",
            		homeTown: "dddddd",
            		name : "ffffff",
            		sex : "1",
            		}
               
            $.ajax({
                url:"/StarkPet/user/register.do",
                type:"post",
                data:JSON.stringify(user),
                success:function(a){
                    alert("----result--->" + a.result );
                    
                },
                dataType: "json",
                contentType: "application/json",
            });
        });
        
 $("#publishArticle").click(function(){
            
            var article ={
            		user:{userId:"178",name:"夏日泡沫"},
            	    content:"夏日泡沫",
            	    url:"http://www.baidu.com",
            	    picSet:["pic1","pic2","pic3"],
            	    atUser:["200","22"],
                }
               
            $.ajax({
                url:"/StarkPet/article/publish2.do",
                type:"post",
                data:JSON.stringify(article),
                success:function(a){
                    alert("status--->" + a.status + "----------result--->" + a.result );
                    
                },
                dataType: "json",
                contentType: "application/json",
            });
        });
 
	 $("#comment").click(function(){
	     
	     var comment = {
	             article:{articleId:"225",user:{userId:"100"}},
	             user:{userId:"178",userName:"test"},
	             content:"ddddddddddddddddddddd",
	             atUsers:["123","234"],
	         }
	//  var user = {userName:userName,age:age};
	 $.ajax({
	     url:"/StarkPet/comment/publish.do",
	     type:"post",
	     data:JSON.stringify(comment),
	     success:function(a){
	         alert("status--->" + a.status + "----------result--->" + a.result );
	         
	     },
	     dataType: "json",
	     contentType: "application/json",
	 });
	});
	 
$("#activity").click(function(){
         
         var activity = {
        		 user:{id:"ff808181495fe0ec01495fe127390000"},
        		    article:
        		    {
        		    	id:"ff8081814964c546014964c9bc5b0000",
        		        user:{id:"ff808181495fe0ec01495fe127390000"},
        		        content:"dfsfsfsfsfsfs",
        		        picSet:["pic1","pic2","pic3"],
        		    },
        		    subject:"ddsfds",
        		    bannerPic:"sfafaf",
             }
    //  var user = {userName:userName,age:age};
     $.ajax({
         url:"/StarkPet/activity/publish.do",
         type:"post",
         data:JSON.stringify(activity),
         success:function(a){
             alert("status--->" + a.status + "----------result--->" + a.result );
             
         },
         dataType: "json",
         contentType: "application/json",
     });
    });
		$("#addFollows").click(function(){
		    
		    var userFollows = {
		            user:"ff808181495fe0ec01495fe127390000",
		            follows:["ff8081814963fc83014964006e350003","ff8081814963fc83014963fcb1410000"],
		        }
		//  var user = {userName:userName,age:age};
		$.ajax({
		    url:"/StarkPet/user/addFollows.do",
		    type:"post",
		    data:JSON.stringify(userFollows),
		    success:function(a){
		        alert("status--->" + a.status + "----------result--->" + a.result );
		        
		    },
		    dataType: "json",
		    contentType: "application/json",
		});
		});
    });
</script>
</head>
<body>
   <h1>json添加用户</h1>

    <input type="button" id="add" value="添加">
    <input type="button" id="addUser" value="Add User">    
    <input type="button" id="publishArticle" value="Publish Article">    
    <input type="button" id="comment" value="Publish Comment"> 
    <input type="button" id="activity" value="Add Activity"> 
    <input type="button" id="addFollows" value="Add Follows"> 
</body>
</html>