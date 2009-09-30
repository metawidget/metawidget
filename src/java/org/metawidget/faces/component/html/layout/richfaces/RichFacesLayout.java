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

import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.layout.impl.BaseLayout;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;
import org.richfaces.component.UITab;
import org.richfaces.component.UITabPanel;

/**
 * RichFaces layout.
 *
 * @author Richard Kennard
 */

public class RichFacesLayout
	extends BaseLayout<UIComponent, UIMetawidget>
{
	//
	// Public methods
	//

	@Override
	public void onStartBuild( UIMetawidget metawidget )
	{
		metawidget.putClientProperty( RichFacesLayout.class, null );
	}

	@Override
	public void layoutChild( UIComponent widget, String elementName, Map<String, String> attributes, UIMetawidget metawidget )
	{
		State state = getState( metawidget );

		// Change of section?

		String section = attributes.get( SECTION );

		if ( section != null && !section.equals( state.section ))
		{
			if ( widget instanceof UIStub && widget.getChildren().isEmpty() )
			{
				// Ignore empty stubs. Do not create a new tab in case it ends
				// up being an empty tab
			}
			else
			{
				state.section = section;

				// End of sections?

				if ( "".equals( section ))
				{
					state.sectionComponent = null;
				}
				else
				{
					FacesContext context = FacesContext.getCurrentInstance();
					Application application = context.getApplication();
					UIViewRoot viewRoot = context.getViewRoot();

					// Create parent tab panel

					UITabPanel tabPanel;

					if ( state.sectionComponent == null )
					{
						tabPanel = (UITabPanel) application.createComponent( "org.richfaces.TabPanel" );
						tabPanel.setId( viewRoot.createUniqueId() );
						tabPanel.setSwitchType( "client" );
						metawidget.getChildren().add( tabPanel );

						// Suppress label

						Map<String, String> metadata = CollectionUtils.newHashMap();
						metadata.put( LABEL, "" );
						tabPanel.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA, metadata );
					}
					else
					{
						tabPanel = (UITabPanel) state.sectionComponent.getParent().getParent();
					}

					// Section name (possibly localized)

					String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

					if ( localizedSection == null )
						localizedSection = section;

					// Create tab for section

					UITab tab = (UITab) application.createComponent( "org.richfaces.Tab" );
					tab.setId( viewRoot.createUniqueId() );
					tab.setLabel( localizedSection );
					tabPanel.getChildren().add( tab );

					// Create nested panel (use a Metawidget for layout)

					UIMetawidget nestedMetawidget = (UIMetawidget) application.createComponent( "org.metawidget.HtmlMetawidget" );
					// Maybe if we support nested tabs: nestedMetawidget.setLayout( metawidget.getLayout() );
					nestedMetawidget.setRendererType( metawidget.getRendererType() );
					nestedMetawidget.setId( viewRoot.createUniqueId() );
					tab.getChildren().add( nestedMetawidget );

					// Use this nested panel from now on

					state.sectionComponent = nestedMetawidget;
				}
			}
		}

		// The tab is the section header, so remove section header for the widget
		// before the HtmlTableLayoutRenderer (or HtmlDivLayoutRenderer etc) sees it

		attributes.remove( SECTION );

		// Normal remove/re-add

		List<UIComponent> children;

		if ( state.sectionComponent == null )
			children = metawidget.getChildren();
		else
			children = state.sectionComponent.getChildren();

		children.remove( widget );
		children.add( widget );
	}

	//
	// Private methods
	//

	private State getState( UIMetawidget metawidget )
	{
		State state = (State) metawidget.getClientProperty( RichFacesLayout.class );

		if ( state == null )
		{
			state = new State();
			metawidget.putClientProperty( RichFacesLayout.class, state );
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
		public String		section;

		public UIComponent	sectionComponent;
	}
}
