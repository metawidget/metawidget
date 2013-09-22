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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.servlet.jsp.tagext.BodyTag;

import org.metawidget.jsp.tagext.LiteralTag;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.layout.JspFlatSectionLayoutDecorator;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to decorate widgets from different sections using an HTML heading tag (ie. H1, H2 etc).
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HeadingTagLayoutDecorator
	extends JspFlatSectionLayoutDecorator {

	//
	// Private members
	//

	private String	mStyle;

	private String	mStyleClass;

	//
	// Constructor
	//

	public HeadingTagLayoutDecorator( HeadingTagLayoutDecoratorConfig config ) {

		super( config );

		mStyle = config.getStyle();
		mStyleClass = config.getStyleClass();
	}

	//
	// Protected methods
	//

	@Override
	protected void addSectionWidget( String section, int level, BodyTag containerTag, MetawidgetTag metawidgetTag ) {

		StringBuilder builder = new StringBuilder( "<h" );
		builder.append( String.valueOf( level + 1 ) );

		if ( mStyle != null ) {
			builder.append( " style=\"" );
			builder.append( mStyle );
			builder.append( "\"" );
		}

		if ( mStyleClass != null ) {
			builder.append( " class=\"" );
			builder.append( mStyleClass );
			builder.append( "\"" );
		}

		builder.append( ">" );

		// Section name (possibly localized)

		String localizedSection = metawidgetTag.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection != null ) {
			builder.append( localizedSection );
		} else {
			builder.append( section );
		}

		builder.append( "</h" );
		builder.append( String.valueOf( level + 1 ) );
		builder.append( ">" );

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( LABEL, "" );
		attributes.put( WIDE, TRUE );

		getDelegate().layoutWidget( new LiteralTag( builder.toString() ), PROPERTY, attributes, containerTag, metawidgetTag );
	}
}
