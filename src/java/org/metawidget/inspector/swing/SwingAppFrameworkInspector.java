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
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.actionstyle.Action;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;

/**
 * Inspects annotations defined by Metawidget (declared in this same package).
 * <p>
 * Note: the name of this class is longwinded for extra clarity. It is not just a
 * 'MetawidgetInspector', because of course there are lots of different Metawidget Inspectors.
 * Equally, it is not just an 'AnnotationInspector', because it doesn't generically scan all
 * possible annotations.
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

	public SwingAppFrameworkInspector( BaseObjectInspectorConfig config )
	{
		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected Map<String, String> inspectProperty( Property property )
		throws Exception
	{
		return null;
	}

	@Override
	protected Map<String, String> inspectAction( Action action )
		throws Exception
	{
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Action (this is kind of a given)

		org.jdesktop.application.Action actionAnnotation = action.getAnnotation( org.jdesktop.application.Action.class );

		if ( actionAnnotation != null )
		{
			attributes.put( NAME, action.getName() );

			if ( !"".equals( actionAnnotation.name() ))
				attributes.put( LABEL, actionAnnotation.name() );
		}

		return attributes;
	}
}
