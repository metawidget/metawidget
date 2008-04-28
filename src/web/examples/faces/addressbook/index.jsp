<%
	String strURL = request.getContextPath() + "/indexFaces.jsf";
	
	if ( request.getQueryString() != null )
		strURL += '?' + request.getQueryString();
		
	response.sendRedirect( strURL );
%>