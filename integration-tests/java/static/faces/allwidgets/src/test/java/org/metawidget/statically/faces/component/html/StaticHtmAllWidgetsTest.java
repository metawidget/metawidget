// Metawidget (licensed under LGPL)
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

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
		metawidget.setValue( "#{allWidgets}" );
		metawidget.setPath( AllWidgets.class.getName() );

		// Because AllWidgets was designed to test Objects, it recurses too far with just Classes

		metawidget.setMaximumInspectionDepth( 2 );

		String result = "<h:panelGrid columns=\"3\">\r\n" +
				"\t<h:outputLabel for=\"allWidgetsTextbox\" value=\"Textbox:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"allWidgetsTextbox\" required=\"true\" value=\"#{allWidgets.textbox}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsTextbox\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText value=\"*\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsLimitedTextbox\" value=\"Limited Textbox:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"allWidgetsLimitedTextbox\" maxLength=\"20\" value=\"#{allWidgets.limitedTextbox}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsLimitedTextbox\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsTextarea\" value=\"Textarea:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputTextarea id=\"allWidgetsTextarea\" value=\"#{allWidgets.textarea}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsTextarea\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsPassword\" value=\"Password:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputSecret id=\"allWidgetsPassword\" value=\"#{allWidgets.password}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsPassword\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsBytePrimitive\" value=\"Byte Primitive:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"allWidgetsBytePrimitive\" value=\"#{allWidgets.bytePrimitive}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsBytePrimitive\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsByteObject\" value=\"Byte Object:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"allWidgetsByteObject\" value=\"#{allWidgets.byteObject}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsByteObject\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsShortPrimitive\" value=\"Short Primitive:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"allWidgetsShortPrimitive\" value=\"#{allWidgets.shortPrimitive}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsShortPrimitive\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsShortObject\" value=\"Short Object:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"allWidgetsShortObject\" value=\"#{allWidgets.shortObject}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsShortObject\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsIntPrimitive\" value=\"Int Primitive:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"allWidgetsIntPrimitive\" value=\"#{allWidgets.intPrimitive}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsIntPrimitive\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsIntegerObject\" value=\"Integer Object:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"allWidgetsIntegerObject\" value=\"#{allWidgets.integerObject}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsIntegerObject\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsRangedInt\" value=\"Ranged Int:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"allWidgetsRangedInt\" value=\"#{allWidgets.rangedInt}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsRangedInt\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsRangedInteger\" value=\"Ranged Integer:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"allWidgetsRangedInteger\" value=\"#{allWidgets.rangedInteger}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsRangedInteger\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsLongPrimitive\" value=\"Long Primitive:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"allWidgetsLongPrimitive\" value=\"#{allWidgets.longPrimitive}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsLongPrimitive\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsLongObject\" value=\":\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"allWidgetsLongObject\" value=\"#{allWidgets.longObject}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsLongObject\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsFloatPrimitive\" value=\"Float Primitive:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"allWidgetsFloatPrimitive\" value=\"#{allWidgets.floatPrimitive}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsFloatPrimitive\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsFloatObject\" value=\"nullInBundle:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"allWidgetsFloatObject\" value=\"#{allWidgets.floatObject}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsFloatObject\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsDoublePrimitive\" value=\"Double Primitive:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"allWidgetsDoublePrimitive\" value=\"#{allWidgets.doublePrimitive}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsDoublePrimitive\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsDoubleObject\" value=\"null:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"allWidgetsDoubleObject\" value=\"#{allWidgets.doubleObject}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsDoubleObject\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsCharPrimitive\" value=\"Char Primitive:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"allWidgetsCharPrimitive\" maxLength=\"1\" value=\"#{allWidgets.charPrimitive}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsCharPrimitive\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsCharacterObject\" value=\"Character Object:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"allWidgetsCharacterObject\" maxLength=\"1\" value=\"#{allWidgets.characterObject}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsCharacterObject\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsBooleanPrimitive\" value=\"Boolean Primitive:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:selectBooleanCheckbox id=\"allWidgetsBooleanPrimitive\" value=\"#{allWidgets.booleanPrimitive}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsBooleanPrimitive\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsBooleanObject\" value=\"Boolean Object:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:selectOneMenu id=\"allWidgetsBooleanObject\" value=\"#{allWidgets.booleanObject}\">\r\n" +
				"\t\t\t<f:selectItem/>\r\n" +
				"\t\t\t<f:selectItem itemLabel=\"Yes\" itemValue=\"true\"/>\r\n" +
				"\t\t\t<f:selectItem itemLabel=\"No\" itemValue=\"false\"/>\r\n" +
				"\t\t</h:selectOneMenu>\r\n" +
				"\t\t<h:message for=\"allWidgetsBooleanObject\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsDropdown\" value=\"Dropdown:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:selectOneMenu id=\"allWidgetsDropdown\" value=\"#{allWidgets.dropdown}\">\r\n" +
				"\t\t\t<f:selectItem/>\r\n" +
				"\t\t\t<f:selectItem itemValue=\"foo1\"/>\r\n" +
				"\t\t\t<f:selectItem itemValue=\"dropdown1\"/>\r\n" +
				"\t\t\t<f:selectItem itemValue=\"bar1\"/>\r\n" +
				"\t\t</h:selectOneMenu>\r\n" +
				"\t\t<h:message for=\"allWidgetsDropdown\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsDropdownWithLabels\" value=\"Dropdown With Labels:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:selectOneMenu id=\"allWidgetsDropdownWithLabels\" value=\"#{allWidgets.dropdownWithLabels}\">\r\n" +
				"\t\t\t<f:selectItem/>\r\n" +
				"\t\t\t<f:selectItem itemLabel=\"Foo #2\" itemValue=\"foo2\"/>\r\n" +
				"\t\t\t<f:selectItem itemLabel=\"Dropdown #2\" itemValue=\"dropdown2\"/>\r\n" +
				"\t\t\t<f:selectItem itemLabel=\"Bar #2\" itemValue=\"bar2\"/>\r\n" +
				"\t\t\t<f:selectItem itemLabel=\"Baz #2\" itemValue=\"baz2\"/>\r\n" +
				"\t\t</h:selectOneMenu>\r\n" +
				"\t\t<h:message for=\"allWidgetsDropdownWithLabels\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsNotNullDropdown\" value=\"Not Null Dropdown:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:selectOneMenu id=\"allWidgetsNotNullDropdown\" value=\"#{allWidgets.notNullDropdown}\">\r\n" +
				"\t\t\t<f:selectItem itemValue=\"-1\"/>\r\n" +
				"\t\t\t<f:selectItem itemValue=\"0\"/>\r\n" +
				"\t\t\t<f:selectItem itemValue=\"1\"/>\r\n" +
				"\t\t</h:selectOneMenu>\r\n" +
				"\t\t<h:message for=\"allWidgetsNotNullDropdown\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsNotNullObjectDropdown\" value=\"Not Null Object Dropdown:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:selectOneMenu id=\"allWidgetsNotNullObjectDropdown\" required=\"true\" value=\"#{allWidgets.notNullObjectDropdown}\">\r\n" +
				"\t\t\t<f:selectItem itemValue=\"foo3\"/>\r\n" +
				"\t\t\t<f:selectItem itemValue=\"dropdown3\"/>\r\n" +
				"\t\t\t<f:selectItem itemValue=\"bar3\"/>\r\n" +
				"\t\t\t<f:selectItem itemValue=\"baz3\"/>\r\n" +
				"\t\t\t<f:selectItem itemValue=\"abc3\"/>\r\n" +
				"\t\t\t<f:selectItem itemValue=\"def3\"/>\r\n" +
				"\t\t</h:selectOneMenu>\r\n" +
				"\t\t<h:message for=\"allWidgetsNotNullObjectDropdown\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText value=\"*\"/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsNestedWidgets\" value=\"Nested Widgets:\"/>\r\n" +
				"\t<h:panelGrid columns=\"3\" id=\"allWidgetsNestedWidgets\">\r\n" +
				"\t\t<h:outputLabel for=\"allWidgetsNestedWidgetsFurtherNestedWidgets\" value=\"Further Nested Widgets:\"/>\r\n" +
				"\t\t<h:panelGrid columns=\"3\" id=\"allWidgetsNestedWidgetsFurtherNestedWidgets\">\r\n" +
				"\t\t\t<h:outputLabel for=\"allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox1\" value=\"Nested Textbox 1:\"/>\r\n" +
				"\t\t\t<h:panelGroup>\r\n" +
				"\t\t\t\t<h:inputText id=\"allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox1\" value=\"#{allWidgets.nestedWidgets.furtherNestedWidgets.nestedTextbox1}\"/>\r\n" +
				"\t\t\t\t<h:message for=\"allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox1\"/>\r\n" +
				"\t\t\t</h:panelGroup>\r\n" +
				"\t\t\t<h:outputText/>\r\n" +
				"\t\t\t<h:outputLabel for=\"allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox2\" value=\"Nested Textbox 2:\"/>\r\n" +
				"\t\t\t<h:panelGroup>\r\n" +
				"\t\t\t\t<h:inputText id=\"allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox2\" value=\"#{allWidgets.nestedWidgets.furtherNestedWidgets.nestedTextbox2}\"/>\r\n" +
				"\t\t\t\t<h:message for=\"allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox2\"/>\r\n" +
				"\t\t\t</h:panelGroup>\r\n" +
				"\t\t\t<h:outputText/>\r\n" +
				"\t\t</h:panelGrid>\r\n" +
				"\t\t<h:outputText/>\r\n" +
				"\t\t<h:outputLabel for=\"allWidgetsNestedWidgetsNestedTextbox1\" value=\"Nested Textbox 1:\"/>\r\n" +
				"\t\t<h:panelGroup>\r\n" +
				"\t\t\t<h:inputText id=\"allWidgetsNestedWidgetsNestedTextbox1\" value=\"#{allWidgets.nestedWidgets.nestedTextbox1}\"/>\r\n" +
				"\t\t\t<h:message for=\"allWidgetsNestedWidgetsNestedTextbox1\"/>\r\n" +
				"\t\t</h:panelGroup>\r\n" +
				"\t\t<h:outputText/>\r\n" +
				"\t\t<h:outputLabel for=\"allWidgetsNestedWidgetsNestedTextbox2\" value=\"Nested Textbox 2:\"/>\r\n" +
				"\t\t<h:panelGroup>\r\n" +
				"\t\t\t<h:inputText id=\"allWidgetsNestedWidgetsNestedTextbox2\" value=\"#{allWidgets.nestedWidgets.nestedTextbox2}\"/>\r\n" +
				"\t\t\t<h:message for=\"allWidgetsNestedWidgetsNestedTextbox2\"/>\r\n" +
				"\t\t</h:panelGroup>\r\n" +
				"\t\t<h:outputText/>\r\n" +
				"\t</h:panelGrid>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsReadOnlyNestedWidgets\" value=\"Read Only Nested Widgets:\"/>\r\n" +
				"\t<h:panelGrid columns=\"3\" id=\"allWidgetsReadOnlyNestedWidgets\">\r\n" +
				"\t\t<h:outputLabel for=\"allWidgetsReadOnlyNestedWidgetsFurtherNestedWidgets\" value=\"Further Nested Widgets:\"/>\r\n" +
				"\t\t<h:panelGrid columns=\"3\" id=\"allWidgetsReadOnlyNestedWidgetsFurtherNestedWidgets\">\r\n" +
				"\t\t\t<h:outputLabel for=\"allWidgetsReadOnlyNestedWidgetsFurtherNestedWidgetsNestedTextbox1\" value=\"Nested Textbox 1:\"/>\r\n" +
				"\t\t\t<h:outputText id=\"allWidgetsReadOnlyNestedWidgetsFurtherNestedWidgetsNestedTextbox1\" value=\"#{allWidgets.readOnlyNestedWidgets.furtherNestedWidgets.nestedTextbox1}\"/>\r\n" +
				"\t\t\t<h:outputText/>\r\n" +
				"\t\t\t<h:outputLabel for=\"allWidgetsReadOnlyNestedWidgetsFurtherNestedWidgetsNestedTextbox2\" value=\"Nested Textbox 2:\"/>\r\n" +
				"\t\t\t<h:outputText id=\"allWidgetsReadOnlyNestedWidgetsFurtherNestedWidgetsNestedTextbox2\" value=\"#{allWidgets.readOnlyNestedWidgets.furtherNestedWidgets.nestedTextbox2}\"/>\r\n" +
				"\t\t\t<h:outputText/>\r\n" +
				"\t\t</h:panelGrid>\r\n" +
				"\t\t<h:outputText/>\r\n" +
				"\t\t<h:outputLabel for=\"allWidgetsReadOnlyNestedWidgetsNestedTextbox1\" value=\"Nested Textbox 1:\"/>\r\n" +
				"\t\t<h:outputText id=\"allWidgetsReadOnlyNestedWidgetsNestedTextbox1\" value=\"#{allWidgets.readOnlyNestedWidgets.nestedTextbox1}\"/>\r\n" +
				"\t\t<h:outputText/>\r\n" +
				"\t\t<h:outputLabel for=\"allWidgetsReadOnlyNestedWidgetsNestedTextbox2\" value=\"Nested Textbox 2:\"/>\r\n" +
				"\t\t<h:outputText id=\"allWidgetsReadOnlyNestedWidgetsNestedTextbox2\" value=\"#{allWidgets.readOnlyNestedWidgets.nestedTextbox2}\"/>\r\n" +
				"\t\t<h:outputText/>\r\n" +
				"\t</h:panelGrid>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsNestedWidgetsDontExpand\" value=\"Nested Widgets Dont Expand:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"allWidgetsNestedWidgetsDontExpand\" value=\"#{allWidgets.nestedWidgetsDontExpand}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsNestedWidgetsDontExpand\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsReadOnlyNestedWidgetsDontExpand\" value=\"Read Only Nested Widgets Dont Expand:\"/>\r\n" +
				"\t<h:outputText id=\"allWidgetsReadOnlyNestedWidgetsDontExpand\" value=\"#{allWidgets.readOnlyNestedWidgetsDontExpand}\"/>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsDate\" value=\"Date:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"allWidgetsDate\" value=\"#{allWidgets.date}\">\r\n" +
				"\t\t\t<f:convertDateTime/>\r\n" +
				"\t\t</h:inputText>\r\n" +
				"\t\t<h:message for=\"allWidgetsDate\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputText value=\"Section Break\"/>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsReadOnly\" value=\"Read Only:\"/>\r\n" +
				"\t<h:outputText id=\"allWidgetsReadOnly\" value=\"#{allWidgets.readOnly}\"/>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"allWidgetsMystery\" value=\"Mystery:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"allWidgetsMystery\" value=\"#{allWidgets.mystery}\"/>\r\n" +
				"\t\t<h:message for=\"allWidgetsMystery\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"</h:panelGrid>\r\n";

		StringWriter writer = new StringWriter();
		metawidget.write( writer, 0 );
		assertEquals( result, writer.toString() );
	}
}
