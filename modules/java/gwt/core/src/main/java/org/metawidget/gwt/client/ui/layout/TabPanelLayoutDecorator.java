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

package org.metawidget.gwt.client.ui.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.HashMap;
import java.util.Map;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.util.simple.StringUtils;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Layout to decorate widgets from different sections using a TabPanel.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class TabPanelLayoutDecorator
	extends GwtNestedSectionLayoutDecorator {

	//
	// Constructor
	//

	public TabPanelLayoutDecorator( LayoutDecoratorConfig<Widget, Panel, GwtMetawidget> config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected Panel createSectionWidget( Panel previousSectionWidget, String section, Map<String, String> attributes, Panel container, GwtMetawidget metawidget ) {

		// Whole new tab panel?

		TabPanel tabPanel;

		if ( previousSectionWidget == null ) {
			tabPanel = new TabPanel();

			// Add to parent container

			Map<String, String> tabPanelAttributes = new HashMap<String, String>();
			tabPanelAttributes.put( LABEL, "" );
			tabPanelAttributes.put( LARGE, TRUE );
			getDelegate().layoutWidget( tabPanel, PROPERTY, tabPanelAttributes, container, metawidget );
		} else {
			tabPanel = (TabPanel) previousSectionWidget.getParent().getParent().getParent();
		}

		// New tab

		final Panel newPanel = new FlowPanel();

		// Section name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null ) {
			localizedSection = section;
		}

		tabPanel.add( newPanel, localizedSection );

		if ( previousSectionWidget == null ) {
			tabPanel.selectTab( 0 );
		}

		return newPanel;
	}
}
