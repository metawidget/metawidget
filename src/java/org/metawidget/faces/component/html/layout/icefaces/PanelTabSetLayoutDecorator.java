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

package org.metawidget.faces.component.html.layout.icefaces;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
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
 * @author Richard Kennard
 */

// TODO: composite components?

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
	protected UIComponent createNewSectionWidget( UIComponent previousSectionWidget, Map<String, String> attributes, UIComponent container, UIMetawidget metawidget ) {

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		UIViewRoot viewRoot = context.getViewRoot();

		PanelTabSet panelTabSet;

		// Whole new PanelTabSet?

		if ( previousSectionWidget == null ) {
			panelTabSet = (PanelTabSet) application.createComponent( "com.icesoft.faces.PanelTabSet" );
			panelTabSet.setId( viewRoot.createUniqueId() );

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

		PanelTab tab = (PanelTab) application.createComponent( "com.icesoft.faces.PanelTab" );
		tab.setId( viewRoot.createUniqueId() );
		panelTabSet.getChildren().add( tab );

		// Tab name (possibly localized)

		String section = getState( container, metawidget ).currentSection;
		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null ) {
			localizedSection = section;
		}

		tab.setLabel( localizedSection );

		// Create nested Metawidget (for layout)

		UIMetawidget nestedMetawidget = (UIMetawidget) application.createComponent( metawidget.getComponentType() );
		nestedMetawidget.setRendererType( metawidget.getRendererType() );
		nestedMetawidget.setId( viewRoot.createUniqueId() );
		nestedMetawidget.setLayout( metawidget.getLayout() );
		FacesUtils.copyParameters( metawidget, nestedMetawidget );
		tab.getChildren().add( nestedMetawidget );

		return nestedMetawidget;
	}
}
