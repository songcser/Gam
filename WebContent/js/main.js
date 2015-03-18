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

	$("#createArticleDiv").css({
		display : "block"
	});
	$("#commentListDiv").css({
        display : "none"
    });
	$("#noticeListDiv").css({
        display : "none"
    });
	
	//$("#secondDiv").scrollTop = 0;
	var div = document.getElementById('articleList'); 
	div.scrollTop = 0; 
	
	$("#articleUserId").val(userId);
    $("#articleUserName").val(userName);
    var url = "/StarkPet/article/getArticlesByUserId.do?userId="+ userId;
	showArticleList(url,page);
}

function showArticleList(url,page){
	$.ajax({
		url : url + "&page="+page,
		type : "post",
		//data:"userId="+id+"",
		success : function(list) {
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
		    if(list.length==10||page>0){
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
	if(list.length==10){
		ination += '<li><a href="javascript:showArticleList('+url+','+nextPage+')">下一页</a></li></ul>';
	}else {
		ination += '<li><a disabled="false" href="javascript:void(0);">下一页</a></li></ul>';
	}
	
	return ination;
}

function createMediaDiv(art){
	
	var mediaDiv = $('<div class="media bg-white radius" ></div>');
	var mediaHeader = $('<div class="media-left"></div>');
	var mediaBody = $('<div class="media-body padding-left padding-top"></div>');
	mediaBody.append('<h4 class="media-heading">'+ art.userName+'</h4>');
	mediaBody.append('<p>'+art.content+'</p>');
	mediaDiv.append(mediaBody);
	mediaHeader.append('<a href="#"> <img class="media-object" src="" alt="..."> </a>');
	var picList = art.pictures;
	var mediaPic = $('<div class="padding-left padding-small-bottom">');
	for (var j = 0; j < picList.length; j++) {
		var picstr = "'"+picList[j]+"'";
		mediaPic.append('<a href="javascript:viewPicture('+picstr+')"><img src="'+picList[j]+'" width="100px" height="100px" class="img-border radius-small" alt="..." /> </a>');
	}
	mediaDiv.append(mediaPic);
	
	var date = strToDate(art.date);
	var strDate = calculateDate(date);
	
	var mediaDate = $('<div class="margin-left text-small text-muted">'+ strDate +'  来自: '+art.reference+ '</div>');
	mediaDiv.append(mediaDate);
	var mediaOper = $('<div class="btn-group btn-group-justified border" role="group"></div>');
	mediaOper.append('<a href="javascript:getComments('+art.articleId+','+art.userId+')" class="btn">评论 <span class="badge bg-white-light" id="commentCount'+art.articleId+'">'+art.commentCount+'</span></a>');
	mediaOper.append('<a href="javascript:praiseArticle('+art.articleId+')" class="btn">赞<span class="badge bg-white-light" id="praiseCount'+art.articleId+'">'+art.praiseCount+'</span></a>');
	
	 var type=art.type;
     var typeStr = typeChangeStr(type);
	
	mediaOper.append('<a href="javascript:changeArticleType('+art.articleId+')" class="btn" id="articleType'+art.articleId+'">'+typeStr+' </a>');
	mediaOper.append('<a href="javascript:deleteArticle('+art.articleId+')" class="btn">删除 </a>');
	mediaDiv.append(mediaOper);
	
	return mediaDiv;
}

function typeChangeStr(type){
	var typeStr = "";
    
    if(type=="0"){
        typeStr = "普通";
    }else if(type=="2"){
        typeStr = "每日精选";
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
        typeStr = "每日精选被举报";
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
    
    return typeStr;
}