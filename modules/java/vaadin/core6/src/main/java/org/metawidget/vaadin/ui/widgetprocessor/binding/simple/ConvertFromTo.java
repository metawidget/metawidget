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

package org.metawidget.vaadin.ui.widgetprocessor.binding.simple;

import org.metawidget.util.simple.ObjectUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

/* package private */class ConvertFromTo {

	//
	// Private members
	//

	private Class<?>	mSource;

	private Class<?>	mTarget;

	//
	// Constructor
	//

	public ConvertFromTo( Class<?> source, Class<?> target ) {

		mSource = source;
		mTarget = target;
	}

	//
	// Public methods
	//

	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}

		if ( !ObjectUtils.nullSafeClassEquals( this, that ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mSource, ( (ConvertFromTo) that ).mSource ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mTarget, ( (ConvertFromTo) that ).mTarget ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mSource.hashCode() );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mTarget.hashCode() );

		return hashCode;
	}
}
