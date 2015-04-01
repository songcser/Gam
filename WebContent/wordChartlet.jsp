<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="block-inline">
	<input type="text" class="input radius-rounded" id="wordChartletTitle" />
	<button class="btn bg-sub " onclick="createWordChartlet()">创建套图</button>
</div>
<div id="wordChartletList">
	<c:forEach items="${chartlets }" var="o">
		<c:if test="${o.type=='0'}">
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
								<td class="border-right" width="200px" height="270px" id="picObj${pic.id }">
									<div style="height: 200px; width: 200px">
										<a href="javascript:removeChartletPicture(${o.chartletId },${pic.id })" style="position: relative; left: 195px; top: 0px"> <span class="icon-times-circle"></span></a> <a><img
											src="${pic.getPicUrl(o.chartletId) }" width="200px" height="200px"></a>
										<div class="button-group checkbox padding-top" style="width: 200px; text-align: center;">
											<c:if test="${pic.status==0 }">
												<label class="button "><input name="pintuer" type="checkbox" onclick="changeChartletStatus(this,'${pic.id}')"><span class="icon icon-check"></span> 最新</label>
											</c:if>
											<c:if test="${pic.status==1 }">
												<label class="button active"><input name="pintuer" type="checkbox" checked onclick="changeChartletStatus(this,'${pic.id}')"><span
													class="icon icon-check"></span> 最新</label>
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
<script type="text/javascript">
	function createWordChartlet() {
		var type = 0;
		chartletType = "wordChartletList";
		var title = $("#wordChartletTitle").val();
		if (title == "") {
			alert("请输入标签")
			return;
		}
		var chartlet = {
			type : type,
			title : title
		}
		var url = "/StarkPet/article/createChartlet.do";
		jsonAjax(url, chartlet, chartletBack)
	}
	
</script>