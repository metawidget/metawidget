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

package org.metawidget.inspector.impl.propertystyle.scala;

import java.lang.reflect.Method;
import java.util.Map;

import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;

/**
 * PropertyStyle for Scala-style properties.
 * <p>
 * Scala-style properties can <em>almost</em> be handled using <code>JavaBeanPropertyStyle</code>,
 * because the Scala compiler automatically generates JavaBean-style getters and setters as well as
 * Scala-style getters and setters. Unfortunately, it only copies any annotations defined on the
 * property to the Scala getters and setters, not the JavaBean ones. This <code>PropertyStyle</code>
 * is designed to access those annotations.
 * 
 * @author Richard Kennard
 */

public class ScalaPropertyStyle
	extends JavaBeanPropertyStyle
{
	//
	// Private statics
	//

	private final static String	SCALA_SET_SUFFIX	= "_$eq";

	//
	// Protected methods
	//

	/**
	 * Overridden because Scala always wraps properties with getter and setter methods. It never uses
	 * public fields directly.
	 */

	@Override
	protected void lookupFields( Map<String, Property> properties, Class<?> clazz )
	{
		// Not supported
	}

	/**
	 * Overridden because Scala uses <code>x()</code> instead of <code>getX()</code>.
	 */

	@Override
	protected String matchGetterNamingConvention( Method method )
	{
		try
		{
			String name = method.getName();
			method.getDeclaringClass().getDeclaredField( name );
			return name;
		}
		catch ( NoSuchFieldException e )
		{
			return null;
		}
	}

	/**
	 * Overridden because Scala uses <code>foo_$eq( x )</code> instead of <code>setFoo( x )</code>.
	 */

	@Override
	protected String matchSetterNamingConvention( Method method )
	{
		String methodName = method.getName();

		if ( !methodName.endsWith( SCALA_SET_SUFFIX ) )
			return null;

		return methodName.substring( 0, methodName.length() - SCALA_SET_SUFFIX.length() );
	}
}
