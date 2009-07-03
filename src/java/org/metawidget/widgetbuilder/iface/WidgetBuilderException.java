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

package org.metawidget.widgetbuilder.iface;

import org.metawidget.MetawidgetException;

/**
 * Any exception that occurs widget building.
 */

public class WidgetBuilderException
	extends MetawidgetException
{
	//
	// Private statics
	//

	private final static long	serialVersionUID	= 1l;

	//
	// Public statics
	//

	/**
	 * Static constructor.
	 * <p>
	 * Using static constructor methods prevents unnecessarily nesting WidgetBuilderExceptions within
	 * WidgetBuilderExceptions.
	 */

	public static WidgetBuilderException newException( Throwable cause )
	{
		if ( cause instanceof WidgetBuilderException )
			return (WidgetBuilderException) cause;

		return new WidgetBuilderException( cause );
	}

	/**
	 * Static constructor.
	 * <p>
	 * For consistency with <code>InspectorException.newException( Throwable )</code>.
	 */

	public static WidgetBuilderException newException( String message )
	{
		return new WidgetBuilderException( message );
	}

	//
	// Constructor
	//

	private WidgetBuilderException( String message )
	{
		super( message );
	}

	private WidgetBuilderException( Throwable cause )
	{
		super( cause );
	}
}
