/**
 * 
 */
var indexArray = new Array("0", "0", "0", "0","0","0","0","0","0");
function previewArticleImage(file) {
	//var image = new Image();
    ///image.src = "/home/stark/FileStore/temp/8.jpg";
    //var naturalWidth = image.width;
	for (var i = 0; i < indexArray.length; i++) {
		if (indexArray[i] == "0") {
			indexArray[i] = i + 3;
			$("#addArticlePicture" + i).css({
				display : "none"
			});
			previewImage(file, i + 3);
			break;
		}
	}

	for (var i = 0; i < indexArray.length; i++) {
		if (indexArray[i] == "0") {
			$("#addArticlePicture" + i).css({
				display : "block"
			});
			break;
		} else {
			$("#addArticlePicture" + i).css({
				display : "none"
			});
		}
		if (i == indexArray.length - 1) {
			$("#disablesArticlePicture").css({
				display : "block"
			});
		}
	}
	
}

function resetArticlePicture(){
	for (var i = 0; i < indexArray.length; i++) {
		if (i== 0) {
			$("#addArticlePicture" + i).css({
				display : "block"
			});
		} else {
			$("#addArticlePicture" + i).css({
				display : "none"
			});
		}
		indexArray[i]="0";
	}
	$("#disablesArticlePicture").css({
		display : "none"
	});
	
	$("#articlePictureInput1").attr("name","picture1");
	$("#articlePictureInput2").attr("name","picture2");
	$("#articlePictureInput3").attr("name","picture3");
	$("#articlePictureInput4").attr("name","picture4");
	$("#articlePictureInput5").attr("name","picture5");
	$("#articlePictureInput6").attr("name","picture6");
	$("#articlePictureInput7").attr("name","picture7");
	$("#articlePictureInput8").attr("name","picture8");
	$("#articlePictureInput9").attr("name","picture9");
	
	$("#articlePictureInput1").attr("value","");
	$("#articlePictureInput2").attr("value","");
	$("#articlePictureInput3").attr("value","");
	$("#articlePictureInput4").attr("value","");
	$("#articlePictureInput5").attr("value","");
	$("#articlePictureInput6").attr("value","");
	$("#articlePictureInput7").attr("value","");
	$("#articlePictureInput8").attr("value","");
	$("#articlePictureInput9").attr("value","");
	
	 $("#preview3")[0].innerHTML = "";
	 $("#preview4")[0].innerHTML = "";
	 $("#preview5")[0].innerHTML = "";
	 $("#preview6")[0].innerHTML = "";
	 $("#preview7")[0].innerHTML = "";
	 $("#preview8")[0].innerHTML = "";
	 $("#preview9")[0].innerHTML = "";
	 $("#preview10")[0].innerHTML = "";
	 $("#preview11")[0].innerHTML = "";
	 $("#articleContent").val("");
	 
	 
}

function removePicture(t) {
	var j = t - 3;
	var div = document.getElementById('preview' + t);
	div.innerHTML = "";
	if (t < 3) {
		return;
	}
	indexArray[j] = "0";
	var m = j+1;
	$("#articlePictureInput"+m).attr("value","");
	$("#articlePictureInput"+m).attr("name","picture"+m);

	$("#addArticlePicture" + j).css({
		display : "block"
	});
	for (var i = 0; i < indexArray.length; i++) {
		if (i != j) {
			$("#addArticlePicture" + i).css({
				display : "none"
			});

		}
	}
	$("#disablesArticlePicture").css({
		display : "none"
	});
}

