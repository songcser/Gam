<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Modal -->
<div class="modal fade" id="commentDialog" tabindex="-1" role="dialog" aria-labelledby="commentLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 900px">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">评论</h4>
			</div>
			<div class="modal-body" id="noticeDiologId">
				<div id="commentDiv" >
					<div id="createCommentDiv">
						<input type="hidden" id="commentArticleId">
						<div class="field margin-left">
							<textarea id="commentContent" name="content" class="form-control" rows="3" cols="30" placeholder="500字以内"></textarea>
						</div>
						<div class="form-button text-right padding-top">
							<button class="btn bg-green" type="submit" onclick="publishComments()">发布</button>
						</div>
					</div>
					<div id="commentListDiv" class="bg-back margin-top padding radius">fdsfdsfds</div>
				</div>
			</div>
			<div class="modal-footer">
				<!-- <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button> -->
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
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

var atUserList = [];
function publishComment(userId,userName){
    var content = $("#commentContent").val();
    var articleId = $("#commentArticleId").val();
    var auId = $("#articleUserId").val();
    //alert(content+"==="+articleId+"==="+userId);
    
    var comment = {
          article:{
              articleId:articleId,
              user:{
                  userId:auId,
              }
          },
          user:{
              userId:userId,
              name:userName,
          },
          content:content,
          atUsers:atUserList,
    }
    
    $.ajax({
        url:"/StarkPet/comment/publish.do",
        type:"post",
        data:JSON.stringify(comment),
        success:function(a){
            //alert("status--->" + a.status + "----------result--->" + a.result );
            if(a.result=="1"){
                
                //alert("发布成功");
                $("#commentContent").val("");
                getComments(articleId,auId);
            }
            else{
                alert("发布失败");
            }
        },
        dataType: "json",
        contentType: "application/json",
    });
}

function getComments(articleId,userId){
	$('#commentDialog').modal('show');
	$("#commentArticleId").val(articleId);
    $("#articleUserId").val(userId);
    $.ajax({
        url : "/StarkPet/comment/getByArticleId.do?articleId=" + articleId + "&page=0",
        type : "post",
        //data:"userId="+id+"",
        success : function(comment) {
            if(comment.result=="0"){
                return;
            }
            list = comment.articles;
            var comPart = "";
            for(var i=0;i<list.length;i++){
                var comment = list[i];
                var date = strToDate(comment.date);
                var strDate = calculateDate(date);
                var commentUserId = "'"+comment.userId+"'";
                var commentUserName = "'"+comment.userName+"'";
                comPart+= '<div class="media media-x bg-white padding">'
                    +'<div class="pull-left text-center">'
                    +'<a href="javascript:atUser('+commentUserId+','+commentUserName+')" class="tips" data-toggle="hover" data-place="left" title="点击头像进行回复"> '
                    +'<img src="'+comment.headPic+'" class="radius-circle" width="40px" height="40px" alt="..."></a> '
                    +'</div> <div class="media-body ">'
                    +'<strong>'+comment.userName+'</strong><div class="margin-top margin-bottom">'
                    +comment.content
                    +'</div><div class=" text-small text-gray">'
                    + strDate
                    + '</div>'
                    +' </div></div>';
            }
            
            //$("#commentListDiv")[0].innerHTML=comPart;
            $("#commentListDiv").html(comPart);
        },
        dataType : "json",
        contentType : "application/json",
    });
}

function atUser(userId,userName){
    var content = $("#commentContent").val();
    content += "@"+userName+" ";
    $("#commentContent").val(content);
    atUserList[atUserList.length] = userId;
}

</script>
