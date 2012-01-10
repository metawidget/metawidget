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

package org.metawidget.statically.spring;

import java.io.StringWriter;

import org.metawidget.integrationtest.shared.allwidgets.model.AllWidgets;

import junit.framework.TestCase;

public class StaticSpringAllWidgetsTest
    extends TestCase {
    
    //
    // Public methods
    //
    
    public void testAllWidgets() {
        
        StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
        metawidget.setConfig( "org/metawidget/integrationtest/static/spring/allwidgets/metawidget.xml" );
        metawidget.setValue( "${allWidgets}" );
        metawidget.setPath( AllWidgets.class.getName() );
        
        String result = "<table>\r\n" +
            "\t<form:input path=\"textbox\"/>\r\n" +
            "\t<form:input maxLength=\"20\" path=\"limitedTextbox\"/>\r\n" +
            "\t<textarea path=\"textarea\"/>\r\n" +
            "\t<input path=\"password\" type=\"secret\"/>\r\n" +
            "\t<form:input path=\"bytePrimitive\"/>\r\n" +
            "\t<form:input path=\"byteObject\"/>\r\n" +
            "\t<form:input path=\"shortPrimitive\"/>\r\n" +
            "\t<form:input path=\"shortObject\"/>\r\n" +
            "\t<form:input path=\"intPrimitive\"/>\r\n" +
            "\t<form:input path=\"integerObject\"/>\r\n" +
            "\t<form:input path=\"rangedInt\"/>\r\n" +
            "\t<form:input path=\"rangedInteger\"/>\r\n" +
            "\t<form:input path=\"longPrimitive\"/>\r\n" +
            "\t<form:input path=\"longObject\"/>\r\n" +
            "\t<form:input path=\"floatPrimitive\"/>\r\n" +
            "\t<form:input path=\"floatObject\"/>\r\n" +
            "\t<form:input path=\"doublePrimitive\"/>\r\n" +
            "\t<form:input path=\"doubleObject\"/>\r\n" +
            "\t<form:input maxLength=\"1\" path=\"charPrimitive\"/>\r\n" +
            "\t<form:input maxLength=\"1\" path=\"characterObject\"/>\r\n" +
            "\t<input path=\"booleanPrimitive\" type=\"checkbox\"/>\r\n" +
            "\t<form:select path=\"booleanObject\">\r\n" +
            "\t\t<form:option/>\r\n" +
            "\t\t<form:option value=\"true\">Yes</form:option>\r\n" +
            "\t\t<form:option value=\"false\">No</form:option>\r\n" +
            "\t</form:select>\r\n" +
            "\t<form:select path=\"dropdown\">\r\n" +
            "\t\t<form:option/>\r\n" +
            "\t\t<form:option value=\"foo1\"/>\r\n" +
            "\t\t<form:option value=\"dropdown1\"/>\r\n" +
            "\t\t<form:option value=\"bar1\"/>\r\n" +
            "\t</form:select>\r\n" +
            "\t<form:select path=\"dropdownWithLabels\">\r\n" +
            "\t\t<form:option/>\r\n" +
            "\t\t<form:option value=\"foo2\">Foo #2</form:option>\r\n" +
            "\t\t<form:option value=\"dropdown2\">Dropdown #2</form:option>\r\n" +
            "\t\t<form:option value=\"bar2\">Bar #2</form:option>\r\n" +
            "\t\t<form:option value=\"baz2\">Baz #2</form:option>\r\n" +
            "\t</form:select>\r\n" +
            "\t<form:select path=\"notNullDropdown\">\r\n" +
            "\t\t<form:option value=\"-1\"/>\r\n" +
            "\t\t<form:option value=\"0\"/>\r\n" +
            "\t\t<form:option value=\"1\"/>\r\n" +
            "\t</form:select>\r\n" +
            "\t<form:select path=\"notNullObjectDropdown\">\r\n" +
            "\t\t<form:option value=\"foo3\"/>\r\n" +
            "\t\t<form:option value=\"dropdown3\"/>\r\n" +
            "\t\t<form:option value=\"bar3\"/>\r\n" +
            "\t\t<form:option value=\"baz3\"/>\r\n" +
            "\t\t<form:option value=\"abc3\"/>\r\n" +
            "\t\t<form:option value=\"def3\"/>\r\n" +
            "\t</form:select>\r\n" +
            
            /*
             * Nested Metawidget support is not fully debugged yet. 
             */
            
            "\t<table/>\r\n" +
            "\t<table/>\r\n" +
            "\t<form:input path=\"nestedWidgetsDontExpand\"/>\r\n" +
            "\t<span path=\"readOnlyNestedWidgetsDontExpand\"/>\r\n" +
            "\t<form:input path=\"date\"/>\r\n" +
            "\t<form:hidden path=\"hidden\"/>\r\n" +
            "\t<span path=\"readOnly\"/>\r\n" +
            "\t<form:input path=\"mystery\"/>\r\n" +

            /*
             * Collection and doAction not handled properly yet.
             */
            
            "</table>\r\n" +
            "<form:hidden path=\"hidden\"/>\r\n"
            ;
        
        StringWriter writer = new StringWriter();
        metawidget.write( writer, 0 );
        assertEquals( result, writer.toString() );
    }

}
