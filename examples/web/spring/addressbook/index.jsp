<%
	String strURL = request.getContextPath() + "/index.html";
	
	if ( request.getQueryString() != null )
		strURL += '?' + request.getQueryString();
		
	response.sendRedirect( strURL );
%>