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

package org.metawidget.statically.freemarker.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.statically.BaseStaticXmlWidget;
import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.MetawidgetTestUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class FreemarkerLayoutTest
	extends TestCase {

	//
	// Public methods
	//

	public void testFreemarker() {

		FreemarkerLayoutConfig config = new FreemarkerLayoutConfig().setDirectoryForTemplateLoading( "./src/test/resources/org/metawidget/freemarker/layout" ).setTemplate( "template.ftl" );
		FreemarkerLayout layout = new FreemarkerLayout( config );

		StaticXmlMetawidget metawidget = new StaticXmlMetawidget() {

			@Override
			protected String getDefaultConfiguration() {

				return null;
			}
		};

		StaticXmlWidget container = new MyStaticXmlWidget( "container" );
		layout.startContainerLayout( container, metawidget );

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "foo" );
		layout.layoutWidget( new MyStaticXmlWidget( "widget1" ), PROPERTY, attributes, container, metawidget );

		attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "bar" );
		attributes.put( REQUIRED, "true" );
		layout.layoutWidget( new MyStaticXmlWidget( "widget2" ), PROPERTY, attributes, container, metawidget );

		layout.endContainerLayout( container, metawidget );

		StringBuilder builder = new StringBuilder();
		builder.append( "<prefix:container><table>\r\n" );
		builder.append( "\t<tbody>\r\n" );
		builder.append( "\t\t<tr>\r\n" );
		builder.append( "\t\t\t<th>Foo:</th>\r\n" );
		builder.append( "\t\t\t<td><prefix:widget1/></td>\r\n" );
		builder.append( "\t\t\t<td></td>\r\n" );
		builder.append( "\t\t</tr><tr>\r\n" );
		builder.append( "\t\t\t<th>Bar:</th>\r\n" );
		builder.append( "\t\t\t<td><prefix:widget2/></td>\r\n" );
		builder.append( "\t\t\t<td>*</td>\r\n" );
		builder.append( "\t\t</tr>\r\n" );
		builder.append( "\t</tbody>\r\n" );
		builder.append( "</table></prefix:container>" );

		assertEquals( builder.toString(), container.toString() );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( FreemarkerLayoutConfig.class, new FreemarkerLayoutConfig() {
			// Subclass
		} );
	}

	//
	// Inner class
	//

	class MyStaticXmlWidget
		extends BaseStaticXmlWidget {

		//
		// Constructor
		//

		public MyStaticXmlWidget( String tagName ) {

			super( "prefix", tagName, "namespace" );
		}
	}
}
