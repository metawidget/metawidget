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
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.layout.UIComponentNestedSectionLayoutDecorator;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to decorate widgets from different sections using a JSF PanelGroup.
 * <p>
 * The <code>styleClass</code> attribute of the section includes the section name, as well as any
 * additional style class specified by <code>PanelGroupLayoutDecoratorConfig</code>.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class PanelGroupLayoutDecorator
	extends UIComponentNestedSectionLayoutDecorator {

	//
	// Private members
	//

	private String	mStyle;

	private String	mStyleClass;

	private String	mPanelLayout;

	//
	// Constructor
	//

	public PanelGroupLayoutDecorator( PanelGroupLayoutDecoratorConfig config ) {

		super( config );

		mStyle = config.getStyle();
		mStyleClass = config.getStyleClass();
		mPanelLayout = config.getPanelLayout();
	}

	//
	// Protected methods
	//

	@Override
	protected UIComponent createNewSectionWidget( UIComponent previousSectionWidget, String section, Map<String, String> attributes, UIComponent container, UIMetawidget metawidget ) {

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();

		HtmlPanelGroup panel = (HtmlPanelGroup) application.createComponent( HtmlPanelGroup.COMPONENT_TYPE );
		panel.setId( FacesUtils.createUniqueId() );
		panel.setStyle( mStyle );
		panel.setLayout( mPanelLayout );

		// Section name as style class

		String sectionStyleClass = StringUtils.camelCase( getState( container, metawidget ).currentSection );

		if ( mStyleClass == null ) {
			panel.setStyleClass( sectionStyleClass );
		} else {
			panel.setStyleClass( mStyleClass + ' ' + sectionStyleClass );
		}

		// Add to parent container

		Map<String, String> panelAttributes = CollectionUtils.newHashMap();
		panelAttributes.put( LABEL, "" );
		panel.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA, panelAttributes );

		getDelegate().layoutWidget( panel, PROPERTY, panelAttributes, container, metawidget );

		// Create nested Metawidget (for layout)

		UIMetawidget nestedMetawidget = (UIMetawidget) application.createComponent( metawidget.getComponentType() );
		nestedMetawidget.setRendererType( metawidget.getRendererType() );
		nestedMetawidget.setId( FacesUtils.createUniqueId() );
		nestedMetawidget.setLayout( metawidget.getLayout() );
		nestedMetawidget.copyParameters( metawidget );
		panel.getChildren().add( nestedMetawidget );

		return nestedMetawidget;
	}
}
