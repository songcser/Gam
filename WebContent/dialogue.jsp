<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="block-inline">
	<input type="text" class="input radius-rounded" id="dialogueChartletTitle" />
	<button class="btn bg-sub margin-left" onclick="createDialogueChartlet()">创建台词系列</button>
</div>
<div role="tabpanel" class="padding-big-top">
	<ul id="dialogueUL" class="nav nav-tabs" role="tablist">
		<c:forEach items="${chartlets }" var="o" varStatus="s">
			<c:if test="${o.type=='3'}">
				<li role="presentation" id="chartletli${o.chartletId }">
				<a href="#dialogue${o.chartletId }" aria-controls="dialogue${o.chartletId }" role="tab" data-toggle="tab" title="${o.chartletId }" name="${o.type }" >${o.title }</a>
				</li>
			</c:if>
		</c:forEach>
		<c:forEach items="${chartlets }" var="o" varStatus="s">
            <c:if test="${o.type=='4'}">
                <li role="presentation">
                <a href="#dialogue${o.chartletId }" aria-controls="dialogue${o.chartletId }" role="tab" data-toggle="tab" title="${o.chartletId }" name="${o.type }">${o.title }</a>
                </li>
            </c:if>
        </c:forEach>
	</ul>
	<div id="dialogueTabContent" class="tab-content">
	   <div class="row" id="editDialogueDiv">
	    <div class="col-lg-6 ">
		<div id="" class="input-group margin-top" >
			<input type="text" id="dialogueNumber" class="form-control" style="width: 10%" placeholder="编号"> 
			<input type="text" id="dialogueContent" class="form-control" style="width: 90%" placeholder="输入台词"> 
			<span class="input-group-btn">
				<button class="btn btn-info margin-right" type="button" onclick="addDialogue()">Go!</button>
			</span>
			<span class="input-group-btn ">
                <button class="btn btn-danger " type="button" onclick="deleteDialogueChartlet()">删除</button>
            </span>
		</div>
		</div>
		</div>
		<c:forEach items="${chartlets }" var="o" varStatus="s">
			<c:if test="${o.type=='3'}">
				<div role="tabpanel" class="tab-pane margin-big-top" id="dialogue${o.chartletId }">
				    <div class="row">
				        <div class="col-lg-6 col-md-10 " id="dialogueList${o.chartletId }"></div>
				    </div>
				</div>
			</c:if>
		</c:forEach>
		<c:forEach items="${chartlets }" var="o" varStatus="s">
            <c:if test="${o.type=='4'}">
                <div role="tabpanel" class="tab-pane margin-big-top" id="dialogue${o.chartletId }">
                    <div class="row">
                        <div class="col-lg-6 col-md-10" id="dialogueList${o.chartletId }"></div>
                    </div>
                </div>
            </c:if>
        </c:forEach>
	</div>
