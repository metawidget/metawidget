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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
