<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal fade" id="auditingDialog" tabindex="-1" role="dialog" aria-labelledby="auditingLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 50%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="selectLabel">审核</h4>
            </div>
            <div class="modal-body">
                <div id="auditingContent"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-default bg-blue" onclick="auditingOk()">确定</button> 
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
var dialogType = 0;
function showAuditingDialog(content,type){
	dialogType = type;
	$("#auditingContent").html(content);
	$('#auditingDialog').modal('show');
}

function auditingOk(){
	$('#auditingDialog').modal('hide');
	auditingBack(dialogType);
}
</script>