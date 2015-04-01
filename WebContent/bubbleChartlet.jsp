<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="block-inline">
	<input type="text" class="input radius-rounded" id="bubbleChartletTitle" />
	<button class="btn bg-sub " onclick="createBubbleChartlet()">创建套图</button>
</div>
<div id="bubbleChartletList">
	<c:forEach items="${chartlets }" var="o">
		<c:if test="${o.type=='2'}">
			<div class=" border margin-top" id="chartletPictureDiv${o.chartletId }">
				<div class="text-large  padding-large-left  padding-top">
					<div class="text-large text-yellow inline">${o.title}</div>
					<a class="btn bg-sub margin-large-left" href="javascript:addChartletPicture(${o.chartletId })">添加图片</a> 
					<a class="btn input-file bg-sub margin-large-left" href="javascript:deleteChartlet(${o.chartletId });"> 删除套图 </a>
				</div>
				<div class=" padding" style="overflow: auto;">
					<table class="table table-hover ">
						<tr id="piclist${o.chartletId }">
							<c:forEach items="${o.picList }" var="pic">
								<td class="" width="200px"  id="picObj${pic.id }">
									<div style="height: 250px; width: 200px">
										<a href="javascript:removeChartletPicture(${o.chartletId },${pic.id })" style="position: relative; left: 193px; top: 7px"> 
										<span class="glyphicon glyphicon-remove"></span></a> 
										<a class="thumbnail"><img src="${pic.getPicUrl(o.chartletId) }" width="200px" height="200px"></a>
										<div class="button-group checkbox " style="width: 200px; text-align: center;">
											<div class="input-group ">
												<input type="text" class="form-control-small" placeholder="X" style="width:50px" value="${pic.coordinateX }" onclick="getFocus(this)" onblur="inputblur(this)"
												 onkeyup="setBubbleSize(${pic.id},this,'coordinateX')" data-toggle="popover" data-content="坐标:X" data-placement="top" data-container="body">
												<input type="text" class="form-control-small" placeholder="Y" style="width:50px" value="${pic.coordinateY }" onclick="getFocus(this)" onblur="inputblur(this)"
												onkeyup="setBubbleSize(${pic.id},this,'coordinateY')" data-toggle="popover" data-content="坐标:Y" data-placement="top" data-container="body">
												<input type="text" class="form-control-small" placeholder="W" style="width:50px" value="${pic.width }" onclick="getFocus(this)" onblur="inputblur(this)"
												onkeyup="setBubbleSize(${pic.id},this,'width')" data-toggle="popover" data-content="宽度:W" data-placement="top" data-container="body">
												<input type="text" class="form-control-small" placeholder="H" style="width:50px" value="${pic.height }" onclick="getFocus(this)" onblur="inputblur(this)"
												onkeyup="setBubbleSize(${pic.id},this,'height')" data-toggle="popover" data-content="高度:H" data-placement="top" data-container="body">
											</div>
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
<script type="text/javascript">
	function createBubbleChartlet() {
		var type = 2;
		chartletType = "bubbleChartletList";
		var title = $("#bubbleChartletTitle").val();
		if (title == "") {
			alert("请输入标签")
			return;
		}
		var chartlet = {
			type : type,
			title : title
		}
		var url = "/StarkPet/article/createChartlet.do";
		jsonAjax(url, chartlet, bubbleBack)
	}
	var selectObj = null;
	function setBubbleSize(picId,obj,flag){
		selectObj = obj;
		var value = parseInt($(obj).val(),10);
		if(isNaN(value)){
			alert("请输入正确数字");
			return;
		}
		var url = "/StarkPet/article/setBubbleCoordinate.do?bubbleId="+picId+"&flag="+flag+"&value="+value;
		ajaxRequest(url,bubbleResponse);
	}
	function bubbleResponse(ret){
		if(ret.result==0){
			$(selectObj).val("0");
		}
	}
	function getFocus(obj){
		$(obj).popover('show')
	}
	function inputblur(obj){
		$(obj).popover('destroy');
	}
</script>