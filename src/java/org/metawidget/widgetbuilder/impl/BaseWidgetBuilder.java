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

package org.metawidget.widgetbuilder.impl;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;

/**
 * Convenience implementation.
 *
 * @author Richard Kennard
 */

public abstract class BaseWidgetBuilder<W, M extends W>
	implements WidgetBuilder<W, M>
{
	//
	// Public methods
	//

	public W buildWidget( String elementName, Map<String, String> attributes, M metawidget )
	{
		try
		{
			// Note: we tried further refining this to buildReadOnlyFieldWidget,
			// buildReadOnlyActionWidget, buildActiveFieldWidget, buildActiveActionWidget, but it wasn't
			// really better because we still had to pass 'elementName' to other methods (such as
			// UIMetawidget.getOverriddenWidget) and so it seemed simpler and more symmetrical to also
			// pass it here

			if ( TRUE.equals( attributes.get( READ_ONLY ) ) )
				return buildReadOnlyWidget( elementName, attributes, metawidget );

			// If the attribute has NO_SETTER, we consider it read-only.
			//
			// Note: this relies on complex attributes being rendered by nested Metawidgets, and the
			// nested Metawidgets will NOT have setReadOnly set on them. This gets us the desired
			// result: primitive types without a setter are rendered as read-only, complex types without
			// a setter are rendered as writeable (because their nested primitives are writeable).
			//
			// Furthermore, what is considered 'primitive' is up to the platform. Some
			// platforms may consider, say, an Address as 'primitive', using a dedicated Address
			// widget. Other platforms may consider an Address as complex, using a nested Metawidget.

			if ( TRUE.equals( attributes.get( NO_SETTER ) ) )
				return buildReadOnlyWidget( elementName, attributes, metawidget );

			return buildActiveWidget( elementName, attributes, metawidget );
		}
		catch( Exception e )
		{
			throw WidgetBuilderException.newException( e );
		}
	}

	//
	// Protected abstract methods
	//

	/**
	 * Build a read-only widget for this element.
	 * <p>
	 * Default implementation returns null, as most subclasses will only be interested in overriding
	 * <code>buildActiveWidget</code>.
	 */

	protected W buildReadOnlyWidget( String elementName, Map<String, String> attributes, M metawidget )
		throws Exception
	{
		return null;
	}

	protected abstract W buildActiveWidget( String elementName, Map<String, String> attributes, M metawidget )
		throws Exception;
}
