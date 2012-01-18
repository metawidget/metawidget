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

package org.metawidget.integrationtest.swing.userguide;

import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;

import junit.framework.TestCase;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public class PropertyStyleExampleTest
	extends TestCase {

	//
	// Public methods
	//

	public void testPropertyStyleExample()
		throws Exception {

		Person person = new Person();

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new PropertyTypeInspector( new BaseObjectInspectorConfig().setPropertyStyle( new BundlePropertyStyle() ) ) );
		metawidget.setToInspect( person );

		assertEquals( "name", metawidget.getComponent( 1 ).getName() );
		assertEquals( "retired", metawidget.getComponent( 3 ).getName() );
		assertTrue( 5 == metawidget.getComponentCount() );
	}

	//
	// Inner classes
	//

	static class BundlePropertyStyle
		extends JavaBeanPropertyStyle {

		@Override
		protected Map<String, Property> inspectProperties( String type ) {

			try {
				Class<?> clazz = ClassUtils.niceForName( type );
				Map<String, Property> properties = CollectionUtils.newTreeMap();
				ResourceBundle bundle = ResourceBundle.getBundle( "org/metawidget/integrationtest/swing/userguide/Bundle" );

				for ( Enumeration<String> e = bundle.getKeys(); e.hasMoreElements(); ) {
					String key = e.nextElement();
					properties.put( key, new FieldProperty( key, clazz.getField( key ) ) );
				}

				return properties;
			} catch ( Exception ex ) {
				throw InspectorException.newException( ex );
			}
		}
	}

	public class Person {

		public String	name;

		public int		age;

		public boolean	retired;
	}
}
