// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.jsp.tagext.html.layout.spring;

import static org.metawidget.inspector.InspectionResultConstants.NAME;

import java.util.Map;

import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.html.layout.HtmlTableLayout;
import org.metawidget.layout.iface.LayoutException;
import org.springframework.web.servlet.tags.form.ErrorsTag;

/**
 * Layout to arrange widgets in a table, with Spring in-line validation.
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

	protected void layoutAfterChild( Map<String, String> attributes, MetawidgetTag metawidgetTag ) {

		try {

			// Setup

			ErrorsTag errorsTag = new ErrorsTag();
			errorsTag.setPageContext( metawidgetTag.getPageContext() );
			errorsTag.setCssStyle( mErrorStyle );
			errorsTag.setCssClass( mErrorStyleClass );

			// Set path

			String path = attributes.get( NAME );
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

		super.layoutAfterChild( attributes, metawidgetTag );
	}
}
