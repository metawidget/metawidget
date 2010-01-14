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

import java.awt.Component;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;

import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.swing.Facet;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.simple.SimpleLayoutUtils;

/**
 * Layout to arrange widgets using <code>net.miginfocom.swing.MigLayout</code>.
 * <p>
 * Widgets are arranged in a table, with one column for labels and another for the widget.
 *
 * @author Stefan Ackerman
 */

public class MigLayout
	implements AdvancedLayout<JComponent, JComponent, SwingMetawidget>
{
	//
	// Private members
	//

	private int					mNumberOfColumns;

	//
	// Constructor
	//

	public MigLayout()
	{
		this( new MigLayoutConfig() );
	}

	public MigLayout( MigLayoutConfig config )
	{
		mNumberOfColumns = config.getNumberOfColumns();
	}

	//
	// Public methods
	//

	public void startLayout( JComponent container, SwingMetawidget metawidget )
	{
		container.putClientProperty( MigLayout.class, null );
		State state = getState( container );

		// The entire layout should fill both horizontally and vertically,
		// with no insets. This allows us to be properly nested, as well as
		// embedded within existing UIs, without alignment problems

		LC layoutConstraints = new LC().insets( "0" );

		// Debug Info (draws the red and blue lines)
		//
		// layoutConstraints.debug( 500 );

		// Create the Layout
		//
		// Note: we don't use column/row constraints, because we don't know
		// what the components will be in advance. Rather, we use 'cell' and 'push'

		java.awt.LayoutManager layoutManager = new net.miginfocom.swing.MigLayout( layoutConstraints );
		container.setLayout( layoutManager );

		// Calculate default label inset
		//
		// We top align all our labels, not just those belonging to 'tall' components,
		// so that tall components, regular components and nested Metawidget components all line up.
		// However, we still want the JLabels to be middle aligned for one-line components (such as
		// JTextFields), so we top inset them a bit

		JTextField dummyTextField = new JTextField();
		dummyTextField.setLayout( layoutManager );
		double dummyTextFieldHeight = dummyTextField.getPreferredSize().getHeight();

		JLabel dummyLabel = new JLabel( "X" );
		dummyLabel.setLayout( layoutManager );
		double dummyLabelHeight = dummyLabel.getPreferredSize().getHeight();

		state.defaultLabelVerticalPadding = (int) Math.max( 0, Math.floor( ( dummyTextFieldHeight - dummyLabelHeight ) / 2 ) );
	}

	public void layoutWidget( JComponent component, String elementName, Map<String, String> attributes, JComponent container, SwingMetawidget metawidget )
	{
		// Do not render empty stubs

		if ( component instanceof Stub && ( (Stub) component ).getComponentCount() == 0 )
			return;

		// Special support for large components

		State state = getState( container );
		boolean spanAllColumns = ( component instanceof JScrollPane || component instanceof SwingMetawidget || SimpleLayoutUtils.isSpanAllColumns( attributes ) );

		if ( spanAllColumns && state.currentColumn > 0 )
		{
			state.currentColumn = 0;
			state.currentRow++;
		}

		// Layout a label...

		String labelText = null;

		if ( attributes != null )
			labelText = metawidget.getLabelString( attributes );

		layoutBeforeChild( component, labelText, elementName, attributes, container, metawidget );

		// ...and layout the component

		CC componentConstraints = new CC();

		if ( labelText != null )
		{
			componentConstraints.cell( ( state.currentColumn * 2 ) + 1, state.currentRow );
		}
		else
		{
			componentConstraints.cell( state.currentColumn * 2, state.currentRow );
			componentConstraints.spanX( 2 );
		}

		componentConstraints.pushX( 1f ).growX();

		if ( spanAllColumns )
		{
			componentConstraints.spanX();
			state.currentColumn = mNumberOfColumns;
		}

		// Assume JScrollPanes should grow vertically

		if ( willFillVertically( component, attributes ) )
		{
			componentConstraints.growY();
			( (LC) ( (net.miginfocom.swing.MigLayout) container.getLayout() ).getLayoutConstraints() ).fill();
		}

		// Add it

		container.add( component, componentConstraints );

		state.currentColumn++;

		if ( state.currentColumn >= mNumberOfColumns )
		{
			state.currentColumn = 0;
			state.currentRow++;
		}
	}

	public void endLayout( JComponent container, SwingMetawidget metawidget )
	{
		// Buttons

		if ( container.equals( metawidget ))
		{
			Facet buttonsFacet = metawidget.getFacet( "buttons" );

			if ( buttonsFacet != null )
			{
				State state = getState( metawidget );

				if ( state.currentColumn > 0 )
				{
					state.currentColumn = 0;
					state.currentRow++;
				}

				metawidget.add( buttonsFacet, new CC().cell( 0, state.currentRow ).spanX().growX() );
			}
		}
	}

	//
	// Protected methods
	//

	protected String layoutBeforeChild( Component component, String labelText, String elementName, Map<String, String> attributes, JComponent container, SwingMetawidget metawidget )
	{
		State state = getState( container );

		// Add label

		if ( SimpleLayoutUtils.needsLabel( labelText, elementName ))
		{
			JLabel label = new JLabel();

			// Required

			if ( attributes != null && TRUE.equals( attributes.get( REQUIRED ) ) && !TRUE.equals( attributes.get( READ_ONLY ) ) && !metawidget.isReadOnly() )
				label.setText( labelText + "*:" );
			else
				label.setText( labelText + ":" );

			CC labelConstraints = new CC();
			labelConstraints.cell( state.currentColumn * 2, state.currentRow );
			labelConstraints.growX();

			// Top align all labels, not just those belonging to 'tall' components,
			// so that tall components, regular components and nested Metawidget
			// components all line up

			labelConstraints.alignY( "top" );

			// Apply some vertical padding so the label lines up with the component nicely

			labelConstraints.pad( state.defaultLabelVerticalPadding, 0, state.defaultLabelVerticalPadding, 0 );

			// Add it

			container.add( label, labelConstraints );
		}

		return labelText;
	}

	protected boolean willFillVertically( JComponent component, Map<String, String> attributes )
	{
		if ( attributes != null && TRUE.equals( attributes.get( LARGE )))
			return true;

		if ( component instanceof JScrollPane )
			return true;

		return false;
	}

	//
	// Private methods
	//

	private State getState( JComponent container )
	{
		State state = (State) container.getClientProperty( MigLayout.class );

		if ( state == null )
		{
			state = new State();
			container.putClientProperty( MigLayout.class, state );
		}

		return state;
	}

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	/* package private */static class State
	{
		/* package private */int	currentColumn;

		/* package private */int	currentRow;

		/* package private */int	defaultLabelVerticalPadding;
	}
}
