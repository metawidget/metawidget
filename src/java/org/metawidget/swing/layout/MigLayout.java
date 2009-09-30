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

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;

import org.metawidget.layout.iface.Layout;
import org.metawidget.layout.impl.LayoutUtils;
import org.metawidget.swing.Facet;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to arrange widgets using <code>net.miginfocom.swing.MigLayout</code>.
 * <p>
 * Widgets are arranged in a table, with one column for labels and another for the widget,
 * <p>
 * This implementation recognizes the following parameters:
 * <p>
 * <ul>
 * <li><code>numberOfColumns</code>
 * <li><code>sectionStyle</code> - either <code>SECTION_AS_HEADING</code> (the default) or
 * <code>SECTION_AS_TAB</code> (render sections as tabs in a <code>JTabbedPane</code>)
 * </ul>
 *
 * @author Stefan Ackerman
 */

public class MigLayout
	implements Layout<JComponent, SwingMetawidget>
{
	//
	// Private members
	//

	private int					mNumberOfColumns;

	private int					mSectionStyle;

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
		mSectionStyle = config.getSectionStyle();
	}

	//
	// Public methods
	//

	public void onStartBuild( SwingMetawidget metawidget )
	{
		metawidget.putClientProperty( MigLayout.class, null );
		State state = getState( metawidget );

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
		metawidget.setLayout( layoutManager );

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

	public void layoutChild( JComponent component, String elementName, Map<String, String> attributes, SwingMetawidget metawidget )
	{
		// Do not render empty stubs

		if ( component instanceof Stub && ( (Stub) component ).getComponentCount() == 0 )
			return;

		// Special support for large components

		State state = getState( metawidget );
		boolean spanAllColumns = ( component instanceof JScrollPane || component instanceof SwingMetawidget || LayoutUtils.isSpanAllColumns( attributes ) );

		if ( spanAllColumns && state.currentColumn > 0 )
		{
			state.currentColumn = 0;
			state.currentRow++;
		}

		// Layout a label...

		String labelText = null;

		if ( attributes != null )
			labelText = metawidget.getLabelString( attributes );

		layoutBeforeChild( component, labelText, attributes, metawidget );

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

		if ( component instanceof JScrollPane )
		{
			componentConstraints.growY();

			if ( state.panelCurrent == null )
				( (LC) ( (net.miginfocom.swing.MigLayout) metawidget.getLayout() ).getLayoutConstraints() ).fill();
			else
				( (LC) ( (net.miginfocom.swing.MigLayout) state.panelCurrent.getLayout() ).getLayoutConstraints() ).fill();
		}

		// Add to either current panel or direct to the Metawidget

		if ( state.panelCurrent == null )
			metawidget.add( component, componentConstraints );
		else
			state.panelCurrent.add( component, componentConstraints );

		state.currentColumn++;

		if ( state.currentColumn >= mNumberOfColumns )
		{
			state.currentColumn = 0;
			state.currentRow++;
		}
	}

	public void onEndBuild( SwingMetawidget metawidget )
	{
		sectionEnd( metawidget );

		// Buttons

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

	//
	// Protected methods
	//

	protected String layoutBeforeChild( Component component, String labelText, Map<String, String> attributes, SwingMetawidget metawidget )
	{
		State state = getState( metawidget );

		if ( attributes != null )
		{
			// Section headings

			String section = attributes.get( SECTION );

			if ( section != null && !section.equals( state.currentSection ) )
			{
				state.currentSection = section;
				layoutSection( section, metawidget );
			}
		}

		// Add label

		if ( labelText != null && !"".equals( labelText ) && !( component instanceof JButton ) )
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

			// Add to either current panel or direct to the Metawidget

			if ( state.panelCurrent == null )
				metawidget.add( label, labelConstraints );
			else
				state.panelCurrent.add( label, labelConstraints );
		}

		return labelText;
	}

	protected void layoutSection( String section, SwingMetawidget metawidget )
	{
		if ( "".equals( section ) )
		{
			sectionEnd( metawidget );
			return;
		}

		// Section name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null )
			localizedSection = section;

		// Start a new row

		State state = getState( metawidget );

		if ( state.currentColumn > 0 )
		{
			state.currentColumn = 0;
			state.currentRow++;
		}

		// Insert the section

		switch ( mSectionStyle )
		{
			case MigLayoutConfig.SECTION_AS_TAB:

				JTabbedPane tabbedPane;

				if ( state.panelCurrent == null )
				{
					tabbedPane = new JTabbedPane();
					metawidget.add( tabbedPane, new CC().cell( state.currentColumn, state.currentRow ).spanX().grow() );
					( (LC) ( (net.miginfocom.swing.MigLayout) metawidget.getLayout() ).getLayoutConstraints() ).fill();
				}
				else
				{
					tabbedPane = (JTabbedPane) state.panelCurrent.getParent();
					sectionEnd(metawidget);
				}

				state.panelCurrent = new JPanel();
				state.panelCurrent.setName( localizedSection );
				state.panelCurrent.setLayout( new net.miginfocom.swing.MigLayout( new LC() ) );

				tabbedPane.add( state.panelCurrent );
				break;

			default:

				// Compared to GridBagLayout, we can achieve the heading without a nested JPanel

				JLabel label = new JLabel( localizedSection );
				metawidget.add( label, new CC().cell( state.currentColumn, state.currentRow ).spanX() );
				JSeparator separator = new JSeparator( SwingConstants.HORIZONTAL );
				metawidget.add( separator, new CC().cell( state.currentColumn, state.currentRow ).growX() );

				state.currentRow++;
				break;
		}
	}

	protected void sectionEnd( SwingMetawidget metawidget )
	{
		State state = getState( metawidget );

		if ( state.panelCurrent == null )
			return;

		state.currentColumn = 0;
		state.panelCurrent = null;
	}

	//
	// Private methods
	//

	private State getState( SwingMetawidget metawidget )
	{
		State state = (State) metawidget.getClientProperty( MigLayout.class );

		if ( state == null )
		{
			state = new State();
			metawidget.putClientProperty( MigLayout.class, state );
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

		/* package private */int	currentColumn;

		/* package private */int	currentRow;

		/* package private */JPanel	panelCurrent;

		/* package private */int	defaultLabelVerticalPadding;
	}
}
