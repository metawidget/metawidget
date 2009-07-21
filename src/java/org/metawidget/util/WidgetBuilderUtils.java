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

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.propertytype.PropertyTypeInspectionResultConstants.*;

import java.util.Map;

/**
 * Utilities for working with WidgetBuilders.
 * <p>
 * Some of the logic behind WidgetBuilder decisions can be a little involved, so we refactor it
 * here.
 * <p>
 * Not located under <code>org.metawidget.widgetbuilder.impl</code> because GWT cannot compile this
 * class.
 *
 * @author Richard Kennard
 */

public final class WidgetBuilderUtils
{
	//
	// Public methods
	//

	/**
	 * Looks up the TYPE attribute, but first checks the ACTUAL_CLASS attribute.
	 *
	 * @return ACTUAL_CLASS of, if none, TYPE or, if none, null. Never an empty String.
	 */

	public static String getActualClassOrType( Map<String, String> attributes )
	{
		String type = attributes.get( ACTUAL_CLASS );

		if ( type != null && !"".equals( type ) )
			return type;

		type = attributes.get( TYPE );

		if ( "".equals( type ) )
			return null;

		return type;
	}

	/**
	 * Returns true if the lookup is nullable, not required, or has a forced empty choice.
	 */

	public static boolean needsEmptyLookupItem( Map<String, String> attributes)
	{
		if ( TRUE.equals( attributes.get( LOOKUP_HAS_EMPTY_CHOICE ) ) )
			return true;

		if ( TRUE.equals( attributes.get( REQUIRED ) ) )
			return false;

		String type = getActualClassOrType( attributes );

		// Type can be null if this lookup was specified by a metawidget-metadata.xml
		// and the type was omitted from the XML. In that case, assume nullable
		//
		// Note: there's an extra caveat for Groovy dynamic types: if we can't load
		// the class, assume it is non-primitive and therefore add a null choice

		if ( type != null )
		{
			Class<?> clazz = ClassUtils.niceForName( type );

			if ( clazz != null && clazz.isPrimitive() )
				return false;
		}

		return true;
	}

	//
	// Private constructor
	//

	private WidgetBuilderUtils()
	{
		// Can never be called
	}
}
