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

package org.metawidget.example.gwt.clientside.client.ui;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.GwtUtils;
import org.metawidget.gwt.client.ui.Stub;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.impl.BaseWidgetProcessor;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Richard Kennard
 */

public class MapBindingProcessor
	extends BaseWidgetProcessor<Widget, GwtMetawidget>
{
	//
	// Public methods
	//

	@Override
	public void onStartBuild( GwtMetawidget metawidget )
	{
		metawidget.putClientProperty( MapBindingProcessor.class, null );
	}

	@Override
	public void onAdd( Widget widget, String elementName, Map<String, String> attributes, final GwtMetawidget metawidget )
	{
		// Don't bind to Actions

		if ( ACTION.equals( elementName ))
			return;

		// Doesn't bind to Stubs or FlexTables

		if ( widget instanceof Stub || widget instanceof FlexTable )
			return;

		String path = metawidget.getPath();

		if ( PROPERTY.equals( elementName ))
			path += StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + attributes.get( NAME );

		try
		{
			if ( TRUE.equals( attributes.get( READ_ONLY ) ) )
				return;

			Set<String[]> bindings = metawidget.getClientProperty( MapBindingProcessor.class );

			if ( bindings == null )
			{
				bindings = new HashSet<String[]>();
				metawidget.putClientProperty( MapBindingProcessor.class, bindings );
			}

			bindings.add( PathUtils.parsePath( path ).getNamesAsArray() );
		}
		catch ( Exception e )
		{
			Window.alert( path + ": " + e.getMessage() );
		}
	}

	public void save( GwtMetawidget metawidget )
	{
		Set<String[]> bindings = metawidget.getClientProperty( MapBindingProcessor.class );

		if ( bindings == null )
			return;

		@SuppressWarnings( "unchecked" )
		Map<String, Object> model = (Map<String, Object>) metawidget.getToInspect();

		// For each bound property...

		for ( String[] binding : bindings )
		{
			// ...fetch the value...

			Object value = metawidget.getValue( binding[binding.length - 1] );

			// ...and set it back to the model

			model.put( GwtUtils.toString( binding, '.' ), value );
		}
	}
}
