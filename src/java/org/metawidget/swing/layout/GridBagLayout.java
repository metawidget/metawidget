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

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.swing.Facet;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.simple.SimpleLayoutUtils;

/**
 * Layout to arrange widgets using <code>javax.awt.GridBagLayout</code>.
 * <p>
 * Widgets are arranged in a table, with one column for labels and another for the widget.
 *
 * @author Richard Kennard
 */

public class GridBagLayout
	implements AdvancedLayout<JComponent, JComponent, SwingMetawidget>
{
	//
	// Private statics
	//

	private final static int	SMALL_GAP			= 3;

	private final static Insets	INSETS_COMPONENT	= new Insets( 0, 0, SMALL_GAP, 0 );

	//
	// Private members
	//

	final private int			mNumberOfColumns;

	final private int			mLabelAlignment;

	final private String		mLabelSuffix;

	final private int			mRequiredAlignment;

	final private String		mRequiredText;

	//
	// Constructor
	//

	public GridBagLayout()
	{
		this( new GridBagLayoutConfig() );
	}

	public GridBagLayout( GridBagLayoutConfig config )
	{
		mNumberOfColumns = config.getNumberOfColumns();
		mLabelAlignment = config.getLabelAlignment();
		mLabelSuffix = config.getLabelSuffix();
		mRequiredAlignment = config.getRequiredAlignment();
		mRequiredText = config.getRequiredText();
	}

	//
	// Public methods
	//

	public void startLayout( JComponent container, SwingMetawidget metawidget )
	{
		container.putClientProperty( GridBagLayout.class, null );
		State state = getState( container );

		// Calculate default label inset
		//
		// We top align all our labels, not just those belonging to 'tall' components,
		// so that tall components, regular components and nested Metawidget components all line up.
		// However, we still want the JLabels to be middle aligned for one-line components (such as
		// JTextFields), so we top inset them a bit

		java.awt.GridBagLayout layoutManager = new java.awt.GridBagLayout();
		container.setLayout( layoutManager );

		JTextField dummyTextField = new JTextField();
		dummyTextField.setLayout( layoutManager );
		double dummyTextFieldHeight = dummyTextField.getPreferredSize().getHeight();

		JLabel dummyLabel = new JLabel( "X" );
		dummyLabel.setLayout( layoutManager );
		double dummyLabelHeight = dummyLabel.getPreferredSize().getHeight();

		int defaultLabelVerticalPadding = (int) Math.max( 0, Math.floor( ( dummyTextFieldHeight - dummyLabelHeight ) / 2 ) );
		state.defaultLabelInsetsFirstColumn = new Insets( defaultLabelVerticalPadding, 0, defaultLabelVerticalPadding, SMALL_GAP );
		state.defaultLabelInsetsRemainderColumns = new Insets( defaultLabelVerticalPadding, SMALL_GAP, defaultLabelVerticalPadding, SMALL_GAP );
	}

	public void layoutWidget( JComponent component, String elementName, Map<String, String> attributes, JComponent container, SwingMetawidget metawidget )
	{
		// Do not render empty stubs

		if ( component instanceof Stub && ( (Stub) component ).getComponentCount() == 0 )
			return;

		// Special support for large components

		State state = getState( container );
		boolean spanAllColumns = willFillHorizontally( component, attributes );

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

		GridBagConstraints componentConstraints = new GridBagConstraints();

		if ( !( component instanceof JButton ) )
			componentConstraints.fill = GridBagConstraints.BOTH;

		componentConstraints.anchor = GridBagConstraints.WEST;
		componentConstraints.gridx = state.currentColumn * ( mRequiredAlignment == SwingConstants.RIGHT ? 3 : 2 );

		if ( labelText != null )
		{
			componentConstraints.gridx++;
		}
		else
		{
			componentConstraints.gridwidth = 2;
		}

		int numberOfColumns = getEffectiveNumberOfColumns( metawidget );

		componentConstraints.gridy = state.currentRow;
		componentConstraints.weightx = 1.0f / numberOfColumns;
		componentConstraints.insets = INSETS_COMPONENT;

		if ( spanAllColumns )
		{
			componentConstraints.gridwidth = ( mRequiredAlignment == SwingConstants.RIGHT ? ( numberOfColumns * 3 - componentConstraints.gridx - 1 ) : GridBagConstraints.REMAINDER );
			state.currentColumn = numberOfColumns - 1;
		}

		// Hack for spacer row (see JavaDoc for state.mNeedSpacerRow): assume components
		// embedded in a JScrollPane are their own spacer row

		if ( willFillVertically( component, attributes ) )
		{
			componentConstraints.weighty = 1.0f;
			state.needSpacerRow = false;
		}

		// Add it

		container.add( component, componentConstraints );

		layoutAfterChild( component, attributes, container, metawidget );

		state.currentColumn++;

		if ( state.currentColumn >= numberOfColumns )
		{
			state.currentColumn = 0;
			state.currentRow++;
		}
	}

	public void endLayout( JComponent container, SwingMetawidget metawidget )
	{
		// Buttons

		State state = getState( container );

		if ( container.equals( metawidget ) )
		{
			Facet buttonsFacet = metawidget.getFacet( "buttons" );

			if ( buttonsFacet != null )
			{
				if ( state.currentColumn > 0 )
				{
					state.currentColumn = 0;
					state.currentRow++;
				}

				GridBagConstraints buttonConstraints = new GridBagConstraints();
				buttonConstraints.fill = GridBagConstraints.BOTH;
				buttonConstraints.anchor = GridBagConstraints.WEST;
				buttonConstraints.gridy = state.currentRow;
				buttonConstraints.gridwidth = GridBagConstraints.REMAINDER;

				// (buttons facet can act as the spacer row)

				if ( state.needSpacerRow )
				{
					buttonConstraints.weighty = 1.0f;
					state.needSpacerRow = false;
				}

				metawidget.add( buttonsFacet, buttonConstraints );
			}
		}

		// Spacer row: see JavaDoc for state.needSpacerRow

		if ( state.needSpacerRow )
		{
			if ( state.currentColumn > 0 )
			{
				state.currentColumn = 0;
				state.currentRow++;
			}

			JPanel spacerPanel = new JPanel();
			spacerPanel.setOpaque( false );
			GridBagConstraints spacerConstraints = new GridBagConstraints();
			spacerConstraints.gridy = state.currentRow;
			spacerConstraints.weighty = 1.0f;
			container.add( spacerPanel, spacerConstraints );
		}
	}

	//
	// Protected methods
	//

	protected String layoutBeforeChild( JComponent component, String labelText, String elementName, Map<String, String> attributes, JComponent container, SwingMetawidget metawidget )
	{
		State state = getState( container );

		// Add label

		if ( SimpleLayoutUtils.needsLabel( labelText, elementName ) )
		{
			JLabel label = new JLabel();
			label.setHorizontalAlignment( mLabelAlignment );

			// Required

			String labelTextToUse = labelText;

			if ( attributes != null && mRequiredText != null && TRUE.equals( attributes.get( REQUIRED ) ) && !TRUE.equals( attributes.get( READ_ONLY ) ) && !metawidget.isReadOnly() )
			{
				if ( mRequiredAlignment == SwingConstants.CENTER )
					labelTextToUse += mRequiredText;
				else if ( mRequiredAlignment == SwingConstants.LEFT )
					labelTextToUse = mRequiredText + labelTextToUse;
			}

			if ( mLabelSuffix != null )
				labelTextToUse += mLabelSuffix;

			label.setText( labelTextToUse );

			GridBagConstraints labelConstraints = new GridBagConstraints();
			labelConstraints.gridx = state.currentColumn * ( mRequiredAlignment == SwingConstants.RIGHT ? 3 : 2 );
			labelConstraints.gridy = state.currentRow;
			labelConstraints.fill = GridBagConstraints.HORIZONTAL;
			labelConstraints.weightx = 0.1f / getEffectiveNumberOfColumns( metawidget );

			// Top align all labels, not just those belonging to 'tall' components,
			// so that tall components, regular components and nested Metawidget
			// components all line up

			labelConstraints.anchor = GridBagConstraints.NORTHWEST;

			// Apply some vertical padding, and some left padding on everything but the
			// first column, so the label lines up with the component nicely

			if ( state.currentColumn == 0 )
				labelConstraints.insets = state.defaultLabelInsetsFirstColumn;
			else
				labelConstraints.insets = state.defaultLabelInsetsRemainderColumns;

			container.add( label, labelConstraints );
		}

		return labelText;
	}

	protected void layoutAfterChild( JComponent component, Map<String, String> attributes, JComponent container, SwingMetawidget metawidget )
	{
		State state = getState( container );

		if ( mRequiredAlignment != SwingConstants.RIGHT )
			return;

		if ( attributes == null || !TRUE.equals( attributes.get( REQUIRED ) ) || TRUE.equals( attributes.get( READ_ONLY ) ) || metawidget.isReadOnly() )
			return;

		JLabel star = new JLabel();
		star.setText( mRequiredText );

		GridBagConstraints starConstraints = new GridBagConstraints();
		starConstraints.gridx = ( state.currentColumn * 3 ) + 2;
		starConstraints.gridy = state.currentRow;
		starConstraints.anchor = GridBagConstraints.NORTHWEST;

		if ( state.currentColumn == 0 )
			starConstraints.insets = state.defaultLabelInsetsFirstColumn;
		else
			starConstraints.insets = state.defaultLabelInsetsRemainderColumns;

		container.add( star, starConstraints );
	}

	protected boolean willFillHorizontally( JComponent component, Map<String, String> attributes )
	{
		if ( component instanceof JScrollPane )
			return true;

		if ( component instanceof SwingMetawidget )
			return true;

		return SimpleLayoutUtils.isSpanAllColumns( attributes );
	}

	protected boolean willFillVertically( JComponent component, Map<String, String> attributes )
	{
		if ( attributes != null && TRUE.equals( attributes.get( LARGE ) ) )
			return true;

		if ( component instanceof JScrollPane )
			return true;

		return false;
	}

	//
	// Private methods
	//

	/**
	 * Get the number of columns to use in the layout.
	 * <p>
	 * Nested Metawidgets are always just single column.
	 */

	private int getEffectiveNumberOfColumns( SwingMetawidget metawidget )
	{
		if ( metawidget.getParent() instanceof SwingMetawidget )
			return 1;

		return mNumberOfColumns;
	}

	private State getState( JComponent container )
	{
		State state = (State) container.getClientProperty( GridBagLayout.class );

		if ( state == null )
		{
			state = new State();
			container.putClientProperty( GridBagLayout.class, state );
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
		/* package private */int		currentColumn;

		/* package private */int		currentRow;

		/* package private */Insets		defaultLabelInsetsFirstColumn;

		/* package private */Insets		defaultLabelInsetsRemainderColumns;

		/**
		 * Whether a spacer row is required on the last row of the layout.
		 * <p>
		 * By default, GridBagLayouts expand to fill their vertical space, and 'align middle' their
		 * group of components. To make them 'align top' at least one of the components must have a
		 * vertical constraint weighting &gt; 0.
		 */

		/* package private */boolean	needSpacerRow	= true;
	}
}
