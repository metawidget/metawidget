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
