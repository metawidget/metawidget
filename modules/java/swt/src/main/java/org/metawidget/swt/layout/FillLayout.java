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

import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.swt.SwtMetawidget;

/**
 * Layout to simply output components one after another, with no labels and no structure, using
 * <code>org.eclipse.swt.layout.FillLayout</code>.
 * <p>
 * This is like <code>RowLayout</code>, except it fills width. It can be useful for Table Editors.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class FillLayout
	implements AdvancedLayout<Control, Composite, SwtMetawidget> {

	//
	// Public methods
	//

	public void onStartBuild( SwtMetawidget metawidget ) {

		// Do nothing
	}

	public void startContainerLayout( Composite container, SwtMetawidget metawidget ) {

		container.setLayout( new org.eclipse.swt.layout.FillLayout() );
	}

	public void layoutWidget( Control component, String elementName, Map<String, String> attributes, Composite container, SwtMetawidget metawidget ) {

		// Do nothing
	}

	public void endContainerLayout( Composite container, SwtMetawidget metawidget ) {

		// Do nothing
	}

	public void onEndBuild( SwtMetawidget metawidget ) {

		// Do nothing
	}
}
