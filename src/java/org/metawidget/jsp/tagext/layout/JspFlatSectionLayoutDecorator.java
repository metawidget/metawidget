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

package org.metawidget.jsp.tagext.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.servlet.jsp.tagext.Tag;

import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.StubTag;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.util.ArrayUtils;

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections within JSP-based Layouts.
 * <p>
 * Note: it is not clear it is possible to implement a <code>JspNestedSectionLayoutDecorator</code>,
 * because the JSP component model does not allow adding children to tags.
 *
 * @author Richard Kennard
 */

public abstract class JspFlatSectionLayoutDecorator
	extends org.metawidget.layout.decorator.FlatSectionLayoutDecorator<Tag, MetawidgetTag>
{
	//
	// Constructor
	//

	protected JspFlatSectionLayoutDecorator( LayoutDecoratorConfig<Tag, MetawidgetTag> config )
	{
		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected String[] getSections( Map<String, String> attributes )
	{
		return ArrayUtils.fromString( attributes.get( SECTION ) );
	}

	@Override
	protected State getState( Tag containerTag, MetawidgetTag metawidgetTag )
	{
		State state = (State) metawidgetTag.getClientProperty( getClass() );

		if ( state == null )
		{
			state = new State();
			metawidgetTag.putClientProperty( getClass(), state );
		}

		return state;
	}

	@Override
	protected boolean isEmptyStub( Tag tag )
	{
		if ( !( tag instanceof StubTag ) )
			return false;

		String literal = ( (StubTag) tag ).getSavedBodyContent();

		if ( literal == null || literal.length() == 0 )
			return true;

		return false;
	}
}
