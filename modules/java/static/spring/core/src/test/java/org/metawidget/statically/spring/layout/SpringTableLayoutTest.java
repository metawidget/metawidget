// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

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

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

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
