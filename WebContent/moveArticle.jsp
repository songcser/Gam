<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="modal fade" id="moveDialog" tabindex="-1" role="dialog" aria-labelledby="moveLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 50%">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="moveLabel">复制推文</h4>
			</div>
			<div class="modal-body">
			     <div class="row">
			     <div class="col-lg-3">
				<div class="btn-group " >
					<label class="btn btn-primary active"> <input type="radio" name="moveArticle" id="moveRecommend" autocomplete="off" checked onclick="moveToRecommend()"> 推荐
					</label> <label class="btn btn-primary"> <input type="radio" name="moveArticle" id="moveShow" autocomplete="off" onclick="moveToShow()"> 节目单
					</label>
				</div>
				</div>
				<div style="display: inline;" class="col-lg-6">
					<select name="moveShowId" id="moveShowId" class="form-control" style="width:60%" disabled>
						<c:if test="${!empty showList }">
							<c:forEach items="${showList }" var="u">
								<option value="${u.activityId }" title="${u.type }">${u.subject }</option>
							</c:forEach>
						</c:if>
					</select>
				</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<button type="button" class="btn btn-default bg-blue" onclick="moveOk()">确定</button>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
var moveToWhere = 0;
var removeShowOption = [];
function showMoveArticleDialog(content, type) {
	if(type==10||type==14){
		if(removeShowOption.length>0){
	        for(var i=0;i<removeShowOption.length;i++){
	            $("#moveShowId").append(removeShowOption[i]);
	        }
	        removeShowOption.length=0;
	    }
		if(typeof(selectActivityId) != 'undefined'){
			var obj = $("#moveShowId option[value='"+selectActivityId+"']");
	        var index=0;
	        for(var i=0;i<obj.length;i++){
	            removeShowOption[index] = obj[i];
	            index++;
	            $(obj[i]).remove();
	        }
	        obj = $("#moveShowId option[title!='"+selectActivityType+"']");
	        for(var i=0;i<obj.length;i++){
	            removeShowOption[index] = obj[i];
	            index++;
	            $(obj[i]).remove();
	        }
		}
	}
	if(type==14||type==13){
		$("#moveShow").attr("checked","checked");
		$('input[type="radio"]:checked').parent('label').addClass('active');
		$('#moveShowId').attr("disabled",false);
		$("#moveShow").attr("disabled",true);
		$("#moveRecommend").attr("disabled",true);
		moveToWhere = 1;
	}
	if(type==0||type==16||type==13){
		$("#moveShowId option[title!='2']").remove();
	}
	$('#moveDialog').modal('show');
}

function moveToRecommend(){
	moveToWhere = 0;
	$('#moveShowId').attr("disabled",true);
}

function moveToShow(){
	moveToWhere = 1;
	$('#moveShowId').attr("disabled",false);
}

function moveOk() {
	$('#moveDialog').modal('hide');
	var showId = $("#moveShowId").val();
	moveBack(moveToWhere,showId);
}
</script>