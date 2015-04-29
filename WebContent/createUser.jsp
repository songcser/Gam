<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="panel panel-default">
	<div class="panel-body">
		<form id='formFile' name='formFile' method="post" action="../user/addOperator.do" target='frameFile' enctype="multipart/form-data">
			<input type="hidden" name="userId" id="userId" value="${user.userId }">
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
                <label for="email">邮箱</label> <input type="email" class="form-control" id="email" name="email" size="50" />
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
							<c:if test="${user.role=='1'}">
								<c:forEach items="${roles }" var="o">
									<c:if test="${o.index=='3'||o.index=='2'||o.index=='6'||o.index=='8' }">
										<option value="${o.index}">${o.name}</option>
									</c:if>
								</c:forEach>
							</c:if>
							<c:if test="${user.role=='2'}">
								<c:forEach items="${roles }" var="o">
									<c:if test="${o.index=='3'||o.index=='6' }">
										<option value="${o.index}">${o.name}</option>
									</c:if>
								</c:forEach>
							</c:if>
							<c:if test="${user.role=='0' }">
							     <c:forEach items="${roles }" var="o">
                                    <c:if test="${o.index=='0'}">
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
		<iframe id='frameFile' name='frameFile' style='display: none;'></iframe>
		<script type="text/javascript">
		var pwd = '';
	    var editUserId = '';
	    function edit(id) {
	        $("#addOperationDiv").css({
	            display : "block"
	        });
	        $("#articlesDiv").css({
	            display : "none"
	        });

	        $("#createArticleDiv").css({
	            display : "none"
	        });
	       
	        editUserId = id;
	        //$("input[name^='type']").each(function(index, o) {
	        //  o.checked = false;
	        //});
	        $.ajax({
	            url : "/StarkPet/user/getOperationInfo.do?userId=" + id,
	            type : "get",
	            //data:"userId="+id+"",
	            success : function(user) {
	                //alert("name--->" + user.name  );
	                $("#name").val(user.name);
	                var birthday = user.birthday;
	                if (user.sex == "0") {
	                    $("#male").attr({
	                        checked : "checked"
	                    });
	                    $("#maleLable").addClass("active");
	                    $("#femaleLable").removeClass("active");
	                } else {
	                    $("#female").attr({
	                        checked : "checked"
	                    });
	                    $("#maleLable").removeClass("active");
	                    $("#femaleLable").addClass("active");
	                }

	                $("#homeTown").val(user.homeTown);
	                $("#signature").val(user.signature);
	                // alert(user.role);
	                $("#email").val(user.email);
	                if(user.role=="0"){
	                	$("#role").empty();
	                	var option = $("<option>").val(0).text("普通用户");
	                	$("#role").append(option)
	                }
	                else{
	                	$("#role").val(user.role);
	                }
	                
	                $("#phoneNumber").val(user.phoneNumber);
	                $("#password").val('');
	                pwd = user.password;
	            },
	            dataType : "json",
	            contentType : "application/json",
	        });
	        $("#userId").val(id);
	        $("#update").css({
	            display : "block"
	        });
	        $("#add").css({
	            display : "none"
	        });
	    }
		function viewPassword(){
	        if('${user.role}'!="1"&&'${user.userId}'!=editUserId){
	            alert("没有权限查看");
	            return;
	        }
	        $("#password").val(pwd);
	    }
	    function updateUser() {
	       // var targetForm = document.forms[1];
	        $("#formFile").attr("action", "../user/updateOperator.do");
	        //targetForm.action = "../user/updateOperator.do";
	        $("#formFile").submit();
	    }
	    function addUser() {
	        if ($("#name").val() == "") {
	            alert("用户名不能为空");
	            return;
	        }
	        if ($("#birthday").val() == "") {
	            alert("用户生日不能为空");
	            return;
	        }
	        if ($("#homeTown").val() == "") {
	            alert("用户所在地不能为空");
	            return;
	        }
	        if ($("#phoneNumber").val() == "") {
	            alert("用户手机号不能为空");
	            return;
	        }
	        if ($("#password").val() == "") {
	            alert("用户密码不能为空");
	            return;
	        }
	        if ($("#signature").val() == "") {
	            alert("用户签名不能为空");
	            return;
	        }

	        if ($("#petName") == "") {
	            alert("宠物名称不能为空");
	            return;
	        }

	        if ($("#petBirthday") == "") {
	            alert("宠物生日不能为空");
	            return;
	        }

	        var count = 0;
	        var obj = document.getElementsByName("type");
	        for (var i = 0; i < obj.length; i++) {
	            if (obj[i].checked)
	                count++;
	            if (count > 3) {
	                alert("身份总数不能超过3个")
	                return;
	            }
	        }
	        $("#formFile").submit();
	    }
	    function callbackUpdate(str){
	        alert(str);
	        location.reload();
	    }   
	    function createUser() {
	        location.reload();
	    }
	    function callback2(str){
	    	if(str=="1"){
	    		alert("成功");
	    		location.reload();
	    	}
	    }
		</script>
	</div>
</div>