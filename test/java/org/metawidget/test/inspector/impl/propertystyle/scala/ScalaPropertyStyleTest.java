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

package org.metawidget.test.inspector.impl.propertystyle.scala;

import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;

import junit.framework.TestCase;

import org.hibernate.validator.NotNull;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.scala.ScalaPropertyStyle;

/**
 * @author Richard Kennard
 */

public class ScalaPropertyStyleTest
	extends TestCase
{
	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public ScalaPropertyStyleTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testScala()
	{
		ScalaPropertyStyle propertyStyle = new ScalaPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( org.metawidget.scalatest.inspector.impl.propertystyle.scala.ScalaFoo.class );

		assertTrue( properties.size() == 3 );

		assertTrue( !properties.get( "foo" ).getAnnotation( Column.class ).nullable() );
		assertTrue( properties.get( "bar" ).isAnnotationPresent( NotNull.class ) );
		assertTrue( Date.class.equals( ( (ParameterizedType) properties.get( "bar" ).getGenericType() ).getActualTypeArguments()[0] ) );
		assertTrue( properties.get( "baz" ).isReadable() );
		assertTrue( !properties.get( "baz" ).isWritable() );
	}
}
