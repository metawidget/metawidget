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
import javax.faces.component.UIViewRoot;
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
 * @author Richard Kennard
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
	protected UIComponent createSectionWidget( UIComponent previousSectionWidget, Map<String, String> attributes, UIComponent container, UIMetawidget metawidget ) {

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		UIViewRoot viewRoot = context.getViewRoot();

		HtmlPanelGroup panel = (HtmlPanelGroup) application.createComponent( "javax.faces.HtmlPanelGroup" );
		panel.setId( viewRoot.createUniqueId() );
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
		nestedMetawidget.setId( viewRoot.createUniqueId() );
		nestedMetawidget.setLayout( metawidget.getLayout() );
		FacesUtils.copyParameters( metawidget, nestedMetawidget );
		panel.getChildren().add( nestedMetawidget );

		return nestedMetawidget;
	}
}
