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
import javax.swing.border.Border;

import org.metawidget.layout.delegate.DelegateLayout;
import org.metawidget.layout.delegate.DelegateLayoutConfig;
import org.metawidget.layout.impl.LayoutUtils;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * @author Richard Kennard
 */

public class TitledPanelSectionLayout
	extends DelegateLayout<JComponent, SwingMetawidget>
{
	//
	// Private statics
	//

	/**
	 * The border around the entire titled panel.
	 */

	private final static Border	OUTER_BORDER	= BorderFactory.createEmptyBorder( 5, 0, 5, 0 );

	/**
	 * The insets inside the titled panel.
	 */

	private final static Border	INNER_BORDER	= BorderFactory.createEmptyBorder( 3, 3, 3, 3 );

	//
	// Constructor
	//

	public TitledPanelSectionLayout( DelegateLayoutConfig<JComponent, SwingMetawidget> config )
	{
		super( config );
	}

	//
	// Public methods
	//

	@Override
	public void startLayout( JComponent container, SwingMetawidget metawidget )
	{
		super.startLayout( container, metawidget );
		metawidget.putClientProperty( TitledPanelSectionLayout.class, null );
	}

	@Override
	public void layoutWidget( JComponent component, String elementName, Map<String, String> attributes, JComponent container, SwingMetawidget metawidget )
	{
		String section = LayoutUtils.stripSection( attributes );
		State state = getState( metawidget );

		// Stay where we are?

		if ( section == null || section.equals( state.currentSection ) )
		{
			if ( state.titledPanel == null )
				super.layoutWidget( component, elementName, attributes, container, metawidget );
			else
				super.layoutWidget( component, elementName, attributes, state.titledPanel, metawidget );

			return;
		}

		state.currentSection = section;

		// End current section

		if ( state.titledPanel != null )
		{
			super.endLayout( state.titledPanel, metawidget );
			state.titledPanel = null;
		}

		// No new section?

		if ( "".equals( section ) )
		{
			super.layoutWidget( component, elementName, attributes, container, metawidget );
			return;
		}

		// New section

		state.titledPanel = new JPanel();
		state.titledPanel.setOpaque( false );
		super.startLayout( state.titledPanel, metawidget );

		// Section name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null )
			localizedSection = section;

		state.titledPanel.setBorder( BorderFactory.createCompoundBorder( OUTER_BORDER, BorderFactory.createCompoundBorder( BorderFactory.createTitledBorder( localizedSection ), INNER_BORDER )));

		// Add to parent container

		Map<String, String> panelAttributes = CollectionUtils.newHashMap();
		panelAttributes.put( LABEL, "" );
		panelAttributes.put( LARGE, TRUE );
		super.layoutWidget( state.titledPanel, PROPERTY, panelAttributes, container, metawidget );

		// Add component to new section

		super.layoutWidget( component, elementName, attributes, state.titledPanel, metawidget );
	}

	@Override
	public void endLayout( JComponent container, SwingMetawidget metawidget )
	{
		State state = getState( metawidget );

		if ( state.titledPanel != null )
			super.endLayout( state.titledPanel, metawidget );

		super.endLayout( container, metawidget );
	}

	//
	// Private methods
	//

	private State getState( SwingMetawidget metawidget )
	{
		State state = (State) metawidget.getClientProperty( TitledPanelSectionLayout.class );

		if ( state == null )
		{
			state = new State();
			metawidget.putClientProperty( TitledPanelSectionLayout.class, state );
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

		/* package private */JPanel	titledPanel;
	}
}
