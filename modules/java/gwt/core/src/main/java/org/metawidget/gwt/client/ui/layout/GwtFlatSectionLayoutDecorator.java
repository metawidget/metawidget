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

package org.metawidget.gwt.client.ui.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.GwtUtils;
import org.metawidget.gwt.client.ui.Stub;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections within GWT Layouts.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class GwtFlatSectionLayoutDecorator
	extends org.metawidget.layout.decorator.FlatSectionLayoutDecorator<Widget, Panel, GwtMetawidget> {

	//
	// Constructor
	//

	protected GwtFlatSectionLayoutDecorator( LayoutDecoratorConfig<Widget, Panel, GwtMetawidget> config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected String stripSection( Map<String, String> attributes ) {

		return GwtUtils.stripSection( attributes );
	}

	@Override
	protected String[] getSections( Map<String, String> attributes ) {

		return GwtUtils.fromString( attributes.get( SECTION ), ',' ).toArray( new String[0] );
	}

	@Override
	protected State getState( Panel container, GwtMetawidget metawidget ) {

		State state = (State) metawidget.getClientProperty( getClass() );

		if ( state == null ) {
			state = new State();
			metawidget.putClientProperty( getClass(), state );
		}

		return state;
	}

	@Override
	protected boolean isIgnored( Widget widget ) {

		return ( widget instanceof Stub && ( (Stub) widget ).getWidgetCount() == 0 );
	}
}
