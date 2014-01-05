// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.vaadin.ui.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.vaadin.ui.VaadinMetawidget;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;

/**
 * Layout to decorate widgets from different sections using a TabSheet.
 *
 * @author Loghman Barari
 */

public class TabSheetLayoutDecorator
	extends VaadinNestedSectionLayoutDecorator {

	//
	// Constructor
	//

	public TabSheetLayoutDecorator( LayoutDecoratorConfig<Component, ComponentContainer, VaadinMetawidget> config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected ComponentContainer createSectionWidget( ComponentContainer previousSectionWidget, String section, Map<String, String> attributes, ComponentContainer container, VaadinMetawidget metawidget ) {

		TabSheet tabSheet;

		// Whole new tabbed pane?

		if ( previousSectionWidget == null ) {

			tabSheet = new TabSheet();
			tabSheet.setWidth( "100%" );

			// Add to parent container

			Map<String, String> tabbedPaneAttributes = CollectionUtils.newHashMap();
			tabbedPaneAttributes.put( LABEL, "" );
			tabbedPaneAttributes.put( LARGE, TRUE );
			getDelegate().layoutWidget( tabSheet, PROPERTY, tabbedPaneAttributes, container, metawidget );
		} else {
			tabSheet = (TabSheet) previousSectionWidget.getParent().getParent();
		}

		// New tab

		Panel tabPanel = new Panel( new com.vaadin.ui.VerticalLayout() );

		// Tab name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null ) {
			localizedSection = section;
		}

		tabSheet.addTab( tabPanel, localizedSection, null );

		return (ComponentContainer) tabPanel.getContent();
	}
}
