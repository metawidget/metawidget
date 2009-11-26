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

import org.metawidget.layout.delegate.DelegateLayout;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to separate widgets in different sections using a JTabbedPane.
 *
 * @author Richard Kennard
 */

public class TabbedPaneSectionLayout
	extends DelegateLayout<JComponent, SwingMetawidget>
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

	private int					mTabPlacement;

	//
	// Constructor
	//

	public TabbedPaneSectionLayout( TabbedPaneSectionLayoutConfig config )
	{
		super( config );

		mTabPlacement = config.getTabPlacement();
	}

	//
	// Public methods
	//

	@Override
	public void startLayout( JComponent container, SwingMetawidget metawidget )
	{
		super.startLayout( container, metawidget );
		metawidget.putClientProperty( TabbedPaneSectionLayout.class, null );
	}

	@Override
	public void layoutWidget( JComponent component, String elementName, Map<String, String> attributes, JComponent container, SwingMetawidget metawidget )
	{
		String section = attributes.get( SECTION );

		State state = getState( metawidget );

		// Stay where we are?

		if ( section == null || section.equals( state.currentSection ) )
		{
			if ( state.tabPanel == null )
				super.layoutWidget( component, elementName, attributes, container, metawidget );
			else
				super.layoutWidget( component, elementName, attributes, state.tabPanel, metawidget );

			return;
		}

		state.currentSection = section;

		// End current section

		JTabbedPane tabbedPane = null;

		if ( state.tabPanel != null )
		{
			super.endLayout( state.tabPanel, metawidget );
			tabbedPane = (JTabbedPane) state.tabPanel.getParent();
			state.tabPanel = null;
		}

		// No new section?

		if ( "".equals( section ) )
		{
			super.layoutWidget( component, elementName, attributes, container, metawidget );
			return;
		}

		// New tab

		state.tabPanel = new JPanel();
		state.tabPanel.setBorder( TAB_PANEL_BORDER );
		super.startLayout( state.tabPanel, metawidget );

		// Tab name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null )
			localizedSection = section;

		// Whole new tabbed pane?

		if ( tabbedPane == null )
		{
			tabbedPane = new JTabbedPane();
			tabbedPane.setBorder( TABBED_PANE_BORDER );
			//TODO:tabbedPane.setTabPlacement( mTabPlacement );

			Map<String, String> tabbedPaneAttributes = CollectionUtils.newHashMap();
			tabbedPaneAttributes.put( LABEL, "" );
			tabbedPaneAttributes.put( LARGE, TRUE );
			super.layoutWidget( tabbedPane, PROPERTY, tabbedPaneAttributes, container, metawidget );
		}

		tabbedPane.addTab( localizedSection, state.tabPanel );

		// Add component to new tab

		super.layoutWidget( component, elementName, attributes, state.tabPanel, metawidget );
	}

	@Override
	public void endLayout( JComponent container, SwingMetawidget metawidget )
	{
		State state = getState( metawidget );

		if ( state.tabPanel != null )
			super.endLayout( state.tabPanel, metawidget );

		super.endLayout( container, metawidget );
	}

	//
	// Private methods
	//

	private State getState( SwingMetawidget metawidget )
	{
		State state = (State) metawidget.getClientProperty( TabbedPaneSectionLayout.class );

		if ( state == null )
		{
			state = new State();
			metawidget.putClientProperty( TabbedPaneSectionLayout.class, state );
		}

		return state;
	}

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	/* package private */class State
	{
		/* package private */String	currentSection;

		/* package private */JPanel	tabPanel;
	}
}
