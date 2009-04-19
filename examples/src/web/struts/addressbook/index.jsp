<%@ page language="java" %>
<%@ page import="org.metawidget.example.shared.addressbook.model.*, org.metawidget.example.shared.addressbook.controller.*" %>

<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://metawidget.org/struts" prefix="m"%>
<%@ taglib uri="http://metawidget.org/example/struts/addressbook" prefix="a"%>

<%
	ContactSearch contactSearch = (ContactSearch) pageContext.findAttribute( "contactSearch" );
	
	pageContext.setAttribute( "contactResults", ((ContactsController) pageContext.findAttribute( "contacts" )).getAllByExample( contactSearch ));
%>

<tags:page>
	
	<div id="page-image">
		<img src="media/addressbook.gif">
	</div>

	<div id="content">
		
		<html:form action="/search">

			<m:metawidget property="contactSearchForm">
				<m:facet name="footer" styleClass="buttons">
					<html:submit property="search">
						<bean:message key="search"/>
 					</html:submit>
					<html:submit property="addPersonal">
						<bean:message key="addPersonal"/>
 					</html:submit>
					<html:submit property="addBusiness">
						<bean:message key="addBusiness"/>
 					</html:submit>
				</m:facet>
			</m:metawidget>

		</html:form>

		<table class="data-table">
			<thead>
				<tr>
					<th class="column-half">Name</th>
					<th class="column-half">Contact</th>
					<th class="column-tiny">&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${contactResults}" var="_contact">
					<tr>
						<td class="column-half">
							<a href="load.do?id=${_contact.id}">
								${_contact.fullname}
							</a>
						</td>
						<td class="column-half">${a:collectionToString(_contact.communications,", ")}</td>
						<td class="column-tiny">
							<c:choose>
								<c:when test="${_contact.class.simpleName == 'PersonalContact'}">
									<img src="media/personal-small.gif" alt="Personal Contact"/>
								</c:when>
								<c:otherwise>
									<img src="media/business-small.gif" alt="Business Contact"/>
								</c:otherwise>							
							</c:choose>
						</td>
					</tr>		
				</c:forEach>
			</tbody>
		</table>
		
	</div>
		
</tags:page>
