<%
	// Need pre-JSP 1.2 style redirect because AllWidgets Test runs under Tomcat 4
	
	String strURL = request.getContextPath() + "/indexFaces.jsf";
	
	if ( request.getQueryString() != null )
		strURL += '?' + request.getQueryString();
		
	response.sendRedirect( strURL );
%>