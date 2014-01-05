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
