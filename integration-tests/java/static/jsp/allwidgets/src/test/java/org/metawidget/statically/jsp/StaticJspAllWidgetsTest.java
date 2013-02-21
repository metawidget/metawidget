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

package org.metawidget.statically.jsp;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.metawidget.integrationtest.shared.allwidgets.model.AllWidgets;

public class StaticJspAllWidgetsTest
    extends TestCase {

    //
    // Public methods
    //

    public void testAllWidgets() {

        StaticJspMetawidget metawidget = new StaticJspMetawidget();
        metawidget.setConfig( "org/metawidget/integrationtest/static/jsp/allwidgets/metawidget.xml" );
        metawidget.setValue( "${allWidgets}" );
        metawidget.setPath( AllWidgets.class.getName() );

        // Because AllWidgets was designed to test Objects, it recurses too far with just Classes

        metawidget.setMaximumInspectionDepth( 2 );

        String result = "<table>\r\n" +
            "\t<tbody>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"textbox\">Textbox:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"textbox\" name=\"textbox\" type=\"text\" value=\"${allWidgets.textbox}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td>*</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"limitedTextbox\">Limited Textbox:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"limitedTextbox\" maxlength=\"20\" name=\"limitedTextbox\" type=\"text\" value=\"${allWidgets.limitedTextbox}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"textarea\">Textarea:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<textarea id=\"textarea\" name=\"textarea\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"password\">Password:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"password\" name=\"password\" type=\"secret\" value=\"${allWidgets.password}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"bytePrimitive\">Byte Primitive:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"bytePrimitive\" min=\"-5\" name=\"bytePrimitive\" type=\"number\" value=\"${allWidgets.bytePrimitive}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"byteObject\">Byte Object:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"byteObject\" name=\"byteObject\" type=\"number\" value=\"${allWidgets.byteObject}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"shortPrimitive\">Short Primitive:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"shortPrimitive\" min=\"-6\" name=\"shortPrimitive\" type=\"number\" value=\"${allWidgets.shortPrimitive}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"shortObject\">Short Object:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"shortObject\" name=\"shortObject\" type=\"number\" value=\"${allWidgets.shortObject}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"intPrimitive\">Int Primitive:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"intPrimitive\" name=\"intPrimitive\" type=\"number\" value=\"${allWidgets.intPrimitive}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"integerObject\">Integer Object:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"integerObject\" name=\"integerObject\" type=\"number\" value=\"${allWidgets.integerObject}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"rangedInt\">Ranged Int:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"rangedInt\" max=\"100\" min=\"1\" name=\"rangedInt\" type=\"number\" value=\"${allWidgets.rangedInt}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"rangedInteger\">Ranged Integer:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"rangedInteger\" max=\"99\" min=\"2\" name=\"rangedInteger\" type=\"number\" value=\"${allWidgets.rangedInteger}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"longPrimitive\">Long Primitive:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"longPrimitive\" min=\"-7\" name=\"longPrimitive\" type=\"number\" value=\"${allWidgets.longPrimitive}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"longObject\"/>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"longObject\" name=\"longObject\" type=\"number\" value=\"${allWidgets.longObject}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"floatPrimitive\">Float Primitive:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"floatPrimitive\" max=\"2048\" name=\"floatPrimitive\" type=\"number\" value=\"${allWidgets.floatPrimitive}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"floatObject\">nullInBundle:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"floatObject\" name=\"floatObject\" type=\"number\" value=\"${allWidgets.floatObject}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"doublePrimitive\">Double Primitive:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"doublePrimitive\" min=\"-8\" name=\"doublePrimitive\" type=\"number\" value=\"${allWidgets.doublePrimitive}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"doubleObject\"/>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"doubleObject\" name=\"doubleObject\" type=\"number\" value=\"${allWidgets.doubleObject}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"charPrimitive\">Char Primitive:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"charPrimitive\" maxlength=\"1\" name=\"charPrimitive\" type=\"text\" value=\"${allWidgets.charPrimitive}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"characterObject\">Character Object:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"characterObject\" maxlength=\"1\" name=\"characterObject\" type=\"text\" value=\"${allWidgets.characterObject}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"booleanPrimitive\">Boolean Primitive:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"booleanPrimitive\" name=\"booleanPrimitive\" type=\"checkbox\" value=\"${allWidgets.booleanPrimitive}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"booleanObject\">Boolean Object:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<select id=\"booleanObject\" name=\"booleanObject\" value=\"${allWidgets.booleanObject}\">\r\n" +
            "\t\t\t\t\t<option value=\"\"/>\r\n" +
            "\t\t\t\t\t<option value=\"true\">Yes</option>\r\n" +
            "\t\t\t\t\t<option value=\"false\">No</option>\r\n" +
            "\t\t\t\t</select>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"dropdown\">Dropdown:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<select id=\"dropdown\" name=\"dropdown\" value=\"${allWidgets.dropdown}\">\r\n" +
            "\t\t\t\t\t<option value=\"\"/>\r\n" +
            "\t\t\t\t\t<option value=\"foo1\"/>\r\n" +
            "\t\t\t\t\t<option value=\"dropdown1\"/>\r\n" +
            "\t\t\t\t\t<option value=\"bar1\"/>\r\n" +
            "\t\t\t\t</select>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"dropdownWithLabels\">Dropdown With Labels:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<select id=\"dropdownWithLabels\" name=\"dropdownWithLabels\" value=\"${allWidgets.dropdownWithLabels}\">\r\n" +
            "\t\t\t\t\t<option value=\"\"/>\r\n" +
            "\t\t\t\t\t<option value=\"foo2\">Foo #2</option>\r\n" +
            "\t\t\t\t\t<option value=\"dropdown2\">Dropdown #2</option>\r\n" +
            "\t\t\t\t\t<option value=\"bar2\">Bar #2</option>\r\n" +
            "\t\t\t\t\t<option value=\"baz2\">Baz #2</option>\r\n" +
            "\t\t\t\t</select>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"notNullDropdown\">Not Null Dropdown:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<select id=\"notNullDropdown\" name=\"notNullDropdown\" value=\"${allWidgets.notNullDropdown}\">\r\n" +
            "\t\t\t\t\t<option value=\"-1\"/>\r\n" +
            "\t\t\t\t\t<option value=\"0\"/>\r\n" +
            "\t\t\t\t\t<option value=\"1\"/>\r\n" +
            "\t\t\t\t</select>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"notNullObjectDropdown\">Not Null Object Dropdown:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<select id=\"notNullObjectDropdown\" name=\"notNullObjectDropdown\" value=\"${allWidgets.notNullObjectDropdown}\">\r\n" +
            "\t\t\t\t\t<option value=\"foo3\"/>\r\n" +
            "\t\t\t\t\t<option value=\"dropdown3\"/>\r\n" +
            "\t\t\t\t\t<option value=\"bar3\"/>\r\n" +
            "\t\t\t\t\t<option value=\"baz3\"/>\r\n" +
            "\t\t\t\t\t<option value=\"abc3\"/>\r\n" +
            "\t\t\t\t\t<option value=\"def3\"/>\r\n" +
            "\t\t\t\t</select>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td>*</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"nestedWidgets\">Nested Widgets:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<table id=\"nestedWidgets\">\r\n" +
            "\t\t\t\t\t<tbody>\r\n" +
            "\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t<label for=\"nestedWidgets-furtherNestedWidgets\">Further Nested Widgets:</label>\r\n" +
            "\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t<table id=\"nestedWidgets-furtherNestedWidgets\">\r\n" +
            "\t\t\t\t\t\t\t\t\t<tbody>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<label for=\"nestedWidgets-furtherNestedWidgets-nestedTextbox1\">Nested Textbox 1:</label>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<input id=\"nestedWidgets-furtherNestedWidgets-nestedTextbox1\" name=\"nestedWidgetsFurtherNestedWidgetsNestedTextbox1\" type=\"text\" value=\"${allWidgets.nestedWidgets.furtherNestedWidgets.nestedTextbox1}\"/>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</td>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<td/>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t</tr>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<label for=\"nestedWidgets-furtherNestedWidgets-nestedTextbox2\">Nested Textbox 2:</label>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<input id=\"nestedWidgets-furtherNestedWidgets-nestedTextbox2\" name=\"nestedWidgetsFurtherNestedWidgetsNestedTextbox2\" type=\"text\" value=\"${allWidgets.nestedWidgets.furtherNestedWidgets.nestedTextbox2}\"/>\r\n" +
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
            "\t\t\t\t\t\t\t\t<label for=\"nestedWidgets-nestedTextbox1\">Nested Textbox 1:</label>\r\n" +
            "\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t<input id=\"nestedWidgets-nestedTextbox1\" name=\"nestedWidgetsNestedTextbox1\" type=\"text\" value=\"${allWidgets.nestedWidgets.nestedTextbox1}\"/>\r\n" +
            "\t\t\t\t\t\t\t</td>\r\n" +
            "\t\t\t\t\t\t\t<td/>\r\n" +
            "\t\t\t\t\t\t</tr>\r\n" +
            "\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t<label for=\"nestedWidgets-nestedTextbox2\">Nested Textbox 2:</label>\r\n" +
            "\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t<input id=\"nestedWidgets-nestedTextbox2\" name=\"nestedWidgetsNestedTextbox2\" type=\"text\" value=\"${allWidgets.nestedWidgets.nestedTextbox2}\"/>\r\n" +
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
            "\t\t\t\t<label for=\"readOnlyNestedWidgets\">Read Only Nested Widgets:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<table id=\"readOnlyNestedWidgets\">\r\n" +
            "\t\t\t\t\t<tbody>\r\n" +
            "\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t<label for=\"readOnlyNestedWidgets-furtherNestedWidgets\">Further Nested Widgets:</label>\r\n" +
            "\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t<table id=\"readOnlyNestedWidgets-furtherNestedWidgets\">\r\n" +
            "\t\t\t\t\t\t\t\t\t<tbody>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<label>Nested Textbox 1:</label>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<c:out value=\"${allWidgets.readOnlyNestedWidgets.furtherNestedWidgets.nestedTextbox1}\">\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<input name=\"readOnlyNestedWidgetsFurtherNestedWidgetsNestedTextbox1\" type=\"hidden\" value=\"${allWidgets.readOnlyNestedWidgets.furtherNestedWidgets.nestedTextbox1}\"/>\r\n" +
        	"\t\t\t\t\t\t\t\t\t\t\t\t</c:out>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</td>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<td/>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t</tr>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<label>Nested Textbox 2:</label>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<c:out value=\"${allWidgets.readOnlyNestedWidgets.furtherNestedWidgets.nestedTextbox2}\">\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<input name=\"readOnlyNestedWidgetsFurtherNestedWidgetsNestedTextbox2\" type=\"hidden\" value=\"${allWidgets.readOnlyNestedWidgets.furtherNestedWidgets.nestedTextbox2}\"/>\r\n" +
        	"\t\t\t\t\t\t\t\t\t\t\t\t</c:out>\r\n" +
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
            "\t\t\t\t\t\t\t\t<label>Nested Textbox 1:</label>\r\n" +
            "\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t<c:out value=\"${allWidgets.readOnlyNestedWidgets.nestedTextbox1}\">\r\n" +
            "\t\t\t\t\t\t\t\t\t<input name=\"readOnlyNestedWidgetsNestedTextbox1\" type=\"hidden\" value=\"${allWidgets.readOnlyNestedWidgets.nestedTextbox1}\"/>\r\n" +
            "\t\t\t\t\t\t\t\t</c:out>\r\n" +
            "\t\t\t\t\t\t\t</td>\r\n" +
            "\t\t\t\t\t\t\t<td/>\r\n" +
            "\t\t\t\t\t\t</tr>\r\n" +
            "\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t<label>Nested Textbox 2:</label>\r\n" +
            "\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t<c:out value=\"${allWidgets.readOnlyNestedWidgets.nestedTextbox2}\">\r\n" +
            "\t\t\t\t\t\t\t\t\t<input name=\"readOnlyNestedWidgetsNestedTextbox2\" type=\"hidden\" value=\"${allWidgets.readOnlyNestedWidgets.nestedTextbox2}\"/>\r\n" +
            "\t\t\t\t\t\t\t\t</c:out>\r\n" +
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
            "\t\t\t\t<label for=\"nestedWidgetsDontExpand\">Nested Widgets Dont Expand:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"nestedWidgetsDontExpand\" name=\"nestedWidgetsDontExpand\" type=\"text\" value=\"${allWidgets.nestedWidgetsDontExpand}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label>Read Only Nested Widgets Dont Expand:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<c:out value=\"${allWidgets.readOnlyNestedWidgetsDontExpand}\">\r\n" +
            "\t\t\t\t\t<input name=\"readOnlyNestedWidgetsDontExpand\" type=\"hidden\" value=\"${allWidgets.readOnlyNestedWidgetsDontExpand}\"/>\r\n" +
            "\t\t\t\t</c:out>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"date\">Date:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"date\" name=\"date\" type=\"date\" value=\"${allWidgets.date}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th/>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"hidden\" name=\"hidden\" type=\"hidden\" value=\"${allWidgets.hidden}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label>Read Only:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<c:out value=\"${allWidgets.readOnly}\">\r\n" +
            "\t\t\t\t\t<input name=\"readOnly\" type=\"hidden\" value=\"${allWidgets.readOnly}\"/>\r\n" +
            "\t\t\t\t</c:out>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"mystery\">Mystery:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"mystery\" name=\"mystery\" type=\"text\" value=\"${allWidgets.mystery}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"collection\">Collection:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<table id=\"collection\">\r\n" +
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
