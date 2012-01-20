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

package org.metawidget.statically.spring.widgetbuilder;


import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.spring.SpringInspectionResultConstants.*;

import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.util.CollectionUtils;

/**
 * JUnit test for the SpringWidgetBuilder, a WidgetBuilder for the StaticSpringMetawidget
 *
 * @author Ryan Bradley
 */

public class SpringWidgetBuilderTest
    extends TestCase {

    //
    // Public methods
    //

    public void testLookup() {

        SpringWidgetBuilder widgetBuilder = new SpringWidgetBuilder();
        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( LOOKUP, "${foo.bar}" );
        attributes.put( LOOKUP_LABELS, "foo.bar" );

        // Without 'required'

        StaticXmlWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
        assertEquals( "<form:select><form:option/><form:option label=\"foo.bar\" value=\"${foo.bar}\"/></form:select>", widget.toString() );

        // With 'required

        attributes.put( REQUIRED, TRUE );

        widget = widgetBuilder.buildWidget( PROPERTY, attributes, null);
        assertEquals( "<form:select><form:option label=\"foo.bar\" value=\"${foo.bar}\"/></form:select>", widget.toString() );
    }

    public void testSpringLookup() {

        // Without 'required'

        SpringWidgetBuilder widgetBuilder = new SpringWidgetBuilder();
        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( SPRING_LOOKUP, "${foo.bar}" );
        StaticXmlWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, null);
        assertEquals( "<form:select><form:option/><form:options items=\"${foo.bar}\"/></form:select>", widget.toString());

        // With 'required'

        attributes.put( REQUIRED, TRUE );
        widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
        assertEquals( "<form:select><form:options items=\"${foo.bar}\"/></form:select>", widget.toString() );
    }

    public void testCollection() {

        // SpringWidgetBuilder returns stubs for all collections at the moment.

        SpringWidgetBuilder widgetBuilder = new SpringWidgetBuilder();
        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( TYPE, Set.class.getName() );
        StaticXmlWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
        assertNull( widget );
    }

}
