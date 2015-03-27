<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>史塔克后台管理-活动管理</title>
<link rel="stylesheet" type="text/css" href="../css/styles.css">
<link rel="stylesheet" href="../css/pintuer.css">
<link rel="stylesheet" href="../css/kalendae.css">
<link type="image/x-icon" href="${webIcon }" rel="shortcut icon" />
<link href="${webIcon }" rel="bookmark icon" />
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<script src="../js/pintuer.js"></script>
<script src="../js/respond.js"></script>
<script src="../js/content.js"></script>
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

.expdiv {
	padding: 0px;
	width: 150px;
	border: 0px solid #c8c8c8;
}

.expbtn {
	position: relative;
	right: 0px;
	z-index: 2;
	width: 150px;
	margin: 0px 0px 0px 0px;
}
</style>
<script type="text/javascript">
    var selectActivityId = "";
	function callback(str) {
	    if (str == '1') {
	        alert('发布成功');
	    }else if(str == '2'){
	        alert('图片不可用 发布失败');
	    } else {
	        alert('发布失败');
	    }
	    $("#preview3")[0].innerHTML = "";
	    $("#preview4")[0].innerHTML = "";
	    $("#preview5")[0].innerHTML = "";
	    $("#preview6")[0].innerHTML = "";
	    $("#articleContent").val("");
	    $("#reference").val("");
	    if(selectActivityId!=""){
	    	selectActivity(selectActivityId,"0");
	    }
	    ///var name = $("#articleUserName").val();
	    //selectUser(userId,name,0);
	    resetArticlePicture();
	    
	    
	    $("#bnnerPictureInput").attr("name","bnner");
	    $("#contentPictureInput").attr("name","content");
	    $("#preview1")[0].innerHTML = "";
        $("#preview2")[0].innerHTML = "";
        $("#subject").val("");
        var offdate = $("#offLineDate").val();
        if(offdate!=""){
        	location.reload();
        }
        
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
		
		$("#commentDiv").css({
            display : "none"
        });
		$("#userGroupDiv").css({
	        display : "none"
	    });
	}

	function cancleCreateActivity() {
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
    
	function changeActivityColor(activityId){

        var divList = document.getElementsByName("activityListDiv");
        for(var i=0;i<divList.length;i++){
            var divObj = divList[i];
            if(divObj.id=="activity"+activityId){
                divObj.className = "media bg-mix border margin";
            }
            else if(divObj.title=="0"){
                divObj.className = "media bg-green-light border margin";
            }
            else if(divObj.title==1){
                divObj.className = "media bg-red-light border margin";
            }
        }
	}
	
	 function viewPicture(str){
	        $("#picImgId").attr("src", str);
	        $("#picDialogBtn").click();
	    }
	
	function selectActivity(activityId,page) {
		
		selectActivityId = activityId;
		page = parseInt(page);
        if(page<0)
            return;
		$("#activityArticleListDiv").css({
			display : "block"
		});
		$("#createActivityDiv").css({
			display : "none"
		});
		$("#createArticleDiv").css({
			display : "none"
		});
		$("#commentDiv").css({
            display : "none"
        });
		$("#userGroupDiv").css({
            display : "none"
        });
		changeActivityColor(activityId);
	    var userId="${userId}";
		 $.ajax({
	            url:"/StarkPet/article/getAllListByActivityId.do?activityId="+activityId+"&userId="+userId+"&page=0",
	            type:"post",
	            //data:JSON.stringify(comment),
	            success:function(a){
	                //alert("status--->" + a.status + "----------result--->" + a.result );
	                if(a.result=="1"){
	                    var articleList = a.articles;
	                    var articles = "";
	                    $("#ActivityContentPicId").attr("src",a.activityPic);
	                    for(var i=0;i<articleList.length;i++){
	                    	var art = articleList[i];
	                    	var pictures = "";
	                        var date = strToDate(art.date);
	                        var strDate = calculateDate(date);
	                        
	                        var picList = art.pictures;
	                        for (var j = 0; j < picList.length; j++) {
	                        	var picstr = "'"+picList[j]+"'";
	                        	 pictures += '<a href="javascript:viewPicture('+picstr+')"><img src="'+picList[j]+'" width="100px" height="100px" class="img-border radius-small" alt="..." /> </a>';
	                        }
	                        var type=art.type;
	                        var typeStr = "";
	                        
	                        if(type=="9"){
	                            typeStr = "未审核";
	                        }
	                        else if(type=="10"){
	                            typeStr = "已审核";
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
	                        articles += '<div class="media media-x radius bg-white">'
	                        	    + '<div class="float-left text-center padding-top padding-left">'
	                                + '<a href="#"> <img src="'+art.headPic+'" class="radius-circle" width="50px" height="50px" alt="..."> </a>'
	                                + '<div class="padding-top"><a href="javascript:followUser('+art.userId+')" ><span class="icon-check-square-o text-large text-green"></span></a></div>'
	                                + '</div>'
	                                + '   <div class="media-body"><div class="margin">'
	                                + '   <h4>'+ art.userName +'<small> ('+roleStr+')</small></h4>'
	                                +            art.content
	                                + '    </div><div class="margin-big-top margin-right margin-left margin-big-bottom">'
	                                +        pictures
	                                + '        </div>'
	                                + '    <div class="margin-left text-small text-gray">'
	                                + strDate +'  出处: '+art.reference
	                                + '</div></div><div class="button-group button-group-justified">'
	                                + '       <a href="javascript:getComments('+art.articleId+')" class="button ">评论 <span class="badge bg-white-light" id="commentCount'+art.articleId+'">'+art.commentCount+'</span></a>'
	                                + '        <a href="javascript:praiseArticle('+art.articleId+')" class="button ">赞<span class="badge bg-white-light" id="praiseCount'+art.articleId+'">'+art.praiseCount+'</span></a>'
	                                + '        <a href="javascript:auditing('+art.articleId+')" class="button" id="articleType'+art.articleId+'">'+typeStr+' </a>'
	                                + '        <a href="javascript:deleteArticle('+art.articleId+')" class="button ">删除 </a>'
	                                + '</div>   </div>' + '</div>' + '</div>';
	                    }
	                    
	                    if(articleList.length==10||page>0){
	                        $("#paginationDiv").css({
	                            display : "block"
	                        });
	                        var nextPage = page+1;
	                        var prePage = page-1;
	                        var ination = '<ul class="pagination pagination-group ">';
	                        if(page!=0){
	                            ination += '<li><a href="javascript:selectUser('+userId+','+prePage+')">上一页</a></li>';
	                        }else {
	                            ination += '<li><a disabled="false" href="javascript:void(0);">上一页</a></li>';
	                        }
	                        if(articleList.length==10){
	                            ination += '<li><a href="javascript:selectUser('+userId+','+nextPage+')">下一页</a></li></ul>';
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
	                    alert("发布失败");
	                }
	            },
	            dataType: "json",
	            contentType: "application/json",
	        });
		//$("#"+"activityDiv"+activityId).attr("className","media bg-mix border margin");
	}
	function publishComments(){
		//alert(${userId});
		if($("#commentContent").val()==""){
            alert("请输入文字");
            return;
        }
		$("#articleUserId").val(${userId});
		var userId = $("#commentUserId").val();
		var name = $("#commentUserId").find("option:selected").text();
		publishComment(userId,name);
		var articleId = $("#commentArticleId").val();
        var pObj = document.getElementById("commentCount"+articleId);
        var count = pObj.innerHTML;
        pObj.innerHTML = parseInt(count)+1;
		
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
	
	function auditing(articleId){
		if('${userRole}'!="1"){
            alert("没有审核权限");
            return;
        }
		$("#articleId").val(articleId);
        $("#dialogBtn").click();
	}
	function okDialog(){
		var articleId=$("#articleId").val();
		 $.ajax({
	            url : "/StarkPet/article/changeArticleType.do?articleId="+articleId+"&type="+10,
	            type : "get",
	            //data:JSON.stringify(chartlet),
	            success : function(ret) {
	                
	                if (ret.result == "1") {
	                    var pObj = document.getElementById("articleType"+articleId);
	                    pObj.innerHTML = "已审核";
	                }
	                else {
	                    alert("审核失败");
	                }
	            },
	            dataType : "json",
	            contentType : "application/json",
	        });
	}
	function createArticle(activityId) {
		$("#activityArticleListDiv").css({
			display : "none"
		});
		$("#createActivityDiv").css({
			display : "none"
		});
		$("#createArticleDiv").css({
			display : "block"
		});

        $("#commentDiv").css({
            display : "none"
        });
        $("#paginationDiv").css({
            display : "none"
        });
        $("#userGroupDiv").css({
            display : "none"
        });
		//alert(activityId);
        changeActivityColor(activityId);
        $("#articleUserId").val(${userId});
        $("#activityArticleId").val(activityId);
	}
	function praiseArticle2(articleId){
	    $.ajax({
	        url : "/StarkPet/article/praise.do?userId="+${userId}+"&articleId="+articleId,
	        type : "get",
	        //data:JSON.stringify(chartlet),
	        success : function(ret) {
	            if (ret.result == "1") {
	                //alert("ddd");
	                var pObj = document.getElementById("praiseCount"+articleId);
	                var count = pObj.innerHTML;
	                pObj.innerHTML = parseInt(count)+1;
	            }
	        },
	        dataType : "json",
	        contentType : "application/json",
	    });
	}
	
	function deleteActivity(activityId){
		 $.ajax({
	            url : "/StarkPet/activity/delete.do?activityId="+activityId,
	            type : "get",
	            //data:JSON.stringify(chartlet),
	            success : function(ret) {
	                if (ret.result == "1") {
	                    //alert("ddd");
	                	$("#activity"+activityId).remove();
	                	if(activityId==selectActivityId){
	                		createActivity();
	                	}
	                	//location.reload();
	                }
	            },
	            dataType : "json",
	            contentType : "application/json",
	        });
	}
	
	function publishArticle(){
        //if($("#articleContent").val()==""){
        //    alert("请输入内容");
        //    return;
        //}
        $("#articleFormFile").submit();
        
    }
	var type = 0;
	var activityId = 0;
	function uploadBannerPic(id){
		type=0;
		activityId=id;
		$("#activityId").val(id);
		$("#activityType").val("banner");
		$("#uploadFileId").click();
	}
	
	function uploadContentPic(){
		type=1;
		activityId=selectActivityId;
		$("#activityId").val(selectActivityId);
        $("#activityType").val("content");
        $("#uploadFileId").click();
	}
	
	var flagCount = 0;
	function addFile(file) {
		if (file.files && file.files[0]) {
			var img ;
			if(type==0){
				img =  $("#activityBannerPic"+activityId)[0];
			}
			else {
				img = $("#ActivityContentPicId")[0];
			}
			img.onload = function() {
	            var w = img.width;
	            var h = img.height;
	            var fileSize = file.files[0].size;
	            if(flagCount==0){
	            	file.name+="?"+img.naturalWidth+"&"+img.naturalHeight;
	            	document.getElementById("formFileUpload").submit();
	            }
	            else {
	            	flagCount=0;
	            }
	            flagCount++;
	            
	        }
	        var reader = new FileReader();
	        reader.onload = function(evt) {
	            //var quality =  80;
	            img.src = evt.target.result;
	            //img.src = jic.compress(img,quality).src
	            //
	        }
	        reader.readAsDataURL(file.files[0]);
	        
		}
		
		//document.forms[0].submit();
    }
	function callbackUpload(str){
		
        //$("#activityBannerPic"+activityId).attr("src",str);
        $("#formFileUpload").reset();
    }
</script>

</head>
<body>
	<div style="height:100%">
		<ul id="header">
			<li style="width: 70px"><a href="toMain.do">首页</a></li>
			<li><a href="operatorManage.do">账号管理</a></li>
			<li><a href="articleManage.do">推文管理</a></li>
			<li><a href="activityManage.do">活动管理</a></li>
			<li><a href="publishManage.do">发布管理</a></li>
			<li><a href="systemManage.do">系统管理</a></li>
		</ul>

		<div  style="width: 33%; height: 93%; float: left;">
		<form id='formFileUpload' name='formFile' method="post" action="../activity/uploadPicture.do" target='frameFile' enctype="multipart/form-data">
             <input size="100" type="file" name="file" id="uploadFileId" style="display:none" onchange="addFile(this)"/>
             <input type="hidden" name="activityType"  id="activityType">
             <input type="hidden" name="activityId" id="activityId">
        </form>
			<div id="activityDiv" style="width: 100%;height:100%;">
				<div class="bg padding-large" style="height:100%">
					<button class="button bg-blue radius-rounded " onclick="createActivity()">新建活动</button>
					<div style="height:98%; overflow: auto">
						<c:if test="${!empty activitys }">
							<c:forEach items="${activitys }" var="o">
								<c:if test="${o.type==0 }">
									<div class="media bg-green-light border margin" name="activityListDiv" title="0" id="activity${o.activityId }">
								</c:if>
								<c:if test="${o.type==1 }">
									<div class="media bg-red-light border margin" name="activityListDiv" title="1" id="activity${o.activityId }">
								</c:if>
										<div class="float-left margin-right">
											<a href="javascript:selectActivity(${o.activityId})"> <img src="${o.getBannerPicUrl() }" id="activityBannerPic${o.activityId }" width="300px" height="200px" class="radius" alt="...">
											</a>
										</div>
										<div class="media-body margin-large" style="height: 80px">
											<a href="javascript:uploadBannerPic(${o.activityId })"><strong>${o.subject }</strong></a>
		                                    <p class="margin-left text-small text-gray">下线时间：${o.getOffDateString().substring(0,10) }</p>
		                                    <c:if test="${o.type==0 }"> <p class="margin-left text-small text-gray">类型：Banner</p></c:if>
		                                   <c:if test="${o.type==1 }"> <p class="margin-left text-small text-gray">类型：置顶推荐</p></c:if>
										</div>
										<div class="container-layout  bg-inverse padding-big-top " style="height: 60px">
											<button class="button bg-blue-light" style="position: relative; bottom: 0px;" onclick="createArticle(${o.activityId})">新建推文</button>
											<button class="button bg-blue-light" style="position: relative; bottom: 0px;" onclick="deleteActivity(${o.activityId})">删除活动</button>
										</div>
					               </div>
					       </c:forEach>
					   </c:if>
				    </div>
			     </div>
		      </div>
		  </div>
        <div id="secondDiv" class="padding-large" style="width: 42%; height: 93%; overFlow-y: scroll;float:left; border-right: 1px #aaa solid; border-left: 1px #aaa solid;">
        <div class="margin-large border" style="display: none" id="activityArticleListDiv">
            <div class="media media-y bg-blue-light border margin">
                <a href="javascript:uploadContentPic()"><img src="" width="300px" height="200px" class="radius" alt="..." id="ActivityContentPicId"></a>
            </div>
            <div id="articleListDiv" class="padding bg-mix radius"></div>
        </div>
        <div id="paginationDiv" class="button-group button-group-justified bg-mix" style="display: none"></div>
        <div class="margin-large" id="createActivityDiv">
            <!--    <input type="file" id="upload"> <img id="pic" style="display: none"> <br>-->
            <form id='activityform' name='formFile' method="post" class="form form-tips" action="../activity/addActivity.do" target='frameFile' enctype="multipart/form-data">
                <div class="label">
                    <label for="subject">标题</label>
                </div>
                <div class="field margin-large-bottom">
                    <input type="text" class="input" id="subject" name="subject" size="30" />
                    <div class="input-note"></div>
                </div>
                <div class="label">
                    <label for="offLineDate">下线时间</label>
                </div>
                <div class="field margin-large-bottom">
                    <input type="text" class="input auto-kal" id="offLineDate" name="offlineDate" size="30" data-kal="format:'YYYY-MM-DD'" data-validate="required:必填" />
                </div>
                <div class="label">
                    <label for="activityType">类型</label>
                </div>
                <div class="field margin-large-bottom">
                    <select id="activityType" name="activityType" class="input">
                        <c:if test="${!empty types }">
                            <c:forEach items="${types }" var="o">

                                <option value="${o.index}">${o.name}</option>

                            </c:forEach>
                        </c:if>
                    </select>
                </div>
                <div class="float-left " style="width: 50%;">

                    <div class="label">
                        <label for="subject">上传banner图</label>
                    </div>
                    <div class="field margin-large-bottom" style="width: 100%;">

                        <div class="field ">
                            <a class="button input-file bg-blue" href="javascript:void(0);">+ 浏览图片
                             <input size="100" type="file" name="bnner" id="bnnerPictureInput" onchange="previewImage(this,1)" data-validate="regexp#.+.(jpg|jpeg|png|gif)$:只能上传jpg|gif|png格式文件" />
                            </a>
                        </div>
                        <div id="preview1" class="margin-bottom padding-big-top"></div>
                    </div>
                </div>
                <div class="float-left " style="width: 50%;">
                    <div class="label">
                        <label for="subject">上传活动图</label>
                    </div>
                    <div class="field margin-large-bottom" style="width: 100%;">
                        <div class="field ">
                            <a class="button input-file bg-blue" href="javascript:void(0);">+ 浏览图片
                             <input size="100" type="file" name="content" id="contentPictureInput" onchange="previewImage(this,2)" data-validate="regexp#.+.(jpg|jpeg|png|gif)$:只能上传jpg|gif|png格式文件" />
                            </a>
                        </div>
                        <div id="preview2" class="margin-bottom padding-big-top"></div>
                    </div>
                </div>

                <div style="width: 100%; clear: both">
                    <div class="form-button text-right">

                        <button class="button bg-green" type="submit">提交</button>
                    </div>
                </div>
            </form>
        </div>
        <div class="margin-large" style="display: none" id="createArticleDiv">
            <form id='articleFormFile' name='formFile' method="post" action="../article/addArticle.do" target='frameFile' enctype="multipart/form-data">
                <input type="hidden" id="articleUserId"> <input type="hidden" id="activityArticleId" name="activityId">
                <div class="form-group">
                <div  class="label">
                       <label for="userId">选择用户</label>
                    </div>
                    <div class="field margin-left  margin-bottom">
                    <select  class="input"  name="userId">
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
                    <label for="readme">发布内容</label>
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
                <div style="height: 50px" class="padding-big-top margin-left">
                    <div class="label float-left">
                        <a class="button input-file bg-green " style="width: 50px" href="javascript:void(0);" id="addArticlePicture0"> 
                          <span class="icon-image text-big"></span> 
                          <input type="file" name="picture1" onchange="previewArticleImage(this)" id="articlePictureInput1"/>
                       </a> 
                       <a class="button input-file bg-green" style="display: none; width: 50px" href="javascript:void(0);" id="addArticlePicture1"> 
                           <span class="icon-image text-big"></span> 
                           <input type="file" name="picture2" onchange="previewArticleImage(this)" id="articlePictureInput2"/>
                       </a> 
                       <a class="button input-file bg-green" style="display: none; width: 50px" href="javascript:void(0);" id="addArticlePicture2">
                           <span class="icon-image text-big"></span> 
                           <input type="file" name="picture3" onchange="previewArticleImage(this)" id="articlePictureInput3"/>
                       </a> 
                       <a class="button input-file bg-green" style="display: none; width: 50px" href="javascript:void(0);" id="addArticlePicture3"> 
                           <span class="icon-image text-big"></span>
                           <input type="file" name="picture4" onchange="previewArticleImage(this)" id="articlePictureInput4"/>
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
                           <span class="icon-image text-big"></span> 
                           <input type="file" onchange="previewArticleImage(this)" />
                        </a>
                    </div>
                    <div class="field float-left ">

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
                        <button class="button bg-green" type="button" onclick="publishArticle()" >发布</button>
                    </div>
                </div>
            </form>

        </div>
    </div>
    <div id="thirdDiv" class="padding-large" style="width: 25%; height: 93%; overflow: auto; float: left; border-right: 1px #aaa solid;">
        <div id="commentDiv" style="display: none">
            <div id="createCommentDiv">
                <input type="hidden" id="commentArticleId">
                <div  class="label">
                       <label for="userId">选择用户</label>
                    </div>
                    <div class="field margin-left  margin-bottom">
                    <select  class="input" id="commentUserId" >
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
            <div id="commentListDiv" class="bg-back margin-top padding radius"></div>
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
	
	
	<button class="button button-big bg-main dialogs" id="dialogBtn" style="display: none" data-toggle="click" data-target="#mydialog" data-mask="1" data-width="40%"></button>
	<div id="mydialog">
		<div class="dialog">
			<div class="dialog-head">
				<span class="dialog-close close rotate-hover"></span> <strong>设置推文</strong>
			</div>
			<div class="dialog-body">
			<input type="hidden" id="articleId">
				推文审核通过
			</div>
			<div class="dialog-foot">
				<button class="dialog-close button">取消</button>
				<button class="dialog-close button bg-green" onclick="okDialog()">确认</button>
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