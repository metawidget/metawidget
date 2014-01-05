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

package org.metawidget.swing.layout;

import java.util.Map;

import javax.swing.JComponent;

import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;

/**
 * Layout to simply output components one after another, with no labels and no structure, using
 * <code>javax.swing.BoxLayout</code>.
 * <p>
 * This is like <code>FlowLayout</code>, except it fills width. It can be useful for JTable
 * CellEditors.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class BoxLayout
	implements AdvancedLayout<JComponent, JComponent, SwingMetawidget> {

	//
	// Public methods
	//

	public void onStartBuild( SwingMetawidget metawidget ) {

		// Do nothing
	}

	public void startContainerLayout( JComponent container, SwingMetawidget metawidget ) {

		container.setLayout( new javax.swing.BoxLayout( container, javax.swing.BoxLayout.PAGE_AXIS ) );
	}

	public void layoutWidget( JComponent component, String elementName, Map<String, String> attributes, JComponent container, SwingMetawidget metawidget ) {

		// Do not render empty stubs

		if ( component instanceof Stub && ( (Stub) component ).getComponentCount() == 0 ) {
			return;
		}

		// Add to the Metawidget

		container.add( component );
	}

	public void endContainerLayout( JComponent container, SwingMetawidget metawidget ) {

		// Do nothing
	}

	public void onEndBuild( SwingMetawidget metawidget ) {

		// Do nothing
	}
}
