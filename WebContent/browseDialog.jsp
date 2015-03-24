<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal fade" id="browseDialog" tabindex="-1" role="dialog" aria-labelledby="browseLabel" aria-hidden="true">
    <div class="modal-dialog" style="width:600px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="selectLabel">浏览数</h4>
            </div>
            <div class="modal-body">
                <div id="browseContent">
                <input type="number"  class="form-control" id="browseId" placeholder="">
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-default bg-blue" onclick="browseOk()">确定</button> 
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
function showBrowseDialog(count){
	$("#browseId").val(count);
	$('#browseDialog').modal('show');
}

function browseOk(){
	$('#browseDialog').modal('hide');
	var count = $("#browseId").val();
	browseBack(count);
}
</script>