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
import org.richfaces.component.html.HtmlPanel;

/**
 * Layout to decorate widgets from different sections using a RichFaces Panel.
 *
 * @author Richard Kennard
 */

public class PanelSectionLayoutDecorator
	extends LayoutDecorator<UIComponent, UIMetawidget>
{
	//
	// Private members
	//

	private String	mStyle;

	private String	mStyleClass;

	//
	// Constructor
	//

	public PanelSectionLayoutDecorator( PanelSectionLayoutDecoratorConfig config )
	{
		super( config );

		mStyle = config.getStyle();
		mStyleClass = config.getStyleClass();
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
			if ( state.currentPanel == null )
				super.layoutWidget( component, elementName, attributes, container, metawidget );
			else
				super.layoutWidget( component, elementName, attributes, state.currentPanel, metawidget );

			return;
		}

		state.currentSection = section;

		// End current section

		if ( state.currentPanel != null )
		{
			super.endLayout( state.currentPanel, metawidget );
			state.currentPanel = null;
		}

		// No new section?

		if ( "".equals( section ) )
		{
			super.layoutWidget( component, elementName, attributes, container, metawidget );
			return;
		}

		// Ignore empty stubs. Do not create a new panel in case it ends up being empty

		if ( component instanceof UIStub && component.getChildren().isEmpty() )
			return;

		// New section

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		UIViewRoot viewRoot = context.getViewRoot();

		HtmlPanel panel = (HtmlPanel) application.createComponent( "org.richfaces.panel" );
		panel.setId( viewRoot.createUniqueId() );
		panel.setStyle( mStyle );
		panel.setStyleClass( mStyleClass );

		// Section name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null )
			localizedSection = section;

		panel.setHeader( localizedSection );

		// Add to parent container

		Map<String, String> panelAttributes = CollectionUtils.newHashMap();
		panelAttributes.put( LABEL, "" );
		panel.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA, panelAttributes );

		super.layoutWidget( panel, PROPERTY, panelAttributes, container, metawidget );

		// Create nested Metawidget (for layout)

		UIMetawidget nestedMetawidget = (UIMetawidget) application.createComponent( "org.metawidget.HtmlMetawidget" );
		nestedMetawidget.setRendererType( metawidget.getRendererType() );
		nestedMetawidget.setId( viewRoot.createUniqueId() );
		nestedMetawidget.setLayout( metawidget.getLayout() );
		panel.getChildren().add( nestedMetawidget );
		state.currentPanel = nestedMetawidget;

		// Add component to new tab

		super.layoutWidget( component, elementName, attributes, state.currentPanel, metawidget );
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
		Map<UIComponent, State> stateMap = (Map<UIComponent, State>) metawidget.getClientProperty( PanelSectionLayoutDecorator.class );

		if ( stateMap == null )
		{
			stateMap = CollectionUtils.newHashMap();
			metawidget.putClientProperty( PanelSectionLayoutDecorator.class, stateMap );
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

		public UIMetawidget	currentPanel;
	}
}
