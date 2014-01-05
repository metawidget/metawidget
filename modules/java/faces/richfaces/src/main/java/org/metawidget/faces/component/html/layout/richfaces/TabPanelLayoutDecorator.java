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

package org.metawidget.faces.component.html.layout.richfaces;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.lang.reflect.Method;
import java.util.Map;

import javax.faces.component.UIComponent;
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
import org.richfaces.component.html.HtmlTab;
import org.richfaces.component.html.HtmlTabPanel;

/**
 * Layout to decorate widgets from different sections using a RichFaces TabPanel.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
	protected UIComponent createNewSectionWidget( UIComponent previousSectionWidget, String section, Map<String, String> attributes, UIComponent container, UIMetawidget metawidget ) {

		FacesContext context = FacesContext.getCurrentInstance();
		UITabPanel tabPanel;

		// Whole new UITabPanel?

		if ( previousSectionWidget == null ) {
			tabPanel = FacesUtils.createComponent( HtmlTabPanel.COMPONENT_TYPE, "org.richfaces.TabPanelRenderer" );
			tabPanel.setId( FacesUtils.createUniqueId() );

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

		UITab tab = FacesUtils.createComponent( HtmlTab.COMPONENT_TYPE, "org.richfaces.TabRenderer" );
		tab.setId( FacesUtils.createUniqueId() );

		tabPanel.getChildren().add( tab );

		// Tab name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null ) {
			localizedSection = section;
		}

		try {
			tab.setHeader( localizedSection );

			// Set name too, as per https://issues.jboss.org/browse/RF-10951

			tab.setName( localizedSection );
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
		nestedMetawidget.setId( FacesUtils.createUniqueId() );
		nestedMetawidget.setLayout( metawidget.getLayout() );
		nestedMetawidget.copyParameters( metawidget );
		tab.getChildren().add( nestedMetawidget );

		return nestedMetawidget;
	}
}
