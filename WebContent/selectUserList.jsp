<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Modal -->
<div class="modal fade" id="selectDialog" tabindex="-1" role="dialog" aria-labelledby="selectUserLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 70%">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="selectLabel">选择用户</h4>
			</div>
			<div class="modal-body">
				<div class="row">
				 <c:forEach items="${operations }" var="u">
					<div class="col-xs-6 col-sm-4 col-md-2 col-lg-1 ">
						<div class="margin-bottom" style="height:110px;width:80px">
							<a id="userHeadImg${u.userId}" class="thumbnail" href="javascript:clickUserHead('${u.userId}','${u.name }','${u.getHeadUrl() }')"> 
							<img  src="${u.getHeadUrl()}" style="height: 70px; width: 70px;" class="img-circle " />
							</a>
							<p class="text-center  text-small text-gray">
								${u.name }
							</p>
						</div>
					</div>
				</c:forEach>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default bg-blue" onclick="selectUserOk()">确定</button> 
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">

var selectUserId = [];
var selectUserName = [];
var selectUserHeadUrl=[];

var multiSelect = false;

function selectUserList(){
	$('#selectDialog').modal('show');
}

function setMultiSelect(){
	multiSelect = true;
}

function clickUserHead(userId,name,headUrl){
	if(multiSelect){
		var length = selectUserId.length;
		var isSelect = false;
		for(var i=0;i<length;i++){
			if(selectUserId[i]==userId){
				$("#userHeadImg"+userId).removeClass("bg-blue");
				isSelect = true;
			}
			if(isSelect){
				selectUserId[i]=selectUserId[i+1];
				length--;
			}
		}
		if(isSelect){
			selectUserId.length = length;
		}
		else{
			selectUserId[selectUserId.length] = userId;
	        $("#userHeadImg"+userId).addClass("bg-blue");
		}
		
	}
	else{
		selectUserId[0] = userId;
	    selectUserName[0]= name;
	    selectUserHeadUrl[0]=headUrl;
	    $(".thumbnail").removeClass("bg-blue");
	    $("#userHeadImg"+userId).addClass("bg-blue");
	}
	//alert(selectUserId);
}

function resetSelectUser(){
	selectUserId.length = 0;
	selectUserName.length = 0;
	selectUserHeadUrl.length = 0;
	multiSelect = false;
	$(".thumbnail").removeClass("bg-blue");
}

function selectUserOk(){
	
	$('#selectDialog').modal('hide')
	selectBack();
	resetSelectUser();
}
</script>