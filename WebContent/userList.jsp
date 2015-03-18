<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="radius" style="height:100%;overflow:auto">
<table id="userTable" class="table text-center table-hover bg-white ">
	<tbody>
		<tr class="blue text-center">
			<td><strong>编号</strong></td>
			<td><strong>头像</strong></td>
			<td><strong>姓名</strong></td>
			<td><strong>角色</strong></td>
			<td><strong>编辑</strong></td>
		</tr>
		<c:if test="${!empty operations }">
			<c:forEach items="${operations }" var="u">
				<tr>
					<td style="text-align: center; vertical-align: middle;width:10%">${u.userId }</td>
					<td style="text-align: center; vertical-align: middle;width:15%"><a href="javascript:selectUser('${u.userId }','${u.name }','0')"> <img src="${u.getHeadUrl()}"
							style="height: 50px; width: 50px" class="img-circle " />
					</a></td>
					<td style="text-align: center; vertical-align: middle;width:20%">${u.name }</td>
					<c:if test="${u.role==2 }">
						<td style="text-align: center; vertical-align: middle;width:15%">运营</td>
					</c:if>
					<c:if test="${u.role==3 }">
						<td style="text-align: center; vertical-align: middle;width:15%">模拟用户</td>
					</c:if>
					<c:if test="${u.role==6 }">
						<td style="text-align: center; vertical-align: middle;width:15%">重要用户</td>
					</c:if>
					<td style="width: 40%; text-align: center; vertical-align: middle;"><a class="btn btn-default" href="javascript:edit('${u.userId}');"><span
							class="glyphicon glyphicon-edit"></span></a> <a class="btn btn-default" href="javascript:del('${u.userId}')"><span class="glyphicon glyphicon-trash"></span></a> <c:if
							test="${u.noticeStatus==1 }">
							<a id="noticeStatus${u.userId }" class="btn btn-default btn-warning" href="javascript:viewNotice('${u.userId}')"> <span class="glyphicon glyphicon-volume-up "></span>
							</a>
						</c:if> <c:if test="${u.noticeStatus==0 }">
							<a id="noticeStatus${u.userId }" class="btn btn-default" href="javascript:viewNotice('${u.userId}')"> <span class="glyphicon glyphicon-volume-up"></span>
							</a>
						</c:if></td>
				</tr>

			</c:forEach>
		</c:if>
	</tbody>
</table>
</div>