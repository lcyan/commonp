<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<title>QuickStart示例:<sitemesh:title/></title>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link type="image/x-icon" href="${ctx}/static/images/favicon.ico" rel="shortcut icon">
	<link href="${ctx}/static/bootstrap/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<link href="${ctx}/static/font-awesome/css/font-awesome.min.css" type="text/css" rel="stylesheet" />
	<link href="${ctx}/static/styles/default.css" type="text/css" rel="stylesheet" />
	<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
	<!--[if lt IE 9]>
	  <script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
	  <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
	<![endif]-->
	<script type="text/javascript">
		window.COMMONP = {ctx: '${ctx}'};
	</script>
	<script type="text/javascript" src="${ctx}/static/js/require.js"></script>
	<script type="text/javascript">
		require.config({
			baseUrl: COMMONP.ctx + '/static/js',
			paths: {
				jquery: 'jquery-1.9.1.min',
				'jquery.validate': 'validation/jquery.validate',
				'jquery.validate.min': 'validation/jquery.validate',
				validation_ext: 'validation/additional-methods.min',
				validation_i18n: 'validation/localization/messages_zh.min',
				bootstrap: 'bootstrap/bootstrap.min'
			},
			shim: {
				bootstrap: {
					deps:['jquery'],
					exports: 'bootstrap'
				}
			}
		});
	</script>
	<sitemesh:head/>
</head>

<body>
		<%@ include file="/WEB-INF/layouts/header.jsp"%>
		<div class="container">
			<sitemesh:body/>
		</div>
		<%@ include file="/WEB-INF/layouts/footer.jsp"%>
	</div>
</body>
</html>