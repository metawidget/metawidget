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

package org.metawidget.swt.layout;

import org.metawidget.layout.iface.LayoutException;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a MigLayout prior to use. Once instantiated, Layouts are immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class MigLayoutConfig {

	//
	// Private members
	//

	private int		mNumberOfColumns	= 1;

	private boolean	mDebugMode			= false;

	//
	// Public methods
	//

	/**
	 * @return this, as part of a fluent interface
	 */

	public MigLayoutConfig setNumberOfColumns( int numberOfColumns ) {

		if ( numberOfColumns < 1 ) {
			throw LayoutException.newException( "numberOfColumns must be >= 1" );
		}

		mNumberOfColumns = numberOfColumns;

		return this;
	}

	/**
	 * @param debugMode
	 *            true to enable MigLayout debug mode
	 * @return this, as part of a fluent interface
	 */

	public MigLayoutConfig setDebugMode( boolean debugMode ) {

		mDebugMode = debugMode;

		return this;
	}

	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}

		if ( !ObjectUtils.nullSafeClassEquals( this, that ) ) {
			return false;
		}

		if ( mNumberOfColumns != ( (MigLayoutConfig) that ).mNumberOfColumns ) {
			return false;
		}

		if ( mDebugMode != ( (MigLayoutConfig) that ).mDebugMode ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + mNumberOfColumns;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mDebugMode );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected int getNumberOfColumns() {

		return mNumberOfColumns;
	}

	protected boolean isDebugMode() {

		return mDebugMode;
	}
}
