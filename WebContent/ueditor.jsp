<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<input type="hidden" name="richText" id="richTextContent">
<script id="container" name="content" type="text/plain">
</script>
<script type="text/javascript" src="../ueditor/ueditor.config.js"></script>
<!-- 编辑器源码文件 -->
<script type="text/javascript" src="../ueditor/ueditor.all.js"></script>
<!-- 实例化编辑器 -->
<script type="text/javascript">
	var ue = UE.getEditor('container',{
			    toolbars: [
			        ['fullscreen', 'source', 'undo', 'redo','simpleupload','insertimage','insertvideo','music','bold', 'italic', 'underline', 
			         'fontborder','forecolor','strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 
			         'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 
			         'insertunorderedlist', 'selectall', 'cleardoc','snapscreen','link','scrawl']
			    ],
			    autoHeightEnabled: true,
			    autoFloatEnabled: true
	});
	function setRichTextContent(){
		var content = ue.getContent();
		
		var pre = '<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">'
			  + '<html><head>'
			  + '<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">'
			  + '<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">'
			  + '<title>'+$("#title").val()+'</title>'
			  + '<link rel="stylesheet" href="http://image.uha.so/Article/web/css/article.css">'
			  + '</head>'
			  + '<body>'
			  + '<div id="bodyMain" style="margin:auto;padding-left:5px;padding-right:5px;padding-bottom:70px;padding-top:15px;background-color:#fff">'
			  + '<article>';
		var foot = '</article></div></body></html>';
		content = pre + content + foot;
		$("#richTextContent").val(content);
	}
	
	function resetRichTextContent(){
		ue.execCommand('cleardoc');
	}
	
	function setUeditorContent(body){
		ue.setContent(body);
	}
</script>