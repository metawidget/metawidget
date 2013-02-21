// Metawidget (licensed under LGPL)
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
 * but you have to use Scala's <code>BeanProperty</code> annotation everywhere which is cumbersome.
 * This <code>PropertyStyle</code> is designed to access Scala properties natively.
 * 
 * @author Richard Kennard
 */

public class ScalaPropertyStyle
	extends JavaBeanPropertyStyle {

	//
	// Private statics
	//

	/**
	 * Standard suffix applied to Scala-generated 'setter' methods. Note this is different from
	 * Scala-generated 'bean setter' methods, which follow standard JavaBean convention.
	 */

	private static final String	SCALA_SET_SUFFIX	= "_$eq";

	//
	// Constructor
	//

	public ScalaPropertyStyle() {

		super( new ScalaPropertyStyleConfig() );
	}

	//
	// Protected methods
	//

	/**
	 * Overridden because Scala always wraps properties with getter and setter methods. It never
	 * exposes public fields directly.
	 */

	@Override
	protected void lookupFields( Map<String, Property> properties, Class<?> clazz ) {

		// Not supported
	}

	/**
	 * Overridden because Scala uses <code>x()</code> instead of <code>getX()</code> (what they call
	 * a 'getter' versus a 'bean getter').
	 */

	@Override
	protected String isGetter( Method method ) {

		try {
			String name = method.getName();
			method.getDeclaringClass().getDeclaredField( name );
			return name;
		} catch ( NoSuchFieldException e ) {
			return null;
		}
	}

	/**
	 * Overridden because Scala uses <code>foo_$eq( x )</code> instead of <code>setFoo( x )</code>
	 * (what they call a 'setter' versus a 'bean setter').
	 */

	@Override
	protected String isSetter( Method method ) {

		String methodName = method.getName();

		if ( !methodName.endsWith( SCALA_SET_SUFFIX ) ) {
			return null;
		}

		return methodName.substring( 0, methodName.length() - SCALA_SET_SUFFIX.length() );
	}
}
