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

import org.metawidget.inspector.iface.InspectorException;
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
	// Protected methods
	//

	/**
	 * Overriden because Scala does not use public fields.
	 */

	@Override
	protected void lookupFields( Map<String, Property> properties, Class<?> clazz )
	{
		// Not supported
	}

	/**
	 * Overriden because Scala uses <code>foo()</code> instead of <code>getFoo()</code>.
	 */

	@Override
	protected Method lookupAlternateGetter( String name, Class<?> clazz, Method getter )
	{
		try
		{
			return clazz.getMethod( name );
		}
		catch ( Exception e )
		{
			throw InspectorException.newException( e );
		}
	}

	/**
	 * Overriden because Scala uses <code>foo_$eq()</code> instead of <code>setFoo()</code>.
	 */

	@Override
	protected Method lookupAlternateSetter( String name, Class<?> clazz, Method setter )
	{
		try
		{
			return clazz.getMethod( name + "_$eq", setter.getParameterTypes()[0] );
		}
		catch ( Exception e )
		{
			throw InspectorException.newException( e );
		}
	}
}
