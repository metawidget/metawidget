// Metawidget (licensed under LGPL)
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

package org.metawidget.jsp.tagext.layout;

import java.util.Map;

import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.StubTag;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.util.LayoutUtils;

/**
 * @author Richard Kennard
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
