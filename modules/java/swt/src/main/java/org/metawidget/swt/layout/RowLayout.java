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

package org.metawidget.swt.layout;

import java.util.Map;

import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.swt.Stub;
import org.metawidget.swt.SwtMetawidget;

/**
 * Layout to simply output components one after another, with no labels and no structure, using
 * <code>org.eclipse.swt.layout.RowLayout</code>.
 * <p>
 * This is like <code>FillLayout</code>, except it does not fill width. It can be useful for button
 * bars.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class RowLayout
	implements AdvancedLayout<Control, Composite, SwtMetawidget> {

	//
	// Public methods
	//

	public void onStartBuild( SwtMetawidget metawidget ) {

		// Do nothing
	}

	public void startContainerLayout( Composite container, SwtMetawidget metawidget ) {

		container.setLayout( new org.eclipse.swt.layout.RowLayout() );
	}

	public void layoutWidget( Control control, String elementName, Map<String, String> attributes, Composite container, SwtMetawidget metawidget ) {

		// Do not layout space for empty stubs

		if ( control instanceof Stub && ( (Stub) control ).getChildren().length == 0 ) {
			RowData stubData = new RowData();
			stubData.exclude = true;
			control.setLayoutData( stubData );
			return;
		}

		// Do nothing
	}

	public void endContainerLayout( Composite container, SwtMetawidget metawidget ) {

		// Do nothing
	}

	public void onEndBuild( SwtMetawidget metawidget ) {

		// Do nothing
	}
}
