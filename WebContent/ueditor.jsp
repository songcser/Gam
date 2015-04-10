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
		$("#richTextContent").val(ue.getContent());
	}
	
	function resetRichTextContent(){
		ue.execCommand('cleardoc');
	}
	
	function setUeditorContent(body){
		ue.setContent(body);
	}
</script>