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

package org.metawidget.swing.widgetbuilder.swingx;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Component;
import java.util.Date;
import java.util.Map;

import javax.swing.JComponent;

import org.jdesktop.swingx.JXDatePicker;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.SwingValuePropertyProvider;
import org.metawidget.util.ClassUtils;
import org.metawidget.widgetbuilder.impl.BaseWidgetBuilder;

/**
 * WidgetBuilder for the SwingX library.
 * <p>
 * For more details on SwingX see <a
 * href="https://swingx.dev.java.net">https://swingx.dev.java.net</a>.
 *
 * @author Richard Kennard
 */

public class SwingXWidgetBuilder
	extends BaseWidgetBuilder<JComponent, SwingMetawidget>
	implements SwingValuePropertyProvider
{
	//
	// Public methods
	//

	public String getValueProperty( Component component )
	{
		if ( component instanceof JXDatePicker )
			return "date";

		return null;
	}

	//
	// Protected methods
	//

	@Override
	protected JComponent buildActiveWidget( String elementName, Map<String, String> attributes, SwingMetawidget metawidget )
		throws Exception
	{
		// Not for us?

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return new Stub();

		String type = attributes.get( TYPE );

		if ( type == null || "".equals( type ) )
			return null;

		Class<?> clazz = ClassUtils.niceForName( type );

		// Dates

		if ( Date.class.equals( clazz ) )
			return new JXDatePicker();

		return null;
	}
}
