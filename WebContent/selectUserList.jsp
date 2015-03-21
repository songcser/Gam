<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Modal -->
<div class="modal fade" id="selectDialog" tabindex="-1" role="dialog" aria-labelledby="selectUserLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 80%">
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
						<div class="thumbnail" id="userHeadDiv${u.userId}" style="height:100px">
							<a href="javascript:clickUserHead(${u.userId})"> <img src="${u.getHeadUrl()}" style="height: 50px; width: 50px" class="img-circle" /></a>
							<div class="text-center margin-top text-small text-gray">
								${u.name }
							</div>
						</div>
					</div>
				</c:forEach>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default bg-blue" >确定</button> 
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
function selectUserList(){
	$('#selectDialog').modal('show');
}

function clickUserHead(userId){
	var user = userId;
	$("#userHeadDiv"+userId).addClass("border");
}
</script>