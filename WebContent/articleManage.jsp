<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>史塔克后台管理-推文管理</title>
<link rel="stylesheet" type="text/css" href="../css/styles.css">
<link rel="stylesheet" href="../css/pintuer.css">
<link rel="stylesheet" href="../css/kalendae.css">
<link type="image/png" href="${webIcon }" rel="shortcut icon" />
<link href="${webIcon }" rel="bookmark icon" />
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<script src="../js/pintuer.js"></script>
<script src="../js/respond.js"></script>
<script src="../js/content.js"></script>
<script src="../js/admin.js"></script>
<script src="../js/kalendae.standalone.js" type="text/javascript" charset="utf-8"></script>
<style type="text/css">
#preview {
	width: 100px;
	height: 100px;
	border: 0px solid #000;
	overflow: hidden;
}

#imghead {
	filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=image);
}

.expdiv{

    padding:0px;

    width:150px;
    
    border:0px solid #c8c8c8;

}
.expbtn{

    position:relative;
    right:0px;
    z-index:2;
    width:150px;
    margin:0px 0px 0px 0px;

}

</style>
<script type="text/javascript">

    //var selectType = "";
	function callback(str, userId) {
	    if (str == '1') {
	        alert('发布成功');
	    } else if(str == '2'){
	        alert('图片不可用 发布失败');
	    }else {
	        alert('发布失败');
	    }
	    $("#preview3")[0].innerHTML = "";
	    $("#preview4")[0].innerHTML = "";
	    $("#preview5")[0].innerHTML = "";
	    $("#preview6")[0].innerHTML = "";
	    $("#articleContent").val("");
	    $("#reference").val("");
	    if($("#articleType").val()=="3"){
	    	var url = "/StarkPet/article/getMagazineArticles.do?";
            showArticleList(url,0);
	    }
	    else if($("#articleType").val()=="2"){
	    	var url = "/StarkPet/article/getDayExquisiteArticles.do?userId="+${userId}+"&";
            showArticleList(url,0);
	    }
	    
	    resetArticlePicture();
	}
	function createActivity() {
		
		$("#activityArticleListDiv").css({
			display : "none"
		});
		$("#createActivityDiv").css({
			display : "block"
		});
		$("#createArticleDiv").css({
            display : "none"
        });
	}
	
	function cancleCreateActivity(){
		$("#activityArticleListDiv").css({
            display : "block"
        });
        $("#createActivityDiv").css({
            display : "none"
        });
        $("#createArticleDiv").css({
            display : "none"
        });
	}
	
	function selectActivity(){
		$("#activityArticleListDiv").css({
            display : "block"
        });
        $("#createActivityDiv").css({
            display : "none"
        });
        $("#createArticleDiv").css({
            display : "none"
        });
	}
	
	function createArticle(){
		$("#activityArticleListDiv").css({
            display : "none"
        });
        $("#createActivityDiv").css({
            display : "none"
        });
        $("#createArticleDiv").css({
            display : "block"
        });
	}
	
	function showDiv(divObj){
		if(divObj=="article"){
			
			$("#articleDiv").css({
	            display : "block"
	        });
			
			$("#filterDateDiv").css({
                display : "block"
            });
			$("#chartletDiv").css({
	            display : "none"
	        });
			$("#systemDiv").css({
                display : "none"
            });
			 $("#createArticleDiv").css({
	                display : "none"
	         });
			 $("#commentListDiv").css({
	                display : "none"
	         });
			 $("#userGroupDiv").css({
		            display : "none"
		     });
		     $("#commentDiv").css({
		         display : "none"
		     });
			 var currentDate=$("#filterDate").val();
			 var type = $("#articleTypeSelect").val();
		     var url = "/StarkPet/article/getArticlesByDate.do?date="+currentDate+"&type="+type+"&";
		     showArticleList(url,0);
		}
		else if(divObj=="chartlet"){
			$("#articleDiv").css({
                display : "none"
            });
            $("#chartletDiv").css({
                display : "block"
            });
            $("#systemDiv").css({
                display : "none"
            });
            $("#commentListDiv").css({
                display : "none"
            });
		}
		else if(divObj=="system"){
            $("#articleDiv").css({
                display : "none"
            });
            $("#chartletDiv").css({
                display : "none"
            });
            $("#systemDiv").css({
                display : "block"
            });
            $("#commentListDiv").css({
                display : "none"
            });
        }else if(divObj=="magazine"){
        	$("#articleDiv").css({
                display : "block"
            });
            $("#chartletDiv").css({
                display : "none"
            });
            $("#systemDiv").css({
                display : "none"
            });
            $("#createArticleDiv").css({
                display : "block"
            });
            $("#filterDateDiv").css({
                display : "none"
            });
            $("#commentListDiv").css({
                display : "none"
            });
            $("#userGroupDiv").css({
                display : "none"
            });
            $("#commentDiv").css({
               display : "none"
            });
           // var date = new Date();
            //var currentDate = date.format("yyyy-MM-dd");
            
            $("#articleType").val("3");
            var url = "/StarkPet/article/getMagazineArticles.do?";
            showArticleList(url,0);
           // $("#filterDate").val(currentDate);
            
        }else if(divObj=="exquisite"){
        	$("#articleDiv").css({
                display : "block"
            });
            $("#chartletDiv").css({
                display : "none"
            });
            $("#systemDiv").css({
                display : "none"
            });
            $("#filterDateDiv").css({
                display : "none"
            });
            $("#createArticleDiv").css({
                display : "block"
            });
            $("#commentListDiv").css({
                display : "none"
            });
            $("#userGroupDiv").css({
                display : "none"
            });
            $("#commentDiv").css({
               display : "none"
            });
            $("#articleType").val("2");
            var url = "/StarkPet/article/getDayExquisiteArticles.do?userId="+${userId}+"&";
            showArticleList(url,0);
        }
	}
	
	function selectChartletType(type){
		//alert(type);
		$("#chartletTypeId").val(type);
		if(type=="0"){
			document.getElementById("wordChartlet").style.display="block";
            document.getElementById("pictureChartlet").style.display="none";
            $("#wordChartletBtn").addClass("bg-blue");
            $("#pictureChartletBtn").removeClass("bg-blue");
		}
		else if(type=="1"){
			document.getElementById("pictureChartlet").style.display="block";
			document.getElementById("wordChartlet").style.display="none";
			$("#wordChartletBtn").removeClass("bg-blue");
            $("#pictureChartletBtn").addClass("bg-blue");
        }
		
	}
	
	function createChartlet(){
		if('${userRole}'!="1"){
            alert("没有权限设置");
            return;
        }
		var type = $("#chartletTypeId").val();
		var title= $("#chartletTitle").val();
		if(title==""){
			alert("请输入标签")
			return;
		}
		var chartlet = {type:type,title:title}
		$.ajax({
            url : "/StarkPet/article/createChartlet.do",
            type : "post",
            data:JSON.stringify(chartlet),
            success : function(ret) {

                if (ret.result == "1") {
                    //alert("创建成功");
                   var chartletId = ret.chartletId;
                   var title = ret.title;
                   var divStr =  '<div class="media-inline border" >'
                     +'<form id="chartletPicture'+chartletId+'" name="formFile" method="post" action="../article/addChartletPicture.do" target="frameFile" enctype="multipart/form-data">'
                     +'<div class="text-large text-yellow padding-large-left text-justify padding-top">'
                     +'<input type="hidden" value="'+chartletId+'" name="chartletId">'
                     +'<div class="text-large text-yellow float-left">'+title+'</div>'
                     +' <div class="float-left">'
                     +' <a class="button input-file bg-sub margin-large-left" href="javascript:void(0);"> '
                     +' <input size="100" type="file" name="file" id="chartletPic'+chartletId+'" onchange="addFile('+chartletId+')"/>添加图片</a>'
                     +' </div> </div></form>'
                     +'<div  class="media-inline padding" style="overflow:auto;clear:both">'
                     +'  <table class="table table-hover " width="100%">'
                     +'     <tr id="piclist'+chartletId+'"> <td></td></tr>'
                     +'   </table> </div></div>';
                    
                   var ctId = $("#chartletTypeId").val();
                   if(ctId=="0"){
                	   $("#wordChartlet").prepend(divStr); 
                   }
                   else if(ctId=="1"){
                	   $("#pictureChartlet").prepend(divStr); 
                   }
                   $("#chartletTitle").val("");
                }
            },
            dataType : "json",
            contentType : "application/json",
        });
	}
	
	function addFile(id){
		var form = "chartletPicture"+id;
		$('#chartletPicture'+id).submit();
	}
	
	function callbackChartlet(url,id,picId){
		if(url=="0"){
			return ;
		}
		//alert(id);
		var tdObj = '<td class="border-right" width="200px" height="270px" ><div  style="height:200px;width:200px">'
		+' <a href="javascript:removeChartletPicture()" style="position:relative;left:195px;top:0px">'
		+' <span class="icon-times-circle" ></span></a>'
		+'<a><img src="'+url+'" alt="..." width="200px" height="200px"></a>'
		+'<div class="button-group checkbox padding-top" style="width:200px;text-align:center;">'
        +'<label  class="button "><input name="pintuer" type="checkbox"  onclick="changeChartletStatus(this,'+picId+')"><span class="icon icon-check"></span> 最新</label>'
		+' </div></div></td>';
		$("#piclist"+id).prepend(tdObj); 
		$('#chartletPicture'+id).reset();
		
	}
	
	function changeChartletStatus(checkboxObj,picId){
		if('${userRole}'!="1"){
            alert("没有权限设置");
            return;
        }
		var status=0;
		if(checkboxObj.checked){
			status=1;
		}else{
			status=0;
		}
		$.ajax({
            url : "/StarkPet/article/changeChartletPictureStatus.do?pictureId="+picId+"&status="+status,
            type : "get",
            //data:JSON.stringify(chartlet),
            success : function(ret) {
                if (ret.result == "0") {
                	checkboxObj.checked=!checkboxObj.checked;
                }
            },
            dataType : "json",
            contentType : "application/json",
        });
	}
	
	function removeChartletPicture(trId,picId){
		if('${userRole}'!="1"){
            alert("没有权限设置");
            return;
        }
		$.ajax({
            url : "/StarkPet/article/removeChartletPicture.do?pictureId="+picId,
            type : "get",
            //data:JSON.stringify(chartlet),
            success : function(ret) {

                if (ret.result == "1") {
                    var trObj = document.getElementById("piclist"+trId);
                    var tdObj = document.getElementById("picObj"+picId);
                    trObj.removeChild(tdObj);
                }
            },
            dataType : "json",
            contentType : "application/json",
        });
	}
	

    function viewPicture(str){
        $("#picImgId").attr("src", str);
        $("#picDialogBtn").click();
    }
	
	function showArticleList(url,page){
		page = parseInt(page);
		var div = document.getElementById('articleObjectDiv'); 
        div.scrollTop = 0; 
		$.ajax({
            url:url+"userId="+${userId}+"&page="+page,
            type:"post",
            //data:JSON.stringify(comment),
            success:function(a){
                //alert("status--->" + a.status + "----------result--->" + a.result );
                if(a.result=="1"){
                	//alert(a.result);
                    var articleList = a.articles;
                    var articles = "";
                    if(articleList.length==0&&page>0){
                    	return;
                    }
                    for(var i=0;i<articleList.length;i++){
                        var art = articleList[i];
                        var pictures = "";
                        var date = strToDate(art.date);
                        var strDate = calculateDate(date);
                        
                        var picList = art.pictures;
                        for (var j = 0; j < picList.length; j++) {
                        	var pic = picList[j];
                        	var indexP = pic.indexOf("?");
                        	if(indexP>0){
                        		pic = pic.substring(0,indexP);
                        	}
                        	var picstr = "'"+picList[j]+"'";
                            pictures += '<a href="javascript:viewPicture('+picstr+')"><img src="'+picList[j]+'" width="100px" height="100px" class="img-border radius-small" alt="..." /> </a>';
                        }
                        var type=art.type;
                        var typeStr = "";
                        
                        if(type=="0"){
                        	typeStr = "普通";
                        }else if(type=="2"){
                        	typeStr = "每日精选";
                        }
                        else if(type=="3"){
                            typeStr = "杂志";
                        }
                        else if(type=="4"){
                            typeStr = "精选杂志";
                        }
                        else if(type=="5"){
                            typeStr = "举报";
                        }
                        else if(type=="6"){
                            typeStr = "每日精选被举报";
                        }
                        else if(type=="7"){
                            typeStr = "杂志被举报";
                        }
                        else if(type=="8"){
                            typeStr = "精选杂志被举报";
                        }
                        else if(type=="9"){
                            typeStr = "未审核活动推文";
                        }
                        else if(type=="10"){
                            typeStr = "活动推文";
                        }
                        else if(type=="11"){
                        	typeStr = "已删除";
                        }
                        var role = art.userRole;
                        var roleStr;
                        if(role==0){
                        	roleStr="普通用户";
                        }
                        else if(role==1){
                        	roleStr = "管理员";
                        }
                        else if(role==2){
                        	roleStr = "运营人员";
                        }
                        else if(role==3){
                        	roleStr = "模拟用户";
                        }
                        else if(role==6){
                        	roleStr = "重要用户";
                        }
                        articles += '<div class="media media-x radius bg-white" id="articleObjDiv'+art.articleId+'">'
                                + '<div class="float-left text-center padding-top padding-left">'
                                + '<a href="#"> <img src="'+art.headPic+'" class="radius-circle" width="60px" height="60px" alt="..."> </a>'
                                + '<div class="padding-top"><a href="javascript:followUser('+art.userId+')" ><span class="icon-check-square-o text-large text-green"></span></a></div>'
                                + '</div>'
                                + '   <div class="media-body"><div class="margin">'
                                + '      <h4>'+ art.userName +'<small> ('+roleStr+')</small></h4>'
                                +            art.content
                                + '    </div><div class="margin-big-top margin-right margin-left margin-big-bottom">'
                                +        pictures
                                + '        </div>'
                                + '    <div class="margin-left text-small text-gray">'
                                + strDate +'  出处: '+art.reference
                                + '</div></div><div class="button-group button-group-justified">'
                                + '       <a href="javascript:getComment('+art.articleId+','+art.userId+')" class="button ">评论 <span class="badge bg-white-light" id="commentCount'+art.articleId+'">'+art.commentCount+'</span></a>'
                                + '       <a href="javascript:praiseArticle('+art.articleId+')" class="button ">赞<span class="badge bg-white-light" id="praiseCount'+art.articleId+'">'+art.praiseCount+'</span></a>'
                                + '       <a href="javascript:changeArticleType('+art.articleId+')" class="button" id="articleType'+art.articleId+'">'+typeStr+' </a>'
                                + '       <a href="javascript:deleteArticle('+art.articleId+')" class="button ">删除 </a>'
                                + '</div>   </div></div></div>';
                    }
                    
                    if(articleList.length==10){
                        $("#paginationDiv").css({
                            display : "block"
                        });
                        var nextPage = page+1;
                        var prePage = page-1;
                        var ination = '<ul class="pagination pagination-group ">';
                        url = "'"+url+"'";
                        if(page!=0){
                            ination += '<li><a href="javascript:showArticleList('+url+','+prePage+')">上一页</a></li>';
                        }else {
                            ination += '<li><a disabled="false" href="javascript:void(0);">上一页</a></li>';
                        }
                        if(articleList.length==10){
                            ination += '<li><a href="javascript:showArticleList('+url+','+nextPage+')">下一页</a></li></ul>';
                        }else {
                            ination += '<li><a disabled="false" href="javascript:void(0);">下一页</a></li></ul>';
                        }
                        
                        $("#paginationDiv")[0].innerHTML=ination;
                    }
                    else{
                        $("#paginationDiv").css({
                            display : "none"
                        });
                    }
                    //alert("发布成功");
                    $("#articleListDiv")[0].innerHTML = articles;
                    //$("#commentContent").val("");
                    //getComments(articleId);
                }
                else{
                    alert("获取失败");
                }
            },
            dataType: "json",
            contentType: "application/json",
        });
	}
	
	function deleteChartlet(chartletId){
		
	}
	
	function getComment(articleId,userId){
		$("#userGroupDiv").css({
            display : "none"
        });
		getComments(articleId,userId);
	}
	
	function changeArticleType(articleId){
		if('${userRole}'!="1"){
            alert("没有权限设置");
            return;
        }
		$("#articleId").val(articleId);
		$("#dialogBtn").click();
	}
	
	function okDialog(){
		var articleId=$("#articleId").val();
        var type = $('input:radio[name=articleTypeInput]:checked').val();
        $.ajax({
            url : "/StarkPet/article/changeArticleType.do?articleId="+articleId+"&type="+type,
            type : "get",
            //data:JSON.stringify(chartlet),
            success : function(ret) {
            	
                if (ret.result == "1") {
                	 var pObj = document.getElementById("articleType"+articleId);
                     pObj.innerHTML = ret.type;
                }
                else {
                	alert("设置失败");
                }
            },
            dataType : "json",
            contentType : "application/json",
        });
	}
	
	function deleteArticle(articleId){
		if('${userRole}'!="1"){
			alert("没有权限删除");
			return;
		}
		$.ajax({
            url : "/StarkPet/article/changeArticleType.do?articleId="+articleId+"&type="+11,
            type : "get",
            //data:JSON.stringify(chartlet),
            success : function(ret) {
                
                if (ret.result == "1") {
                	var pObj = document.getElementById("articleType"+articleId);
                    pObj.innerHTML = ret.type;
                }
                else {
                    alert("删除失败");
                }
            },
            dataType : "json",
            contentType : "application/json",
        });
	}
	
    function deleteChartlet(chartletId){
    	if('${userRole}'!="1"){
            alert("没有权限删除");
            return;
        }
    	$.ajax({
            url : "/StarkPet/article/deleteChartlet.do?chartletId="+chartletId,
            type : "get",
            //data:JSON.stringify(chartlet),
            success : function(ret) {
                
                if (ret.result == "1") {
                	$("#chartletPictureDiv"+chartletId).remove();
                }
                else {
                    alert("删除失败");
                }
            },
            dataType : "json",
            contentType : "application/json",
        });
    }
	
	function systemNotice(){
		if('${userRole}'!="1"){
            alert("没有权限发布");
            return;
        }
		
		$("#systemform").submit();
	}
	
	
	function publishComments(){
        //alert(${userId});
        //$("#articleUserId").val(${userId});
        if($("#commentContent").val()==""){
            alert("请输入文字");
            return;
        }
        var userId = $("#commentUserId").val();
        var name =  $("#commentUserId").find("option:selected").text();
        publishComment(userId,name);
        var articleId = $("#commentArticleId").val();
        var pObj = document.getElementById("commentCount"+articleId);
        var count = pObj.innerHTML;
        pObj.innerHTML = parseInt(count)+1;
    }
	
	function getArticlesByDate(){
		var currentDate=$("#filterDate").val();
		var type = $("#articleTypeSelect").val();
        var url = "/StarkPet/article/getArticlesByDate.do?date="+currentDate+"&type="+type+"&";
        showArticleList(url,0);
	}
	
	function callbackSystem(str){
		alert(str);
	}
	
	
	function init(){
		var date = new Date();
		var currentDate = date.format("yyyy-MM-dd");
		var url = "/StarkPet/article/getArticlesByDate.do?date="+currentDate+"&type=100&";
		showArticleList(url,0);
		$("#filterDate").val(currentDate);
	}
	
	function publishArticle(){
        //if($("#articleContent").val()==""){
        //    alert("请输入内容");
        //    return;
        //}
        $("#articleFormFile").submit();
        
    }
