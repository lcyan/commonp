<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>登录页</title>
</head>

<body>
	<form id="loginForm" action="${ctx}/login" method="post" class="form-horizontal" style="min-height:350px; margin-top: 170px;">
		<%
		String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		if(error != null){
		%>
			<div class="alert alert-error input-medium controls">
				<button class="close" data-dismiss="alert">×</button>登录失败，请重试.
			</div>
		<%
		}
		%>
		<div class="form-group">
			<label for="username" class="col-sm-offset-2 col-sm-2 control-label">名称:</label>
			<div class="col-sm-4">
				<input type="text" id="username" name="username"  value="admin" class="form-control required"/>
			</div>
		</div>
		<div class="form-group">
			<label for="password" class="col-sm-offset-2 col-sm-2 control-label">密码:</label>
			<div class="col-sm-4">
				<input type="password" id="password" name="password" value="admin" class="form-control required"/>
			</div>
		</div>
				
		<div class="form-group">
			<div class="col-sm-offset-4 col-sm-4">
				<input id="submit_btn" class="btn btn-primary btn-block" type="submit" value="登录"/> 
			</div>
		</div>
	</form>
</body>
</html>
