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

package org.metawidget.inspector.swing;

import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.actionstyle.ActionStyle;
import org.metawidget.inspector.impl.actionstyle.swing.SwingAppFrameworkActionStyle;

/**
 * Configures a SwingAppFrameworkInspector prior to use. Once instantiated, Inspectors are
 * immutable.
 * <p>
 * <code>SwingAppFrameworkInspector</code> differs from most Inspectors in that it defaults to using
 * <code>SwingAppFrameworkActionStyle</code> instead of <code>MetawidgetActionStyle</code>.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SwingAppFrameworkInspectorConfig
	extends BaseObjectInspectorConfig {

	//
	// Private statics
	//

	private static ActionStyle	DEFAULT_APPFRAMEWORK_ACTION_STYLE;

	//
	// Package private methods
	//

	@Override
	protected ActionStyle getActionStyle() {

		if ( mActionStyle == null && !mNullActionStyle ) {
			if ( DEFAULT_APPFRAMEWORK_ACTION_STYLE == null ) {
				DEFAULT_APPFRAMEWORK_ACTION_STYLE = new SwingAppFrameworkActionStyle();
			}

			return DEFAULT_APPFRAMEWORK_ACTION_STYLE;
		}

		return mActionStyle;
	}
}
