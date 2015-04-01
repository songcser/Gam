<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>友哈-贴图管理</title>
<link rel="stylesheet" href="../css/bootstrap.css">
<style type="text/css">
body {
	margin-top: 60px;
}
</style>
</head>
<body class="bg-blue-light">
	<%@ include file="header.jsp"%>
	<div class="container-fluid">
		<div id="chartletDiv" class="panel-group">
			<div class="panel panel-default">
				<div class="panel-heading" role="tab" id="headingOne">
					<button type="button" class="btn btn-primary" data-toggle="collapse" data-parent="#chartletDiv" data-target="#wordChartlet" aria-expanded="false" aria-controls="wordChartlet"
						id="wordChartletBtn">字帖</button>
					<button type="button" class="btn btn-primary" data-toggle="collapse" data-parent="#chartletDiv" data-target="#pictureChartlet" aria-expanded="false"
						aria-controls="pictureChartlet" id="pictureChartletBtn">图贴</button>
					<button type="button" class="btn btn-primary" data-toggle="collapse" data-parent="#chartletDiv" data-target="#dialogChartlet" aria-expanded="false"
						aria-controls="dialogChartlet" id="dialogChartletBtn">气泡框</button>
				</div>
				<div id="wordChartlet" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
					<div class="panel-body">
						<%@include file="wordChartlet.jsp"%>
					</div>
				</div>
				<form id='chartletPictureForm' name='formFile' method="post" action="../article/addChartletPicture.do" target='frameFile' enctype="multipart/form-data">
                    <input type="hidden" id="addChartletId" name="chartletId">
                    <input type="file" id="addChartletPictureFile" name="file" onchange="addFile()" style="display: none" />
                </form>
				<div id="pictureChartlet" class="panel-collapse collapse " role="tabpanel" aria-labelledby="headingTwo">
					<div class="panel-body">
							<%@include file="pictureChartlet.jsp"%>
					</div>
				</div>
				<div id="dialogChartlet" class="panel-collapse collapse " role="tabpanel" aria-labelledby="headingThree">
					<div class="panel-body">
						<%@include file="bubbleChartlet.jsp"%>
					</div>
				</div>
			</div>
		</div>
	</div>
	<iframe id='frameFile' name='frameFile' style='display: none;'></iframe>
	<script src="../js/jquery-1.11.2.min.js"></script>
	<script src="../js/bootstrap.min.js"></script>
	<script src="../js/main.js"></script>
	<script type="text/javascript">
		//var total = document.documentElement.clientHeight-80;
		//document.getElementById("").style.height=total+"px";
		var chartletType = "";
		var chartletId = 0;
		function chartletBack(ret) {
			if (ret.result == "1") {
				//alert("创建成功");
				var chartletId = ret.chartletId;
				var title = ret.title;
				var divStr = '<div class="margin-top border" >'
						+ '<form id="chartletPicture'+chartletId+'" name="formFile" method="post" action="../article/addChartletPicture.do" target="frameFile" enctype="multipart/form-data">'
						+ '<div class="text-large text-yellow padding-large-left text-justify padding-top">'
						+ '<input type="hidden" value="'+chartletId+'" name="chartletId">'
						+ '<div class="text-large text-yellow inline">'+ title + '</div>'
						+ ' <a class="btn input-file bg-sub margin-large-left" href="javascript:void(0);"> '
						+ ' 添加图片</a><a class="btn bg-sub margin-large-left" href="javascript:deleteChartlet(' + chartletId + ');"> 删除套图 </a>'
						+ ' </div></form>'
						+ '<div  class="media-inline padding" style="overflow:auto;">'
						+ '  <table class="table table-hover " width="100%">'
						+ '     <tr id="piclist'+chartletId+'"> <td></td></tr>'
						+ '   </table> </div></div>';
				$("#" + chartletType).prepend(divStr);
			}
		}
		function addChartletPicture(cid){
			chartletId = cid;
	        $("#addChartletPictureFile").click();
	    }
		function addFile(){
			$("#addChartletId").val(chartletId);
	        $('#chartletPictureForm').submit();
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
	        $('#chartletPictureForm').reset();
	    }
		function deleteChartlet(cid){
			chartletId = cid;
			var url = "/StarkPet/article/deleteChartlet.do?chartletId="+chartletId;
			ajaxRequest(url,deleteBack);
		}
		function deleteBack(ret){
			 if (ret.result == "1") {
                 $("#chartletPictureDiv"+chartletId).remove();
             }
             else {
                 alert("删除失败");
             }
		}
		function changeChartletStatus(checkboxObj,picId){
	        if(checkboxObj.checked){
	            status=1;
	        }else{
	            status=0;
	        }
	        var url = "/StarkPet/article/changeChartletPictureStatus.do?pictureId="+picId+"&status="+status;
	        ajaxRequest(url,changeResponse);
	    }
		function changeResponse(){
			
		}
		var trIdGlobal = '';
		var picIdGlobal = '';
		function removeChartletPicture(trId,picId){
			trIdGlobal = trId;
			picIdGlobal = picId;
			var url = "/StarkPet/article/removeChartletPicture.do?pictureId="+picId;
			ajaxRequest(url,removeResponse);
	    }
		function removeResponse(ret){
			 if (ret.result == "1") {
                 var trObj = document.getElementById("piclist"+trIdGlobal);
                 var tdObj = document.getElementById("picObj"+picIdGlobal);
                 trObj.removeChild(tdObj);
             }
		}
	</script>
</body>
</html>