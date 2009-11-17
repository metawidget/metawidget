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

package org.metawidget.swing.widgetprocessor.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.impl.BaseWidgetProcessor;

/**
 * @author Richard Kennard
 */

public class SectionHeadingProcessor
	extends BaseWidgetProcessor<JComponent, SwingMetawidget>
{
	//
	// Private statics
	//

	private final static int	MEDIUM_GAP				= 5;

	private final static Insets	INSETS_SECTION_LABEL	= new Insets( 0, 0, 0, MEDIUM_GAP );

	private final static Border	BORDER_SECTION			= BorderFactory.createEmptyBorder( MEDIUM_GAP, 0, MEDIUM_GAP, 0 );

	//
	// Public methods
	//

	@Override
	public void onStartBuild( SwingMetawidget metawidget )
	{
		metawidget.putClientProperty( SectionHeadingProcessor.class, null );
	}

	public JComponent processWidget( JComponent component, String elementName, Map<String, String> attributes, SwingMetawidget metawidget )
	{
		String section = attributes.remove( SECTION );

		State state = getState( metawidget );

		// Stay where we are?

		if ( section == null || section.equals( state.currentSection ) )
		{
			if ( state.nestedMetawidget == null )
				return component;

			addNestedComponent( component, attributes, metawidget );
			return null;
		}

		state.currentSection = section;

		// End current section?

		if ( "".equals( section ) )
		{
			if ( state.nestedMetawidget != null )
				state.nestedMetawidget = null;

			return component;
		}

		// New section

		state.nestedMetawidget = new SwingMetawidget();
		state.nestedMetawidget.setMetawidgetLayout( metawidget.getMetawidgetLayout() );

		// Section name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null )
			localizedSection = section;

		JLabel labelSection = new JLabel( localizedSection );

		// Section panel

		JPanel sectionPanel = new JPanel();
		sectionPanel.setBorder( BORDER_SECTION );
		sectionPanel.setLayout( new java.awt.GridBagLayout() );
		sectionPanel.setOpaque( false );

		GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints.insets = INSETS_SECTION_LABEL;
		sectionPanel.add( labelSection, labelConstraints );

		GridBagConstraints separatorConstraints = new GridBagConstraints();
		separatorConstraints.fill = GridBagConstraints.HORIZONTAL;
		separatorConstraints.gridx = 1;
		separatorConstraints.weightx = 1.0;
		separatorConstraints.anchor = GridBagConstraints.EAST;
		sectionPanel.add( new JSeparator( SwingConstants.HORIZONTAL ), separatorConstraints );

		GridBagConstraints sectionConstraints = new GridBagConstraints();
		sectionConstraints.gridy = 1;
		sectionConstraints.gridwidth = GridBagConstraints.REMAINDER;
		sectionConstraints.fill = GridBagConstraints.BOTH;
		sectionConstraints.weighty = 1.0f;
		sectionPanel.add( state.nestedMetawidget, sectionConstraints );

		// Push the given component into the nested Metawidget

		addNestedComponent( component, attributes, metawidget );

		// Return the JPanel for adding

		attributes.put( LABEL, "" );
		attributes.put( LARGE, TRUE );
		return sectionPanel;
	}

	//
	// Private methods
	//

	private State getState( SwingMetawidget metawidget )
	{
		State state = (State) metawidget.getClientProperty( SectionHeadingProcessor.class );

		if ( state == null )
		{
			state = new State();
			metawidget.putClientProperty( SectionHeadingProcessor.class, state );
		}

		return state;
	}

	/**
	 * Adds the component to the current nestedMetawidget, wrapping it in a Stub so as to pass along
	 * attributes.
	 */

	private void addNestedComponent( JComponent component, Map<String, String> attributes, SwingMetawidget metawidget )
	{
		Stub stub = new Stub();
		stub.setAttributes( attributes );
		stub.add( component );

		State state = (State) metawidget.getClientProperty( SectionHeadingProcessor.class );
		state.nestedMetawidget.add( stub );
	}

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	/* package private */class State
	{
		/* package private */String				currentSection;

		/* package private */SwingMetawidget	nestedMetawidget;
	}
}
