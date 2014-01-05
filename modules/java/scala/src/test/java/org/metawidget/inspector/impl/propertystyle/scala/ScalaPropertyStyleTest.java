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

package org.metawidget.inspector.impl.propertystyle.scala;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;

import junit.framework.TestCase;

import org.hibernate.validator.NotNull;
import org.metawidget.inspector.impl.propertystyle.Property;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ScalaPropertyStyleTest
	extends TestCase {

	//
	// Public methods
	//

	public void testScala() {

		ScalaPropertyStyle propertyStyle = new ScalaPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( org.metawidget.inspector.impl.propertystyle.scala.ScalaFoo.class.getName() );

		assertEquals( properties.size(), 3 );

		assertFalse( properties.get( "foo" ).getAnnotation( Column.class ).nullable() );
		assertTrue( properties.get( "bar" ).isAnnotationPresent( NotNull.class ) );
		assertEquals( Date.class.getName(), properties.get( "bar" ).getGenericType() );
		assertTrue( properties.get( "baz" ).isReadable() );
		assertFalse( properties.get( "baz" ).isWritable() );
	}
}
