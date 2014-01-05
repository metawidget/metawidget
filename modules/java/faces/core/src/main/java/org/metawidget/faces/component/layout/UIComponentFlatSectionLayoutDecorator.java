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

package org.metawidget.faces.component.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputHidden;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.LayoutUtils;

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections within JSF Layouts.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class UIComponentFlatSectionLayoutDecorator
	extends org.metawidget.layout.decorator.FlatSectionLayoutDecorator<UIComponent, UIComponent, UIMetawidget> {

	//
	// Constructor
	//

	protected UIComponentFlatSectionLayoutDecorator( LayoutDecoratorConfig<UIComponent, UIComponent, UIMetawidget> config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected String stripSection( Map<String, String> attributes ) {

		return LayoutUtils.stripSection( attributes );
	}

	@Override
	protected String[] getSections( Map<String, String> attributes ) {

		return ArrayUtils.fromString( attributes.get( SECTION ) );
	}

	@Override
	protected State getState( UIComponent container, UIMetawidget metawidget ) {

		State state = (State) metawidget.getClientProperty( getClass() );

		if ( state == null ) {
			state = new State();
			metawidget.putClientProperty( getClass(), state );
		}

		return state;
	}

	@Override
	protected boolean isIgnored( UIComponent component ) {

		if ( component instanceof UIStub && component.getChildren().isEmpty() ) {
			return true;
		}

		// Ignore HtmlInputHidden, so that we don't create section headings for it

		if ( component instanceof HtmlInputHidden ) {
			return true;
		}

		return false;
	}
}
