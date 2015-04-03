<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>用户管理</title>
</head>

<body>
	<form id="inputForm" action="${ctx}/admin/users/update" method="post" class="form-horizontal">
		<input type="hidden" name="id" value="${user.id}"/>
		<fieldset>
			<legend><small>用户管理</small></legend>
			<div class="form-group">
				<label class="col-sm-2 control-label">登录名:</label>
				<div class="col-sm-10">
					<input type="text" value="${user.loginName}" class="form-control" disabled="" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">用户名:</label>
				<div class="col-sm-10">
					<input type="text" id="name" name="name" value="${user.name}" class="form-control required"/>
				</div>
			</div>
			<div class="form-group">
				<label for="plainPassword" class="col-sm-2 control-label">密码:</label>
				<div class="col-sm-10">
					<input type="password" id="id_plainPwd" name="plainPwd" class="form-control" placeholder="...Leave it blank if no change"/>
				</div>
			</div>
			<div class="form-group">
				<label for="confirmPassword" class="col-sm-2 control-label">确认密码:</label>
				<div class="col-sm-10">
					<input type="password" id="confirmPassword" name="confirmPassword" class="form-control" equalTo="#id_plainPwd" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">注册日期:</label>
				<div class="col-sm-10">
					<span class="help-block" ><fmt:formatDate value="${user.createdTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒" /></span>
				</div>
			</div>
			<div class="form-group">
				<input id="submit_btn" class="btn btn-primary col-sm-offset-3 col-sm-3" type="submit" value="提交"/>&nbsp;	
				<input id="cancel_btn" class="btn col-sm-offset-1 col-sm-3" type="button" value="返回" onclick="history.back()"/>
			</div>
		</fieldset>
	</form>
	
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#name").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate();
		});
	</script>
</body>
</html>
