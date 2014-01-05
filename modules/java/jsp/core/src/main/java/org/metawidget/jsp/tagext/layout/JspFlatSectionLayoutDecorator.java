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

package org.metawidget.jsp.tagext.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.StubTag;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.LayoutUtils;

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections within JSP-based Layouts.
 * <p>
 * Note: it is not clear it is possible to implement a <code>JspNestedSectionLayoutDecorator</code>,
 * because the JSP component model does not allow adding children to tags.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class JspFlatSectionLayoutDecorator
	extends org.metawidget.layout.decorator.FlatSectionLayoutDecorator<Tag, BodyTag, MetawidgetTag> {

	//
	// Constructor
	//

	protected JspFlatSectionLayoutDecorator( LayoutDecoratorConfig<Tag, BodyTag, MetawidgetTag> config ) {

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
	protected State getState( BodyTag containerTag, MetawidgetTag metawidgetTag ) {

		State state = (State) metawidgetTag.getClientProperty( getClass() );

		if ( state == null ) {
			state = new State();
			metawidgetTag.putClientProperty( getClass(), state );
		}

		return state;
	}

	@Override
	protected boolean isIgnored( Tag tag ) {

		if ( !( tag instanceof StubTag ) ) {
			return false;
		}

		String literal = ( (StubTag) tag ).getSavedBodyContent();

		if ( literal == null || literal.length() == 0 ) {
			return true;
		}

		return false;
	}
}
