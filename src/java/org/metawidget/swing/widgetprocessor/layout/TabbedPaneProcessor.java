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

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.impl.BaseWidgetProcessor;

/**
 * @author Richard Kennard
 */

public class TabbedPaneProcessor
	extends BaseWidgetProcessor<JComponent, SwingMetawidget>
{
	//
	// Public methods
	//

	@Override
	public void onStartBuild( SwingMetawidget metawidget )
	{
		metawidget.putClientProperty( TabbedPaneProcessor.class, null );
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

		// End current section?

		if ( "".equals( section ) )
		{
			if ( state.nestedMetawidget != null )
				state.nestedMetawidget = null;

			return component;
		}

		boolean newTabbedPane = ( state.nestedMetawidget == null );
		JTabbedPane tabbedPane;

		// Whole new tabbed pane?

		if ( newTabbedPane )
		{
			tabbedPane = new JTabbedPane();
		}

		// New tab in an existing tab pane

		else
		{
			tabbedPane = (JTabbedPane) state.nestedMetawidget.getParent().getParent();
		}

		state.nestedMetawidget = new SwingMetawidget();
		state.nestedMetawidget.setMetawidgetLayout( metawidget.getMetawidgetLayout() );

		// Section name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null )
			localizedSection = section;

		JPanel opaqueBackground = new JPanel();
		opaqueBackground.setLayout( new javax.swing.BoxLayout( opaqueBackground, javax.swing.BoxLayout.PAGE_AXIS ) );
		opaqueBackground.add( state.nestedMetawidget );

		tabbedPane.addTab( localizedSection, opaqueBackground );

		// Push the given component into the nested Metawidget

		addNestedComponent( component, attributes, metawidget );

		// Return the JTabbedPane for adding

		if ( !newTabbedPane )
			return null;

		attributes.put( LABEL, "" );
		attributes.put( WIDE, TRUE );
		return tabbedPane;
	}

	//
	// Private methods
	//

	private State getState( SwingMetawidget metawidget )
	{
		State state = (State) metawidget.getClientProperty( TabbedPaneProcessor.class );

		if ( state == null )
		{
			state = new State();
			metawidget.putClientProperty( TabbedPaneProcessor.class, state );
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

		// TODO: maybe use client props instead of stub wrapping?

		State state = (State) metawidget.getClientProperty( TabbedPaneProcessor.class );
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
