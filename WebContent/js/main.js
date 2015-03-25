var maxResults = 20;
var isShowHeader = false;
var selectUserFlag = "";
var currentUser = new Object();
var currentArticle = new Object();
function selectUser(userId,userName,page){
	page = parseInt(page);
	if(page<0)
		return;
	$("#addOperationDiv").css({
		display : "none"
	});
	$("#articlesDiv").css({
		display : "block"
	});
	
	setReply();
	//$("#secondDiv").scrollTop = 0;
	currentUser.userId = userId;
	currentUser.name = userName;
	
	$("#articleUserId").val(userId);
    $("#articleUserName").val(userName);
    var url = "/StarkPet/article/getArticlesByUserId.do?userId="+ userId+"&";
	showArticleList(url,page);
}
function showArticleList(url,page){
	var div = document.getElementById('articleList'); 
	div.scrollTop = 0; 
	$.ajax({
		url : url + "page="+page,
		type : "post",
		//data:"userId="+id+"",
		success : function(result) {
			if(result.result==0){
				$("#articleListDiv")[0].innerHTML = "";
				$("#paginationDiv").css({
                    display : "none"
                });
				return;
			}
			var list = result.articles;
			if (list.length < 1) {
				$("#articleListDiv")[0].innerHTML = "";
				$("#paginationDiv").css({
                    display : "none"
                });
				return;
			}
			var articles = "";
			$("#articleListDiv")[0].innerHTML = "";
			for (var i = 0; i < list.length; i++) {
				var art = list[i];
				var mediaDiv = createMediaDiv(art);
				$("#articleListDiv").append(mediaDiv);
			}
		    if(list.length==maxResults||page>0){
		    	var ination = createInation(url,list,page);
		    	$("#paginationDiv")[0].innerHTML=ination;
		    }
		    else{
		    	$("#paginationDiv").css({
                    display : "none"
                });
		    }
		},
		dataType : "json",
		contentType : "application/json",
	});
}

function createInation(url,list,page){
	var nextPage = page+1;
	var prePage = page-1;
	var ination = '<ul class="pager">';
	url = "'"+url+"'";
	if(page!=0){
		ination += '<li><a href="javascript:showArticleList('+url+','+prePage+')">上一页</a></li>';
	}else {
		ination += '<li><a disabled="false" href="javascript:void(0);">上一页</a></li>';
	}
	if(list.length==maxResults){
		ination += '<li><a href="javascript:showArticleList('+url+','+nextPage+')">下一页</a></li></ul>';
	}else {
		ination += '<li><a disabled="false" href="javascript:void(0);">下一页</a></li></ul>';
	}
	
	return ination;
}

function createMediaDiv(art){
	
	var mediaDiv = $('<div id="mediaMainId'+art.articleId+'" class="media bg-white radius" ></div>');
	var mediaHeader = $('<div class="media-left text-center padding-left"></div>');
	mediaHeader.append('<a href="javascript:showUser('+art.userId+')"> <img class="media-object margin-top img-circle" style="height:50px;width:50px" src="'+art.headPic+'" alt="..."> </a>');
	var roleStr = art.userRole;
	mediaHeader.append('<div class="padding-top center-block"><a href="javascript:followUser('+art.userId+')" ><span class="glyphicon glyphicon-check text-large text-green"></span></a></div>');
	if(isShowHeader){
		mediaDiv.append(mediaHeader);
	}
	
	var mediaBody = $('<div class="media-body padding-left padding-top"></div>');
	if(roleStr=="普通用户"||roleStr=="运营"){
		mediaBody.append('<h4 class="media-heading"><strong>'+ art.name+'</strong><small> ('+roleStr+') <a href="#"><span class="glyphicon glyphicon-tag"></span></a></small></h4>');
	}
	else{
		mediaBody.append('<h4 class="media-heading"><strong>'+ art.name+'</strong><small> ('+roleStr+')</small></h4>');
	}
	var title = art.title;
	
	if(title!=""){
		mediaBody.append('<a href="#"><p class="text-big text-muted">标题: '+title+'</p></a>');
	}
	mediaBody.append('<p>'+art.content+'</p>');
	mediaDiv.append(mediaBody);
	
	var picList = art.pictures;
	var mediaPic = $('<div class=" padding-small-bottom">');
	for (var j = 0; j < picList.length; j++) {
		var picstr = "'"+picList[j]+"'";
		mediaPic.append('<a href="javascript:viewPicture('+art.articleId+')"><img src="'+picList[j]+'" width="100px" height="100px" class="img-border radius-small" name="sliderPicture'+art.articleId+'" /> </a>');
	}
	mediaBody.append(mediaPic);
	
	var date = strToDate(art.date);
	var strDate = calculateDate(date);
	
	var mediaDate = $('<div class=" text-small text-muted">'+ strDate +'  来自: '+art.reference+ '</div>');
	mediaBody.append(mediaDate);
	var mediaOper = $('<div class="btn-group btn-group-justified " role="group" aria-label="..."></div>');
	var mediaOperExt = $('<div class="btn-group" role="button"></div>');
	mediaOper.append('<div class="btn-group" role="group"><a href="javascript:getComments('+art.articleId+','+art.userId+')" class="btn btn-default">评论 <span class="badge bg-white-light" id="commentCount'+art.articleId+'">'+art.commentCount+'</span></a></div>');
	mediaOper.append('<div class="btn-group" role="group"><a href="javascript:praiseArticle('+art.articleId+')" class="btn btn-default">赞 <span class="badge bg-white-light" id="praiseCount'+art.articleId+'">'+art.praiseCount+'</span></a></div>');
	mediaOper.append('<div class="btn-group" role="group"><a  class="btn btn-default">收藏 <span class="badge bg-white-light" >'+art.collectionCount+'</span></a></div>');
	mediaOper.append('<div class="btn-group" role="group"><a href="javascript:browseArticle('+art.articleId+','+art.browseCount+')" class="btn btn-default">浏览 <span class="badge bg-white-light" id="browseCount'+art.articleId+'">'+art.browseCount+'</span></a></div>');
	 var type=art.type;
     //var typeStr = typeChangeStr(type);
	 var typeStr = art.typeStr;
	
	mediaOper.append('<div class="btn-group" role="button"><a href="javascript:changeArticleType('+art.articleId+','+art.type+')" class="btn btn-default" id="articleType'+art.articleId+'">'+typeStr+' </a></div>');
	mediaOper.append('<div class="btn-group" role="button"><a href="javascript:deleteArticle('+art.articleId+')" class="btn btn-default">删除 </a></div>');
	mediaDiv.append(mediaOper);
	
	return mediaDiv;
}

