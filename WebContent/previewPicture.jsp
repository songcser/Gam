<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div style="" class="margin-left">
	<div class="margin-bottom" id="addPictureId">
		<a class="btn bg-green btn-lg " style="" href="javascript:addPicture();" id="addArticlePicture">
		<span class="glyphicon glyphicon-picture text-big"></span> </a>
	</div>
	<div class="row " style="width:100%" id="previewDivId">
		
	</div>
</div>
<script type="text/javascript">
var index = 0;
var picCount = 0;
function addPicture(){
	picCount++;
	$("#previewDivId").append('<div id="preview'+index+'" class="col-xs-6 col-sm-4 col-md-3 col-lg-2" style=""></div>');
	$('#addPictureId').append('<input type="file" style="display:none" name="picture'+index+'" onchange="previewArticleImage(this)" id="articlePictureInput'+index+'" />')
	$("#articlePictureInput"+index).click();
}

function previewArticleImage(file) {
    previewImage(file, index);
}

function removePicture(t) {
	picCount--;
    //var div = document.getElementById('preview' + t);
    $("#preview"+t).remove();
    $("#articlePictureInput"+t).remove();
    //var divObj = document.getElementById('articlePictureInput'+t);
    //$("#previewDivId").remove(div);
    //$('#addPictureId').remove(divObj)
}

function getPicCount(){
	return picCount;
}

function resetPreviewPicture(){
	$("#previewDivId").html("");
	$('#addPictureId').html('<a class="btn bg-green btn-lg " style="" href="javascript:addPicture();" id="addArticlePicture"><span class="glyphicon glyphicon-picture text-big"></span> </a>');
}

function previewImage(file, i) {
    var MAXWIDTH = 130;
    var MAXHEIGHT = 130;
    var div = document.getElementById('preview'+i);
    if (file.files && file.files[0]) {
        div.innerHTML ='<a style="position:relative" href="javascript:removePicture(' + i
                + ')" title="删除"><span  class="glyphicon glyphicon-remove-sign" style="position:absolute;left:123px;top:-5px;"></span></a>'
                +'<a style="width:130px;height:130px;" class="thumbnail"><img id=imghead'
                + i + '></a>';
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
            index++;
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