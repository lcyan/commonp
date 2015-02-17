<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>用户管理</title>
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<div class="row">
		<div class="span4 offset7">
			<form class="form-search" action="#">
				<label>名称：</label> <input type="text" name="search_LIKE_email" class="input-medium" value="${param.search_LIKE_email}">
				<button type="submit" class="btn" id="search_btn">Search</button>
		    </form>
	    </div>
	    <tags:sort/>
	</div>
	<table id="contentTable" class="table table-striped table-hover">
		<thead><tr><th>登录名</th><th>用户名</th><th>邮箱</th><th>注册时间<th>管理</th></tr></thead>
		<tbody>
		<c:forEach items="${users.content}" var="user">
			<tr>
				<td><a href="${ctx}/admin/users/update/${user.id}">${user.loginName}</a></td>
				<td>${user.nickName}</td>
				<td>${user.email}</td>
				<td>
					<fmt:formatDate value="${user.createdTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒" />
				</td>
				<td><a href="${ctx}/admin/users/delete/${user.id}">删除</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<tags:pagination paginationSize="5" page="${users}" />
</body>
</html>
