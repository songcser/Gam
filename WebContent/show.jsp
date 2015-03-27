<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>友哈-节目单管理</title>
<link rel="stylesheet" href="../css/bootstrap.css">
<style type="text/css">
body {
	margin-top: 50px;
	height: 100%;
}
</style>
</head>
<body class="bg-blue-light ">
	<%@ include file="header.jsp"%>
	<div id="mainBody" class="container-fluid padding-top">
		<div class="row" style="height: 100%">
			<div class="col-lg-5" style="height: 100%;">
				<button class="btn bg-blue radius-rounded " onclick="createShow()">新建节目单</button>
				<%@ include file="showList.jsp" %>
			</div>
			<div class="col-lg-7 border-left"  style="height: 100%;">
				<div id="createShowId" style="width: 85%;  overflow: auto;"  class="center-block ">
					<%@ include file="createShow.jsp"%>
				</div>
				<div style="width: 90%; height: 100%; overflow: auto;display:none;" id="articleList" class="center-block">
				    <div class="radius row bg border" style="width:99%">
				        <div class="col-md-offset-2 col-lg-8">
				        <a href="javascript:uploadContentPic()" style="height:300px;"  class="thumbnail "><img style="height:100%;width:100%" id="ActivityContentPicId" alt="" src=""></a>
				        </div>
				    </div>
					<div id="articleListDiv" class="padding-top"></div>
					<div id="paginationDiv"></div>
				</div>
			</div>
		</div>
	</div>
	<form id='formFileUpload' name='formFile' method="post" action="../activity/uploadPicture.do" target='frameFile' enctype="multipart/form-data">
        <input size="100" type="file" name="file" id="uploadFileId" style="display:none" onchange="addFile(this)"/>
        <input type="hidden" name="activityType"  id="activityFormType">
        <input type="hidden" name="activityId" id="activityId">
   </form>
   <%@ include file="commentDialog.jsp" %>
   <%@ include file="selectUserList.jsp"%>
   <%@ include file="browseDialog.jsp"%>
   <%@ include file="auditingDialog.jsp"%>
   <%@ include file="sliderShow.jsp"%>
	<script src="../js/jquery-1.11.2.min.js"></script>
	<script src="../js/bootstrap.min.js"></script>
    <script src="../js/main.js"></script>
	<script type="text/javascript">
		var isAuditing = false;
		var total = document.documentElement.clientHeight - 70;
		document.getElementById("mainBody").style.height = total + "px";
		function callback(str) {
			if (str == '1') {
				alert('发布成功');
				location.reload();
			} else if (str == '2') {
				alert('图片不可用 发布失败');
			} else {
				alert('发布失败');
			}

		}
		function showArticles(activityId) {
			selectActivityId = activityId;
			$("#createShowId").css({
                display : "none"
            });
			
			$("#articleList").css({
				display : "block"
			});
			var url = "/StarkPet/article/getShowArticleList2.0.do?showId=" + activityId + "&userId=0&";
			isShowHeader = true;
			showArticleList(url, 0);
			
		}
		
		function createShow(){
			$("#createShowId").css({
                display : "block"
            });
			$("#articleList").css({
                display : "none"
            });
		}
		var type = 0;
	    var selectActivityId = 0;
	    function uploadBannerPic(id){
	        type=0;
	        selectActivityId=id;
	        $("#activityId").val(id);
	        $("#activityFormType").val("banner");
	        $("#uploadFileId").click();
	    }
	    
	    function uploadContentPic(){
	        type=1;
	        //activityId=selectActivityId;
	        $("#activityId").val(selectActivityId);
	        $("#activityFormType").val("content");
	        $("#uploadFileId").click();
	    }
	    
	    var flagCount = 0;
	    function addFile(file) {
	        if (file.files && file.files[0]) {
	            var img ;
	            if(type==0){
	                img =  $("#activityBannerPic"+selectActivityId)[0];
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
	                img.src = evt.target.result;
	            }
	            reader.readAsDataURL(file.files[0]);
	            
	        }
	    }
	    function callbackUpload(str){
	        $("#formFileUpload").reset();
	    }
	    
	    function orderSort(obj,activityId){
	    	selectActivityId=activityId;
	    	$(obj).attr('type','text');
	    	
	    	var old = $(obj).val();
	    	$(obj).val("");
	    	$(obj).blur(function(){
	    		$(obj).attr('type','button');
	    		var value = parseInt($(obj).val(),10)
	    		if(!isNaN(value)){
	    			$(obj).val("序号:"+value);
	    			var url = "/StarkPet/activity/setActivityOrder.do?activityId="+activityId+"&order="+value;
	    			ajaxRequest(url,orderResponse);
	    		}
	    		else{
	    			alert("请输入数字");
	    			$(obj).val(old);
	    		}
	    		$(obj).unbind( "blur" );
	    	});
	    }
	    function orderResponse(){
	    	
	    }
	    
	    function getNoAutitingShowArticles(activityId){
	    	selectActivityId = activityId;
            $("#createShowId").css({
                display : "none"
            });
            
            $("#articleList").css({
                display : "block"
            });
	    	var url = "/StarkPet/article/getNoAutitingShowArticles.do?showId=" + activityId + "&userId=0&";
            isShowHeader = true;
            showArticleList(url, 0);
	    }
	    function selectBack() {
            selectUserSubmit();
        }
	    
	    function auditingBack(type){
	    	var articleId = currentArticle.Id;
	    	if(type==9){
	    		
	            var url = "/StarkPet/article/changeArticleType.do?articleId="+ articleId + "&type=" + 10;
	            ajaxRequest(url, response);
	    	}
	    	else if(type==10){
	    		isAuditing = true;
	    		 var url = "/StarkPet/article/changeArticleType.do?articleId="+ articleId + "&type=" + 14;
	             ajaxRequest(url, response);
	    	}
	    }
	    function response(data){
	    	if (data.result == 1) {
                if(isAuditing){
                    $("#articleType"+currentArticle.Id).html(data.type);
                }
                else{
                    $("#mediaMainId" + currentArticle.Id).remove();
                }
            } else {
                alert("失败了!!!");
            }
	    }
	    function deleteActivity(activityId){
	    	selectActivityId = activityId;
	    	var url ="/StarkPet/activity/delete.do?activityId="+activityId;
	    	ajaxRequest(url, deleteResponse);
	    }
	    function deleteResponse(ret){
	    	if (ret.result == "1") {
                $("#activity"+selectActivityId).remove();
                createShow();
            }
	    }
	</script>
</body>
</html>