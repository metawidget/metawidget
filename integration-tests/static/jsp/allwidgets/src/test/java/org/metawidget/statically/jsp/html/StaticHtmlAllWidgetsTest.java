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

package org.metawidget.statically.jsp.html;

import java.io.StringWriter;

import org.metawidget.integrationtest.shared.allwidgets.model.AllWidgets;
import org.metawidget.statically.jsp.html.StaticHtmlMetawidget;

import junit.framework.TestCase;

public class StaticHtmlAllWidgetsTest
    extends TestCase {
    
    //
    // Public methods
    //
    
    public void testAllWidgets() {
        
        StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
        metawidget.setConfig( "org/metawidget/integrationtest/static/jsp/allwidgets/metawidget.xml" );
        metawidget.setValue( "${allWidgets}" );
        metawidget.setPath( AllWidgets.class.getName() );
        
        String result = "<table id=\"table-orgMetawidgetIntegrationtestSharedAllwidgetsModelAllWidgets\">\r\n" +
            "\t<tbody>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Textbox</label>\r\n" +
            "\t\t\t\t<input name=\"allWidgetsTextbox\" type=\"text\" value=\"${allWidgets.textbox}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Limited Textbox</label>\r\n" +            
            "\t\t\t\t<input maxlength=\"20\" name=\"allWidgetsLimitedTextbox\" type=\"text\" value=\"${allWidgets.limitedTextbox}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Textarea</label>\r\n" +
            "\t\t\t\t<textarea name=\"allWidgetsTextarea\" value=\"${allWidgets.textarea}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Password</label>\r\n" +
            "\t\t\t\t<input name=\"allWidgetsPassword\" type=\"secret\" value=\"${allWidgets.password}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Byte Primitive</label>\r\n" +
            "\t\t\t\t<input name=\"allWidgetsBytePrimitive\" type=\"text\" value=\"${allWidgets.bytePrimitive}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Byte Object</label>\r\n" +
            "\t\t\t\t<input name=\"allWidgetsByteObject\" type=\"text\" value=\"${allWidgets.byteObject}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Short Primitive</label>\r\n" +
            "\t\t\t\t<input name=\"allWidgetsShortPrimitive\" type=\"text\" value=\"${allWidgets.shortPrimitive}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Short Object</label>\r\n" +
            "\t\t\t\t<input name=\"allWidgetsShortObject\" type=\"text\" value=\"${allWidgets.shortObject}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Int Primitive</label>\r\n" +            
            "\t\t\t\t<input name=\"allWidgetsIntPrimitive\" type=\"text\" value=\"${allWidgets.intPrimitive}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Integer Object</label>\r\n" +
            "\t\t\t\t<input name=\"allWidgetsIntegerObject\" type=\"text\" value=\"${allWidgets.integerObject}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Ranged Int</label>\r\n" +            
            "\t\t\t\t<input name=\"allWidgetsRangedInt\" type=\"text\" value=\"${allWidgets.rangedInt}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Ranged Integer</label>\r\n" +
            "\t\t\t\t<input name=\"allWidgetsRangedInteger\" type=\"text\" value=\"${allWidgets.rangedInteger}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Long Primitive</label>\r\n" +
            "\t\t\t\t<input name=\"allWidgetsLongPrimitive\" type=\"text\" value=\"${allWidgets.longPrimitive}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label></label>\r\n" +
            "\t\t\t\t<input name=\"allWidgetsLongObject\" type=\"text\" value=\"${allWidgets.longObject}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Float Primitive</label>\r\n" +
            "\t\t\t\t<input name=\"allWidgetsFloatPrimitive\" type=\"text\" value=\"${allWidgets.floatPrimitive}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>nullInBundle</label>\r\n" +
            "\t\t\t\t<input name=\"allWidgetsFloatObject\" type=\"text\" value=\"${allWidgets.floatObject}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Double Primitive</label>\r\n" +
            "\t\t\t\t<input name=\"allWidgetsDoublePrimitive\" type=\"text\" value=\"${allWidgets.doublePrimitive}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label/>\r\n" +
            "\t\t\t\t<input name=\"allWidgetsDoubleObject\" type=\"text\" value=\"${allWidgets.doubleObject}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Char Primitive</label>\r\n" +
            "\t\t\t\t<input maxlength=\"1\" name=\"allWidgetsCharPrimitive\" type=\"text\" value=\"${allWidgets.charPrimitive}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Character Object</label>\r\n" +
            "\t\t\t\t<input maxlength=\"1\" name=\"allWidgetsCharacterObject\" type=\"text\" value=\"${allWidgets.characterObject}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Boolean Primitive</label>\r\n" +
            "\t\t\t\t<input name=\"allWidgetsBooleanPrimitive\" type=\"checkbox\" value=\"${allWidgets.booleanPrimitive}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Boolean Object</label>\r\n" +
            "\t\t\t\t<select name=\"allWidgetsBooleanObject\" value=\"${allWidgets.booleanObject}\">\r\n" +
            "\t\t\t\t\t<option/>\r\n" +
            "\t\t\t\t\t<option value=\"true\">Yes</option>\r\n" +
            "\t\t\t\t\t<option value=\"false\">No</option>\r\n" +
            "\t\t\t\t</select>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Dropdown</label>\r\n" +
            "\t\t\t\t<select name=\"allWidgetsDropdown\" value=\"${allWidgets.dropdown}\">\r\n" +
            "\t\t\t\t\t<option/>\r\n" +
            "\t\t\t\t\t<option value=\"foo1\"/>\r\n" +
            "\t\t\t\t\t<option value=\"dropdown1\"/>\r\n" +
            "\t\t\t\t\t<option value=\"bar1\"/>\r\n" +
            "\t\t\t\t</select>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Dropdown With Labels</label>\r\n" +
            "\t\t\t\t<select name=\"allWidgetsDropdownWithLabels\" value=\"${allWidgets.dropdownWithLabels}\">\r\n" +
            "\t\t\t\t\t<option/>\r\n" +
            "\t\t\t\t\t<option value=\"foo2\">Foo #2</option>\r\n" +
            "\t\t\t\t\t<option value=\"dropdown2\">Dropdown #2</option>\r\n" +
            "\t\t\t\t\t<option value=\"bar2\">Bar #2</option>\r\n" +
            "\t\t\t\t\t<option value=\"baz2\">Baz #2</option>\r\n" +
            "\t\t\t\t</select>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Not Null Dropdown</label>\r\n" +
            "\t\t\t\t<select name=\"allWidgetsNotNullDropdown\" value=\"${allWidgets.notNullDropdown}\">\r\n" +
            "\t\t\t\t\t<option value=\"-1\"/>\r\n" +
            "\t\t\t\t\t<option value=\"0\"/>\r\n" +
            "\t\t\t\t\t<option value=\"1\"/>\r\n" +
            "\t\t\t\t</select>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Not Null Object Dropdown</label>\r\n" +
            "\t\t\t\t<select name=\"allWidgetsNotNullObjectDropdown\" value=\"${allWidgets.notNullObjectDropdown}\">\r\n" +
            "\t\t\t\t\t<option value=\"foo3\"/>\r\n" +
            "\t\t\t\t\t<option value=\"dropdown3\"/>\r\n" +
            "\t\t\t\t\t<option value=\"bar3\"/>\r\n" +
            "\t\t\t\t\t<option value=\"baz3\"/>\r\n" +
            "\t\t\t\t\t<option value=\"abc3\"/>\r\n" +
            "\t\t\t\t\t<option value=\"def3\"/>\r\n" +
            "\t\t\t\t</select>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Nested Widgets</label>\r\n" +
            "\t\t\t\t<table id=\"table-orgMetawidgetIntegrationtestSharedAllwidgetsModelAllWidgetsnestedWidgets\">\r\n" +
            "\t\t\t\t\t<tbody/>\r\n" +
            "\t\t\t\t</table>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Read Only Nested Widgets</label>\r\n" +
            "\t\t\t\t<table id=\"table-orgMetawidgetIntegrationtestSharedAllwidgetsModelAllWidgetsreadOnlyNestedWidgets\">\r\n" +
            "\t\t\t\t\t<tbody/>\r\n" +
            "\t\t\t\t</table>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Nested Widgets Dont Expand</label>\r\n" +
            "\t\t\t\t<input name=\"allWidgetsNestedWidgetsDontExpand\" type=\"text\" value=\"${allWidgets.nestedWidgetsDontExpand}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Read Only Nested Widgets Dont Expand</label>\r\n" +
            "\t\t\t\t<span name=\"allWidgetsReadOnlyNestedWidgetsDontExpand\" value=\"${allWidgets.readOnlyNestedWidgetsDontExpand}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Date</label>\r\n" +
            "\t\t\t\t<input name=\"allWidgetsDate\" type=\"text\" value=\"${allWidgets.date}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Hidden</label>\r\n" +
            "\t\t\t\t<input name=\"allWidgetsHidden\" type=\"hidden\" value=\"${allWidgets.hidden}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Read Only</label>\r\n" +
            "\t\t\t\t<span name=\"allWidgetsReadOnly\" value=\"${allWidgets.readOnly}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t\t<tr>\r\n" +
            "\t\t\t<td>\r\n" +
            "\t\t\t\t<label>Mystery</label>\r\n" +
            "\t\t\t\t<input name=\"allWidgetsMystery\" type=\"text\" value=\"${allWidgets.mystery}\"/>\r\n" +
            "\t\t\t</td>\r\n" +
            "\t\t</tr>\r\n" +
            "\t</tbody>\r\n" +
            "</table>\r\n";
        
        StringWriter writer = new StringWriter();
        metawidget.write( writer, 0 );
        assertEquals( result, writer.toString() );
    }

}
