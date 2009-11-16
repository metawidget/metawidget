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
import org.richfaces.component.UIPanel;
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
	// Private members
	//

	private int	mSectionStyle;

	//
	// Constructor
	//

	public RichFacesLayout()
	{
		this( new RichFacesLayoutConfig() );
	}

	public RichFacesLayout( RichFacesLayoutConfig config )
	{
		mSectionStyle = config.getSectionStyle();
	}

	//
	// Public methods
	//

	@Override
	public void onStartBuild( UIMetawidget metawidget )
	{
		metawidget.putClientProperty( RichFacesLayout.class, null );
	}

	@Override
	public UIComponent layoutChild( UIComponent widget, String elementName, Map<String, String> attributes, UIMetawidget metawidget )
	{
		State state = getState( metawidget );

		// Change of section?

		String section = attributes.get( SECTION );

		if ( section != null && !section.equals( state.section ) )
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

				if ( "".equals( section ) )
				{
					state.sectionComponent = null;
				}
				else
				{
					FacesContext context = FacesContext.getCurrentInstance();
					Application application = context.getApplication();
					UIViewRoot viewRoot = context.getViewRoot();

					// Section name (possibly localized)

					String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

					if ( localizedSection == null )
						localizedSection = section;

					// Create panel for section

					UIComponent panelWithSuppressedLabel;
					UIComponent panel;

					switch ( mSectionStyle )
					{
						case RichFacesLayoutConfig.SECTION_AS_TAB:
						{
							// Create parent tab panel

							if ( state.sectionComponent == null )
							{
								panelWithSuppressedLabel =  application.createComponent( "org.richfaces.TabPanel" );
								panelWithSuppressedLabel.setId( viewRoot.createUniqueId() );
								((UITabPanel) panelWithSuppressedLabel).setSwitchType( "client" );
								metawidget.getChildren().add( panelWithSuppressedLabel );
							}
							else
							{
								panelWithSuppressedLabel = state.sectionComponent.getParent().getParent();
							}

							// Create tab for section

							panel = application.createComponent( "org.richfaces.Tab" );
							( (UITab) panel ).setLabel( localizedSection );
							panelWithSuppressedLabel.getChildren().add( panel );
						}
							break;

						default:
						{
							// Create panel for section

							panel = application.createComponent( "org.richfaces.panel" );
							( (UIPanel) panel ).setHeader( localizedSection );
							metawidget.getChildren().add( panel );

							// (one and the same)

							panelWithSuppressedLabel = panel;
						}
							break;
					}

					// Initialize section panel

					panel.setId( viewRoot.createUniqueId() );

					// Suppress label (make the panel go the whole width of the form)

					Map<String, String> metadata = CollectionUtils.newHashMap();
					metadata.put( LABEL, "" );
					panelWithSuppressedLabel.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA, metadata );

					// Create nested panel (use a Metawidget for layout)

					UIMetawidget nestedMetawidget = (UIMetawidget) application.createComponent( "org.metawidget.HtmlMetawidget" );
					nestedMetawidget.setRendererType( metawidget.getRendererType() );
					nestedMetawidget.setId( viewRoot.createUniqueId() );
					// Maybe someday if we support nested sections:
					// nestedMetawidget.setLayout( metawidget.getLayout() );
					panel.getChildren().add( nestedMetawidget );

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

		return null;
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
