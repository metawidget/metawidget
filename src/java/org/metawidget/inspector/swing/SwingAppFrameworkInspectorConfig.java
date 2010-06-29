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
 * @author Richard Kennard
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

			mActionStyle = DEFAULT_APPFRAMEWORK_ACTION_STYLE;
		}

		return mActionStyle;
	}
}
