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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.actionstyle.Action;
import org.metawidget.util.CollectionUtils;

/**
 * Inspects annotations defined by Swing AppFramework.
 *
 * @author Richard Kennard
 */

public class SwingAppFrameworkInspector
	extends BaseObjectInspector
{
	//
	// Constructor
	//

	public SwingAppFrameworkInspector()
	{
		this( new SwingAppFrameworkInspectorConfig() );
	}

	public SwingAppFrameworkInspector( SwingAppFrameworkInspectorConfig config )
	{
		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected Map<String, String> inspectAction( Action action )
		throws Exception
	{
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// org.jdesktop.application.Action (this is kind of a given)

		org.jdesktop.application.Action actionAnnotation = action.getAnnotation( org.jdesktop.application.Action.class );

		if ( actionAnnotation != null )
		{
			attributes.put( NAME, action.getName() );

			if ( !"".equals( actionAnnotation.name() ))
			{
				attributes.put( LABEL, actionAnnotation.name() );
			}
		}

		return attributes;
	}
}
