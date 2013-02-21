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

package org.metawidget.statically.spring.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.html.layout.HtmlTableLayoutConfig;
import org.metawidget.statically.html.widgetbuilder.HtmlDiv;
import org.metawidget.statically.spring.StaticSpringMetawidget;
import org.metawidget.statically.spring.widgetbuilder.FormInputTag;
import org.metawidget.util.CollectionUtils;

import junit.framework.Assert;
import junit.framework.TestCase;

public class SpringTableLayoutTest
    extends TestCase {

    //
    // Public methods
    //

    public void testSpringTableLayout()
        throws Exception {

        HtmlTableLayoutConfig config = new HtmlTableLayoutConfig();
        config.setTableStyle( "tableStyle" );
        config.setTableStyleClass( "tableStyleClass" );
        config.setLabelColumnStyleClass( "label" );
        config.setComponentColumnStyleClass( "component" );
        config.setRequiredColumnStyleClass( "required" );
        SpringTableLayout layout = new SpringTableLayout( config );

        StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
        metawidget.setValue("foo");
        HtmlDiv container = new HtmlDiv();
        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( NAME, "bar" );
        FormInputTag input = new FormInputTag();
        layout.startContainerLayout( container, metawidget );
        layout.layoutWidget( input, PROPERTY, attributes, container, metawidget );
        layout.endContainerLayout( container, metawidget );

        StringBuilder builder = new StringBuilder();
        builder.append( "<div><table class=\"tableStyleClass\" style=\"tableStyle\"><tbody><tr>" );
        builder.append( "<th class=\"label\"><form:label path=\"bar\">Bar:</form:label></th>" );
        builder.append( "<td class=\"component\"><form:input/></td>" );
        builder.append( "<td class=\"required\"/></tr></tbody></table></div>" );
        Assert.assertEquals( builder.toString(), container.toString() );
    }
}
