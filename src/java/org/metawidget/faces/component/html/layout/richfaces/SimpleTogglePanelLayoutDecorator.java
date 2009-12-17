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
import org.metawidget.faces.component.layout.UIComponentNestedSectionLayoutDecorator;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;
import org.richfaces.component.html.HtmlSimpleTogglePanel;

/**
 * Layout to decorate widgets from different sections using a RichFaces SimpleTogglePanel.
 *
 * @author Richard Kennard
 */

public class SimpleTogglePanelLayoutDecorator
	extends UIComponentNestedSectionLayoutDecorator
{
	//
	// Private members
	//

	private String	mStyle;

	private String	mStyleClass;

	private String	mSwitchType;

	private boolean	mOpened;

	//
	// Constructor
	//

	public SimpleTogglePanelLayoutDecorator( SimpleTogglePanelLayoutDecoratorConfig config )
	{
		super( config );

		mStyle = config.getStyle();
		mStyleClass = config.getStyleClass();
		mSwitchType = config.getSwitchType();
		mOpened = config.isOpened();
	}

	//
	// Protected methods
	//

	@Override
	protected UIComponent createSectionWidget( UIComponent previousSectionWidget, Map<String, String> attributes, UIComponent container, UIMetawidget metawidget )
	{
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		UIViewRoot viewRoot = context.getViewRoot();

		HtmlSimpleTogglePanel panel = (HtmlSimpleTogglePanel) application.createComponent( "org.richfaces.SimpleTogglePanel" );
		panel.setId( viewRoot.createUniqueId() );
		panel.setStyle( mStyle );
		panel.setStyleClass( mStyleClass );
		panel.setSwitchType( mSwitchType );
		panel.setOpened( isOpened( attributes ) );

		// Section name (possibly localized)

		String section = getState( container, metawidget ).currentSection;
		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null )
			localizedSection = section;

		panel.setLabel( localizedSection );

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

		return nestedMetawidget;
	}

	/**
	 * Hook so subclasses can change what determines opened/closed.
	 */

	protected boolean isOpened( Map<String, String> attributes )
	{
		return mOpened;
	}
}
