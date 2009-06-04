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

import org.metawidget.gwt.client.propertybinding.BasePropertyBinding;
import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.GwtUtils;
import org.metawidget.gwt.client.ui.Stub;
import org.metawidget.util.simple.PathUtils;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Richard Kennard
 */

public class MapPropertyBinding
	extends BasePropertyBinding
{
	//
	// Private members
	//

	private Set<String[]>	mBindings;

	//
	// Constructor
	//

	public MapPropertyBinding( GwtMetawidget metawidget )
	{
		super( metawidget );
	}

	//
	// Public methods
	//

	public void bindProperty( Widget widget, Map<String, String> attributes, String path )
	{
		// Doesn't bind to Stubs or FlexTables

		if ( widget instanceof Stub || widget instanceof FlexTable )
			return;

		try
		{
			if ( TRUE.equals( attributes.get( READ_ONLY ) ) )
				return;

			if ( mBindings == null )
				mBindings = new HashSet<String[]>();

			mBindings.add( PathUtils.parsePath( path ).getNamesAsArray() );
		}
		catch ( Exception e )
		{
			Window.alert( path + ": " + e.getMessage() );
		}
	}

	public void rebindProperties()
	{
		// Not implemented
	}

	public void saveProperties()
	{
		if ( mBindings == null )
			return;

		GwtMetawidget metawidget = getMetawidget();
		@SuppressWarnings("unchecked")
		Map<String, Object> model = (Map<String, Object>) metawidget.getToInspect();

		// For each bound property...

		for ( String[] binding : mBindings )
		{
			// ...fetch the value...

			Object value = metawidget.getValue( binding[ binding.length - 1 ] );

			// ...and set it back to the model

			model.put( GwtUtils.toString( binding, '.' ), value );
		}
	}
}