</script>
</head>
<body onload="init()">
	<div style="height:100%">
		<ul id="header">
			<li style="width: 70px"><a href="toMain.do">首页</a></li>
			<li><a href="operatorManage.do">账号管理</a></li>
			<li><a href="articleManage.do">推文管理</a></li>
			<li><a href="activityManage.do">活动管理</a></li>
			<li><a href="publishManage.do">发布管理</a></li>
			<li><a href="systemManage.do">系统管理</a></li>
		</ul>
		<div class="  nav-navicon padding-large-top bg" style="width: 10%; height: 93%; float: left;">
			<ul id="subject" class="nav nav-pills nav-navicon border-main nav-menu ">
				<li><a href="javascript: showDiv('article');">推文管理</a></li>
				<li><a href="javascript: showDiv('magazine');">杂志管理</a></li>
				<li><a href="javascript: showDiv('exquisite');">每日精选</a></li>
				<li><a href="javascript: showDiv('chartlet');">贴图管理</a></li>
				<li><a href="javascript: showDiv('system');">系统群发</a></li>
			</ul>
		</div>
		<div id="articleDiv" style="width: 90%; height: 93%; float: left;">
		  
			<div id="articleObjectDiv" style="width: 60%;height: 100%;float: left;overflow:auto;padding-left:80px;padding-right:80px;padding-top:40px" class="border-right " >
			<div id="createArticleDiv" style="display:none">
                <form id='articleFormFile' name='formFile' method="post" action="../article/addArticle.do" target='frameFile' enctype="multipart/form-data">
                    <input type="hidden" name="articleType" id="articleType">
                     <div class="form-group">
                      <div  class="label">
                     <label for="userId">选择用户</label>
                     </div>
                     <div class="field margin-left  ">
                     <select name="userId" class="input">
                        <c:if test="${!empty operations }">
                            <c:forEach items="${operations }" var="u">
                                <option value="${u.userId }">${u.name }</option>
                            </c:forEach>
                         </c:if>
                     </select>
                     </div>
                     </div>
                     <div class="form-group">
                    <div class="label">
                        <label for="content">发布内容</label>
                    </div>
                    <div class="field margin-left  ">
                        <textarea id="articleContent" name="content" class="input" rows="3" cols="30" placeholder="500字以内"></textarea>
                    </div>
                    </div>
                    <div class="form-group">
                        <div class="label">
                            <label for="reference">推文出处</label>
                        </div>
                        <div class="field margin-left">
                            <input type="text" class="input" id="reference" name="reference" size="50" />
                        </div>
                    </div>
                    
                    <div style="height:50px" class="padding-big-top margin-left">
                    
                    <div class="label float-left">
                        <a class="button input-file bg-green " style="width: 50px" href="javascript:void(0);" id="addArticlePicture0"> <span class="icon-image text-big"></span> <input
                            type="file" name="picture1" onchange="previewArticleImage(this)"  id="articlePictureInput1"/>
                        </a> <a class="button input-file bg-green" style="display: none; width: 50px" href="javascript:void(0);" id="addArticlePicture1"> <span class="icon-image text-big"></span>
                            <input type="file" name="picture2" onchange="previewArticleImage(this)"  id="articlePictureInput2"/>
                        </a> <a class="button input-file bg-green" style="display: none; width: 50px" href="javascript:void(0);" id="addArticlePicture2"> <span class="icon-image text-big"></span>
                            <input type="file" name="picture3" onchange="previewArticleImage(this)"  id="articlePictureInput3"/>
                        </a> <a class="button input-file bg-green" style="display: none; width: 50px" href="javascript:void(0);" id="addArticlePicture3"> <span class="icon-image text-big"></span>
                            <input type="file" name="picture4" onchange="previewArticleImage(this)"  id="articlePictureInput4"/>
                        </a>
                        <a class="button input-file bg-green" style="display: none; width: 50px" href="javascript:void(0);" id="addArticlePicture4">
                          <span class="icon-image text-big"></span>
                          <input type="file" name="picture5" onchange="previewArticleImage(this)"  id="articlePictureInput5"/>
                        </a>
                        <a class="button input-file bg-green" style="display: none; width: 50px" href="javascript:void(0);" id="addArticlePicture5">
                          <span class="icon-image text-big"></span>
                          <input type="file" name="picture6" onchange="previewArticleImage(this)"  id="articlePictureInput6"/>
                        </a>
                        <a class="button input-file bg-green" style="display: none; width: 50px" href="javascript:void(0);" id="addArticlePicture6">
                          <span class="icon-image text-big"></span>
                          <input type="file" name="picture7" onchange="previewArticleImage(this)"  id="articlePictureInput7"/>
                        </a>
                        <a class="button input-file bg-green" style="display: none; width: 50px" href="javascript:void(0);" id="addArticlePicture7">
                          <span class="icon-image text-big"></span>
                          <input type="file" name="picture8" onchange="previewArticleImage(this)"  id="articlePictureInput8"/>
                        </a>
                        <a class="button input-file bg-green" style="display: none; width: 50px" href="javascript:void(0);" id="addArticlePicture8">
                          <span class="icon-image text-big"></span>
                          <input type="file" name="picture9" onchange="previewArticleImage(this)"  id="articlePictureInput9"/>
                        </a>
                        <a class="button input-file bg-green" style="display: none; width: 50px;" disabled="disabled" href="javascript:void(0);" id="disablesArticlePicture"> 
                          <span class="icon-image text-big"></span> <input type="file" onchange="previewArticleImage(this)" />
                        </a>
                         <a class="button input-file bg-green" style="display: none; width: 50px;" disabled="disabled" href="javascript:void(0);" id="disablesArticlePicture"> <span class="icon-image text-big"></span> <input type="file" onchange="previewArticleImage(this)" />
                        </a>
                    </div>
                    <div class="field float-left " >

                        <div id="preview3" class="margin-center  padding-small float-left "></div>
                        <div id="preview4" class="margin-center  padding-small float-left "></div>
                        <div id="preview5" class="margin-center  padding-small float-left "></div>
                        <div id="preview6" class="margin-center  padding-small float-left "></div>
                        <div id="preview7" class="margin-center  padding-small float-left "></div>
                        <div id="preview8" class="margin-center  padding-small float-left "></div>
                        <div id="preview9" class="margin-center  padding-small float-left "></div>
                        <div id="preview10" class="margin-center  padding-small float-left "></div>
                        <div id="preview11" class="margin-center  padding-small float-left "></div>
                    </div>
                    </div>
                    <div style="width: 100%; clear: both">
                        <div class="form-button text-right padding-bottom">
                            <button class="button bg-green" type="button" onclick="publishArticle()">发布</button>
                        </div>
                    </div>
                </form>
            </div>
             <div class="form-group">
			  <div class="input-inline  float-right margin-large-bottom" id="filterDateDiv">
				  <select class=" button float-left" name="age" id="articleTypeSelect">
				   <c:if test="${!empty articleTypes }">
				     <option selected="selected" value="100">全部</option>
	                  <c:forEach items="${articleTypes }" var="a">
	                      <option value="${a.index }">${a.name }</option>
	                  </c:forEach>
	               </c:if>
	            </select>
                <input type="text" class=" input auto-kal" id="filterDate" name="filterDate" data-kal="format:'YYYY-MM-DD'" placeholder="输入日期" />
                <input class="button" type="button" onclick="getArticlesByDate()" value="查询" />
                   
              </div>
              <div id="articleListDiv" class="padding bg-mix radius margin-large-top" style="clear:both">
              </div>
               <div id="paginationDiv" class="button-group button-group-justified bg-mix" style="display: none"></div>
			</div>
			</div>
			<div id="thirdDiv" class="padding-large" style="width:40%;height: 100%; overflow: auto; float: left; border-right: 1px #aaa solid;">
              <div id="commentDiv" style="display:none">
                  <div id="createCommentDiv">
                      <input type="hidden" id="commentArticleId">
                       <input type="hidden" id="articleUserId">
                       <div  class="label">
                        <label for="userId">选择用户</label>
                     </div>
                     <div class="field margin-left  margin-bottom">
                     <select name="commentUser" class="input" id="commentUserId" >
                        <c:if test="${!empty operations }">
                            <c:forEach items="${operations }" var="u">
                                <option value="${u.userId }">${u.name }</option>
                            </c:forEach>
                         </c:if>
                     </select>
                     </div>
                     
                      <div class="label">
                            <label for="readme">发布评论</label>
                        </div>
                        <div class="field margin-left  ">
                            <textarea id="commentContent" name="content" class="input" rows="3" cols="30" placeholder="500字以内"></textarea>
                        </div>
                        <div class="form-button text-right padding-top">
                            <button class="button bg-green" type="submit" onclick="publishComments()">发布</button>
                        </div>
                        
                  </div>
                  <div id="commentListDiv" class="bg-back margin-top padding radius">
                      
                  </div>
              </div>
              <div id="userGroupDiv" style="display:none">
                <div class="media-inline media-y border-gray padding-large-top" >
                    <c:forEach items="${operations }" var="u">
                    <div class="media media-y clearfix padding margin-large-top " style="width:80px">
                        <a href="#"> <img src="${u.getHeadUrl()}" style="height: 50px; width: 50px" class="radius-circle" /></a>
                        <div class="media-body"> <strong>${u.name }</strong></div>
                        <div class=" button-group checkbox">
                           <input name="userGroupBox" value="${u.userId }" type="checkbox"><span class="icon icon-check text-green"></span>
                        </div>
                    </div>
                    </c:forEach>
                     <div class="form-button text-right padding-top margin-large-right">
                        <button class="button bg-green" type="button" onclick="selectUserSubmit()">提交</button>
                     </div>
                </div>
              </div>
         </div>
		</div>
		<div id="chartletDiv" style="width: 90%;height: 93%;overflow:auto; float: left; display: none">
		
		  <div class="padding-large" style="width: 10%;float: left;">
		      <div class="button-group-y">
		        <div class="button-group">
		            <button type="button" class="button bg-blue" onclick="selectChartletType(0)" id="wordChartletBtn">字帖</button>
		             <button type="button" class="button"  onclick="selectChartletType(1)" id="pictureChartletBtn">图贴</button>
		        </div>
		      </div>
		  </div>
		  <div class="padding-large" style="width:90%;float: left;">
		      <div class="margin-large-top">
		           <input type="hidden" id="chartletTypeId" value="0">
                   <ul class="list-inline">
                     <li> <p >标签</p></li>
                     <li><input  type="text" class="input radius-rounded" id="chartletTitle" /></li>
                     <li> <button class="button bg-sub " onclick="createChartlet()">创建套图</button></li>
                     
                   </ul>
		      </div>
		     
		          <div class="border margin-large-top " id="wordChartlet">
		          <c:forEach items="${chartlets }" var="o">
                    <c:if test="${o.type=='0'}">
	                    <div class="media-inline border" id="chartletPictureDiv${o.chartletId }">
		                  <form id='chartletPicture${o.chartletId}' name='formFile' method="post" action="../article/addChartletPicture.do" target='frameFile' enctype="multipart/form-data">
		                    <div class="text-large text-yellow padding-large-left text-justify padding-top">
		                    <input type="hidden" value="${o.chartletId}" name="chartletId">
		                    <div class="text-large text-yellow float-left">${o.title}</div>
		                      <div class="float-left">
		                     <a class="button input-file bg-sub margin-large-left" href="javascript:void(0);"> 
		                     <input size="100" type="file" name="file" id="chartletPic${o.chartletId }" onchange="addFile('${o.chartletId }')"/>添加图片 
		                     </a>
		                     <a class="button input-file bg-sub margin-large-left" href="javascript:deleteChartlet(${o.chartletId });"> 
		                      删除套图
		                      </a>
		                     </div>
		                   </div>
		                   </form>
		                    <div  class="media-inline padding" style="overflow:auto;clear:both">
		                       <table class="table table-hover " width="100%">
		                          <tr id="piclist${o.chartletId }">
		                        <c:forEach items="${o.picList }" var="pic">
		                         <td class="border-right" width="200px" height="270px" id="picObj${pic.id }">
		                         <div style="height:200px;width:200px">
		                         <a href="javascript:removeChartletPicture(${o.chartletId },${pic.id })" style="position:relative;left:195px;top:0px">
		                         <span class="icon-times-circle" ></span></a>
		                          <a><img src="${pic.getPicUrl(o.chartletId) }" width="200px" height="200px"></a>
		                           <div class="button-group checkbox padding-top" style="width:200px;text-align:center;">
		                            <c:if test="${pic.status==0 }">
                                    <label  class="button "><input name="pintuer" type="checkbox"  onclick="changeChartletStatus(this,'${pic.id}')"><span class="icon icon-check"></span> 最新</label>
                                    </c:if>
                                     <c:if test="${pic.status==1 }">
                                    <label  class="button active"><input name="pintuer" type="checkbox" checked onclick="changeChartletStatus(this,'${pic.id}')"><span class="icon icon-check"></span> 最新</label>
                                    </c:if>
                                    </div>
		                          </div>
		                         </td>
		                        </c:forEach>
		                        <td></td>
		                       </tr>
		                       </table>
		                   </div>
		                  
		                 </div>
	                </c:if>
                </c:forEach>
		      </div>
		       <div class="border margin-large-top " id="pictureChartlet" style="display:none">
                  <c:forEach items="${chartlets }" var="o">
                    <c:if test="${o.type=='1'}">
                        <div class="media-inline border" id="chartletPictureDiv${o.chartletId }">
                          <form id='chartletPicture${o.chartletId}' name='formFile' method="post" action="../article/addChartletPicture.do" target='frameFile' enctype="multipart/form-data">
                            <div class="text-large text-yellow padding-large-left text-justify padding-top">
                            <input type="hidden" value="${o.chartletId}" name="chartletId">
                            <div class="text-large text-yellow float-left">${o.title}</div>
                              <div class="float-left">
                             <a class="button input-file bg-sub margin-large-left" href="javascript:void(0);"> 
                             <input size="100" type="file" name="file" id="chartletPic${o.chartletId }" onchange="addFile('${o.chartletId }')"/>添加图片
                             </a>
                             <a class="button input-file bg-sub margin-large-left" href="javascript:deleteChartlet(${o.chartletId });"> 
                              删除套图
                              </a>
                             </div>
                           </div>
                           </form>
                            <div  class="media-inline padding" style="overflow:auto;clear:both">
                               <table class="table table-hover " width="100%">
                                  <tr id="piclist${o.chartletId }">
                                <c:forEach items="${o.picList }" var="pic">
                                 <td class="border-right" width="250px" height="270px" id="picObj${pic.id }">
                                 <div style="height:200px;width:200px">
                                 <a href="javascript:removeChartletPicture(${o.chartletId },${pic.id })" style="position:relative;left:195px;top:0px">
                                 <span class="icon-times-circle" ></span></a>
                                  <a><img src="${pic.getPicUrl(o.chartletId) }" width="200px" height="200px"></a>
                                   <div class="button-group checkbox padding-top" style="width:200px;text-align:center;">
                                    <c:if test="${pic.status==0 }">
                                    <label  class="button "><input name="pintuer" type="checkbox"  onclick="changeChartletStatus(this,'${pic.id}')"><span class="icon icon-check"></span> 最新</label>
                                    </c:if>
                                     <c:if test="${pic.status==1 }">
                                    <label  class="button active"><input name="pintuer" type="checkbox" checked onclick="changeChartletStatus(this,'${pic.id}')"><span class="icon icon-check"></span> 最新</label>
                                    </c:if>
                                    </div>
                                  </div>
                                 </td>
                                </c:forEach>
                                <td></td>
                               </tr>
                               </table>
                           </div>
                          
                         </div>
                    </c:if>
                </c:forEach>
              </div>
			
		</div>
		
	</div>
	   <div id="systemDiv" style="width: 40%; display: none;" class="padding-large float-left">
            <form id='systemform' name='formFile' method="post" class="form form-tips" action="../notice/systemNotice.do" target='frameFile' enctype="multipart/form-data">
	            <label class="label">系统通知</label>
	            <input type="hidden" name="userId" value="${userId }">
	            <textarea rows="5" class="input" placeholder="500字以内" id="systemContent" name="systemArticle"></textarea>
	            <button class="button bg-green float-right margin-top" onclick="systemNotice()">发布</button>
            </form>
        </div>
       
    <button class="button button-big bg-main dialogs" id="dialogBtn" style="display:none" data-toggle="click" data-target="#mydialog" data-mask="1" data-width="40%"></button>
    <div id="mydialog">
    <div class="dialog" >
      <div class="dialog-head">
        <span class="dialog-close close rotate-hover"></span>
        <strong>设置推文</strong>
      </div>
      <div class="dialog-body">
      <input type="hidden" id="articleId">
       <div class="form-group">
         <div class="field">
            <div class="button-group radio">
	            <input name="articleTypeInput" value="0" checked="checked" type="radio"><span class="icon icon-bookmark "></span>普通
	            <input name="articleTypeInput" value="2" type="radio"><span class="icon icon-heart-o "></span>每日精选
	            <input name="articleTypeInput" value="3" type="radio"><span class="icon icon-book "></span>杂志
	            <input name="articleTypeInput" value="4" type="radio"><span class="icon icon-heart "></span>精选杂志
	            
            </div>
        </div>
       </div>
      </div>
      <div class="dialog-foot">
        <button class="dialog-close button">取消</button>
        <button class="dialog-close button bg-green" onclick="okDialog()">确认</button>
      </div>
    </div>
    </div>
	</div>
	<button class="button button-big bg-main dialogs" id="picDialogBtn" style="display:none" data-toggle="click" data-target="#picdialog" data-mask="1" ></button>
    <div id="picdialog" >
    <div class="dialog" >
      <div class="dialog-head">
        <span class="dialog-close close rotate-hover"></span>
        <strong>查看图片</strong>
      </div>
      <div class="dialog-body">
      <div style="height:600px;overflow:auto" class="text-center" id="showPicDiv">
      
        <img id="picImgId" src="" style="">
      </div>
      </div>
    </div>
    </div>
	<iframe id='frameFile' name='frameFile' style='display: none;'></iframe>
</body>
</html>