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
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package org.metawidget.statically.html;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.metawidget.integrationtest.shared.allwidgets.model.AllWidgets;

public class StaticHtmlAllWidgetsTest
    extends TestCase {

    //
    // Public methods
    //

    public void testAllWidgets() {

        StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
        metawidget.setConfig( "org/metawidget/integrationtest/static/html/allwidgets/metawidget.xml" );
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
            "\t\t\t\t<input id=\"textbox\" name=\"textbox\" type=\"text\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td>*</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"limitedTextbox\">Limited Textbox:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"limitedTextbox\" maxlength=\"20\" name=\"limitedTextbox\" type=\"text\"/>\r\n" +
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
            "\t\t\t\t<input id=\"password\" name=\"password\" type=\"secret\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"bytePrimitive\">Byte Primitive:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"bytePrimitive\" name=\"bytePrimitive\" type=\"text\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"byteObject\">Byte Object:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"byteObject\" name=\"byteObject\" type=\"text\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"shortPrimitive\">Short Primitive:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"shortPrimitive\" name=\"shortPrimitive\" type=\"text\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"shortObject\">Short Object:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"shortObject\" name=\"shortObject\" type=\"text\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"intPrimitive\">Int Primitive:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"intPrimitive\" name=\"intPrimitive\" type=\"text\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"integerObject\">Integer Object:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"integerObject\" name=\"integerObject\" type=\"text\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"rangedInt\">Ranged Int:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"rangedInt\" name=\"rangedInt\" type=\"text\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"rangedInteger\">Ranged Integer:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"rangedInteger\" name=\"rangedInteger\" type=\"text\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"longPrimitive\">Long Primitive:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"longPrimitive\" name=\"longPrimitive\" type=\"text\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"longObject\"/>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"longObject\" name=\"longObject\" type=\"text\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"floatPrimitive\">Float Primitive:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"floatPrimitive\" name=\"floatPrimitive\" type=\"text\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"floatObject\">nullInBundle:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"floatObject\" name=\"floatObject\" type=\"text\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"doublePrimitive\">Double Primitive:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"doublePrimitive\" name=\"doublePrimitive\" type=\"text\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"doubleObject\"/>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"doubleObject\" name=\"doubleObject\" type=\"text\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"charPrimitive\">Char Primitive:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"charPrimitive\" maxlength=\"1\" name=\"charPrimitive\" type=\"text\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"characterObject\">Character Object:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"characterObject\" maxlength=\"1\" name=\"characterObject\" type=\"text\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"booleanPrimitive\">Boolean Primitive:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"booleanPrimitive\" name=\"booleanPrimitive\" type=\"checkbox\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"booleanObject\">Boolean Object:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<select id=\"booleanObject\" name=\"booleanObject\">\r\n" +
            "\t\t\t\t\t<option/>\r\n" +
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
            "\t\t\t\t<select id=\"dropdown\" name=\"dropdown\">\r\n" +
            "\t\t\t\t\t<option/>\r\n" +
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
            "\t\t\t\t<select id=\"dropdownWithLabels\" name=\"dropdownWithLabels\">\r\n" +
            "\t\t\t\t\t<option/>\r\n" +
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
            "\t\t\t\t<select id=\"notNullDropdown\" name=\"notNullDropdown\">\r\n" +
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
            "\t\t\t\t<select id=\"notNullObjectDropdown\" name=\"notNullObjectDropdown\">\r\n" +
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
            "\t\t\t\t\t\t\t\t\t\t\t\t<input id=\"nestedWidgets-furtherNestedWidgets-nestedTextbox1\" name=\"nestedWidgetsFurtherNestedWidgetsNestedTextbox1\" type=\"text\"/>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</td>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<td/>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t</tr>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<label for=\"nestedWidgets-furtherNestedWidgets-nestedTextbox2\">Nested Textbox 2:</label>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<input id=\"nestedWidgets-furtherNestedWidgets-nestedTextbox2\" name=\"nestedWidgetsFurtherNestedWidgetsNestedTextbox2\" type=\"text\"/>\r\n" +
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
            "\t\t\t\t\t\t\t\t<input id=\"nestedWidgets-nestedTextbox1\" name=\"nestedWidgetsNestedTextbox1\" type=\"text\"/>\r\n" +
            "\t\t\t\t\t\t\t</td>\r\n" +
            "\t\t\t\t\t\t\t<td/>\r\n" +
            "\t\t\t\t\t\t</tr>\r\n" +
            "\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t<label for=\"nestedWidgets-nestedTextbox2\">Nested Textbox 2:</label>\r\n" +
            "\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t<input id=\"nestedWidgets-nestedTextbox2\" name=\"nestedWidgetsNestedTextbox2\" type=\"text\"/>\r\n" +
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
            "\t\t\t\t\t\t\t\t\t\t\t\t<label for=\"readOnlyNestedWidgets-furtherNestedWidgets-nestedTextbox1\">Nested Textbox 1:</label>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<div id=\"readOnlyNestedWidgets-furtherNestedWidgets-nestedTextbox1\"></div>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</td>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<td/>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t</tr>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<label for=\"readOnlyNestedWidgets-furtherNestedWidgets-nestedTextbox2\">Nested Textbox 2:</label>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<div id=\"readOnlyNestedWidgets-furtherNestedWidgets-nestedTextbox2\"></div>\r\n" +
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
            "\t\t\t\t\t\t\t\t<label for=\"readOnlyNestedWidgets-nestedTextbox1\">Nested Textbox 1:</label>\r\n" +
            "\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t<div id=\"readOnlyNestedWidgets-nestedTextbox1\"></div>\r\n" +
            "\t\t\t\t\t\t\t</td>\r\n" +
            "\t\t\t\t\t\t\t<td/>\r\n" +
            "\t\t\t\t\t\t</tr>\r\n" +
            "\t\t\t\t\t\t<tr>\r\n" +
            "\t\t\t\t\t\t\t<th>\r\n" +
            "\t\t\t\t\t\t\t\t<label for=\"readOnlyNestedWidgets-nestedTextbox2\">Nested Textbox 2:</label>\r\n" +
            "\t\t\t\t\t\t\t</th>\r\n" +
            "\t\t\t\t\t\t\t<td>\r\n" +
            "\t\t\t\t\t\t\t\t<div id=\"readOnlyNestedWidgets-nestedTextbox2\"></div>\r\n" +
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
            "\t\t\t\t<input id=\"nestedWidgetsDontExpand\" name=\"nestedWidgetsDontExpand\" type=\"text\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"readOnlyNestedWidgetsDontExpand\">Read Only Nested Widgets Dont Expand:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<div id=\"readOnlyNestedWidgetsDontExpand\"></div>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"date\">Date:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"date\" name=\"date\" type=\"date\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"hidden\">Hidden:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"hidden\" name=\"hidden\" type=\"hidden\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"readOnly\">Read Only:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<div id=\"readOnly\"></div>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t\t<td/>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<th>\r\n" +
            "\t\t\t\t<label for=\"mystery\">Mystery:</label>\r\n" +
            "\t\t\t</th>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<input id=\"mystery\" name=\"mystery\" type=\"text\"/>\r\n" +
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
            "\t\t\t\t\t<tbody/>\r\n" +
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
