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
import javax.faces.context.FacesContext;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.layout.impl.BaseLayout;
import org.metawidget.util.simple.StringUtils;
import org.richfaces.component.html.HtmlTab;
import org.richfaces.component.html.HtmlTabPanel;

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
	public void startLayout( UIMetawidget metawidget )
	{
		metawidget.putClientProperty( RichFacesLayout.class, null );
	}

	@Override
	public void layoutChild( UIComponent widget, Map<String, String> attributes, UIMetawidget metawidget )
	{
		State state = getState( metawidget );

		// Change of section?

		String section = attributes.get( SECTION );

		if ( section != null && !section.equals( state.section ))
		{
			// Section name (possibly localized)

			String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

			if ( localizedSection == null )
				localizedSection = section;

			FacesContext context = FacesContext.getCurrentInstance();
			Application application = context.getApplication();

			// Create parent tab panel

			HtmlTabPanel htmlTabPanel;

			if ( state.sectionComponent == null )
			{
				htmlTabPanel = (HtmlTabPanel) application.createComponent( "org.richfaces.tabPanel" );
				metawidget.getChildren().add( htmlTabPanel );
			}
			else
			{
				htmlTabPanel = (HtmlTabPanel) state.sectionComponent.getParent();
			}

			// Create tab for section

			HtmlTab htmlTab = (HtmlTab) application.createComponent( "org.richfaces.tabPanel" );
			htmlTab.setLabel( localizedSection );
			htmlTabPanel.getChildren().add( htmlTab );

			// Use this panel from now on

			state.sectionComponent = htmlTab;
		}

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
