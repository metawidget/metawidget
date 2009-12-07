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

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.layout.decorator.LayoutDecorator;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.LayoutUtils;
import org.metawidget.util.simple.StringUtils;
import org.richfaces.component.UITab;
import org.richfaces.component.UITabPanel;

/**
 * Layout to decorate widgets from different sections using a RichFaces TabPanel.
 *
 * @author Richard Kennard
 */

public class TabPanelSectionLayoutDecorator
	extends LayoutDecorator<UIComponent, UIMetawidget>
{
	//
	// Private members
	//

	private String	mHeaderAlignment;

	//
	// Constructor
	//

	public TabPanelSectionLayoutDecorator( TabPanelSectionLayoutDecoratorConfig config )
	{
		super( config );

		mHeaderAlignment = config.getHeaderAlignment();
	}

	//
	// Public methods
	//

	@Override
	public void layoutWidget( UIComponent component, String elementName, Map<String, String> attributes, UIComponent container, UIMetawidget metawidget )
	{
		String section = LayoutUtils.stripSection( attributes );
		State state = getState( container, metawidget );

		// Stay where we are?

		if ( section == null || section.equals( state.currentSection ) )
		{
			if ( state.tabComponent == null )
				super.layoutWidget( component, elementName, attributes, container, metawidget );
			else
				super.layoutWidget( component, elementName, attributes, state.tabComponent, metawidget );

			return;
		}

		state.currentSection = section;

		// End current section

		UITabPanel tabPanel = null;

		if ( state.tabComponent != null )
		{
			super.endLayout( state.tabComponent, metawidget );
			tabPanel = (UITabPanel) state.tabComponent.getParent().getParent();
			state.tabComponent = null;
		}

		// No new section?

		if ( "".equals( section ) )
		{
			super.layoutWidget( component, elementName, attributes, container, metawidget );
			return;
		}

		// Ignore empty stubs. Do not create a new tab in case it ends
		// up being an empty tab

		// TODO: do this in other Layouts too?

		if ( component instanceof UIStub && component.getChildren().isEmpty() )
			return;

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		UIViewRoot viewRoot = context.getViewRoot();

		// Whole new UITabPanel?

		if ( tabPanel == null )
		{
			tabPanel = (UITabPanel) application.createComponent( "org.richfaces.TabPanel" );
			tabPanel.setId( viewRoot.createUniqueId() );
			tabPanel.setSwitchType( "client" );
			tabPanel.setHeaderAlignment( mHeaderAlignment );

			// Add to parent container

			Map<String, String> tabPanelAttributes = CollectionUtils.newHashMap();
			tabPanelAttributes.put( LABEL, "" );
			tabPanel.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA, tabPanelAttributes );

			super.layoutWidget( tabPanel, PROPERTY, tabPanelAttributes, container, metawidget );
		}

		// New tab

		UITab tab = (UITab) application.createComponent( "org.richfaces.Tab" );
		tab.setId( viewRoot.createUniqueId() );
		tabPanel.getChildren().add( tab );

		// Tab name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null )
			localizedSection = section;

		tab.setLabel( localizedSection );

		// Create nested Metawidget (for layout)

		UIMetawidget nestedMetawidget = (UIMetawidget) application.createComponent( "org.metawidget.HtmlMetawidget" );
		nestedMetawidget.setRendererType( metawidget.getRendererType() );
		nestedMetawidget.setId( viewRoot.createUniqueId() );
		nestedMetawidget.setLayout( metawidget.getLayout() );
		tab.getChildren().add( nestedMetawidget );
		state.tabComponent = nestedMetawidget;

		// Add component to new tab

		super.layoutWidget( component, elementName, attributes, state.tabComponent, metawidget );
	}

	//
	// Private methods
	//

	//
	// Private methods
	//

	private State getState( UIComponent container, UIMetawidget metawidget )
	{
		@SuppressWarnings( "unchecked" )
		Map<UIComponent, State> stateMap = (Map<UIComponent, State>) metawidget.getClientProperty( TabPanelSectionLayoutDecorator.class );

		if ( stateMap == null )
		{
			stateMap = CollectionUtils.newHashMap();
			metawidget.putClientProperty( TabPanelSectionLayoutDecorator.class, stateMap );
		}

		State state = stateMap.get( container );

		if ( state == null )
		{
			state = new State();
			stateMap.put( container, state );
		}

		return state;
	}

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	/* package private */class State
	{
		public String		currentSection;

		public UIMetawidget	tabComponent;
	}
}
