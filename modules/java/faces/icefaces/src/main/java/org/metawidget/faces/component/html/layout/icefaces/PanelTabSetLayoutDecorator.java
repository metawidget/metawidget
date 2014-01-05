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

package org.metawidget.faces.component.html.layout.icefaces;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.layout.UIComponentNestedSectionLayoutDecorator;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

import com.icesoft.faces.component.paneltabset.PanelTab;
import com.icesoft.faces.component.paneltabset.PanelTabSet;

/**
 * Layout to decorate widgets from different sections using an ICEfaces PanelTabSet.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class PanelTabSetLayoutDecorator
	extends UIComponentNestedSectionLayoutDecorator {

	//
	// Constructor
	//

	public PanelTabSetLayoutDecorator( LayoutDecoratorConfig<UIComponent, UIComponent, UIMetawidget> config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected UIComponent createNewSectionWidget( UIComponent previousSectionWidget, String section, Map<String, String> attributes, UIComponent container, UIMetawidget metawidget ) {

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();

		PanelTabSet panelTabSet;

		// Whole new PanelTabSet?

		if ( previousSectionWidget == null ) {
			panelTabSet = (PanelTabSet) application.createComponent( PanelTabSet.COMPONENT_TYPE );
			panelTabSet.setId( FacesUtils.createUniqueId() );

			// Because Metawidget will destroy/recreate the PanelTabSet each time, we must store the
			// 'selectedIndex' external to the PanelTabSet or we will never be able to switch tabs.
			// This is similar to, but less restrictive than, using
			// COMPONENT_ATTRIBUTE_NOT_RECREATABLE to stop the PanelTabSet being recreated

			ExpressionFactory expressionFactory = application.getExpressionFactory();
			ELContext elContext = context.getELContext();
			panelTabSet.setValueExpression( "selectedIndex", expressionFactory.createValueExpression( elContext, "#{requestScope['" + getClass().getName() + "." + attributes.get( NAME ) + "']}", Object.class ) );

			// Add to parent container

			Map<String, String> tabPanelAttributes = CollectionUtils.newHashMap();
			tabPanelAttributes.put( LABEL, "" );
			panelTabSet.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA, tabPanelAttributes );

			getDelegate().layoutWidget( panelTabSet, PROPERTY, tabPanelAttributes, container, metawidget );
		} else {
			panelTabSet = (PanelTabSet) previousSectionWidget.getParent().getParent();
		}

		// New tab

		PanelTab tab = (PanelTab) application.createComponent( PanelTab.COMPONENT_TYPE );
		tab.setId( FacesUtils.createUniqueId() );
		panelTabSet.getChildren().add( tab );

		// Tab name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null ) {
			localizedSection = section;
		}

		tab.setLabel( localizedSection );

		// Create nested Metawidget (for layout)

		UIMetawidget nestedMetawidget = (UIMetawidget) application.createComponent( metawidget.getComponentType() );
		nestedMetawidget.setRendererType( metawidget.getRendererType() );
		nestedMetawidget.setId( FacesUtils.createUniqueId() );
		nestedMetawidget.setLayout( metawidget.getLayout() );
		nestedMetawidget.copyParameters( metawidget );
		tab.getChildren().add( nestedMetawidget );

		return nestedMetawidget;
	}
}
