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

package org.metawidget.gwt.client.widgetbuilder.extgwt;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;
import java.util.Map;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.GwtUtils;
import org.metawidget.gwt.client.ui.GwtValueAccessor;
import org.metawidget.widgetbuilder.impl.BaseWidgetBuilder;

import com.extjs.gxt.ui.client.widget.form.DateField;
import com.google.gwt.user.client.ui.Widget;

/**
 * WidgetBuilder for RichFaces environments.
 * <p>
 * Automatically creates native ExtGWT widgets, such as <code>DateField</code>, to suit the
 * inspected fields.
 *
 * @author Richard Kennard
 */

public class ExtGwtWidgetBuilder
	extends BaseWidgetBuilder<Widget, GwtMetawidget>
	implements GwtValueAccessor
{
	//
	// Public methods
	//

	public Object getValue( Widget widget )
	{
		if ( widget instanceof DateField )
			return ((DateField) widget).getValue();

		return null;
	}

	public boolean setValue( Widget widget, Object value )
	{
		if ( widget instanceof DateField )
		{
			((DateField) widget).setValue( (Date) value );
			return true;
		}

		// Not for us

		return false;
	}

	//
	// Protected methods
	//

	@Override
	protected Widget buildActiveWidget( String elementName, Map<String, String> attributes, GwtMetawidget metawidget )
		throws Exception
	{
		// Not for ExtGWT?

		if ( ACTION.equals( elementName ) )
			return null;

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return null;

		if ( attributes.containsKey( LOOKUP ) )
			return null;

		String type = GwtUtils.getActualClassOrType( attributes );

		if ( type == null )
			return null;

		// Dates

		if ( Date.class.getName().equals( type ) )
			return new DateField();

		// Not for ExtGWT

		return null;
	}
}
