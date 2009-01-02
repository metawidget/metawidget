<%@ page language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://metawidget.org/example/gwt/addressbook" prefix="a" %>

<tags:page>

	<script type="text/javascript" src="org.metawidget.example.GwtAddressBook.nocache.js"></script>
	
	<fmt:setBundle basename="org.metawidget.example.shared.addressbook.resource.Resources" var="localizationContext"/>	
	<a:bundle2Variable bundle="${localizationContext.resourceBundle}" variableName="bundle"/>
	
	<div id="page-image">
		<img src="media/addressbook.gif">
	</div>

	<div id="content">
	
		<div id="metawidget"></div>		
		<div id="contacts"></div>
		
	</div>
		
</tags:page>
