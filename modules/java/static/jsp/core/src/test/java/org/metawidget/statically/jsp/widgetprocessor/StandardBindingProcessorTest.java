// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.statically.jsp.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.statically.html.widgetbuilder.HtmlInput;
import org.metawidget.statically.jsp.StaticJspMetawidget;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StandardBindingProcessorTest
    extends TestCase {

    //
    // Public methods
    //

    public void testWidgetProcessor() {

        StandardBindingProcessor processor = new StandardBindingProcessor();

        HtmlInput input = new HtmlInput();
        Map<String, String> attributes = CollectionUtils.newHashMap();

        // Normal

        attributes.put( NAME, "bar" );
        StaticJspMetawidget metawidget = new StaticJspMetawidget();
        metawidget.setValue( "${foo}" );
        input.putAttribute( "value", "${foo.bar}" );
        processor.processWidget( input, PROPERTY, attributes, metawidget );
        assertEquals( "<input value=\"${foo.bar}\"/>", input.toString() );

        // Do not overwrite existing

        attributes.put( NAME, "baz" );
        processor.processWidget( input, PROPERTY, attributes, metawidget );
        assertEquals( "<input value=\"${foo.bar}\"/>", input.toString() );
    }

}
