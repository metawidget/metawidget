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
import java.util.TreeMap;

import javax.persistence.Column;

import junit.framework.TestCase;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.metawidget.inspector.annotation.UiMasked;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;

/**
 * @author Richard Kennard
 */

public class JavaBeanPropertyStyleTest
	extends TestCase
{
	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public JavaBeanPropertyStyleTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testJavaBeanPropertyStyle()
	{
		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( Foo.class );

		assertTrue( properties instanceof TreeMap );
		assertTrue( properties.size() == 10 );

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
		assertTrue( properties.get( "methodGetterInSuper" ).isReadable() );
		assertTrue( properties.get( "methodGetterInSuper" ).isWritable() );
		assertTrue( properties.get( "methodSetterInSuper" ).isReadable() );
		assertTrue( properties.get( "methodSetterInSuper" ).isWritable() );
		assertTrue( String.class.equals( properties.get( "methodCovariant" ).getType() ));

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
		assertTrue( properties.size() == 9 );

		assertTrue( properties.get( "baz" ) == null );
		assertTrue( !properties.get( "methodGetterInSuper" ).isReadable() );
		assertTrue( properties.get( "methodGetterInSuper" ).isWritable() );
		assertTrue( properties.get( "methodSetterInSuper" ).isReadable() );
		assertTrue( !properties.get( "methodSetterInSuper" ).isWritable() );
		assertTrue( String.class.equals( properties.get( "methodCovariant" ).getType() ));
	}

	public void testInterfaceBasedPropertyStyle()
	{
		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( Proxied_$$_javassist_.class );

		assertTrue( properties instanceof TreeMap );
		assertTrue( properties.get( "interfaceBar" ).isAnnotationPresent( UiMasked.class ));

		properties = propertyStyle.getProperties( new InterfaceFoo()
		{
			@Override
			public Object getInterfaceBar()
			{
				return null;
			}
		}.getClass() );

		assertTrue( properties instanceof TreeMap );
		assertTrue( !properties.get( "interfaceBar" ).isAnnotationPresent( UiMasked.class ));
	}

	//
	// Inner class
	//

	class Foo
		extends SuperFoo
	{
		@Column( nullable = false )
		public String		foo;

		public List<Date>	bar;

		public String getFoo()
		{
			// Test already found via its field

			return null;
		}

		public void setFoo( String aFoo )
		{
			// Test already found via its field
		}

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

		@Override
		public void setMethodGetterInSuper( String methodGetterInSuper )
		{
			// Do nothing
		}

		public String getMethodSetterInSuper()
		{
			return null;
		}

		@Override
		public String getMethodCovariant()
		{
			return null;
		}

		public String getterIgnoreBecauseLowercase()
		{
			return null;
		}

		public void setterIgnoreBecauseLowercase( String ignore )
		{
			// Do nothing
		}
	}

	class SuperFoo
	{
		public boolean	baz;

		public String getMethodGetterInSuper()
		{
			return null;
		}

		public void setMethodGetterInSuper( String methodSetterInSuper )
		{
			// Do nothing
		}

		public void setMethodSetterInSuper( String methodSetterInSuper )
		{
			// Do nothing
		}

		public Object getMethodCovariant()
		{
			return null;
		}
	}

	class FooPropertyStyle
		extends JavaBeanPropertyStyle
	{
		@Override
		protected boolean isExcludedBaseType( String className )
		{
			if ( SuperFoo.class.getName().equals( className ))
				return true;

			return super.isExcludedBaseType( className );
		}
	}

	class Proxied_$$_javassist_
		implements InterfaceFoo
	{
		@Override
		public Object getInterfaceBar()
		{
			return null;
		}
	}

	interface InterfaceFoo
	{
		@UiMasked
		Object getInterfaceBar();
	}
}
