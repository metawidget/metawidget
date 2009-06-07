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

package org.metawidget.test.inspector.impl.propertystyle.groovy;

import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;

import junit.framework.TestCase;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.groovy.GroovyPropertyStyle;

/**
 * @author Richard Kennard
 */

public class GroovyPropertyStyleTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testGroovy()
	{
		GroovyPropertyStyle propertyStyle = new GroovyPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( GroovyFoo.class );

		assertTrue( properties.size() == 7 );

		assertTrue( !properties.get( "foo" ).getAnnotation( Column.class ).nullable() );
		assertTrue( Date.class.equals( ((ParameterizedType) properties.get( "bar" ).getGenericType()).getActualTypeArguments()[0] ));
		assertTrue( properties.get( "methodFoo" ).isAnnotationPresent( NotNull.class ));
		assertTrue( 5 == properties.get( "methodBar" ).getAnnotation( Length.class ).min() );
		assertTrue( String.class.equals( ((ParameterizedType) properties.get( "methodBaz" ).getGenericType()).getActualTypeArguments()[0] ) );
		assertTrue( properties.get( "methodBaz" ).isReadable() );
		assertTrue( !properties.get( "methodBaz" ).isWritable() );
		assertTrue( Boolean.class.equals( ((ParameterizedType) properties.get( "methodAbc" ).getGenericType()).getActualTypeArguments()[0] ) );
		assertTrue( !properties.get( "methodAbc" ).isReadable() );
		assertTrue( properties.get( "methodAbc" ).isWritable() );

		try
		{
			properties.get( "foo" ).read( new Object() );
			assertTrue( false );
		}
		catch( Exception e )
		{
			// Should fail
		}
	}

	public void testIgnoreMetaArrayLengthProperty()
	{
		GroovyPropertyStyle propertyStyle = new GroovyPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( byte[].class );
		assertTrue( properties.isEmpty() );
	}
}