</div>
<script type="text/javascript">
	function createDialogueChartlet() {
		var type = 3;
		chartletType = "dialogueChartletList";
		var title = $("#dialogueChartletTitle").val();
		if (title == "") {
			alert("请输入标签")
			return;
		}
		var chartlet = {
			type : type,
			title : title
		}
		var url = "/StarkPet/article/createChartlet.do";
		jsonAjax(url, chartlet, createDialogueBack)
	}
	function createDialogueBack(ret){
		if(ret.result=="1"){
			var cId = ret.chartletId;
            var title = ret.title;
			var liObj = $('<li role="presentation"> </li>');
			var aObj = '<a href="#'+cId+'" aria-controls="dialogue'+cId+'" role="tab" data-toggle="tab" name="'+cId+'">'+title+'</a>';
			$(liObj).append(aObj);
			$("#dialogueUL").prepend(liObj);
			var divObj = '<div role="tabpanel" class="tab-pane margin-top" id="dialogue'+cId+'"> <div class="row">'
			    +'<div class="col-lg-6 col-md-10" id="dialogueList'+cId+'"></div></div></div>';
			$("#dialogueTabContent").prepend(divObj);
			
			$(aObj).on('shown.bs.tab', function (e) {
	              getDialogueList(e.target);
	        });
		}
	}

	function getDialogueList(obj) {
		var type = obj.name;
		if(type=="4"){
			$("#editDialogueDiv").css({ display : "none" });
		}
		else {
			$("#editDialogueDiv").css({ display : "" });
		}
		chartletId = obj.title;
		var url = "/StarkPet/article/getDialogueListByChartlet.do?chartletId="+chartletId;
		ajaxRequest(url,dialogueBack);
	}
	var colorList = ["list-group-item-success","list-group-item-info","list-group-item-warning","list-group-item-danger"];
	function dialogueBack(ret){
		if(ret.result=="1"){
			if(ret.chartletType=="3"){
				showDialogueList(ret);
			}
			else if(ret.chartletType=="4"){
				showUserDialogueList(ret);
			}
		}
	}
	
	function showUserDialogueList(ret){
		var list = ret.dialogues;
        if(list.length<1){
        	$("#dialogueList"+chartletId).html("没有台词");
            return;
        }
        $("#dialogueList"+chartletId).html("");
        for(var i=0;i<list.length;i++){
        	var dia = list[i];
        	var color = colorList[i%colorList.length];
        	var date = strToDate(dia.date);
            var strDate = calculateDate(date);
        	var mediaDiv = $('<div class="media padding-top padding-small-bottom radius-rounded '+color+'"></div>');
            var mediaHead = $('<div class="media-left"><a href="javascript:showUser('+dia.userId+')"><img class="media-object img-circle" style="width:50px;height:50px" src="'+dia.headPic+'" alt="..."></a></div>');
            var mediaBody = $('<div class="media-body"><p class="text-small text-muted">'+strDate+'</p><p>'+dia.content+'</p></div>');
            mediaDiv.append(mediaHead);
            mediaDiv.append(mediaBody);
            $("#dialogueList"+chartletId).append(mediaDiv);
        }
	}
	
	function showDialogueList(ret){
		var list = ret.dialogues;
        if(list.length<1){
            return;
        }
        $("#dialogueList"+chartletId).html("");
        var ulObj = $('<ul class="list-group" id="dialogueulList'+chartletId+'"></ul>');
        var num = list[0].number;
        var index =0;
        for(var i=0;i<list.length;i++){
            var dia = list[i];
            if(num!=dia.number){
                index++;    
            }
            var color = colorList[index%colorList.length];
            $(ulObj).append('<li class="list-group-item '+color+'" id="dialogueli'+dia.id+'"><span class="badgeexp margin-right">'+dia.number+'</span>'
                    +dia.content+'<a href="javascript:deleteDialogue('+dia.id+')"><span class="pull-right" aria-hidden="true">&times;</span></a></li>');
            num=dia.number;
        }
        $("#dialogueList"+chartletId).append(ulObj);
	}

	function addDialogue() {
		var number = $("#dialogueNumber").val();
		var content = $("#dialogueContent").val();
		if (number == "") {
			alert("请输入编号");
			return;
		}
		if (content == "") {
			alert("请输入内容");
			return;
		}
		var dialogue = {
				chartlet:{chartletId:chartletId},
				user:{
		              userId:1,
		        },
		        content:content,
		        number:number,
		} 
		var url = "/StarkPet/article/addDialogue.do";
		jsonAjax(url, dialogue,dialogueResponse);
	}
	function dialogueResponse(ret) {
		var number = $("#dialogueNumber").val();
        var content = $("#dialogueContent").val();
	    if(ret.result=="1"){
	    	$("#dialogueulList"+chartletId).append('<li class="list-group-item "><span class="badgeexp">'+number+'</span>&nbsp;&nbsp;&nbsp;'+content+'</li>');
	    }
	    $("#dialogueNumber").val("");
	    $("#dialogueContent").val("");
	}
	var dialogueId = 0;
	function deleteDialogue(id){
		dialogueId=id;
		var url = "/StarkPet/article/deleteDialogue.do?dialogueId="+id;
		ajaxRequest(url,deleteDialogueResponse);
	}
	function deleteDialogueResponse(ret){
		if(ret.result==1){
			$("#dialogueli"+dialogueId).remove();
		}
	}
	
	function deleteDialogueChartlet(){
		var url = "/StarkPet/article/deleteChartlet.do?chartletId="+chartletId;
        ajaxRequest(url,deleteChartletBack);
	}
	function deleteChartletBack(ret){
		if(ret.result=="1"){
			$("#dialogue"+chartletId).remove();
			$("#chartletli"+chartletId).remove();
		}
	}
</script>