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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class TabbedPaneLayoutDecorator
	extends SwingNestedSectionLayoutDecorator {

	//
	// Private statics
	//

	/**
	 * The border around the entire tabbed pane.
	 */

	private static final Border	TABBED_PANE_BORDER	= BorderFactory.createEmptyBorder( 5, 0, 5, 0 );

	/**
	 * The insets around each tab.
	 */

	private static final Border	TAB_PANEL_BORDER	= BorderFactory.createEmptyBorder( 3, 3, 3, 3 );

	//
	// Private members
	//

	private final int			mTabPlacement;

	//
	// Constructor
	//

	public TabbedPaneLayoutDecorator( TabbedPaneLayoutDecoratorConfig config ) {

		super( config );

		mTabPlacement = config.getTabPlacement();
	}

	//
	// Protected methods
	//

	@Override
	protected JComponent createSectionWidget( JComponent previousSectionWidget, String section, Map<String, String> attributes, JComponent container, SwingMetawidget metawidget ) {

		JTabbedPane tabbedPane;

		// Whole new tabbed pane?

		if ( previousSectionWidget == null ) {
			tabbedPane = new JTabbedPane();
			tabbedPane.setBorder( TABBED_PANE_BORDER );
			tabbedPane.setTabPlacement( mTabPlacement );

			// Add to parent container

			Map<String, String> tabbedPaneAttributes = CollectionUtils.newHashMap();
			tabbedPaneAttributes.put( LABEL, "" );
			tabbedPaneAttributes.put( LARGE, TRUE );
			getDelegate().layoutWidget( tabbedPane, PROPERTY, tabbedPaneAttributes, container, metawidget );
		} else {
			tabbedPane = (JTabbedPane) previousSectionWidget.getParent();
		}

		// New tab

		JPanel tabPanel = new JPanel();
		tabPanel.setBorder( TAB_PANEL_BORDER );

		// Tab name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null ) {
			localizedSection = section;
		}

		tabbedPane.addTab( localizedSection, tabPanel );

		return tabPanel;
	}
}
