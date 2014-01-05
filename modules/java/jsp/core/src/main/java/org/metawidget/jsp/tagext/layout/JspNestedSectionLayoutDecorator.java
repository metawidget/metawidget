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

import java.util.Map;

import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.StubTag;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.util.LayoutUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class JspNestedSectionLayoutDecorator
	extends org.metawidget.layout.decorator.NestedSectionLayoutDecorator<Tag, BodyTag, MetawidgetTag> {

	//
	// Constructor
	//

	protected JspNestedSectionLayoutDecorator( LayoutDecoratorConfig<Tag, BodyTag, MetawidgetTag> config ) {

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
	protected State<BodyTag> getState( BodyTag containerTag, MetawidgetTag metawidgetTag ) {

		@SuppressWarnings( "unchecked" )
		State<BodyTag> state = (State<BodyTag>) metawidgetTag.getClientProperty( getClass() );

		if ( state == null ) {
			state = new State<BodyTag>();
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
