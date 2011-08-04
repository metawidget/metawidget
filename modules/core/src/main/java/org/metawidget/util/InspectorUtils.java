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

package org.metawidget.util;

import java.util.Set;

import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.util.LogUtils.Log;
import org.metawidget.util.simple.Pair;
import org.metawidget.util.simple.StringUtils;

/**
 * Utilities for working with Inspectors.
 * <p>
 * Some of the logic behind Inspectors can be a little involved, so we refactor it here.
 *
 * @author Richard Kennard
 */

public final class InspectorUtils {

	//
	// Public methods
	//

	/**
	 * Traverses the given Object using properties of the given names.
	 * <p>
	 * Note: traversal involves calling Property.read, which invokes getter methods and can
	 * therefore have side effects. For example, a JSF controller 'ResourceController' may have a
	 * method 'getLoggedIn' which has to check the HttpSession, maybe even hit some EJBs or access
	 * the database.
	 *
	 * @return a tuple of Object (may be null) and declared type (not actual type). Never null
	 */

	public static Pair<Object, Class<?>> traverse( PropertyStyle propertyStyle, Object toTraverse, String type, boolean onlyToParent, String... names ) {

		// Special support for direct class lookup

		if ( toTraverse == null ) {
			// If there are names, return null

			if ( onlyToParent ) {
				return new Pair<Object, Class<?>>( null, null );
			}

			return new Pair<Object, Class<?>>( null, ClassUtils.niceForName( type ) );
		}

		// Use the toTraverse's ClassLoader, to support Groovy dynamic classes
		//
		// (note: for Groovy dynamic classes, this needs the applet to be signed - I think this is
		// still better than 'relaxing' this sanity check, as that would lead to differing behaviour
		// when deployed as an unsigned applet versus a signed applet)

		Class<?> traverseDeclaredType = ClassUtils.niceForName( type, toTraverse.getClass().getClassLoader() );

		if ( traverseDeclaredType == null || !traverseDeclaredType.isAssignableFrom( toTraverse.getClass() ) ) {
			return new Pair<Object, Class<?>>( null, null );
		}

		// Traverse through names (if any)

		Object traverse = toTraverse;

		if ( names == null || names.length == 0 ) {

			// If no names, no parent

			if ( onlyToParent ) {
				return new Pair<Object, Class<?>>( null, null );
			}

		} else {
			Set<Object> traversed = CollectionUtils.newHashSet();
			traversed.add( traverse );

			int length = names.length;

			for ( int loop = 0; loop < length; loop++ ) {
				String name = names[loop];
				Property property = propertyStyle.getProperties( traverse.getClass() ).get( name );

				if ( property == null || !property.isReadable() ) {
					return new Pair<Object, Class<?>>( null, null );
				}

				Object parentTraverse = traverse;
				traverse = property.read( traverse );

				// Unlike BaseXmlInspector (which can never be certain it has detected a
				// cyclic reference because it only looks at types, not objects),
				// BaseObjectInspector can detect cycles and nip them in the bud

				if ( !traversed.add( traverse ) ) {
					// Trace, rather than do a debug log, because it makes for a nicer 'out
					// of the box' experience

					LOG.trace( "Prevented infinite recursion on {0}{1}. Consider marking {2} as hidden", type, ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ), name );
					return new Pair<Object, Class<?>>( null, null );
				}

				// Always come in this loop once, even if onlyToParent, because we
				// want to do the recursion check

				if ( onlyToParent && loop >= length - 1 ) {
					return new Pair<Object, Class<?>>( parentTraverse, traverseDeclaredType );
				}

				if ( traverse == null ) {
					return new Pair<Object, Class<?>>( null, null );
				}

				traverseDeclaredType = property.getType();
			}
		}

		return new Pair<Object, Class<?>>( traverse, traverseDeclaredType );
	}

	//
	// Private statics
	//

	private static final Log	LOG	= LogUtils.getLog( InspectorUtils.class );

	//
	// Private constructor
	//

	private InspectorUtils() {

		// Can never be called
	}
}
