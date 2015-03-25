<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="margin-large panel panel-default">
    <div class="panel-body">
	<form id='activityformId' name='acNameForm' method="post" action="../activity/addActivity.do" target='frameFile' enctype="multipart/form-data">
		<div class="form-group">
			<label for="subject">标题</label><input type="text" class="form-control" id="subject" name="subject" required="required"/>
		</div>
		<div class="form-group">
			<label for="order">序号</label><input type="number" min="1" value="1" class="form-control" id="order" name="order"  data-validate="required:必填" />
		</div>
		<div class="form-group">
			<label for="activityType">类型</label>
		</div>
		<div class="margin-large-bottom">
			<select id="activityType" name="activityType" class="form-control">
				<c:if test="${!empty types }">
					<c:forEach items="${types }" var="o">
						<option value="${o.index}">${o.name}</option>
					</c:forEach>
				</c:if>
			</select>
		</div>
		<div class="pull-left " style="width: 50%;">
				<button class="btn bg-blue" type="button" onclick="uploadCover()">上传封面</button>
				<div style="display:none">
					 <input size="100" type="file" name="cover" id="coverictureInput" 
					 onchange="previewImage(this,1)" data-validate="regexp#.+.(jpg|jpeg|png|gif)$:只能上传jpg|gif|png格式文件" />
				</div>
				 <div id="preview1" class="margin-bottom padding-big-top"></div>
		</div>
		<div class="pull-left " style="width: 50%;">
				<button class="btn bg-blue" type="button" onclick="uploadContent()">上传内容图</button>
				<div style="display:none">
					<input size="100" type="file" name="content" id="contentPictureInput"
						onchange="previewImage(this,2)" data-validate="regexp#.+.(jpg|jpeg|png|gif)$:只能上传jpg|gif|png格式文件" />
				</div>
				<div id="preview2" class="margin-bottom padding-big-top"></div>
		</div>
		<div style="width: 100%; clear: both">
			<div class=" text-right">
				<button class="btn bg-green" type="submit">提交</button>
			</div>
		</div>
	</form>
	<iframe id='frameFile' name='frameFile' style='display: none;'></iframe>
	</div>
</div>
<script type="text/javascript">
function uploadCover(){
	$("#coverictureInput").click();
}

function uploadContent(){
	$("#contentPictureInput").click();
}
var count=0;
function removePicture(t) {
    $("#preview"+t).html("");
    if(t==1){
    	$("#coverictureInput").replaceWith('<input size="100" type="file" name="cover" title="'+count+'" id="coverictureInput" onchange="previewImage(this,1)" data-validate="regexp#.+.(jpg|jpeg|png|gif)$:只能上传jpg|gif|png格式文件" />');
    }
    else if(t==2){
    	$("#contentPictureInput").replaceWith('<input size="100" type="file" name="cover" title="'+count+'" id="contentPictureInput" onchange="previewImage(this,2)" data-validate="regexp#.+.(jpg|jpeg|png|gif)$:只能上传jpg|gif|png格式文件" />');
    }
    count++;
}

function previewImage(file, i) {
    var MAXWIDTH = 130;
    var MAXHEIGHT = 130;
    var div = document.getElementById('preview'+i);
    if (file.files && file.files[0]) {
        div.innerHTML ='<a style="position:relative" href="javascript:removePicture(' + i
                + ')" title="删除"><span  class="glyphicon glyphicon-remove-sign" style="position:absolute;left:123px;top:-5px;"></span></a>'
                +'<a style="width:130px;height:130px;" class="thumbnail"><img id=imghead' + i + '></a>';
        var img = document.getElementById('imghead' + i);
        
        img.onload = function() {
            
            var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
            img.width = rect.width;
            img.height = rect.height;
            //img.width = MAXWIDTH;
            //img.height= MAXHEIGHT;
            //img.style.marginLeft = rect.left + 'px';
            // img.style.marginTop = rect.top + 'px';
            img.className = "img-border radius padding-small";
            var fileSize = file.files[0].size;
            if(fileSize>1024*800){
                alert("图片大小尽量小于800kb");
                
            }
            file.name+="?"+img.naturalWidth+"&"+img.naturalHeight;
        }
        var reader = new FileReader();
        reader.onload = function(evt) {
            var quality =  80;
            img.src = evt.target.result;
            //img.src = jic.compress(img,quality).src
            //
        }
        reader.readAsDataURL(file.files[0]);
        
        //var width=img.naturalWidth;
        //var height=img.naturalHeight;
    } else {
        var sFilter = 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src="';
        file.select();
        var src = document.selection.createRange().text;
        div.innerHTML = '<img id=imghead>';
        var img = document.getElementById('imghead');
        img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
        var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth,
                img.offsetHeight);
        status = ('rect:' + rect.top + ',' + rect.left + ',' + rect.width + ',' + rect.height);
        div.innerHTML = "<div id=divhead style='width:" + rect.width
                + "px;height:" + rect.height + "px;margin-top:" + rect.top
                + "px;margin-left:" + rect.left + "px;" + sFilter + src
                + "\"'></div>";
    }
}
function clacImgZoomParam(maxWidth, maxHeight, width, height) {
    var param = {
        top : 0,
        left : 0,
        width : width,
        height : height
    };
    if (width > maxWidth || height > maxHeight) {
        rateWidth = width / maxWidth;
        rateHeight = height / maxHeight;

        if (rateWidth > rateHeight) {
            param.width = maxWidth;
            param.height = Math.round(height / rateWidth);
        } else {
            param.width = Math.round(width / rateHeight);
            param.height = maxHeight;
        }
    }

    param.left = Math.round((maxWidth - param.width) / 2);
    param.top = Math.round((maxHeight - param.height) / 2);
    return param;
}
</script>