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

import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.statically.StaticPropertyStyle;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.statically.StaticMetawidget;
import org.metawidget.statically.StaticWidget;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.jsp.StaticJspMetawidget;
import org.metawidget.statically.jsp.widgetprocessor.StandardBindingProcessor;
import org.metawidget.statically.layout.SimpleLayout;
import org.metawidget.statically.spring.StaticSpringMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

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
		assertEquals( "<form:select><form:option value=\"\"/><form:option label=\"foo.bar\" value=\"${foo.bar}\"/></form:select>", widget.toString() );

		// With 'required

		attributes.put( REQUIRED, TRUE );

		widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "<form:select><form:option label=\"foo.bar\" value=\"${foo.bar}\"/></form:select>", widget.toString() );
	}

	public void testSpringLookup() {

		// Without 'required'

		SpringWidgetBuilder widgetBuilder = new SpringWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( SPRING_LOOKUP, "${foo.bar}" );
		StaticXmlWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertNull( widget );
		attributes.put( NAME, "${bar}" );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "<form:select items=\"${bar}\"><form:option value=\"\"/></form:select>", widget.toString() );

		// With 'required'

		attributes.put( REQUIRED, TRUE );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "<form:select items=\"${bar}\"/>", widget.toString() );
	}

	public void testCollection() {

		Inspector inspector = new PropertyTypeInspector( new BaseObjectInspectorConfig().setPropertyStyle( new StaticPropertyStyle() ));
		StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
		metawidget.setConfig( "org/metawidget/statically/spring/widgetbuilder/metawidget-test-collection.xml" );
		metawidget.setPath( Foo.class.getName() );
		metawidget.setInspector( inspector );
		SpringWidgetBuilder widgetBuilder = new SpringWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "bar" );
		attributes.put( TYPE, Set.class.getName() );

		// SpringWidgetBuilder defers to StaticJspMetawidget for Collections

		StaticXmlWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertTrue( widget instanceof StaticJspMetawidget );
		assertTrue( inspector == ( (StaticJspMetawidget) widget ).getInspector() );
		assertTrue( ( (StaticJspMetawidget) widget ).getLayout() instanceof SimpleLayout );
		assertEquals( Foo.class.getName() + "/bar", ( (StaticJspMetawidget) widget ).getPath() );
		assertEquals( "org/metawidget/statically/spring/widgetbuilder/metawidget-test-collection.xml", ( (StaticJspMetawidget) widget ).getConfig() );
		List<WidgetProcessor<StaticWidget, StaticMetawidget>> widgetProcessors = ( (StaticJspMetawidget) widget ).getWidgetProcessors();
		assertEquals( 1, widgetProcessors.size() );
		assertTrue( ((WidgetProcessor<?,?>) widgetProcessors.get( 0 )) instanceof StandardBindingProcessor );
		assertEquals( "<table><thead><tr><th>Abc</th><th>Def</th></tr></thead><tbody><c:forEach items=\"${bar}\" var=\"item\"><tr><td><c:out value=\"${item.abc}\"/></td><td><c:out value=\"${item.def}\"/></td></tr></c:forEach></tbody></table>", widget.toString() );
	}

	//
	// Inner class
	//

	public static class Foo {

		public Set<Bar> getBar() {
			return null;
		}
	}

	public static class Bar {

		public String getAbc() {
			return null;
		}

		public String getDef() {
			return null;
		}
	}
}
