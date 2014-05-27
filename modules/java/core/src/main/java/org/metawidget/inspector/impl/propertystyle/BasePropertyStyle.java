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

package org.metawidget.inspector.impl.propertystyle;

import java.util.Map;
import java.util.Set;

import org.metawidget.inspector.impl.BaseTraitStyle;
import org.metawidget.inspector.impl.BaseTraitStyleConfig;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Convenience implementation for PropertyStyles.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class BasePropertyStyle
	extends BaseTraitStyle<Property>
	implements PropertyStyle {

	//
	// Constructor
	//

	protected BasePropertyStyle( BaseTraitStyleConfig config ) {

		super( config );
	}

	//
	// Public methods
	//

	public Map<String, Property> getProperties( String type ) {

		return getTraits( type );
	}

	/**
	 * Traverses the given Object heirarchy using properties of the given names.
	 * <p>
	 * Note: traversal involves calling Property.read, which invokes getter methods and can
	 * therefore have side effects. For example, a JSF controller 'ResourceController' may have a
	 * method 'getLoggedIn' which has to check the HttpSession, maybe even hit some EJBs or access
	 * the database.
	 */

	public ValueAndDeclaredType traverse( Object toTraverse, String type, boolean onlyToParent, String... names ) {

		// Special support for direct class lookup

		if ( toTraverse == null ) {
			// If there are names, return null

			if ( names != null && names.length > 0 ) {
				return new ValueAndDeclaredType( null, null );
			}

			return new ValueAndDeclaredType( null, type );
		}

		// Sanity check that the given toTraverse is, in fact, of the given type.
		//
		// This is harder than it seems. We cannot do a simple 'equals', but to do a
		// 'isAssignableFrom' we must instantiate the class. But instantiating the class may
		// not be possible because of proxies, virtual classes, what tier we're on, etc. Basically,
		// we can't be too strict about it.
		//
		// Use the toTraverse's ClassLoader, to support ClassLoader isolation (eg. classes from a
		// WAR being inspected by an EJB lib) and dynamic classes (eg. Groovy)

		Class<?> sanityCheck = ClassUtils.niceForName( type, toTraverse.getClass().getClassLoader() );

		if ( sanityCheck != null && !sanityCheck.isAssignableFrom( toTraverse.getClass() ) ) {
			return new ValueAndDeclaredType( null, null );
		}

		// Traverse through names (if any)

		Object traverse = toTraverse;
		String traverseDeclaredType = type;

		if ( names == null || names.length == 0 ) {

			// If no names, no parent

			if ( onlyToParent ) {
				return new ValueAndDeclaredType( null, null );
			}

		} else {
			Set<Object> traversed = CollectionUtils.newHashSet();
			traversed.add( traverse );

			int length = names.length;

			for ( int loop = 0; loop < length; loop++ ) {
				String name = names[loop];
				Property property = getProperties( traverse.getClass().getName() ).get( name );

				if ( property == null || !property.isReadable() ) {
					return new ValueAndDeclaredType( null, null );
				}

				Object parentTraverse = traverse;
				traverse = property.read( traverse );

				// Unlike BaseXmlInspector (which can never be certain it has detected a
				// cyclic reference because it only looks at types, not objects),
				// BaseObjectInspector can detect cycles and nip them in the bud

				if ( !traversed.add( traverse ) ) {
					// Trace, rather than do a debug log, because it makes for a nicer 'out
					// of the box' experience

					mLog.trace( "Prevented infinite recursion on {0}{1}. Consider marking {2} as hidden", type, ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ), name );
					return new ValueAndDeclaredType( null, null );
				}

				// Always come in this loop once, even if onlyToParent, because we
				// want to do the recursion check

				if ( onlyToParent && loop >= length - 1 ) {
					return new ValueAndDeclaredType( parentTraverse, traverseDeclaredType );
				}

				traverseDeclaredType = property.getType();

				// If no object, return null

				if ( traverse == null ) {

					// If reached the end of the names, can at least say what the declared type was

					if ( loop == length - 1 ) {
						return new ValueAndDeclaredType( null, traverseDeclaredType );
					}

					if ( onlyToParent && loop == length - 2 ) {
						return new ValueAndDeclaredType( null, traverseDeclaredType );
					}

					return new ValueAndDeclaredType( null, null );
				}

				if ( onlyToParent && loop == length - 2 ) {
					return new ValueAndDeclaredType( traverse, traverseDeclaredType );
				}
			}
		}

		return new ValueAndDeclaredType( traverse, traverseDeclaredType );
	}

	//
	// Protected methods
	//

	@Override
	protected final Map<String, Property> getUncachedTraits( String type ) {

		return inspectProperties( type );
	}

	/**
	 * @return the properties of the given class. Never null.
	 */

	protected abstract Map<String, Property> inspectProperties( String type );
}
