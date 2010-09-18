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

package org.metawidget.faces.component.html.layout.richfaces;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.layout.UIComponentNestedSectionLayoutDecorator;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;
import org.richfaces.component.UITab;
import org.richfaces.component.UITabPanel;

/**
 * Layout to decorate widgets from different sections using a RichFaces TabPanel.
 *
 * @author Richard Kennard
 */

public class TabPanelLayoutDecorator
	extends UIComponentNestedSectionLayoutDecorator {

	//
	// Private members
	//

	private String	mHeaderAlignment;

	//
	// Constructor
	//

	public TabPanelLayoutDecorator( TabPanelLayoutDecoratorConfig config ) {

		super( config );

		mHeaderAlignment = config.getHeaderAlignment();
	}

	//
	// Protected methods
	//

	@Override
	protected UIComponent createNewSectionWidget( UIComponent previousSectionWidget, Map<String, String> attributes, UIComponent container, UIMetawidget metawidget ) {

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		UIViewRoot viewRoot = context.getViewRoot();

		UITabPanel tabPanel;

		// Whole new UITabPanel?

		if ( previousSectionWidget == null ) {
			tabPanel = (UITabPanel) application.createComponent( "org.richfaces.TabPanel" );
			tabPanel.setId( viewRoot.createUniqueId() );
			tabPanel.setSwitchType( "client" );
			tabPanel.setHeaderAlignment( mHeaderAlignment );

			// Add to parent container

			Map<String, String> tabPanelAttributes = CollectionUtils.newHashMap();
			tabPanelAttributes.put( LABEL, "" );
			tabPanel.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA, tabPanelAttributes );

			getDelegate().layoutWidget( tabPanel, PROPERTY, tabPanelAttributes, container, metawidget );
		} else {
			tabPanel = (UITabPanel) previousSectionWidget.getParent().getParent();
		}

		// New tab

		UITab tab = (UITab) application.createComponent( "org.richfaces.Tab" );
		tab.setId( viewRoot.createUniqueId() );
		tabPanel.getChildren().add( tab );

		// Tab name (possibly localized)

		String section = getState( container, metawidget ).currentSection;
		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null ) {
			localizedSection = section;
		}

		tab.setLabel( localizedSection );

		// Create nested Metawidget (for layout)

		UIMetawidget nestedMetawidget = (UIMetawidget) application.createComponent( metawidget.getComponentType() );
		nestedMetawidget.setRendererType( metawidget.getRendererType() );
		nestedMetawidget.setId( viewRoot.createUniqueId() );
		nestedMetawidget.setLayout( metawidget.getLayout() );
		FacesUtils.copyParameters( metawidget, nestedMetawidget );
		tab.getChildren().add( nestedMetawidget );

		return nestedMetawidget;
	}
}
