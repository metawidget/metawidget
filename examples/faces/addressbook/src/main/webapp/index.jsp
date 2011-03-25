<%@ page language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m"%>

<f:loadBundle basename="org.metawidget.example.shared.addressbook.resource.Resources" var="bundle"/>
		
<f:view>

	<tags:page>

		<div id="page-image">
			<img src="media/addressbook.gif" alt=""/>
		</div>

		<div id="content">

			<h:form id="form">

				<h:messages />

				<m:metawidget value="#{contactSearch.current}">
					<f:facet name="footer">
						<m:metawidget value="#{contactSearch}" rendererType="simple"/>
					</f:facet>
				</m:metawidget>

			</h:form>

			<h:dataTable value="#{contactSearch.results}" var="_contact" styleClass="data-table" columnClasses="column-half, column-half, column-tiny" rowClasses="row-odd, row-even">

				<h:column>
					<f:facet name="header">
						<h:outputText value="Name"/>
					</f:facet>
					<h:outputLink value="contact.jsf">
						<f:param name="contact.load" value="#{_contact.id}"/>
						<h:outputText value="#{_contact.fullname}"/>
					</h:outputLink>
				</h:column>

				<h:column>
					<f:facet name="header">
						<h:outputText value="Contact"/>
					</f:facet>
					<h:outputText value="#{_contact.communications}"/>
				</h:column>

				<h:column>
					<f:facet name="header">
						<h:outputText value="&nbsp;" escape="false"/>
					</f:facet>
					<h:graphicImage value="media/personal-small.gif" rendered="#{_contact.class.simpleName == 'PersonalContact'}" alt="Personal Contact"/>
					<h:graphicImage value="media/business-small.gif" rendered="#{_contact.class.simpleName == 'BusinessContact'}" alt="Business Contact"/>
				</h:column>

			</h:dataTable>

		</div>

	</tags:page>

</f:view>
