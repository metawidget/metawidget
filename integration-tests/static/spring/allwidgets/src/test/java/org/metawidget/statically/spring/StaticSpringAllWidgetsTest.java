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
        
        String result = "<table>" +
            "\t<form:input path=\"${allWidgets.mTextbox}\" value=\"Textbox\"/>\r\n" +
            "\t<form:input path=\"${allWidgets.mLimitedTextbox}\" value=\"Limited Textbox\"/>\r\n" +
            "\t<form:input path=\"${allWidgets.mTextarea\" value=\"Textarea\"/>/r/n" +
            "\t<form:input path=\"${allWidgets.mPassword}\" value=\"Password\"/>/r/n" +
            "\t<form:input path=\"${allWidgets.mBytePrimitive}\" value=\"1111111\"/>/r/n" +
            "\t<form:input path=\"${allWidgets.mByteObject}\" value=\"0000000\"/>/r/n" +
            "\t<form:input path=\"${allWidgets.mShortPrimitive}\" value\"\"/>/r/n"
            ;
        
        StringWriter writer = new StringWriter();
        metawidget.write( writer, 0 );
        assertEquals( result, writer.toString() );
    }

}
