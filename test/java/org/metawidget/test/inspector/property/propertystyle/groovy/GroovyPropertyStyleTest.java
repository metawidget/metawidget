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

package org.metawidget.test.inspector.property.propertystyle.groovy;

import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;

import junit.framework.TestCase;

import org.metawidget.groovytest.inspector.groovyproperty.propertystyle.GroovyFoo;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.groovy.GroovyPropertyStyle;

/**
 * @author Richard Kennard
 */

public class GroovyPropertyStyleTest
	extends TestCase
{
	//
	//
	// Public methods
	//
	//

	public void testGroovy()
	{
		GroovyPropertyStyle propertyStyle = new GroovyPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( GroovyFoo.class );

		assertTrue( properties.size() == 3 );

		Column column = properties.get( "foo" ).getAnnotation( Column.class );
		assertTrue( !column.nullable() );

		assertTrue( Date.class.equals( ((ParameterizedType) properties.get( "bar" ).getGenericType()).getActualTypeArguments()[0] ));
	}
}
