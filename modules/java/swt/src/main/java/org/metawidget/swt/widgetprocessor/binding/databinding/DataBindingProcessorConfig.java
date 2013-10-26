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

package org.metawidget.swt.widgetprocessor.binding.databinding;

import org.eclipse.core.databinding.conversion.IConverter;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a DataBindingBindingProcessor prior to use. Once instantiated, WidgetProcessors are
 * immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class DataBindingProcessorConfig {

	//
	// Private members
	//

	private IConverter[]	mConverters;

	//
	// Public methods
	//

	/**
	 * Sets Converters for this DataBindingProcessor.
	 *
	 * @return this, as part of a fluent interface
	 */

	public DataBindingProcessorConfig setConverters( IConverter... converters ) {

		mConverters = converters;

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

		if ( !ObjectUtils.nullSafeEquals( mConverters, ( (DataBindingProcessorConfig) that ).mConverters ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mConverters );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected IConverter[] getConverters() {

		return mConverters;
	}
}
