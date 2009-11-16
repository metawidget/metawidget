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

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import org.metawidget.layout.iface.Layout;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * @author Richard Kennard
 */

public class JTabbedPaneLayout
	implements Layout<JComponent, SwingMetawidget>
{
	//
	// Public methods
	//

	@Override
	public void onStartBuild( SwingMetawidget metawidget )
	{
		metawidget.putClientProperty( JTabbedPaneLayout.class, null );
	}

	public JComponent layoutChild( JComponent component, String elementName, Map<String, String> attributes, SwingMetawidget metawidget )
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

		// Whole new tabbed pane?

		JTabbedPane tabbedPane;

		if ( state.nestedMetawidget == null )
		{
			tabbedPane = new JTabbedPane();
		}

		// New tab in an existing tab pane

		else
		{
			tabbedPane = (JTabbedPane) state.nestedMetawidget.getParent();
		}

		state.nestedMetawidget = new SwingMetawidget();
		state.nestedMetawidget.setMetawidgetLayout( metawidget.getMetawidgetLayout() );

		// Section name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null )
			localizedSection = section;

		tabbedPane.addTab( localizedSection, state.nestedMetawidget );

		// Push the given component into the nested Metawidget

		addNestedComponent( component, attributes, metawidget );

		// Return the JTabbedPane for adding

		attributes.put( LABEL, "" );
		attributes.put( WIDE, TRUE );
		return tabbedPane;
	}

	@Override
	public void onEndBuild( SwingMetawidget metawidget )
	{
		State state = getState( metawidget );

		if ( state.nestedMetawidget != null )
			state.nestedMetawidget.getMetawidgetLayout().onEndBuild( state.nestedMetawidget );
	}

	//
	// Private methods
	//

	private State getState( SwingMetawidget metawidget )
	{
		State state = (State) metawidget.getClientProperty( JTabbedPaneLayout.class );

		if ( state == null )
		{
			state = new State();
			metawidget.putClientProperty( JTabbedPaneLayout.class, state );
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
		stub.setAttributes( CollectionUtils.newHashMap( attributes ) );
		stub.add( component );

		State state = (State) metawidget.getClientProperty( JTabbedPaneLayout.class );
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
