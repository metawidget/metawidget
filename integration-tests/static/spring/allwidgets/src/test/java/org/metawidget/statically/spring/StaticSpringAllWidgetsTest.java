// Metawidget
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
// License along with this library; if Not Requiredt, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package org.metawidget.statically.spring;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.metawidget.integrationtest.shared.allwidgets.model.AllWidgets;

public class StaticSpringAllWidgetsTest
    extends TestCase {

    //
    // Public methods
    //

    public void testAllWidgets() {

        StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
        metawidget.setConfig( "org/metawidget/integrationtest/static/spring/allwidgets/metawidget.xml" );
        metawidget.setValue( "allWidgets" );
        metawidget.setPath( AllWidgets.class.getName() );

        // Because AllWidgets was designed to test Objects, it recurses too far with just Classes

        metawidget.setMaximumInspectionDepth( 2 );

        String result = "<table>\r\n" +
            "\t<tbody>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"textbox\">Textbox:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:input path=\"textbox\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td>*</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"limitedTextbox\">Limited Textbox:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:input maxlength=\"20\" path=\"limitedTextbox\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"textarea\">Textarea:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:textarea path=\"textarea\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"password\">Password:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:password path=\"password\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"bytePrimitive\">Byte Primitive:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:input path=\"bytePrimitive\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"byteObject\">Byte Object:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:input path=\"byteObject\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"shortPrimitive\">Short Primitive:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:input path=\"shortPrimitive\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"shortObject\">Short Object:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:input path=\"shortObject\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"intPrimitive\">Int Primitive:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:input path=\"intPrimitive\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"integerObject\">Integer Object:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:input path=\"integerObject\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"rangedInt\">Ranged Int:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:input path=\"rangedInt\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"rangedInteger\">Ranged Integer:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:input path=\"rangedInteger\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"longPrimitive\">Long Primitive:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:input path=\"longPrimitive\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"longObject\"/>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:input path=\"longObject\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"floatPrimitive\">Float Primitive:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:input path=\"floatPrimitive\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"floatObject\">nullInBundle:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:input path=\"floatObject\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"doublePrimitive\">Double Primitive:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:input path=\"doublePrimitive\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"doubleObject\"/>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:input path=\"doubleObject\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"charPrimitive\">Char Primitive:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:input maxlength=\"1\" path=\"charPrimitive\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"characterObject\">Character Object:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:input maxlength=\"1\" path=\"characterObject\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"booleanPrimitive\">Boolean Primitive:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:checkbox path=\"booleanPrimitive\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"booleanObject\">Boolean Object:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:select path=\"booleanObject\">\r\n" +
            "\t\t\t\t\t<form:option/>\r\n" +
            "\t\t\t\t\t<form:option label=\"Yes\" value=\"true\"/>\r\n" +
            "\t\t\t\t\t<form:option label=\"No\" value=\"false\"/>\r\n" +
            "\t\t\t\t</form:select>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"dropdown\">Dropdown:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:select path=\"dropdown\">\r\n" +
            "\t\t\t\t\t<form:option/>\r\n" +
            "\t\t\t\t\t<form:option value=\"foo1\"/>\r\n" +
            "\t\t\t\t\t<form:option value=\"dropdown1\"/>\r\n" +
            "\t\t\t\t\t<form:option value=\"bar1\"/>\r\n" +
            "\t\t\t\t</form:select>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"dropdownWithLabels\">Dropdown With Labels:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:select path=\"dropdownWithLabels\">\r\n" +
            "\t\t\t\t\t<form:option/>\r\n" +
            "\t\t\t\t\t<form:option label=\"Foo #2\" value=\"foo2\"/>\r\n" +
            "\t\t\t\t\t<form:option label=\"Dropdown #2\" value=\"dropdown2\"/>\r\n" +
            "\t\t\t\t\t<form:option label=\"Bar #2\" value=\"bar2\"/>\r\n" +
            "\t\t\t\t\t<form:option label=\"Baz #2\" value=\"baz2\"/>\r\n" +
            "\t\t\t\t</form:select>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"notNullDropdown\">Not Null Dropdown:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:select path=\"notNullDropdown\">\r\n" +
            "\t\t\t\t\t<form:option value=\"-1\"/>\r\n" +
            "\t\t\t\t\t<form:option value=\"0\"/>\r\n" +
            "\t\t\t\t\t<form:option value=\"1\"/>\r\n" +
            "\t\t\t\t</form:select>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"notNullObjectDropdown\">Not Null Object Dropdown:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:select path=\"notNullObjectDropdown\">\r\n" +
            "\t\t\t\t\t<form:option value=\"foo3\"/>\r\n" +
            "\t\t\t\t\t<form:option value=\"dropdown3\"/>\r\n" +
            "\t\t\t\t\t<form:option value=\"bar3\"/>\r\n" +
            "\t\t\t\t\t<form:option value=\"baz3\"/>\r\n" +
            "\t\t\t\t\t<form:option value=\"abc3\"/>\r\n" +
            "\t\t\t\t\t<form:option value=\"def3\"/>\r\n" +
            "\t\t\t\t</form:select>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td>*</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"nestedWidgets\">Nested Widgets:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<table id=\"nestedWidgets\">\r\n" +
            "\t\t\t\t\t<tbody>\r\n" +
            "\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t<form:label path=\"nestedWidgets.furtherNestedWidgets\">Further Nested Widgets:</form:label>\r\n" +
            "\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t<table id=\"nestedWidgets-furtherNestedWidgets\">\r\n" +
            "\t\t\t\t\t\t\t\t\t<tbody>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<form:label path=\"nestedWidgets.furtherNestedWidgets.nestedTextbox1\">Nested Textbox 1:</form:label>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<form:input path=\"nestedWidgets.furtherNestedWidgets.nestedTextbox1\"/>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</td>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<td/>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t</tr>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<form:label path=\"nestedWidgets.furtherNestedWidgets.nestedTextbox2\">Nested Textbox 2:</form:label>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<form:input path=\"nestedWidgets.furtherNestedWidgets.nestedTextbox2\"/>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</td>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<td/>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t</tr>\r\n" +
            "\t\t\t\t\t\t\t\t\t</tbody>\r\n" +
            "\t\t\t\t\t\t\t\t</table>\r\n" +
            "\t\t\t\t\t\t\t</td>\r\n" +
            "\t\t\t\t\t\t\t<td/>\r\n" +
            "\t\t\t\t\t\t</tr>\r\n" +
            "\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t<form:label path=\"nestedWidgets.nestedTextbox1\">Nested Textbox 1:</form:label>\r\n" +
            "\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t<form:input path=\"nestedWidgets.nestedTextbox1\"/>\r\n" +
            "\t\t\t\t\t\t\t</td>\r\n" +
            "\t\t\t\t\t\t\t<td/>\r\n" +
            "\t\t\t\t\t\t</tr>\r\n" +
            "\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t<form:label path=\"nestedWidgets.nestedTextbox2\">Nested Textbox 2:</form:label>\r\n" +
            "\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t<form:input path=\"nestedWidgets.nestedTextbox2\"/>\r\n" +
            "\t\t\t\t\t\t\t</td>\r\n" +
            "\t\t\t\t\t\t\t<td/>\r\n" +
            "\t\t\t\t\t\t</tr>\r\n" +
            "\t\t\t\t\t</tbody>\r\n" +
            "\t\t\t\t</table>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"readOnlyNestedWidgets\">Read Only Nested Widgets:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<table id=\"readOnlyNestedWidgets\">\r\n" +
            "\t\t\t\t\t<tbody>\r\n" +
            "\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t<form:label path=\"readOnlyNestedWidgets.furtherNestedWidgets\">Further Nested Widgets:</form:label>\r\n" +
            "\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t<table id=\"readOnlyNestedWidgets-furtherNestedWidgets\">\r\n" +
            "\t\t\t\t\t\t\t\t\t<tbody>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<form:label path=\"readOnlyNestedWidgets.furtherNestedWidgets.nestedTextbox1\">Nested Textbox 1:</form:label>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<c:out value=\"${allWidgets.readOnlyNestedWidgets.furtherNestedWidgets.nestedTextbox1}\"/>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</td>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<td/>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t</tr>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<form:label path=\"readOnlyNestedWidgets.furtherNestedWidgets.nestedTextbox2\">Nested Textbox 2:</form:label>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<c:out value=\"${allWidgets.readOnlyNestedWidgets.furtherNestedWidgets.nestedTextbox2}\"/>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</td>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<td/>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t</tr>\r\n" +
            "\t\t\t\t\t\t\t\t\t</tbody>\r\n" +
            "\t\t\t\t\t\t\t\t</table>\r\n" +
            "\t\t\t\t\t\t\t</td>\r\n" +
            "\t\t\t\t\t\t\t<td/>\r\n" +
            "\t\t\t\t\t\t</tr>\r\n" +
            "\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t<form:label path=\"readOnlyNestedWidgets.nestedTextbox1\">Nested Textbox 1:</form:label>\r\n" +
            "\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t<c:out value=\"${allWidgets.readOnlyNestedWidgets.nestedTextbox1}\"/>\r\n" +
            "\t\t\t\t\t\t\t</td>\r\n" +
            "\t\t\t\t\t\t\t<td/>\r\n" +
            "\t\t\t\t\t\t</tr>\r\n" +
            "\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t<form:label path=\"readOnlyNestedWidgets.nestedTextbox2\">Nested Textbox 2:</form:label>\r\n" +
            "\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t<c:out value=\"${allWidgets.readOnlyNestedWidgets.nestedTextbox2}\"/>\r\n" +
            "\t\t\t\t\t\t\t</td>\r\n" +
            "\t\t\t\t\t\t\t<td/>\r\n" +
            "\t\t\t\t\t\t</tr>\r\n" +
            "\t\t\t\t\t</tbody>\r\n" +
            "\t\t\t\t</table>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"nestedWidgetsDontExpand\">Nested Widgets Dont Expand:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:input path=\"nestedWidgetsDontExpand\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"readOnlyNestedWidgetsDontExpand\">Read Only Nested Widgets Dont Expand:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<c:out value=\"${allWidgets.readOnlyNestedWidgetsDontExpand}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"date\">Date:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:input path=\"date\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th/>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:hidden path=\"hidden\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"readOnly\">Read Only:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<c:out value=\"${allWidgets.readOnly}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"mystery\">Mystery:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<form:input path=\"mystery\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<form:label path=\"collection\">Collection:</form:label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<table>\r\n" +
            "\t\t\t\t\t<thead>\r\n" +
            "\t\t\t\t\t\t<tr/>\r\n" +
            "\t\t\t\t\t</thead>\r\n" +
            "\t\t\t\t\t<tbody>\r\n" +
            "\t\t\t\t\t\t<c:forEach items=\"${collection}\" var=\"item\"/>\r\n" +
            "\t\t\t\t\t</tbody>\r\n" +
            "\t\t\t\t</table>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +

            "\t</tbody>\r\n" +
            "</table>\r\n";

        StringWriter writer = new StringWriter();
        metawidget.write( writer, 0 );
        assertEquals( result, writer.toString() );
    }

}
