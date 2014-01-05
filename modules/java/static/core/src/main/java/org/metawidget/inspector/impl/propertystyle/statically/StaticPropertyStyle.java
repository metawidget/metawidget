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

package org.metawidget.inspector.impl.propertystyle.statically;

import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.ValueAndDeclaredType;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleConfig;

/**
 * PropertyStyle for statically-declared properties.
 * <p>
 * StaticPropertyStyle sits at the same conceptual level as, say, JavaBeanPropertyStyle or
 * GroovyPropertyStyle. It allows Metawidget Inspectors to be repurposed to inspect static classes,
 * as opposed to objects. This allows the <em>same</em> Inspector to be used in either runtime or
 * static scenarios. For example JpaInspector can be used to inspect JPA annotations on classes for
 * either UIMetawidget or StaticUIMetawidget.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StaticPropertyStyle
	extends JavaBeanPropertyStyle {

	//
	// Constructor
	//

	public StaticPropertyStyle() {

		this( new JavaBeanPropertyStyleConfig() );
	}

	public StaticPropertyStyle( JavaBeanPropertyStyleConfig config ) {

		super( config );
	}

	//
	// Public methods
	//

	/**
	 * Traverses the given Class heirarchy using properties of the given names.
	 *
	 * @return the declared type (not actual type). May be null
	 */

	@Override
	public ValueAndDeclaredType traverse( Object toTraverse, String type, boolean onlyToParent, String... names ) {

		// Traverse through names (if any)

		if ( names == null || names.length == 0 ) {

			// If no names, no parent

			if ( onlyToParent ) {
				return new ValueAndDeclaredType( null, null );
			}

			return new ValueAndDeclaredType( null, type );
		}

		String traverseDeclaredType = type;

		for ( int loop = 0, length = names.length; loop < length; loop++ ) {

			if ( onlyToParent && loop >= length - 1 ) {
				return new ValueAndDeclaredType( null, traverseDeclaredType );
			}

			String name = names[loop];
			Property property = getProperties( traverseDeclaredType ).get( name );

			if ( property == null || !property.isReadable() ) {
				return new ValueAndDeclaredType( null, null );
			}

			traverseDeclaredType = property.getType();
		}

		return new ValueAndDeclaredType( null, traverseDeclaredType );
	}
}
