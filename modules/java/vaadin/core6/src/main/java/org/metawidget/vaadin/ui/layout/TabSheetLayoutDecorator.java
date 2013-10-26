// Metawidget (licensed under LGPL)
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
			tabSheet = (TabSheet) previousSectionWidget.getParent();
		}

		// New tab

		Panel tabPanel = new Panel();

		// Tab name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null ) {
			localizedSection = section;
		}

		tabSheet.addTab( tabPanel, localizedSection, null );

		return tabPanel;
	}
}
