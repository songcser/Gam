<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog" style="width:700px">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">通知</h4>
      </div>
      <div class="modal-body" id="noticeDiologId">
       
      </div>
      <div class="modal-footer">
        <!-- <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button> -->
      </div>
    </div>
  </div>
</div>
<script type="text/javascript">
function viewNotice(userId){
    $('#myModal').modal('show');
     $.ajax({
            url : "/StarkPet/notice/getListByUserId.do?userId="+userId,
            type : "get",
            //data:JSON.stringify(chartlet),
            success : function(noticeList) {
                $("#noticeDiologId")[0].innerHTML="";
                var notices = noticeList.notices;
                for(var i=0;i<notices.length;i++){
                    var notice = notices[i];
                    
                    var date = strToDate(notice.date);
                    var strDate = calculateDate(date);
                    var typeIcon = "../images/";
                    var headDiv = $('<a class="pull-left" href="#"> <img src="'+notice.senderHeadPic+'" style="height: 50px; width: 50px;margin-top:15px" class="radius-circle" > </a>');
                    var contentDiv = $('<div class=" pull-left margin-top"> <strong>'+notice.senderName+'</strong><br />'+notice.content+'<div class="margin-top text-small text-muted">'+strDate+'</div></div>');
                    var picDiv = $('<div class="pull-right"><img src="'+notice.thumbImg+'" style="height: 80px; width: 80px"  ></div>');
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
                        headDiv = $('<a class="pull-left" style="height: 50px; width: 50px;margin-top:15px"  href="#"> ');
                        contentDiv = $('<div class="media-body pull-left margin-big-top"> <strong>系统通知</strong>'+notice.content+'<div class="margin-top text-small text-gray">'+strDate+'</div></div>');
                        picDiv = "";
                    }
                    
                    var mainDiv = $('<div class="media border-top border-bottom padding" ></div>');
                    var iconDiv = $('<div class="pull-left"><img src="'+typeIcon+'" style="height: 40px; width: 40px ;margin-top:20px" class="radius-circle" ></div>');
                    
                    mainDiv.append(iconDiv);
                    mainDiv.append(headDiv);
                    mainDiv.append(contentDiv);
                    mainDiv.append(picDiv);
                    $("#noticeDiologId").append(mainDiv);
                    $("#noticeStatus"+userId).removeClass("bg-dot");
                }
                
            },
            dataType : "json",
            contentType : "application/json",
        });
}
</script>