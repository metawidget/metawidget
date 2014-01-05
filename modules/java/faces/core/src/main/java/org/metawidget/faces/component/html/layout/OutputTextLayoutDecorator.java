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

package org.metawidget.faces.component.html.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import org.metawidget.faces.FacesUtils;
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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
		HtmlOutputText heading = (HtmlOutputText) application.createComponent( HtmlOutputText.COMPONENT_TYPE );
		heading.setId( FacesUtils.createUniqueId() );
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
