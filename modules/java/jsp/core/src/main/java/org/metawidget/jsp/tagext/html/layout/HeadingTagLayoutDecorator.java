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
