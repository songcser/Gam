<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="showList" class="margin-top" style="height: 95%; overflow: auto">
	<c:if test="${!empty showList }">
		<c:forEach items="${showList }" var="o">
			<div id="activity${o.activityId }" class="border bg-red-light radius media padding-top" style="overflow: auto">
				<div class="media-left padding-small">
					<a style="width: 400px; height: 250px" href="javascript:showArticles(${o.activityId },${o.type })" class="thumbnail"> <img class="media-object" style="width: 100%; height: 100%"
						src="${o.getBannerPicUrl() }" alt="" id="activityBannerPic${o.activityId }">
					</a>
				</div>
				<div class="media-body padding-top">
					<button style="width: 170px; height: 30px; margin-left: -15px; display: block" class=" bg-green radius-rounded margin-bottom ">标题: ${o.subject }</button>
					<c:if test="${o.type==3 }">
						<button style="width: 140px; height: 30px; margin-left: -15px; display: block" class="bg-green radius-rounded margin-bottom ">是否: 可参加</button>
					</c:if>
					<c:if test="${o.type==2 }">
						<button style="width: 140px; height: 30px; margin-left: -15px; display: block" class="bg-green radius-rounded margin-bottom ">是否:不可参加</button>
					</c:if>
					<input value="序号: ${o.order }" type="button" style="width: 110px; height: 30px; margin-left: -15px; display: block" class="bg-green radius-rounded margin-bottom"
						onclick="orderSort(this,${o.activityId })">
					<button style="width: 80px; height: 30px; margin-left: -15px; display: block" class="bg-green radius-rounded margin-bottom" onclick="uploadBannerPic(${o.activityId })">
						<span class="padding-small-left glyphicon glyphicon-picture"></span>
					</button>
					<button style="width: 60px; height: 30px; margin-left: -15px; display: block" class="bg-green radius-rounded margin-bottom"
						onclick="getNoAutitingShowArticles(${o.activityId})">
						<span class="padding-small-left glyphicon glyphicon-eye-close"></span>
					</button>
					<button style="width: 50px; height: 30px; margin-left: -15px; display: block" class="bg-green radius-rounded margin-bottom" onclick="deleteActivity(${o.activityId})">
						<span class="padding-small-left glyphicon glyphicon-trash"></span>
					</button>
				</div>
			</div>
		</c:forEach>
	</c:if>
</div>