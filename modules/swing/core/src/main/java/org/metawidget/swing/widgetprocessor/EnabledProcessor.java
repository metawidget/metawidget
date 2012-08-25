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

package org.metawidget.swing.widgetprocessor;

import java.util.Map;

import javax.swing.JComponent;

import org.metawidget.swing.SwingMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * Processor to call <code>JComponent.setEnabled(false)</code> on all child components if the parent
 * Metawidget has <code>setEnabled(false)</code>.
 *
 * @author Richard Kennard
 */

public class EnabledProcessor
	implements WidgetProcessor<JComponent, SwingMetawidget> {

	//
	// Public methods
	//

	public JComponent processWidget( JComponent component, String elementName, Map<String, String> attributes, SwingMetawidget metawidget ) {

		// Never setEnabled( true ), because this will override JButtons who have explicitly
		// setEnabled( false )

		if ( !metawidget.isEnabled() ) {
			component.setEnabled( false );
		}

		return component;
	}
}
