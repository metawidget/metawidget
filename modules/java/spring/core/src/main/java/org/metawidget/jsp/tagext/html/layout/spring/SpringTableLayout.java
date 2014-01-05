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

package org.metawidget.jsp.tagext.html.layout.spring;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.html.layout.HtmlTableLayout;
import org.metawidget.layout.iface.LayoutException;
import org.springframework.web.servlet.tags.form.ErrorsTag;

/**
 * Layout to arrange widgets in a table, with Spring inline error validation.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SpringTableLayout
	extends HtmlTableLayout {

	//
	// Private methods
	//

	private final String	mErrorStyle;

	private final String	mErrorStyleClass;

	//
	// Constructor
	//

	public SpringTableLayout() {

		this( new SpringTableLayoutConfig() );
	}

	public SpringTableLayout( SpringTableLayoutConfig config ) {

		super( config );

		mErrorStyle = config.getErrorStyle();
		mErrorStyleClass = config.getErrorStyleClass();
	}

	//
	// Protected methods
	//

	@Override
	protected void layoutAfterChild( Map<String, String> attributes, MetawidgetTag metawidgetTag ) {

		// If we have a path (i.e. we are not a section heading), render an
		// inline error tag

		String path = attributes.get( NAME );

		if ( path != null ) {

			try {

				// Setup

				ErrorsTag errorsTag = new ErrorsTag();
				errorsTag.setPageContext( metawidgetTag.getPageContext() );
				errorsTag.setCssStyle( mErrorStyle );
				errorsTag.setCssClass( mErrorStyleClass );

				// Set path

				String pathPrefix = metawidgetTag.getPathPrefix();

				if ( pathPrefix != null ) {
					path = pathPrefix + path;
				}

				errorsTag.setPath( path );

				// Render

				errorsTag.doStartTag();
				errorsTag.doEndTag();

			} catch ( Exception e ) {
				throw LayoutException.newException( e );
			}
		}

		super.layoutAfterChild( attributes, metawidgetTag );
	}
}
