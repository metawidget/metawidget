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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
	protected UIComponent createNewSectionWidget( UIComponent previousSectionWidget, String section, Map<String, String> attributes, UIComponent container, UIMetawidget metawidget ) {

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();

		UICollapsiblePanel panel = (UICollapsiblePanel) FacesUtils.createComponent( UICollapsiblePanel.COMPONENT_TYPE, "org.richfaces.CollapsiblePanelRenderer" );
		panel.setId( FacesUtils.createUniqueId() );
		panel.setSwitchType( mSwitchType );
		panel.setExpanded( isExpanded( attributes ));

		// Section name (possibly localized)

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
