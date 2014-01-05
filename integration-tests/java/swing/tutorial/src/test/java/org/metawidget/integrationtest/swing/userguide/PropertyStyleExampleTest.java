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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
		assertEquals( 5, metawidget.getComponentCount() );
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
