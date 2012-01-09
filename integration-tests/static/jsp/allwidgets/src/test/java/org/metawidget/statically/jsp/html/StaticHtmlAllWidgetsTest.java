package org.metawidget.statically.jsp.html;
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
        
        String result = "<table>\r\n" +
            "\t<input name=\"allWidgetsTextbox\" type=\"text\"/>\r\n" +
            "\t<input maxLength=\"20\" name=\"allWidgetsLimitedTextbox\" type=\"text\"/>\r\n" +
            "\t<textarea name=\"allWidgetsTextarea\"/>\r\n" +
            "\t<input name=\"allWidgetsPassword\" type=\"secret\"/>\r\n" +
            "\t<input name=\"allWidgetsBytePrimitive\" type=\"text\"/>\r\n" +
            "\t<input name=\"allWidgetsByteObject\" type=\"text\"/>\r\n" +
            "\t<input name=\"allWidgetsShortPrimitive\" type=\"text\"/>\r\n" +
            "\t<input name=\"allWidgetsShortObject\" type=\"text\"/>\r\n" +
            "\t<input name=\"allWidgetsIntPrimitive\" type=\"text\"/>\r\n" +
            "\t<input name=\"allWidgetsIntegerObject\" type=\"text\"/>\r\n" +
            "\t<input name=\"allWidgetsRangedInt\" type=\"text\"/>\r\n" +
            "\t<input name=\"allWidgetsRangedInteger\" type=\"text\"/>\r\n" +
            "\t<input name=\"allWidgetsLongPrimitive\" type=\"text\"/>\r\n" +
            "\t<input name=\"allWidgetsLongObject\" type=\"text\"/>\r\n" +
            "\t<input name=\"allWidgetsFloatPrimitive\" type=\"text\"/>\r\n" +
            "\t<input name=\"allWidgetsFloatObject\" type=\"text\"/>\r\n" +
            "\t<input name=\"allWidgetsDoublePrimitive\" type=\"text\"/>\r\n" +
            "\t<input name=\"allWidgetsDoubleObject\" type=\"text\"/>\r\n" +
            "\t<input maxLength=\"1\" name=\"allWidgetsCharPrimitive\" type=\"text\"/>\r\n" +
            "\t<input maxLength=\"1\" name=\"allWidgetsCharacterObject\" type=\"text\"/>\r\n" +
            "\t<input name=\"allWidgetsBooleanPrimitive\" type=\"checkbox\"/>\r\n" +
            "\t<select name=\"allWidgetsBooleanObject\">\r\n" +
            "\t\t<option/>\r\n" +
            "\t\t<option value=\"true\">Yes</option>\r\n" +
            "\t\t<option value=\"false\">No</option>\r\n" +
            "\t</select>\r\n" +
            "\t<select name=\"allWidgetsDropdown\">\r\n" +
            "\t\t<option/>\r\n" +
            "\t\t<option value=\"foo1\"/>\r\n" +
            "\t\t<option value=\"dropdown1\"/>\r\n" +
            "\t\t<option value=\"bar1\"/>\r\n" +
            "\t</select>\r\n" +
            "\t<select name=\"allWidgetsDropdownWithLabels\">\r\n" +
            "\t\t<option/>\r\n" +
            "\t\t<option value=\"foo2\">Foo #2</option>\r\n" +
            "\t\t<option value=\"dropdown2\">Dropdown #2</option>\r\n" +
            "\t\t<option value=\"bar2\">Bar #2</option>\r\n" +
            "\t\t<option value=\"baz2\">Baz #2</option>\r\n" +
            "\t</select>\r\n" +
            "\t<select name=\"allWidgetsNotNullDropdown\">\r\n" +
            "\t\t<option value=\"-1\"/>\r\n" +
            "\t\t<option value=\"0\"/>\r\n" +
            "\t\t<option value=\"1\"/>\r\n" +
            "\t</select>\r\n" +
            "\t<select name=\"allWidgetsNotNullObjectDropdown\">\r\n" +
            "\t\t<option value=\"foo3\"/>\r\n" +
            "\t\t<option value=\"dropdown3\"/>\r\n" +
            "\t\t<option value=\"bar3\"/>\r\n" +
            "\t\t<option value=\"baz3\"/>\r\n" +
            "\t\t<option value=\"abc3\"/>\r\n" +
            "\t\t<option value=\"def3\"/>\r\n" +
            "\t</select>\r\n" +
            "\t<table/>\r\n" +
            "\t<table/>\r\n" +
            "\t<input name=\"allWidgetsNestedWidgetsDontExpand\" type=\"text\"/>\r\n" +
            "\t<span name=\"allWidgetsReadOnlyNestedWidgetsDontExpand\" type=\"text\"/>\r\n" +
            "\t<input type=\"hidden\"/>\r\n" +
            "\t<span name=\"allWidgetsReadOnly\" type=\"text\"/>\r\n" +
            "\t<input name=\"allWidgetsMystery\" type=\"text\"/>\r\n"
            ;
        
        StringWriter writer = new StringWriter();
        metawidget.write( writer, 0 );
        assertEquals( result, writer.toString() );
    }

}
