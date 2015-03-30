<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal fade" id="sliderDialog" tabindex="-1" role="dialog" aria-labelledby="sliderLabel" aria-hidden="true">
	<div class="modal-dialog" id="sliderModalId">
		<div class="modal-content " id="sliderContentId">
			<div id="slidershow" class="carousel slide padding-top padding-bottom" data-ride="carousel" style="height:100%">
				<!-- 设置图片轮播的顺序 -->
				<ol class="carousel-indicators" id="carouselIndicators">
					
				</ol>
				<!-- 设置轮播图片 -->
				<div class="carousel-inner " style="height:100%" id="carouselinner">
					
				</div>
				<a class="left carousel-control " href="#slidershow" role="button" data-slide="prev"> <span class="glyphicon glyphicon-chevron-left"></span>
				</a> <a class="right carousel-control" href="#slidershow" role="button" data-slide="next"> <span class="glyphicon glyphicon-chevron-right"></span>
				</a>
			</div>
		</div>
	</div>
</div>
<script>
var height = document.documentElement.clientHeight*0.8;
var width = document.documentElement.clientWidth*0.7;

document.getElementById("sliderContentId").style.height=height+"px";
document.getElementById("sliderModalId").style.width=width+"px";

	function viewPicture(articleId) {
		$('#sliderDialog').modal('show');
		$("#carouselIndicators").html("");
		$("#carouselinner").html("");
		
		var picObjs = document.getElementsByName("sliderPicture"+articleId);
		$("#carouselIndicators").append('<li class="active" data-target="#slidershow" data-slide-to="0"></li>');
		var pic = picObjs[0].src;
		var itemDiv = $('<div class="item active " style="height:100%"><a  href="##"><img style="display:block; margin:0 auto;max-height:100%" src="'+pic+'" alt="" ></a></div>');
		$("#carouselinner").append(itemDiv);
		for(var i=1;i<picObjs.length;i++){
			//alert(picObjs[i].src);
			pic = picObjs[i].src;
			var liObj = $('<li data-target="#slidershow" data-slide-to="'+i+'"></li>');
			$("#carouselIndicators").append(liObj);
			
			itemDiv = $('<div class="item text-center" style="height:100%"><a href="##"><img style="display:block; margin:0 auto;max-height:100%" src="'+pic+'" alt="" ></a></div>');
			$("#carouselinner").append(itemDiv);
		}
	}
</script>