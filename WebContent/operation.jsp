<%@page import="com.stark.web.entity.EnumBase"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>史塔克后台管理-运营账号管理</title>
<link rel="stylesheet" type="text/css" href="../css/styles.css">
<link rel="stylesheet" type="text/css" href="../css/pintuer.css">
<link type="image/x-icon" href="${webIcon }" rel="shortcut icon" />
<link href="${webIcon }" rel="bookmark icon" />
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<script src="../js/pintuer.js"></script>
<script src="../js/content.js"></script>
<script src="../js/respond.js"></script>

<script type="text/javascript">
function callback(str, userId) {
    if (str == '1') {
        alert('发布成功');
    } else if(str == '2'){
        alert('图片不可用 发布失败');
    }
    else {
    	alert("发布失败");
    }
   
    var name = $("#articleUserName").val();
    $("#reference").val("");
    selectUser(userId,name,0);
    resetArticlePicture();
}

function callback2(str){
	if (str == '1') {
        alert('创建成功');
    } else {
        alert('创建失败');
    }
	$("#userId").val("");
	location.reload();
}

function callbackUpdate(str){
	alert(str);
	location.reload();
}	

function createUser() {
	location.reload();
}

	function del(id) {
		if('${userRole}'!="1"){
            alert("没有权限删除");
            return;
        }
		$.ajax({
			url : "/StarkPet/user/delUser.do?userId=" + id,
			type : "post",
			//data:"userId="+id+"",
			success : function(ret) {

				if (ret.result == 1) {
					alert("删除成功");
					location.reload();
				}
			},
			dataType : "json",
			contentType : "application/json",
		});

	}
    var pwd = '';
    var editUserId = '';
	function edit(id) {
		$("#addOperationDiv").css({
            display : "block"
        });
        $("#articleListDiv").css({
            display : "none"
        });
        $("#paginationDiv").css({
            display : "none"
        });

        $("#createArticleDiv").css({
            display : "none"
        });
        $("#commentDiv").css({
            display : "none"
        });
        $("#noticeListDiv").css({
            display : "none"
        });
        editUserId = id;
		//$("input[name^='type']").each(function(index, o) {
		//	o.checked = false;
		//});
		$.ajax({
			url : "/StarkPet/user/getOperationInfo.do?userId=" + id,
			type : "get",
			//data:"userId="+id+"",
			success : function(user) {
				//alert("name--->" + user.name  );
				$("#name").val(user.name);
				var birthday = user.birthday;
				//var date = birthday.substring(0, 4) + "-"
				//		+ birthday.substring(4, 6) + "-"
				//		+ birthday.substring(6, 8);
				//var date = new Date(birthday);
				//$("#birthday").val(date);
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
				$("#role").val(user.role);
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
		if('${userRole}'!="1"&&'${userId}'!=editUserId){
            alert("没有权限查看");
            return;
        }
		
		$("#password").val(pwd);
	}
	function updateUser() {
		var targetForm = document.forms[0];
		targetForm.action = "../user/updateOperator.do";
		targetForm.submit();
		// location.reload();
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

		$("#userId").val("${userId}");
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
		document.forms[0].submit();

		// location.reload();
	}
	
	function viewPicture(str){
		$("#picImgId").attr("src", str);
		$("#picDialogBtn").click();
	}

	function selectUser(userId,userName,page) {
		page = parseInt(page);
		if(page<0)
			return;
		$("#addOperationDiv").css({
			display : "none"
		});
		$("#articleListDiv").css({
			display : "block"
		});

		$("#createArticleDiv").css({
			display : "block"
		});
		$("#commentListDiv").css({
            display : "none"
        });
		$("#noticeListDiv").css({
            display : "none"
        });
		
		//$("#secondDiv").scrollTop = 0;
		var div = document.getElementById('secondDiv'); 
		div.scrollTop = 0; 
		
		$("#articleUserId").val(userId);
	    $("#articleUserName").val(userName);
		$.ajax({
			url : "/StarkPet/article/getArticlesByUserId.do?userId="+ userId + "&page="+page,
			type : "post",
			//data:"userId="+id+"",
			success : function(list) {
				if (list.length < 1) {
					//alert("没有发布过推文");
					$("#articleListDiv")[0].innerHTML = "";
					$("#paginationDiv").css({
                        display : "none"
                    });
					return;
				}
				var articles = "";
				for (var i = 0; i < list.length; i++) {
					var art = list[i];
					var pictures = "";
					var date = strToDate(art.date);
					var strDate = calculateDate(date);
					 var type=art.type;
                     var typeStr = "";
                     
                     if(type=="0"){
                         typeStr = "普通";
                     }else if(type=="2"){
                         typeStr = "每日精选";
                     }
                     else if(type=="3"){
                         typeStr = "杂志";
                     }
                     else if(type=="4"){
                         typeStr = "精选杂志";
                     }
                     else if(type=="5"){
                         typeStr = "举报";
                     }
                     else if(type=="6"){
                         typeStr = "每日精选被举报";
                     }
                     else if(type=="7"){
                         typeStr = "杂志被举报";
                     }
                     else if(type=="8"){
                         typeStr = "精选杂志被举报";
                     }
                     else if(type=="9"){
                         typeStr = "未审核活动推文";
                     }
                     else if(type=="10"){
                         typeStr = "活动推文";
                     }
                     else if(type=="11"){
                         typeStr = "已删除";
                     }
					var picList = art.pictures;
					for (var j = 0; j < picList.length; j++) {
						var picstr = "'"+picList[j]+"'";
						pictures += '<a href="javascript:viewPicture('+picstr+')"><img src="'+picList[j]+'" width="100px" height="100px" class="img-border radius-small" alt="..." /> </a>';
					}
					articles += '<div class="media media-x radius bg-white" id="articleDivObj'+art.articleId+'">'
							+ '   <div class="media-body"><div class="margin">'
							+ '      <strong>'
							+            art.userName
							+ '      </strong>'
							+            art.content
							+ '    </div><div class="margin-big-top margin-right margin-left margin-big-bottom">'
							+        pictures
							+ '        </div>'
							+ '   </div> <div class="margin-left text-small text-gray">'
							+ strDate +'  出处: '+art.reference
							+ '</div><div class="button-group button-group-justified">'
							+ '        <a href="javascript:getComments('+art.articleId+','+art.userId+')" class="button ">评论 <span class="badge bg-white-light" id="commentCount'+art.articleId+'">'+art.commentCount+'</span></a>'
							+ '        <a href="javascript:praiseArticle('+art.articleId+')" class="button ">赞<span class="badge bg-white-light" id="praiseCount'+art.articleId+'">'+art.praiseCount+'</span></a>'
							+ '       <a href="javascript:changeArticleType('+art.articleId+')" class="button" id="articleType'+art.articleId+'">'+typeStr+' </a>'
							+ '        <a href="javascript:deleteArticle('+art.articleId+')" class="button ">删除 </a>'
							+ '</div>   </div>' + '</div>' + '</div>';
					
				}
			    if(list.length==10||page>0){
			    	$("#paginationDiv").css({
			            display : "block"
			        });
			    	var nextPage = page+1;
			    	var prePage = page-1;
			    	var ination = '<ul class="pagination pagination-group ">';
			    	userName = "'"+userName+"'";
			    	
			    	if(page!=0){
			    		ination += '<li><a href="javascript:selectUser('+userId+','+userName+','+prePage+')">上一页</a></li>';
			    	}else {
			    		ination += '<li><a disabled="false" href="javascript:void(0);">上一页</a></li>';
			    	}
			    	if(list.length==10){
			    		ination += '<li><a href="javascript:selectUser('+userId+','+userName+','+nextPage+')">下一页</a></li></ul>';
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
				//$("#articleListDiv")[0].innerHTML = "";
				$("#articleListDiv")[0].innerHTML = articles;
			},
			dataType : "json",
			contentType : "application/json",
		});
	}
	
	function deleteArticle(articleId){
		var auId = $("#articleUserId").val();
        if('${userRole}'!="1"){
        	var flag = false;
        	
        	$.ajax({
                url : "/StarkPet/user/isChildUser.do?userId=${userId}&childId="+auId,
                type : "get",
                async:false,
                //data:JSON.stringify(chartlet),
                success : function(ret) {
                    if (ret.result != "1") {
                    	flag = true;
                        return;
                    }
                },
                dataType : "json",
                contentType : "application/json",
            });
        	
        	if(flag){
        		alert("没有权限删除");
        		return;
        	}
            
            
        }
        $.ajax({
            url : "/StarkPet/article/changeArticleType.do?articleId="+articleId+"&type="+11,
            type : "get",
            //data:JSON.stringify(chartlet),
            success : function(ret) {
                
                if (ret.result == "1") {
                    
                   $("#articleDivObj"+articleId).remove();
                }
                else {
                    alert("删除失败");
                }
            },
            dataType : "json",
            contentType : "application/json",
        });
    }
	
	var selectArticleId = "";
    function praiseArticle(articleId){
        var usergroup=document.getElementsByName("userGroupBox");
        for(var i=0;i<usergroup.length;i++){
            usergroup[i].checked = false;
        }
        
        selectStatus = "praise";
        selectArticleId = articleId;
        $("#userGroupDiv").css({
            display : "block"
        });
        $("#commentDiv").css({
            display : "none"
        });
        /*
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
        });*/
    }
    
    function selectUserSubmit(){
        var usergroup=document.getElementsByName("userGroupBox");
        var users = [];
        
        for(var i=0;i<usergroup.length;i++){
            var user = usergroup[i];
            if(usergroup[i].checked==true){         
               users[users.length] = user.value;
              
            }
        }
        var objectId;
        var url = "";
        if(selectStatus == "praise"){
            url = "/StarkPet/article/addPraises.do";
            objectId = selectArticleId;
        }
        else if(selectStatus=="follow"){
            url = "/StarkPet/user/addFollows.do";
            objectId = selectUserId;
        }
        var user ={
                objectId:objectId,
                usersId:users,
            }
        $.ajax({
            url:url,
            type:"post",
            data:JSON.stringify(user),
            success:function(a){
                if(a.result==1){
                    if(selectStatus == "praise"){
                         var pObj = document.getElementById("praiseCount"+objectId);
                         var count = pObj.innerHTML;
                         pObj.innerHTML = parseInt(count)+a.count;
                    }
                    else if(selectStatus=="follow"){
                        alert("关注成功");
                    }
                    $("#userGroupDiv").css({
                        display : "none"
                    });
                }
            },
            dataType: "json",
            contentType: "application/json",
        });
        
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
	
    function publishComments(){
    	var auId = $("#articleUserId").val();
    	var name = $("#articleUserName").val();
    	if($("#commentContent").val()==""){
    		alert("请输入内容");
    		return;
    	}
    	publishComment(auId,name);
    	var articleId = $("#commentArticleId").val();
        var pObj = document.getElementById("commentCount"+articleId);
        var count = pObj.innerHTML;
        pObj.innerHTML = parseInt(count)+1;
    }

	
	function createArticle() {

	}

	function inithere() {

		var trs = document.getElementById('userTable').getElementsByTagName('tr');
		for (var i = 0; i < trs.length; i++) {
			trs[i].onmousedown = function() {
				tronmousedown(this);
			}
		}
	}

	function tronmousedown(obj) {
		var trs = document.getElementById('userTable').getElementsByTagName('tr');
		for (var o = 0; o < trs.length; o++) {
			if (trs[o] == obj) {
				trs[o].style.backgroundColor = '#DFEBF2';
			} else {
				trs[o].style.backgroundColor = '';
			}
		}
	}
	
	function viewNotice(userId){
		$("#createArticleDiv").css({
            display : "none"
        });
		$("#addOperationDiv").css({
            display : "none"
        });
		$("#articleListDiv").css({
            display : "none"
        });
		$("#paginationDiv").css({
            display : "none"
        });
		$("#commentDiv").css({
            display : "none"
        });
		$("#noticeListDiv").css({
            display : "block"
        });
		 $.ajax({
	            url : "/StarkPet/notice/getListByUserId.do?userId="+userId,
	            type : "get",
	            //data:JSON.stringify(chartlet),
	            success : function(noticeList) {
	            	$("#noticeListDiv")[0].innerHTML="";
	            	var notices = noticeList.notices;
	                for(var i=0;i<notices.length;i++){
	                	var notice = notices[i];
	                	
	                	var date = strToDate(notice.date);
                        var strDate = calculateDate(date);
	                	var typeIcon = "../images/";
	                	var headDiv = $('<a class="float-left" href="#"> <img src="'+notice.senderHeadPic+'" style="height: 50px; width: 50px;margin-top:15px" class="radius-circle" > </a>');
	                	var contentDiv = $('<div class="media-body float-left margin-big-top"> <strong>'+notice.senderName+'</strong>'+notice.content+'<div class="margin-top text-small text-gray">'+strDate+'</div></div>');
	                	var picDiv = $('<div class="float-right"><img src="'+notice.thumbImg+'" style="height: 80px; width: 80px"  ></div>');
	                	if(notice.type=="0"){
	                		typeIcon += "follow1.png";
	                		picDiv = "";
	                	}
	                	else if(notice.type=="1"){
	                		typeIcon += "comment1.png";
	                	}
	                	else if(notice.type=="2"){
	                		typeIcon += "praise1.png";
	                	}
	                	else if(notice.type=="3"){
	                		typeIcon += "system1.png";
	                		headDiv = $('<a class="float-left" style="height: 50px; width: 50px;margin-top:15px"  href="#"> ');
	                		contentDiv = $('<div class="media-body float-left margin-big-top"> <strong>系统通知</strong>'+notice.content+'<div class="margin-top text-small text-gray">'+strDate+'</div></div>');
	                		picDiv = "";
	                	}
	                	
	                	var mainDiv = $('<div class="media media-x border-top border-bottom padding" ></div>');
	                	var iconDiv = $('<div class="float-left"><img src="'+typeIcon+'" style="height: 40px; width: 40px ;margin-top:20px" class="radius-circle" ></div>');
	                	
	                	mainDiv.append(iconDiv);
	                	mainDiv.append(headDiv);
	                	mainDiv.append(contentDiv);
	                	mainDiv.append(picDiv);
	                	$("#noticeListDiv").append(mainDiv);
	                	$("#noticeStatus"+userId).removeClass("bg-dot");
	                }
	                
	            },
	            dataType : "json",
	            contentType : "application/json",
	        });
	}
    function viewImportantUser(){
    	$("#userTable").find("tr").each(function(){
            var tdArr = $(this).children();
            var tdcon = tdArr.eq(3).html();
            if(tdcon!="重要用户"&&tdcon!="<strong>角色</strong>"){
            	$(tdArr).remove();
            }
        });
    }
    
    function publishArticle(){
    	//if($("#articleContent").val()==""){
    	//	alert("请输入内容");
    	//	return;
    	//}
    	$("#articleFormFile").submit();
    	
    }
</script>
</head>
<body onload="inithere()">
	<div style="height:100%">
		
			<ul id="header">
				<li style="width: 70px"><a href="toMain.do">首页</a></li>
				<li><a href="operatorManage.do">账号管理</a></li>
				<li><a href="articleManage.do">推文管理</a></li>
				<li><a href="activityManage.do">活动管理</a></li>
				<li><a href="publishManage.do">发布管理</a></li>
				<li><a href="systemManage.do">系统管理</a></li>
			</ul>
		
		<div class="padding-big bg" style="width: 25%;height: 93%; float: left;">
			<button class="button bg-blue radius-rounded margin" onclick="createUser()">新建用户</button>
			<a class="button bg-green radius-rounded  " href="./operatorManage.do?view=all">查看所有</a>
			<a class="button bg-green radius-rounded  " href="./operatorManage.do?view=self">查看自己</a>
			<button class="button bg-green radius-rounded " onclick="viewImportantUser()">重要用户</button>
			<div style="overflow: auto; height: 95%;">
				<table id="userTable" class="table text-center " >
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
								    <td  style="text-align:center;vertical-align:middle;">${u.userId }</td>
									<td style="text-align:center;vertical-align:middle;" ><a href="javascript:selectUser('${u.userId }','${u.name }','0')"><img src="${u.getHeadUrl()}" style="height: 50px; width: 50px" class="radius-circle" /></a></td>
									<td style="text-align:center;vertical-align:middle;" >${u.name }</td>
									<c:if test="${u.role==2 }">
										<td  style="text-align:center;vertical-align:middle;">运营</td>
									</c:if>
									<c:if test="${u.role==3 }">
										<td style="text-align:center;vertical-align:middle;" >模拟用户</td>
									</c:if>
									<c:if test="${u.role==6 }">
                                        <td style="text-align:center;vertical-align:middle;" >重要用户</td>
                                    </c:if>
									<td style="width:40%;text-align:center;vertical-align:middle;" ><a  class="button" href="javascript:edit('${u.userId}');"><span class="icon-edit"></span></a> 
									    <a  class="button" href="javascript:del('${u.userId}')"><span class="icon-trash-o"></span></a>
									    <c:if test="${u.noticeStatus==1 }">
									    <a id="noticeStatus${u.userId }" class="button bg-dot" href="javascript:viewNotice('${u.userId}')">
									       <span class="icon-volume-up"></span>
									    </a>
									    </c:if>
									    <c:if test="${u.noticeStatus==0 }">
                                        <a id="noticeStatus${u.userId }" class="button" href="javascript:viewNotice('${u.userId}')">
                                           <span class="icon-volume-up"></span>
                                        </a>
                                        </c:if>
									</td>
								</tr>

							</c:forEach>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>

		<div id="secondDiv" class="padding-large"
			style="width: 43%; height: 93%; overFlow-y:scroll; float: left;  border-right: 1px #aaa solid; border-left: 1px #aaa solid;">

			<div id="addOperationDiv">
            
				<form id='formFile' name='formFile' method="post" action="../user/addOperator.do" target='frameFile' enctype="multipart/form-data">
					<input type="hidden" name="userId" id="userId">
					<div class="form-group">
						<div class="label">
							<label for="username">姓名</label>
						</div>
						<div class="field">
							<input type="text" class="input" id="name" name="name" size="50" />
						</div>
					</div>
					<div class="form-group">
						<div class="label">
							<label for="sex">性别:</label>
						</div>
						<div class="field">
							<div class="button-group radio">
								<label class="button active" id="maleLable"><input name="sex" value="0" checked="checked" type="radio" id="male"><span class="icon icon-male text-green"></span>先生</label>
								<label class="button" id="femaleLable"><input name="sex" value="1" type="radio"><span class="icon icon-female text-red" id="female"></span> 女士</label>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="label">
							<label for="password">密码</label>
						</div>
						<div class="field input-inline  clearfix" >
							<input type="text" class="input" id="password" name="password" size="50" />
							<input class="button" type="button" value="查看" id="viewPwdBtn" onclick="viewPassword()"/>
						</div>
					</div>
					<div class="form-group">
						<div class="label">
							<label for="homeTown">所在地</label>
						</div>
						<div class="field">
							<input type="text" class="input" id="homeTown" name="homeTown" size="50" />
						</div>
					</div>

					<div class="form-group">
						<div class="label">
							<label for="role">角色</label>
						</div>
						<div class="field">
							<select id="role" name="role" class="input">
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
						<div class="label">
							<label for="face">头像</label>
						</div>
						<div class="field">
							<a class="button input-file bg-blue" href="javascript:void(0);">+ 浏览文件<input type="file" name="headPic"></a>
						</div>
					</div>
					<div class="form-button float-right">
						<input class="button bg-green" type="button" value="提交" onclick="addUser()" id="add">
					</div>
					<div class="form-button float-right">
						<input type="button" class="button bg-green" value="更新" onclick="updateUser()" id="update" style="display: none;">
					</div>
				</form>
			</div>

			<div id="createArticleDiv" style="display: none">
				<form id='articleFormFile' name='formFile' method="post" action="../article/addArticle.do" target='frameFile' enctype="multipart/form-data">
					<input type="hidden" id="articleUserId" name="userId"> 
					<input type="hidden" id="articleUserName" name="userName">
					<div class="form-group">
					<div class="label">
						<label for="readme">发布内容</label>
					</div>
					<div class="field margin-left">
						<textarea id="articleContent" name="content" class="input" rows="3" cols="30" placeholder="1000字以内"></textarea>
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
					
					<div style="height:50px" class="padding-big-top margin-left">
					
					<div class="label float-left">
						<a class="button input-file bg-green " style="width: 50px" href="javascript:void(0);" id="addArticlePicture0"> 
						  <span class="icon-image text-big"></span> 
						  <input type="file" name="picture1" onchange="previewArticleImage(this)"  id="articlePictureInput1"/>
						</a> 
						<a class="button input-file bg-green" style="display: none; width: 50px" href="javascript:void(0);" id="addArticlePicture1"> 
						  <span class="icon-image text-big"></span>
						  <input type="file" name="picture2" onchange="previewArticleImage(this)"  id="articlePictureInput2"/>
						</a> 
						<a class="button input-file bg-green" style="display: none; width: 50px" href="javascript:void(0);" id="addArticlePicture2"> 
						  <span class="icon-image text-big"></span>
						  <input type="file" name="picture3" onchange="previewArticleImage(this)"  id="articlePictureInput3"/>
						</a> 
						<a class="button input-file bg-green" style="display: none; width: 50px" href="javascript:void(0);" id="addArticlePicture3">
						  <span class="icon-image text-big"></span>
						  <input type="file" name="picture4" onchange="previewArticleImage(this)"  id="articlePictureInput4"/>
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
						  <span class="icon-image text-big"></span> <input type="file" onchange="previewArticleImage(this)" />
						</a>
					</div>
					<div class="field float-left " >

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
							<button class="button bg-green" type="button" onclick="publishArticle()">发布</button>
						</div>
					</div>
				</form>
			</div>
            
			<div id="articleListDiv" class="padding bg-mix radius" style="display: none"></div>
			<div id="paginationDiv" class="button-group button-group-justified bg-mix" style="display: none">
			</div>
			<div id="noticeListDiv" class="padding-large  radius" style="display: block;width:80%;margin-left:auto;margin-right:auto">
               
			</div>
		</div>
		<div id="thirdDiv" class="padding-large" style="width: 30%; height: 90%; overflow: auto; float: left; border-right: 1px #aaa solid;">
		  <div id="commentDiv" style="display:none">
		      <div id="createCommentDiv">
		          <input type="hidden" id="commentArticleId">
		          <div class="label">
                        <label for="readme">回复评论</label>
                    </div>
                    <div class="field margin-left">
                        <textarea id="commentContent" name="content" class="input" rows="3" cols="30" placeholder="500字以内"></textarea>
                    </div>
                    <div class="form-button text-right padding-top">
                        <button class="button bg-green" type="submit" onclick="publishComments()">发布</button>
                    </div>
		      </div>
		      <div id="commentListDiv" class="bg-back margin-top padding radius">
		          
		      </div>
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
		<iframe id='frameFile' name='frameFile' style='display: none;'></iframe>
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
</body>
</html>