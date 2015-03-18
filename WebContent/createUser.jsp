<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="panel panel-default">
	<div class="panel-body">
		<form id='formFile' name='formFile' method="post" action="../user/addOperator.do" target='frameFile' enctype="multipart/form-data">
			<input type="hidden" name="userId" id="userId">
			<div class="form-group">
				<label for="name">姓名</label> <input type="text" class="form-control" id="name" name="name" size="50" />
			</div>
			<div class="form-group">
				<label for="sex">性别</label><br />
				<div class="btn-group" data-toggle="buttons">
					<label class="btn btn-success active"><input name="sex" autocomplete="off" value="0" checked="checked" type="radio" id="male"><span
						class="icon icon-male text-green"></span>先生</label> <label class="btn btn-success"><input name="sex" autocomplete="off" value="1" type="radio"><span
						class="icon icon-female text-red" id="female"></span> 女士</label>
				</div>
			</div>
			<div class="form-group">
				<label for="password">密码</label>
				<div class="input-group">
					<input type="text" class="form-control" id="password" name="password" size="50" /> <span class="input-group-btn"> <input class="btn" type="button" value="查看"
						id="viewPwdBtn" onclick="viewPassword()" />
					</span>

				</div>
			</div>
			<div class="form-group">
				<label for="homeTown">所在地</label> <input type="text" class="form-control" id="homeTown" name="homeTown" size="50" />
			</div>

			<div class="form-group">
				<label for="role">角色</label>
				<div class="field">
					<select id="role" name="role" class="form-control">
						<c:if test="${!empty roles }">
							<c:if test="${userRole=='1'}">
								<c:forEach items="${roles }" var="o">
									<c:if test="${o.index=='3'||o.index=='2'||o.index=='6' }">
										<option value="${o.index}">${o.name}</option>
									</c:if>
								</c:forEach>
							</c:if>
							<c:if test="${userRole=='2'}">
								<c:forEach items="${roles }" var="o">
									<c:if test="${o.index=='3'||o.index=='6' }">
										<option value="${o.index}">${o.name}</option>
									</c:if>
								</c:forEach>
							</c:if>
						</c:if>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label for="headPic">头像</label><br /> <a class="btn" href="javascript:void(0);"><input type="file" name="headPic"></a>
			</div>
			<div class=" pull-right">
				<input class="btn btn-success" type="button" value="提交" onclick="addUser()" id="add">
			</div>
			<div class=" pull-right">
				<input type="button" class="btn btn-success" value="更新" onclick="updateUser()" id="update" style="display: none;">
			</div>
		</form>
	</div>
</div>