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
