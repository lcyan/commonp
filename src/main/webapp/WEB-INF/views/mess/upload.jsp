<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<title>上传文件示例</title>
	<script src="${ctx}/static/js/vendor/jquery.ui.widget.js"></script>
	<script src="${ctx}/static/js/jquery.iframe-transport.js"></script>
	<script src="${ctx}/static/js/jquery.fileupload.js"></script>
	<script src="${ctx}/static/js/jquery.fileupload-process.js"></script>
	<script src="${ctx}/static/js/myuploadfunction.js"></script>
</head>
<body>
  <div class="col-xs-12 text-center" style="min-height: 530px">
    <!--图片操作区结束--> 
    <!--上传表单开始-->
    <div id="upload_form">
      <input type="hidden" name="src_image" value="" id="src_image">
      <input type="hidden" name="x" value="0" id="x">
      <input type="hidden" name="y" value="0" id="y">
      <input type="hidden" name="h" value="0" id="h">
      <input type="hidden" name="w" value="0" id="w">
      <input type="hidden" name="sizeW" value="0" id="sizeW">
      <input type="hidden" name="quality" value="100" id="quality">
      <input type="hidden" name="sizeH" value="0" id="sizeH">
      <br>
      <br>
      <p align="center">
      	<span class="btn btn-bg btn-success fileinput-button">
      		<span><i class="glyphicon glyphicon-plus"></i> add files...</span>
        	<input id="fileupload" type="file" name="files[]" data-url="${ctx}/files/upload" multiple accept="image/*">
        </span>
        <button type="submit" class="btn btn-primary start">
            <i class="glyphicon glyphicon-upload"></i>
            <span>Start upload</span>
        </button>
        <button type="reset" class="btn btn-warning cancel">
             <i class="glyphicon glyphicon-ban-circle"></i>
             <span>Cancel upload</span>
         </button>
         <button type="button" class="btn btn-danger delete">
             <i class="glyphicon glyphicon-trash"></i>
             <span>Delete</span>
         </button>
        </p>
      <br/>
      <p align="center">
      	<div id="progress" class="progress">
        	<div class="progress-bar progress-bar-info progress-bar-striped"></div>
      	</div>
      </p>
      <p align="center">
      	<table id="uploaded-files" class="table">
			<tr>
				<th>File Name</th>
				<th>File Size</th>
				<th>File Type</th>
				<th>Download</th>
			</tr>
		</table>
      </p>
    </div>
  </div>
</body>
</html>
