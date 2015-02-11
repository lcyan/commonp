<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<script type="text/javascript" src="${ctx}/jquery/jquery-2.1.1.js"></script>
<script type="text/javascript" src="${ctx}/org/cometd.js"></script>
<script type="text/javascript" src="${ctx}/jquery/jquery.cometd.js"></script>
<script type="text/javascript" src="application.js"></script>
<%--
    The reason to use a JSP is that it is very easy to obtain server-side configuration
    information (such as the contextPath) and pass it to the JavaScript environment on the client.
    --%>
<script type="text/javascript">
	var config = {
		contextPath : '${ctx}'
	};
</script>
</head>
<body>

	<div id="body"></div>

</body>
</html>
