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

package org.metawidget.faces.component.html.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.layout.UIComponentFlatSectionLayoutDecorator;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to decorate widgets from different sections using an HtmlOutputText.
 * <p>
 * As dictated by the JSF spec, CSS styles and style classes applied to an HtmlOptionText are
 * wrapped in an HTML span tag. Therefore you must use CSS 'display: block' if you want to use
 * margins or padding around the HtmlOutputText.
 *
 * @author Richard Kennard
 */

public class OutputTextLayoutDecorator
	extends UIComponentFlatSectionLayoutDecorator {

	//
	// Private members
	//

	private String	mStyle;

	private String	mStyleClass;

	//
	// Constructor
	//

	public OutputTextLayoutDecorator( OutputTextLayoutDecoratorConfig config ) {

		super( config );

		mStyle = config.getStyle();
		mStyleClass = config.getStyleClass();
	}

	//
	// Protected methods
	//

	@Override
	protected void addSectionWidget( String section, int level, UIComponent container, UIMetawidget metawidget ) {

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		HtmlOutputText heading = (HtmlOutputText) application.createComponent( "javax.faces.HtmlOutputText" );
		heading.setId( context.getViewRoot().createUniqueId() );
		heading.setStyle( mStyle );
		heading.setStyleClass( mStyleClass );

		// Section name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null ) {
			localizedSection = section;
		}

		heading.setValue( localizedSection );

		// Add to parent container
		//
		// Note: unfortunately this outputs as a SPAN tag, so you'll need to put 'display:
		// block' in your CSS if you want to use margins. Vanilla JSF doesn't have anything that
		// outputs a DIV tag

		Map<String, String> metadataAttributes = CollectionUtils.newHashMap();
		metadataAttributes.put( LABEL, "" );
		metadataAttributes.put( WIDE, TRUE );
		heading.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA, metadataAttributes );

		getDelegate().layoutWidget( heading, PROPERTY, metadataAttributes, container, metawidget );
	}
}
