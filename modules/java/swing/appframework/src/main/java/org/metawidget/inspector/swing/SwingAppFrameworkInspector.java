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

package org.metawidget.inspector.swing;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.actionstyle.Action;
import org.metawidget.util.CollectionUtils;

/**
 * Inspects annotations defined by Swing AppFramework.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SwingAppFrameworkInspector
	extends BaseObjectInspector {

	//
	// Constructor
	//

	public SwingAppFrameworkInspector() {

		this( new SwingAppFrameworkInspectorConfig() );
	}

	public SwingAppFrameworkInspector( SwingAppFrameworkInspectorConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected Map<String, String> inspectAction( Action action )
		throws Exception {

		Map<String, String> attributes = CollectionUtils.newHashMap();

		// org.jdesktop.application.Action (this is kind of a given)

		org.jdesktop.application.Action actionAnnotation = action.getAnnotation( org.jdesktop.application.Action.class );

		if ( actionAnnotation != null ) {
			attributes.put( NAME, action.getName() );

			if ( !"".equals( actionAnnotation.name() ) ) {
				attributes.put( LABEL, actionAnnotation.name() );
			}
		}

		return attributes;
	}
}
