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

package org.metawidget.inspector.xml;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;
import java.util.Set;

import org.metawidget.inspector.InspectionResultConstants;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseXmlInspector;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Element;

/**
 * Inspects <code>inspection-result-1.0.xsd</code>-compliant files (such as
 * <code>metawidget-metadata.xml</code>).
 * <p>
 * XmlInspector is a very simple Inspector: it takes as its input XML in the same format that
 * Inspectors usually output. It can be useful for declaring 'ad hoc' UI entities that do not map to
 * any Java class, as well as for declaring UI-specific attributes for existing Java classes (ie. if
 * you prefer not to use annotations, or if you want to introduce additional 'virtual' properties).
 * <p>
 * Note when using <code>XmlInspector</code> you should still try to avoid...
 *
 * @author Richard Kennard
 */

// TODO: doco here!

public class XmlInspector
	extends BaseXmlInspector
{
	//
	// Private members
	//

	private final PropertyStyle	mCheckForNullObject;

	//
	// Constructors
	//

	/**
	 * Constructs an XmlInspector.
	 * <p>
	 * Note XmlInspector requires a config. It does not have a default constructor, because the
	 * XmlInspectorConfig must be externally configured using <code>setResourceResolver</code> to
	 * support resolving resources from non-standard locations (such as <code>WEB-INF</code).
	 */

	public XmlInspector( XmlInspectorConfig config )
	{
		super( config );

		mCheckForNullObject = config.getCheckForNullObject();
	}

	//
	// Public methods
	//

	@Override
	public String inspect( Object toInspect, String type, String... names )
	{
		if ( mCheckForNullObject != null && isNullObject( toInspect, type, names ) )
			return null;

		return super.inspect( toInspect, type, names );
	}

	//
	// Protected methods
	//

	@Override
	protected String getExtendsAttribute()
	{
		return "extends";
	}

	@Override
	protected Map<String, String> inspectProperty( Element toInspect )
	{
		if ( PROPERTY.equals( toInspect.getNodeName() ) )
		{
			Map<String, String> attributes = XmlUtils.getAttributesAsMap( toInspect );

			// Warn about some common typos

			if ( attributes.containsKey( "readonly" ) )
				throw InspectorException.newException( "Attribute named 'readonly' should be '" + InspectionResultConstants.READ_ONLY + "'" );

			if ( attributes.containsKey( "dontexpand" ) )
				throw InspectorException.newException( "Attribute named 'dontexpand' should be '" + InspectionResultConstants.DONT_EXPAND + "'" );

			// All good

			return attributes;
		}

		return null;
	}

	@Override
	protected Map<String, String> inspectAction( Element toInspect )
	{
		if ( ACTION.equals( toInspect.getNodeName() ) )
			return XmlUtils.getAttributesAsMap( toInspect );

		return null;
	}

	//
	// Private methods
	//

	/**
	 * @return true if the type is a Java Class (ie. is not 'Login Screen') and the Object it maps
	 *         to is null
	 */

	private boolean isNullObject( Object toTraverse, String type, String... names )
	{
		// Special support for class lookup

		if ( toTraverse == null )
		{
			// If there are names, return true

			if ( names != null && names.length > 0 )
				return true;

			// If no such class, return false

			Class<?> clazz = ClassUtils.niceForName( type );

			if ( clazz == null )
				return false;

			return false;
		}

		// Use the toTraverse's ClassLoader, to support Groovy dynamic classes
		//
		// (note: for Groovy dynamic classes, this needs the applet to be signed - I think this is
		// still better than 'relaxing' this sanity check, as that would lead to differing behaviour
		// when deployed as an unsigned applet versus a signed applet)

		Class<?> traverseDeclaredType = ClassUtils.niceForName( type, toTraverse.getClass().getClassLoader() );

		if ( traverseDeclaredType == null || !traverseDeclaredType.isAssignableFrom( toTraverse.getClass() ) )
			return false;

		// Traverse through names (if any)

		Object traverse = toTraverse;

		if ( names != null && names.length > 0 )
		{
			Set<Object> traversed = CollectionUtils.newHashSet();
			traversed.add( traverse );

			int length = names.length;

			for ( int loop = 0; loop < length; loop++ )
			{
				String name = names[loop];
				Property property = mCheckForNullObject.getProperties( traverse.getClass() ).get( name );

				if ( property == null || !property.isReadable() )
					return true;

				traverse = property.read( traverse );

				// Detect cycles and nip them in the bud

				if ( !traversed.add( traverse ) )
				{
					// Trace, rather than do a debug log, because it makes for a nicer 'out
					// of the box' experience

					mLog.trace( ClassUtils.getSimpleName( getClass() ) + " prevented infinite recursion on " + type + ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ) + ". Consider annotating " + name + " as @UiHidden" );
					return true;
				}

				// Always come in this loop once, because we want to do the recursion check

				if ( traverse == null )
					return true;

				traverseDeclaredType = property.getType();
			}
		}

		return false;
	}
}
