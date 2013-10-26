// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.faces.component.html.widgetbuilder.icefaces;

import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures an IceFacesWidgetBuilder prior to use. Once instantiated, WidgetBuilders are
 * immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class IceFacesWidgetBuilderConfig {

	//
	// Private members
	//

	private boolean		mPartialSubmit = true;

	//
	// Public methods
	//

	/**
	 * @return this, as part of a fluent interface
	 */

	public IceFacesWidgetBuilderConfig setPartialSubmit( boolean partialSubmit ) {

		mPartialSubmit = partialSubmit;

		return this;
	}

	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}

		if ( !ObjectUtils.nullSafeClassEquals( this, that )) {
			return false;
		}

		if ( mPartialSubmit != ( (IceFacesWidgetBuilderConfig) that ).mPartialSubmit ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mPartialSubmit );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected boolean isPartialSubmit() {

		return mPartialSubmit;
	}
}
