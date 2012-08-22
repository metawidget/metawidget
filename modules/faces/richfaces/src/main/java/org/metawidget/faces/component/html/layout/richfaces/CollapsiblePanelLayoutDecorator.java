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
import javax.faces.context.FacesContext;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.layout.UIComponentNestedSectionLayoutDecorator;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;
import org.richfaces.component.SwitchType;
import org.richfaces.component.UICollapsiblePanel;

/**
 * Layout to decorate widgets from different sections using a RichFaces (4.x) CollapsiblePanel.
 *
 * @author Richard Kennard
 */

public class CollapsiblePanelLayoutDecorator
	extends UIComponentNestedSectionLayoutDecorator {

	//
	// Private members
	//

	private SwitchType	mSwitchType;

	private boolean		mExpanded;

	//
	// Constructor
	//

	public CollapsiblePanelLayoutDecorator( CollapsiblePanelLayoutDecoratorConfig config ) {

		super( config );

		mSwitchType = config.getSwitchType();
		mExpanded = config.isExpanded();
	}

	//
	// Protected methods
	//

	@Override
	protected UIComponent createNewSectionWidget( UIComponent previousSectionWidget, Map<String, String> attributes, UIComponent container, UIMetawidget metawidget ) {

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();

		UICollapsiblePanel panel = (UICollapsiblePanel) application.createComponent( UICollapsiblePanel.COMPONENT_TYPE );
		panel.setId( FacesUtils.createUniqueId() );
		panel.setSwitchType( mSwitchType );
		panel.setExpanded( isExpanded( attributes ));

		// Section name (possibly localized)

		String section = getState( container, metawidget ).currentSection;
		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null ) {
			localizedSection = section;
		}

		panel.setHeader( localizedSection );

		// Add to parent container

		Map<String, String> sectionAttributes = createSectionWidgetAttributes();
		panel.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA, sectionAttributes );

		getDelegate().layoutWidget( panel, PROPERTY, sectionAttributes, container, metawidget );

		// Create nested Metawidget (for layout)

		UIMetawidget nestedMetawidget = (UIMetawidget) application.createComponent( metawidget.getComponentType() );
		nestedMetawidget.setRendererType( metawidget.getRendererType() );
		nestedMetawidget.setId( FacesUtils.createUniqueId() );
		nestedMetawidget.setLayout( metawidget.getLayout() );
		nestedMetawidget.copyParameters( metawidget );
		panel.getChildren().add( nestedMetawidget );

		return nestedMetawidget;
	}

	/**
	 * Hook so subclasses can change what determines expanded/collapsed.
	 *
	 * @param attributes
	 *            attributes of the widget. Never null
	 */

	protected boolean isExpanded( Map<String, String> attributes ) {

		return mExpanded;
	}

	protected Map<String, String> createSectionWidgetAttributes() {

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( LABEL, "" );

		return attributes;
	}
}
