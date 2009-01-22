<%@ tag language="java" %>
<%@ attribute name="title" %>
<%@ attribute name="useTooltips" %>
<%@ attribute name="useThumbailViewer" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="context" scope="request" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><c:if test="${!empty title}">${title} - </c:if>${pageContext.session.servletContext.servletContextName}</title>
		<meta name="description" content="Metawidget takes your domain objects and automatically creates, at runtime, native User Interface widgets for them - saving you handcoding your UIs" />
		<meta name="keywords" content="<c:if test="${!empty title}">${title},</c:if>Domain objects,automatically create User Interface,Swing,Java Server Faces,JSF,Facelets,Spring,Spring Web MVC,Struts,Android,Hibernate" />
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />		
		<link rel="stylesheet" type="text/css" href="${context}/css/metawidget-all.css" media="all" />		
		<link rel="stylesheet" type="text/css" href="${context}/css/metawidget-screen.css" media="screen" />		
		<link rel="stylesheet" type="text/css" href="${context}/css/metawidget-print.css" media="print" />
		<script src="http://www.google-analytics.com/ga.js" type="text/javascript"></script>
		<c:if test="${useTooltips}">
			<script src="${context}/js/prototype.js" type="text/javascript"></script>
			<script src="${context}/js/effects.js" type="text/javascript"></script>
			<script src="${context}/js/newsticker.js" type="text/javascript"></script>
			<script src="${context}/js/tooltip-v0.2.js" type="text/javascript"></script>
		</c:if>
		<c:if test="${useThumbailViewer}">
			<link rel="stylesheet" type="text/css" href="${context}/css/lightbox.css" />
			<script type="text/javascript" src="${context}/js/lightbox.js"></script>
		</c:if>
	</head>	
		
	<body>
		<jsp:doBody />
		
		<%-- Google Analytics --%>
		
		<script type="text/javascript">
			try {
				var pageTracker = _gat._getTracker("UA-6471965-1");
				pageTracker._setDomainName( "none" );
				pageTracker._setAllowLinker( true );
				pageTracker._trackPageview();
			} catch(e) {}
		</script>
      		
	</body>
		
</html>
