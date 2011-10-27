package org.metawidget.statically.faces.component.html;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.metawidget.integrationtest.shared.allwidgets.model.AllWidgets;

public class StaticHtmAllWidgetsTest
	extends TestCase {

	//
	// Public methods
	//

	public void testAllWidgets() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setConfig( "org/metawidget/integrationtest/static/faces/allwidgets/metawidget.xml" );
		metawidget.setValueExpression( "value", "#{allWidgets}" );
		metawidget.setPath( AllWidgets.class.getName() );

		StringWriter writer = new StringWriter();
		metawidget.write( writer );

		String result = "<h:panelGrid columns=\"2\">\r\n" +
				"\t<h:outputLabel for=\"allWidgetsTextbox\" id=\"allWidgetsTextbox-label\" value=\"Textbox:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsTextbox\" required=\"true\" value=\"#{allWidgets.textbox}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsLimitedTextbox\" id=\"allWidgetsLimitedTextbox-label\" value=\"Limited textbox:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsLimitedTextbox\" maxLength=\"20\" value=\"#{allWidgets.limitedTextbox}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsTextarea\" id=\"allWidgetsTextarea-label\" value=\"Textarea:\"/>\r\n" +
				"\t<h:inputTextarea id=\"allWidgetsTextarea\" value=\"#{allWidgets.textarea}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsPassword\" id=\"allWidgetsPassword-label\" value=\"Password:\"/>\r\n" +
				"\t<h:inputSecret id=\"allWidgetsPassword\" value=\"#{allWidgets.password}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsBytePrimitive\" id=\"allWidgetsBytePrimitive-label\" value=\"Byte primitive:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsBytePrimitive\" value=\"#{allWidgets.bytePrimitive}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsByteObject\" id=\"allWidgetsByteObject-label\" value=\"Byte object:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsByteObject\" value=\"#{allWidgets.byteObject}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsShortPrimitive\" id=\"allWidgetsShortPrimitive-label\" value=\"Short primitive:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsShortPrimitive\" value=\"#{allWidgets.shortPrimitive}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsShortObject\" id=\"allWidgetsShortObject-label\" value=\"Short object:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsShortObject\" value=\"#{allWidgets.shortObject}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsIntPrimitive\" id=\"allWidgetsIntPrimitive-label\" value=\"Int primitive:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsIntPrimitive\" value=\"#{allWidgets.intPrimitive}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsIntegerObject\" id=\"allWidgetsIntegerObject-label\" value=\"Integer object:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsIntegerObject\" value=\"#{allWidgets.integerObject}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsRangedInt\" id=\"allWidgetsRangedInt-label\" value=\"Ranged int:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsRangedInt\" value=\"#{allWidgets.rangedInt}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsRangedInteger\" id=\"allWidgetsRangedInteger-label\" value=\"Ranged integer:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsRangedInteger\" value=\"#{allWidgets.rangedInteger}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsLongPrimitive\" id=\"allWidgetsLongPrimitive-label\" value=\"Long primitive:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsLongPrimitive\" value=\"#{allWidgets.longPrimitive}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsLongObject\" id=\"allWidgetsLongObject-label\" value=\":\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsLongObject\" value=\"#{allWidgets.longObject}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsFloatPrimitive\" id=\"allWidgetsFloatPrimitive-label\" value=\"Float primitive:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsFloatPrimitive\" value=\"#{allWidgets.floatPrimitive}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsFloatObject\" id=\"allWidgetsFloatObject-label\" value=\"nullInBundle:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsFloatObject\" value=\"#{allWidgets.floatObject}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsDoublePrimitive\" id=\"allWidgetsDoublePrimitive-label\" value=\"Double primitive:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsDoublePrimitive\" value=\"#{allWidgets.doublePrimitive}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsDoubleObject\" id=\"allWidgetsDoubleObject-label\" value=\"null:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsDoubleObject\" value=\"#{allWidgets.doubleObject}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsCharPrimitive\" id=\"allWidgetsCharPrimitive-label\" value=\"Char primitive:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsCharPrimitive\" value=\"#{allWidgets.charPrimitive}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsCharacterObject\" id=\"allWidgetsCharacterObject-label\" value=\"Character object:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsCharacterObject\" maxLength=\"1\" value=\"#{allWidgets.characterObject}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsBooleanPrimitive\" id=\"allWidgetsBooleanPrimitive-label\" value=\"Boolean primitive:\"/>\r\n" +
				"\t<h:selectBooleanCheckbox id=\"allWidgetsBooleanPrimitive\" value=\"#{allWidgets.booleanPrimitive}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsBooleanObject\" id=\"allWidgetsBooleanObject-label\" value=\"Boolean object:\"/>\r\n" +
				"\t<h:selectOneMenu id=\"allWidgetsBooleanObject\" value=\"#{allWidgets.booleanObject}\">\r\n" +
				"\t\t<f:selectItem/>\r\n" +
				"\t\t<f:selectItem itemLabel=\"Yes\" itemValue=\"true\"/>\r\n" +
				"\t\t<f:selectItem itemLabel=\"No\" itemValue=\"false\"/>\r\n" +
				"\t</h:selectOneMenu>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsDropdown\" id=\"allWidgetsDropdown-label\" value=\"Dropdown:\"/>\r\n" +
				"\t<h:selectOneMenu id=\"allWidgetsDropdown\" value=\"#{allWidgets.dropdown}\">\r\n" +
				"\t\t<f:selectItem/>\r\n" +
				"\t\t<f:selectItem itemValue=\"foo1\"/>\r\n" +
				"\t\t<f:selectItem itemValue=\"dropdown1\"/>\r\n" +
				"\t\t<f:selectItem itemValue=\"bar1\"/>\r\n" +
				"\t</h:selectOneMenu>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsDropdownWithLabels\" id=\"allWidgetsDropdownWithLabels-label\" value=\"Dropdown with labels:\"/>\r\n" +
				"\t<h:selectOneMenu id=\"allWidgetsDropdownWithLabels\" value=\"#{allWidgets.dropdownWithLabels}\">\r\n" +
				"\t\t<f:selectItem/>\r\n" +
				"\t\t<f:selectItem itemLabel=\"Foo #2\" itemValue=\"foo2\"/>\r\n" +
				"\t\t<f:selectItem itemLabel=\"Dropdown #2\" itemValue=\"dropdown2\"/>\r\n" +
				"\t\t<f:selectItem itemLabel=\"Bar #2\" itemValue=\"bar2\"/>\r\n" +
				"\t\t<f:selectItem itemLabel=\"Baz #2\" itemValue=\"baz2\"/>\r\n" +
				"\t</h:selectOneMenu>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsNotNullDropdown\" id=\"allWidgetsNotNullDropdown-label\" value=\"Not null dropdown:\"/>\r\n" +
				"\t<h:selectOneMenu id=\"allWidgetsNotNullDropdown\" value=\"#{allWidgets.notNullDropdown}\">\r\n" +
				"\t\t<f:selectItem itemValue=\"-1\"/>\r\n" +
				"\t\t<f:selectItem itemValue=\"0\"/>\r\n" +
				"\t\t<f:selectItem itemValue=\"1\"/>\r\n" +
				"\t</h:selectOneMenu>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsNotNullObjectDropdown\" id=\"allWidgetsNotNullObjectDropdown-label\" value=\"Not null object dropdown:\"/>\r\n" +
				"\t<h:selectOneMenu id=\"allWidgetsNotNullObjectDropdown\" required=\"true\" value=\"#{allWidgets.notNullObjectDropdown}\">\r\n" +
				"\t\t<f:selectItem itemValue=\"foo3\"/>\r\n" +
				"\t\t<f:selectItem itemValue=\"dropdown3\"/>\r\n" +
				"\t\t<f:selectItem itemValue=\"bar3\"/>\r\n" +
				"\t\t<f:selectItem itemValue=\"baz3\"/>\r\n" +
				"\t\t<f:selectItem itemValue=\"abc3\"/>\r\n" +
				"\t\t<f:selectItem itemValue=\"def3\"/>\r\n" +
				"\t</h:selectOneMenu>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsNestedWidgets\" id=\"allWidgetsNestedWidgets-label\" value=\"Nested widgets:\"/>\r\n" +
				"\t<h:panelGrid columns=\"2\" id=\"allWidgetsNestedWidgets\">\r\n" +
				"\t</h:panelGrid>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsReadOnlyNestedWidgets\" id=\"allWidgetsReadOnlyNestedWidgets-label\" value=\"Read only nested widgets:\"/>\r\n" +
				"\t<h:panelGrid columns=\"2\" id=\"allWidgetsReadOnlyNestedWidgets\">\r\n" +
				"\t</h:panelGrid>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsNestedWidgetsDontExpand\" id=\"allWidgetsNestedWidgetsDontExpand-label\" value=\"Nested widgets dont expand:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsNestedWidgetsDontExpand\" value=\"#{allWidgets.nestedWidgetsDontExpand}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsReadOnlyNestedWidgetsDontExpand\" id=\"allWidgetsReadOnlyNestedWidgetsDontExpand-label\" value=\"Read only nested widgets dont expand:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsReadOnlyNestedWidgetsDontExpand\" value=\"#{allWidgets.readOnlyNestedWidgetsDontExpand}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsDate\" id=\"allWidgetsDate-label\" value=\"Date:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsDate\" value=\"#{allWidgets.date}\">\r\n" +
				"\t\t<f:convertDateTime/>\r\n" +
				"\t</h:inputText>\r\n" +
				"\t<h:outputText value=\"Section Break\"/>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsReadOnly\" id=\"allWidgetsReadOnly-label\" value=\"Read only:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsReadOnly\" value=\"#{allWidgets.readOnly}\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsMystery\" id=\"allWidgetsMystery-label\" value=\"Mystery:\"/>\r\n" +
				"\t<h:inputText id=\"allWidgetsMystery\" value=\"#{allWidgets.mystery}\"/>\r\n" +
				"</h:panelGrid>\r\n";

		assertEquals( result, writer.toString() );
	}
}
