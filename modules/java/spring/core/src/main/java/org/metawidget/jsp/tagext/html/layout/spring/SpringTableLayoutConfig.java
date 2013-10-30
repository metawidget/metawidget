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

package org.metawidget.jsp.tagext.html.layout.spring;

import org.metawidget.jsp.tagext.html.layout.HtmlTableLayoutConfig;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a SpringTableLayout prior to use. Once instantiated, Layouts are immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SpringTableLayoutConfig
	extends HtmlTableLayoutConfig {

	//
	// Private members
	//

	private String		mErrorStyle;

	private String		mErrorStyleClass;

	//
	// Public methods
	//

	/**
	 * @return this, as part of a fluent interface
	 */

	public SpringTableLayoutConfig setErrorStyle( String errorStyle ) {

		mErrorStyle = errorStyle;
		
		return this;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public SpringTableLayoutConfig setErrorStyleClass( String errorStyleClass ) {

		mErrorStyleClass = errorStyleClass;

		return this;
	}

	@Override
	public boolean equals( Object that ) {

		if ( !super.equals( that )) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mErrorStyle, ( (SpringTableLayoutConfig) that ).mErrorStyle ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mErrorStyleClass, ( (SpringTableLayoutConfig) that ).mErrorStyleClass ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = super.hashCode();
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mErrorStyle );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mErrorStyleClass );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected String getErrorStyle() {

		return mErrorStyle;
	}

	protected String getErrorStyleClass() {

		return mErrorStyleClass;
	}
}
