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

import java.lang.reflect.Method;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.layout.UIComponentNestedSectionLayoutDecorator;
import org.metawidget.layout.iface.LayoutException;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;
import org.richfaces.HeaderAlignment;
import org.richfaces.component.SwitchType;
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
		UIViewRoot viewRoot = context.getViewRoot();

		UITabPanel tabPanel;

		// Whole new UITabPanel?

		if ( previousSectionWidget == null ) {
			tabPanel = FacesUtils.createComponent( "org.richfaces.TabPanel", "org.richfaces.TabPanelRenderer" );
			tabPanel.setId( viewRoot.createUniqueId() );

			try {
				tabPanel.setSwitchType( SwitchType.client );
				tabPanel.setHeaderAlignment( HeaderAlignment.valueOf( mHeaderAlignment ) );
			} catch ( NoClassDefFoundError e1 ) {

				try {
					// RichFaces 3

					Method method = UITabPanel.class.getMethod( "setSwitchType", String.class );
					method.invoke( tabPanel, "client" );

					method = UITabPanel.class.getMethod( "setHeaderAlignment", String.class );
					method.invoke( tabPanel, mHeaderAlignment );
				} catch ( Exception e2 ) {
					throw LayoutException.newException( e2 );
				}
			}

			// Add to parent container

			Map<String, String> tabPanelAttributes = CollectionUtils.newHashMap();
			tabPanelAttributes.put( LABEL, "" );
			tabPanel.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA, tabPanelAttributes );

			getDelegate().layoutWidget( tabPanel, PROPERTY, tabPanelAttributes, container, metawidget );
		} else {
			tabPanel = (UITabPanel) previousSectionWidget.getParent().getParent();
		}

		// New tab

		UITab tab = FacesUtils.createComponent( "org.richfaces.Tab", null );
		tab.setId( viewRoot.createUniqueId() );
		tabPanel.getChildren().add( tab );

		// Tab name (possibly localized)

		String section = getState( container, metawidget ).currentSection;
		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null ) {
			localizedSection = section;
		}

		try {
			tab.setHeader( localizedSection );
		} catch ( NoSuchMethodError e1 ) {

			try {
				// RichFaces 3

				Method method = UITab.class.getMethod( "setLabel", String.class );
				method.invoke( tab, localizedSection );
			} catch ( Exception e ) {
				throw LayoutException.newException( e );
			}
		}

		// Create nested Metawidget (for layout)

		UIMetawidget nestedMetawidget = (UIMetawidget) context.getApplication().createComponent( metawidget.getComponentType() );
		nestedMetawidget.setRendererType( metawidget.getRendererType() );
		nestedMetawidget.setId( viewRoot.createUniqueId() );
		nestedMetawidget.setLayout( metawidget.getLayout() );
		nestedMetawidget.copyParameters( metawidget );
		tab.getChildren().add( nestedMetawidget );

		return nestedMetawidget;
	}
}