function getUserRole(role){
	//var  = art.userRole;
    var roleStr;
    if(role==0){
    	roleStr="普通用户";
    }
    else if(role==1){
    	roleStr = "管理员";
    }
    else if(role==2){
    	roleStr = "运营人员";
    }
    else if(role==3){
    	roleStr = "模拟用户";
    }
    else if(role==6){
    	roleStr = "重要用户";
    }
    
    return roleStr;
}

function praiseArticle(articleId){
	currentArticle.Id = articleId;
	selectUserFlag = "praise";
	setMultiSelect();
	selectUserList();
}

function typeChangeStr(type){
	var typeStr = "";
    
    if(type=="0"){
        typeStr = "普通";
    }else if(type=="2"){
        typeStr = "推荐";
    }
    else if(type=="3"){
        typeStr = "杂志";
    }
    else if(type=="4"){
        typeStr = "精选杂志";
    }
    else if(type=="5"){
        typeStr = "举报";
    }
    else if(type=="6"){
        typeStr = "推荐被举报";
    }
    else if(type=="7"){
        typeStr = "杂志被举报";
    }
    else if(type=="8"){
        typeStr = "精选杂志被举报";
    }
    else if(type=="9"){
        typeStr = "未审核活动推文";
    }
    else if(type=="10"){
        typeStr = "活动推文";
    }
    else if(type=="11"){
        typeStr = "已删除";
    }
    else if(type=="12"){
    	typeStr="推荐未审核";
    }
    else if(type=="13"){
    	typeStr="普通";
    }
    else if(type="14"){
    	typeStr="节目单";
    }
    else if(type="15"){
    	typeStr="普通未审核";
    }
    return typeStr;
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

function selectUserSubmit(){
	
	var objectId;
	var url = "";
	if(selectUserFlag == "praise"){
		url = "/StarkPet/article/addPraises.do";
		objectId = currentArticle.Id;
	}
	else if(selectUserFlag=="follow"){
		url = "/StarkPet/user/addFollows.do";
		objectId = currentUser.userId;
	}
	var user ={
            objectId:objectId,
            usersId:selectUserId,
        }
    $.ajax({
        url:url,
        type:"post",
        data:JSON.stringify(user),
        success:function(a){
        	if(a.result==1){
        		if(selectUserFlag == "praise"){
        			 var pObj = document.getElementById("praiseCount"+objectId);
                     var count = pObj.innerHTML;
                     pObj.innerHTML = parseInt(count)+a.count;
                }
                else if(selectUserFlag=="follow"){
                    alert("关注成功");
                }
        	}
        	else {
        		alert("失败了");
        	}
        },
        dataType: "json",
        contentType: "application/json",
    });
	
}

function changeArticleType(articleId,type){
	if(type==2){
		return;
	}
	currentArticle.Id = articleId;
	showAuditingDialog("精选推文通过审核");
}

function ajaxRequest(url,handle){
	 $.ajax({
         url : url,
         type : "get",
         //data:JSON.stringify(chartlet),
         success : function(data){    
        	 handle(data);
         },
         dataType : "json",
         contentType : "application/json",
     });
}

function browseArticle(articleId,count){
	currentArticle.Id = articleId;
	showBrowseDialog(count);
}
function browseBack(count){
	var articleId = currentArticle.Id;
	var url = "/StarkPet/article/setBrowseCount.do?articleId="+articleId+"&count="+count;
	ajaxRequest(url,browseResponse);
}
function browseResponse(data){
	if(data.result==1){
		 var pObj = document.getElementById("browseCount"+currentArticle.Id);
         pObj.innerHTML = data.count;
	}
}

function followUser(userId){
	currentUser.userId = userId;
	selectUserFlag = "follow";
	setMultiSelect();
	selectUserList();
}

function showUser(userId){
	window.location.href="showUser.do?userId="+userId; 
}

function deleteArticle(articleId){
	currentArticle.Id = articleId;
	var url = "/StarkPet/article/changeArticleType.do?articleId="+articleId+"&type="+11;
	ajaxRequest(url,deleteResponse);
}

function deleteResponse(data){
	if(data.result==1){
		$("#mediaMainId"+currentArticle.Id).remove();
	}
	else{
		alert("失败了!!!");
	}
}
