<%@ page language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m"%>

<f:loadBundle basename="org.metawidget.example.shared.addressbook.resource.Resources" var="bundle"/>

<f:view>

	<tags:page>

		<h:panelGroup rendered="#{contact.current.class.simpleName == 'PersonalContact'}">
			<div id="page-image">
				<img src="media/personal.gif" alt=""/>
			</div>
			<div id="content">				
			<h1>Personal Contact</h1>
		</h:panelGroup>
		<h:panelGroup rendered="#{contact.current.class.simpleName == 'BusinessContact'}">
			<div id="page-image">
				<img src="media/business.gif" alt=""/>
			</div>
			<div id="content">				
			<h1>Business Contact</h1>
		</h:panelGroup>

		<h:form id="form">

			<h:messages globalOnly="true"/>

			<m:metawidget value="#{contact.current}" readOnly="#{contact.readOnly}">
				<f:param name="messageStyleClass" value="inline-error" />
				<f:param name="sectionStyleClass" value="section-heading" />

				<m:stub value="#{contact.current.communications}">
					<h:dataTable id="communications" value="#{contact.currentCommunications}" var="_communication" styleClass="data-table" columnClasses="column-half, column-half, column-tiny table-buttons" rowClasses="row-odd, row-even">

						<h:column>
							<f:facet name="header">
								<h:outputText value="Type"/>
							</f:facet>
							<h:outputText value="#{_communication.type}"/>
							<f:facet name="footer">
								<m:metawidget value="#{communication.current.type}" inspectFromParent="true" rendererType="simple" rendered="#{!contact.readOnly}"/>
							</f:facet>
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Value"/>
							</f:facet>							
							<h:outputText value="#{_communication.value}"/>
							<f:facet name="footer">
								<m:metawidget value="#{communication.current.value}" rendererType="simple" rendered="#{!contact.readOnly}"/>
							</f:facet>
						</h:column>

						<h:column headerClass="column-tiny" footerClass="column-tiny, table-buttons">
							<f:facet name="header">
								<h:outputText value="&nbsp;" escape="false"/>
							</f:facet>							
							<h:commandButton id="deleteCommunication" value="#{bundle.delete}" action="#{contact.deleteCommunication}" onclick="if ( !confirm( 'Are you sure you want to delete this communication?' )) return false" immediate="true" rendered="#{!contact.readOnly}"/>
							<f:facet name="footer">
								<h:commandButton value="#{bundle.add}" action="#{contact.addCommunication}" rendered="#{!contact.readOnly}"/>
							</f:facet>
						</h:column>

					</h:dataTable>
				</m:stub>

				<f:facet name="footer">
					<m:metawidget value="#{contact}" rendererType="simple"/>
				</f:facet>

			</m:metawidget>

		</h:form>

		</div>

	</tags:page>

</f:view>
