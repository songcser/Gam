<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="radius " style="height:100%;overflow:auto">
<table id="userTable" class="table text-center table-hover bg-white " style="word-wrap:break-word;table-layout:fixed">
	<tbody>
		<tr class=" text-center success">
			<td style="width:8%" nowrap><strong>编号</strong></td>
			<td style="width:10%" nowrap><strong>头像</strong></td>
			<td style="width:27%" nowrap><strong>姓名</strong></td>
			<td style="width:12%" nowrap><strong>角色</strong></td>
			<td style="width:40%" nowrap><strong>编辑</strong></td>
		</tr>
		<c:if test="${!empty userList }">
			<c:forEach items="${userList }" var="u">
				<tr>
					<td style="text-align: center; vertical-align: middle;">${u.userId }</td>
					<td style="text-align: center; vertical-align: middle;"><a href="javascript:selectUser('${u.userId }','${u.name }','${u.role }')"> <img src="${u.getHeadUrl()}"
							style="height: 50px; width: 50px" class="img-circle " />
					</a></td>
					<td style="text-align: center; vertical-align: middle;word-break:keep-all; white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">${u.name }</td>
					<c:if test="${u.role==2 }">
						<td style="text-align: center; vertical-align: middle;">运营</td>
					</c:if>
					<c:if test="${u.role==3 }">
						<td style="text-align: center; vertical-align: middle;">模拟用户</td>
					</c:if>
					<c:if test="${u.role==6 }">
						<td style="text-align: center; vertical-align: middle;">重要用户</td>
					</c:if>
					<c:if test="${u.role==0 }">
                        <td style="text-align: center; vertical-align: middle;">普通用户</td>
                    </c:if>
                    <c:if test="${u.role==1 }">
                        <td style="text-align: center; vertical-align: middle;">管理员</td>
                    </c:if>
                    <c:if test="${u.role==7 }">
                        <td style="text-align: center; vertical-align: middle;">标记用户</td>
                    </c:if>
                     <c:if test="${u.role==8 }">
                        <td style="text-align: center; vertical-align: middle;">机构用户</td>
                    </c:if>
					<td style=" text-align: center; vertical-align: middle;"><a class="btn btn-default" href="javascript:edit('${u.userId}');"><span
							class="glyphicon glyphicon-edit"></span></a> <a class="btn btn-default" href="javascript:del('${u.userId}')"><span class="glyphicon glyphicon-trash"></span></a> <c:if
							test="${u.noticeStatus==1 }">
							<a id="noticeStatus${u.userId }" class="btn btn-default bg-dot" href="javascript:viewNotice('${u.userId}')"> <span class="glyphicon glyphicon-volume-up "></span>
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