function previewImage(file, i) {
	var MAXWIDTH = 150;
	var MAXHEIGHT = 150;
	var div = document.getElementById('preview' + i);
	if (file.files && file.files[0]) {
		div.innerHTML = '<div class="expbtn " ><a href="javascript:removePicture('
				+ i
				+ ')" title="#"><span class="icon-times-circle float-right"></span></a><img id=imghead'
				+ i + '></div>';
		var img = document.getElementById('imghead' + i);
		
		img.onload = function() {
			
			var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth,
					img.offsetHeight);
			img.width = rect.width;
			img.height = rect.height;
			img.style.marginLeft = rect.left + 'px';
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

var jic = {
        /**
         * Receives an Image Object (can be JPG OR PNG) and returns a new Image Object compressed
         * @param {Image} source_img_obj The source Image Object
         * @param {Integer} quality The output quality of Image Object
         * @return {Image} result_image_obj The compressed Image Object
         */
 
        compress: function(source_img_obj, quality, output_format){
             
             var mime_type = "image/jpeg";
             if(output_format!=undefined && output_format=="png"){
                mime_type = "image/png";
             }
             
 
             var cvs = document.createElement('canvas');
             //naturalWidth真实图片的宽度
             cvs.width = source_img_obj.naturalWidth/2;
             cvs.height = source_img_obj.naturalHeight/2;
             var ctx = cvs.getContext("2d").drawImage(source_img_obj, 0, 0);
             var newImageData = cvs.toDataURL(mime_type, quality/100);
             var result_image_obj = new Image();
             result_image_obj.src = newImageData;
             return result_image_obj;
        },
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

function strToDate(str){
	var year=str.substr(0,4);
	var month = parseInt(str.substr(4,2));
	var day=str.substr(6,2);
	var hour = str.substr(8,2);
	var minute = str.substr(10,2);
	var second = str.substr(12,2);
	
	return new Date(year,month-1,day,hour,minute,second);
}

function calculateDate(date){
	var current = new Date();
//	var dateDiff = getDateDiff(date,current,"day");
//	var hourDiff = getDateDiff(date,current,"hour");
//	if(dateDiff==0&&hourDiff>0){
//		
//	}
//	if(dateDiff<2*24&&dateDiff>=24){
//		return "前天 "+date.format("hh:mm");
//	}
//	if(dateDiff==1){
//		return "昨天 "+date.format("hh:mm:ss");
//	}
//	if(dateDiff==0){
//		return "今天 "+date.format("hh:mm:ss");
//	}
	var yearDiff = current.getFullYear()-date.getFullYear();
	var monthDiff = current.getMonth()-date.getMonth();
	var dayDiff = current.getDate()-date.getDate();
	var hourDiff = current.getHours()-date.getHours();
	var minuteDiff = current.getMinutes()-date.getMinutes();
	var secondDiff = current.getSeconds()-date.getSeconds();
	
	if(yearDiff>0){
		return date.format("yyyy年MM月dd日 hh:mm");
	}
	if(monthDiff>0){
		return date.format("MM月dd日 hh:mm");
	}
	if(dayDiff>0){
		return date.format("MM月dd日 hh:mm");
	}
	if(hourDiff>0){
		return "今天 "+date.format("hh:mm");
	}
	if(minuteDiff>0){
		return minuteDiff+"分钟前";
	}
	
	return secondDiff+"秒前";
	
}

Date.prototype.format = function(fmt)   
{ //author: meizz   
  var o = {   
	"y+" : this.getFullYear(),
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}  

function getDateDiff(startTime, endTime, diffType) { 
    //将xxxx-xx-xx的时间格式，转换为 xxxx/xx/xx的格式  
    //startTime = startTime.replace(/\-/g, "/"); 
    //endTime = endTime.replace(/\-/g, "/"); 

    //将计算间隔类性字符转换为小写 
    diffType = diffType.toLowerCase(); 
   // var sTime = new Date(startTime);      //开始时间 
    //var eTime = new Date(endTime);  //结束时间 
    //作为除数的数字 
    var divNum = 1; 
    switch (diffType) { 
        case "second": 
            divNum = 1000; 
            break; 
        case "minute": 
            divNum = 1000 * 60; 
            break; 
        case "hour": 
            divNum = 1000 * 3600; 
            break; 
        case "day": 
            divNum = 1000 * 3600 * 24; 
            break; 
        default: 
            break; 
    } 
    return parseInt((endTime.getTime() - startTime.getTime()) / parseInt(divNum)); 
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
	$("#commentListDiv").css({
        display : "block"
    });
	$("#userGroupDiv").css({
        display : "none"
    });
	$("#commentArticleId").val(articleId);
	$("#articleUserId").val(userId);
	$("#commentDiv").css({
        display : "block"
    });
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
                    +'<div class="float-left text-center">'
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
        	
        	$("#commentListDiv")[0].innerHTML=comPart;
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

var selecStatus = "";
var selectUserId = "";
function followUser(userId){
	var usergroup=document.getElementsByName("userGroupBox");
    for(var i=0;i<usergroup.length;i++){
    	usergroup[i].checked = false;
    }
	selectStatus = "follow";
	selectUserId = userId;
    $("#userGroupDiv").css({
        display : "block"
    });
    $("#commentDiv").css({
        display : "none"
    });
    
    
}
var selectArticleId = "";
function praiseArticle(articleId){
	var usergroup=document.getElementsByName("userGroupBox");
	for(var i=0;i<usergroup.length;i++){
        usergroup[i].checked = false;
    }
	
	selectStatus = "praise";
    selectArticleId = articleId;
    $("#userGroupDiv").css({
        display : "block"
    });
    $("#commentDiv").css({
        display : "none"
    });
    /*
    $.ajax({
        url : "/StarkPet/article/praise.do?userId="+${userId}+"&articleId="+articleId,
        type : "get",
        //data:JSON.stringify(chartlet),
        success : function(ret) {
            if (ret.result == "1") {
                //alert("ddd");
                var pObj = document.getElementById("praiseCount"+articleId);
                var count = pObj.innerHTML;
                pObj.innerHTML = parseInt(count)+1;
            }
        },
        dataType : "json",
        contentType : "application/json",
    });*/
}
function selectUserSubmit(){
	var usergroup=document.getElementsByName("userGroupBox");
	var users = [];
	
	for(var i=0;i<usergroup.length;i++){
		var user = usergroup[i];
        if(usergroup[i].checked==true){         
           users[users.length] = user.value;
          
        }
    }
	var objectId;
	var url = "";
	if(selectStatus == "praise"){
		url = "/StarkPet/article/addPraises.do";
		objectId = selectArticleId;
	}
	else if(selectStatus=="follow"){
		url = "/StarkPet/user/addFollows.do";
		objectId = selectUserId;
	}
	var user ={
            objectId:objectId,
            usersId:users,
        }
    $.ajax({
        url:url,
        type:"post",
        data:JSON.stringify(user),
        success:function(a){
        	if(a.result==1){
        		if(selectStatus == "praise"){
        			 var pObj = document.getElementById("praiseCount"+objectId);
                     var count = pObj.innerHTML;
                     pObj.innerHTML = parseInt(count)+a.count;
                }
                else if(selectStatus=="follow"){
                    alert("关注成功");
                }
        		$("#userGroupDiv").css({
                    display : "none"
                });
        	}
        },
        dataType: "json",
        contentType: "application/json",
    });
	
}

