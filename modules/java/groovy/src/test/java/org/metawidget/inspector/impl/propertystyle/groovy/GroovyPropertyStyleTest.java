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

package org.metawidget.inspector.impl.propertystyle.groovy;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import junit.framework.TestCase;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class GroovyPropertyStyleTest
	extends TestCase {

	//
	// Public methods
	//

	public void testGroovy() {

		GroovyPropertyStyle propertyStyle = new GroovyPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( GroovyFoo.class.getName() );

		assertEquals( properties.size(), 8 );
		
		assertFalse( properties.get( "foo" ).getAnnotation( Column.class ).nullable() );
		assertEquals( Date.class.getName(), properties.get( "bar" ).getGenericType() );
		assertTrue( properties.get( "methodFoo" ).isAnnotationPresent( NotNull.class ) );
		assertEquals( 5, properties.get( "methodBar" ).getAnnotation( Length.class ).min() );
		assertEquals( String.class.getName(), properties.get( "methodBaz" ).getGenericType() );
		assertTrue( properties.get( "methodBaz" ).isReadable() );
		assertFalse( properties.get( "methodBaz" ).isWritable() );
		assertEquals( Boolean.class.getName(), properties.get( "methodAbc" ).getGenericType() );
		assertFalse( properties.get( "methodAbc" ).isReadable() );
		assertTrue( properties.get( "methodAbc" ).isWritable() );

		try {
			properties.get( "foo" ).read( new Object() );
			fail();
		} catch ( Exception e ) {
			// Should fail
		}

		// Test case insensitive ordering
		
		List<String> ordered = CollectionUtils.newArrayList( properties.keySet() );

		assertEquals( "FOo", ordered.get( 2 ));
		assertEquals( "foo", ordered.get( 3 ));
	}

	public void testIgnoreMetaArrayLengthProperty() {

		GroovyPropertyStyle propertyStyle = new GroovyPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( byte[].class.getName() );
		assertTrue( properties.isEmpty() );
	}

	public void testIgnoreInvalidTypes() {
		GroovyPropertyStyle propertyStyle = new GroovyPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( "invalid" );
		assertTrue( properties.isEmpty() );
	}
}
