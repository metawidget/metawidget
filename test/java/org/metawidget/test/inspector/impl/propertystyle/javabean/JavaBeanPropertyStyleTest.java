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

package org.metawidget.test.inspector.impl.propertystyle.javabean;

import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import junit.framework.TestCase;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;

/**
 * @author Richard Kennard
 */

public class JavaBeanPropertyStyleTest
	extends TestCase
{
	//
	//
	// Public methods
	//
	//

	public void testJavaBean()
	{
		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( Foo.class );

		assertTrue( properties.size() == 7 );

		assertTrue( "foo".equals( properties.get( "foo" ).toString() ) );
		assertTrue( !properties.get( "foo" ).getAnnotation( Column.class ).nullable() );
		assertTrue( "bar".equals( properties.get( "bar" ).getName() ) );
		assertTrue( Date.class.equals( ( (ParameterizedType) properties.get( "bar" ).getGenericType() ).getActualTypeArguments()[0] ) );
		assertTrue( properties.get( "methodFoo" ).isAnnotationPresent( NotNull.class ) );
		assertTrue( 5 == properties.get( "methodBar" ).getAnnotation( Length.class ).min() );
		assertTrue( String.class.equals( ( (ParameterizedType) properties.get( "methodBaz" ).getGenericType() ).getActualTypeArguments()[0] ) );
		assertTrue( properties.get( "methodBaz" ).isReadable() );
		assertTrue( !properties.get( "methodBaz" ).isWritable() );
		assertTrue( Boolean.class.equals( ( (ParameterizedType) properties.get( "methodAbc" ).getGenericType() ).getActualTypeArguments()[0] ) );
		assertTrue( !properties.get( "methodAbc" ).isReadable() );
		assertTrue( properties.get( "methodAbc" ).isWritable() );

		try
		{
			properties.get( "foo" ).read( new Object() );
			assertTrue( false );
		}
		catch ( Exception e )
		{
			// Should fail
		}

		// Test excludeBaseTypes

		assertTrue( "baz".equals( properties.get( "baz" ).getName() ));

		propertyStyle = new FooPropertyStyle();
		properties = propertyStyle.getProperties( Foo.class );
		assertTrue( properties.size() == 6 );

		assertTrue( properties.get( "baz" ) == null );
	}

	//
	//
	// Inner class
	//
	//

	class Foo
		extends SuperFoo
	{
		@Column( nullable = false )
		public String		foo;

		public List<Date>	bar;

		@NotNull
		public String getMethodFoo()
		{
			return null;
		}

		@Length( min = 5 )
		public void setMethodBar( String methodBar )
		{
			// Do nothing
		}

		public List<String> getMethodBaz()
		{
			return null;
		}

		public void setMethodAbc( List<Boolean> methodAbc )
		{
			// Do nothing
		}
	}

	class SuperFoo
	{
		public boolean	baz;
	}

	class FooPropertyStyle
		extends JavaBeanPropertyStyle
	{
		@Override
		protected boolean isExcludedBaseType( Class<?> clazz )
		{
			if ( SuperFoo.class.equals( clazz ))
				return true;

			return super.isExcludedBaseType( clazz );
		}
	}
}
