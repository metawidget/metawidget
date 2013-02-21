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

package org.metawidget.jsp.tagext.html.layout;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.layout.JspNestedSectionLayoutDecorator;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.layout.iface.LayoutException;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to decorate widgets from different sections using a Fieldset with a Label.
 *
 * @author Richard Kennard
 */

public class FieldsetLayoutDecorator
	extends JspNestedSectionLayoutDecorator {

	//
	// Constructor
	//

	public FieldsetLayoutDecorator( LayoutDecoratorConfig<Tag, BodyTag, MetawidgetTag> config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	public void startContainerLayout( BodyTag container, MetawidgetTag metawidget ) {

		// Suppress super.startContainerLayout( container, metawidget ), because we want <fieldset>
		// to come before <table> or <div>

		State<BodyTag> state = getState( container, metawidget );
		state.currentSection = null;
		state.currentSectionWidget = null;
	}

	@Override
	protected BodyTag createSectionWidget( BodyTag previousSectionWidget, String section, Map<String, String> attributes, BodyTag container, MetawidgetTag metawidgetTag ) {

		JspWriter writer = metawidgetTag.getPageContext().getOut();

		try {
			if ( previousSectionWidget != null ) {
				writer.write( "</fieldset>" );
			}

			// Section name (possibly localized)

			String localizedSection = metawidgetTag.getLocalizedKey( StringUtils.camelCase( section ) );

			if ( localizedSection == null ) {
				localizedSection = section;
			}

			writer.write( "<fieldset>" );
			writer.write( "<label>" );
			writer.write( localizedSection );
			writer.write( "</label>" );
		} catch ( IOException e ) {
			throw LayoutException.newException( e );
		}

		return new BodyTagSupport();
	}

	@Override
	public void endContainerLayout( BodyTag container, MetawidgetTag metawidgetTag ) {

		// End hanging layouts

		State<BodyTag> state = getState( container, metawidgetTag );

		if ( state.currentSectionWidget != null ) {
			try {
				JspWriter writer = metawidgetTag.getPageContext().getOut();
				writer.write( "</fieldset>" );
			} catch ( IOException e ) {
				throw LayoutException.newException( e );
			}
		}

		super.endContainerLayout( container, metawidgetTag );
	}
}
