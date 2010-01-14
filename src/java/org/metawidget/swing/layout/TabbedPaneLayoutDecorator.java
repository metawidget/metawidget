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

package org.metawidget.swing.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;

import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to decorate widgets from different sections using a JTabbedPane.
 *
 * @author Richard Kennard
 */

public class TabbedPaneLayoutDecorator
	extends SwingNestedSectionLayoutDecorator
{
	//
	// Private statics
	//

	/**
	 * The border around the entire tabbed pane.
	 */

	private final static Border	TABBED_PANE_BORDER	= BorderFactory.createEmptyBorder( 5, 0, 5, 0 );

	/**
	 * The insets around each tab.
	 */

	private final static Border	TAB_PANEL_BORDER	= BorderFactory.createEmptyBorder( 3, 3, 3, 3 );

	//
	// Private members
	//

	private final int			mTabPlacement;

	//
	// Constructor
	//

	public TabbedPaneLayoutDecorator( TabbedPaneLayoutDecoratorConfig config )
	{
		super( config );

		mTabPlacement = config.getTabPlacement();
	}

	//
	// Protected methods
	//

	@Override
	protected JComponent createSectionWidget( JComponent previousSectionWidget, Map<String, String> attributes, JComponent container, SwingMetawidget metawidget )
	{
		JTabbedPane tabbedPane;

		// Whole new tabbed pane?

		if ( previousSectionWidget == null )
		{
			tabbedPane = new JTabbedPane();
			tabbedPane.setBorder( TABBED_PANE_BORDER );
			tabbedPane.setTabPlacement( mTabPlacement );

			// Add to parent container

			Map<String, String> tabbedPaneAttributes = CollectionUtils.newHashMap();
			tabbedPaneAttributes.put( LABEL, "" );
			tabbedPaneAttributes.put( LARGE, TRUE );
			getDelegate().layoutWidget( tabbedPane, PROPERTY, tabbedPaneAttributes, container, metawidget );
		}
		else
		{
			tabbedPane = (JTabbedPane) previousSectionWidget.getParent();
		}

		// New tab

		JPanel tabPanel = new JPanel();
		tabPanel.setBorder( TAB_PANEL_BORDER );

		// Tab name (possibly localized)

		String section = getState( container, metawidget ).currentSection;
		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null )
			localizedSection = section;

		tabbedPane.addTab( localizedSection, tabPanel );

		return tabPanel;
	}
}
