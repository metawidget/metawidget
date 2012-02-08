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

import java.awt.Color;
import java.awt.Font;
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
import org.metawidget.util.simple.SimpleLayoutUtils.StrippedMnemonicAndFirstIndex;

/**
 * Layout to arrange widgets using <code>java.awt.GridBagLayout</code>.
 * <p>
 * Widgets are arranged in a table, with one column for labels and another for the widget.
 *
 * @author Richard Kennard
 */

public class GridBagLayout
	implements AdvancedLayout<JComponent, JComponent, SwingMetawidget> {

	//
	// Private statics
	//

	private static final int	SMALL_GAP			= 3;

	private static final Insets	INSETS_COMPONENT	= new Insets( 0, 0, SMALL_GAP, 0 );

	private static final String	LABEL_NAME_SUFFIX	= "_label";

	//
	// Private members
	//

	private final int			mNumberOfColumns;

	private final int			mLabelAlignment;

	private final Font			mLabelFont;

	private final Color			mLabelForeground;

	private final boolean		mSupportMnemonics;

	private final String		mLabelSuffix;

	private final int			mRequiredAlignment;

	private final String		mRequiredText;

	//
	// Constructor
	//

	public GridBagLayout() {

		this( new GridBagLayoutConfig() );
	}

	public GridBagLayout( GridBagLayoutConfig config ) {

		mNumberOfColumns = config.getNumberOfColumns();
		mLabelAlignment = config.getLabelAlignment();
		mLabelForeground = config.getLabelForeground();
		mLabelFont = config.getLabelFont();
		mSupportMnemonics = config.isSupportMnemonics();
		mLabelSuffix = config.getLabelSuffix();
		mRequiredAlignment = config.getRequiredAlignment();
		mRequiredText = config.getRequiredText();
	}

	//
	// Public methods
	//

	public void onStartBuild( SwingMetawidget metawidget ) {

		// Do nothing
	}

	public void startContainerLayout( JComponent container, SwingMetawidget metawidget ) {

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

	public void layoutWidget( JComponent component, String elementName, Map<String, String> attributes, JComponent container, SwingMetawidget metawidget ) {

		// Do not render empty stubs

		if ( component instanceof Stub && ( (Stub) component ).getComponentCount() == 0 ) {
			return;
		}

		// Special support for large components

		State state = getState( container );
		boolean spanAllColumns = willFillHorizontally( component, attributes );

		if ( spanAllColumns && state.currentColumn > 0 ) {
			state.currentColumn = 0;
			state.currentRow++;
		}

		// Layout a label...

		String labelText = null;

		if ( attributes != null ) {
			labelText = metawidget.getLabelString( attributes );
		}

		layoutBeforeChild( component, labelText, elementName, attributes, container, metawidget );

		// ...and layout the component

		GridBagConstraints componentConstraints = new GridBagConstraints();

		setFillConstraints( component, componentConstraints );

		componentConstraints.anchor = GridBagConstraints.WEST;
		componentConstraints.gridx = state.currentColumn * ( mRequiredAlignment == SwingConstants.RIGHT ? 3 : 2 );

		int numberOfColumns = getEffectiveNumberOfColumns( metawidget );

		if ( labelText != null ) {
			if ( numberOfColumns == 0 ) {
				state.currentRow++;
			} else {
				componentConstraints.gridx++;
			}
		} else {
			componentConstraints.gridwidth = 2;
		}

		componentConstraints.gridy = state.currentRow;
		componentConstraints.weightx = 1.0f / numberOfColumns;
		componentConstraints.insets = INSETS_COMPONENT;

		if ( spanAllColumns ) {
			componentConstraints.gridwidth = ( mRequiredAlignment == SwingConstants.RIGHT ? ( numberOfColumns * 3 - componentConstraints.gridx - 1 ) : GridBagConstraints.REMAINDER );
			state.currentColumn = numberOfColumns - 1;
		}

		// Hack for spacer row (see JavaDoc for state.mNeedSpacerRow): assume components
		// embedded in a JScrollPane are their own spacer row

		if ( willFillVertically( component, attributes ) ) {
			componentConstraints.weighty = 1.0f;
			state.needSpacerRow = false;
		}

		// Add it

		container.add( component, componentConstraints );

		layoutAfterChild( component, attributes, container, metawidget );

		state.currentColumn++;

		if ( state.currentColumn >= numberOfColumns ) {
			state.currentColumn = 0;
			state.currentRow++;
		}
	}

	public void endContainerLayout( JComponent container, SwingMetawidget metawidget ) {

		// Spacer row: see JavaDoc for state.needSpacerRow

		State state = getState( container );

		if ( state.needSpacerRow ) {
			if ( state.currentColumn > 0 ) {
				state.currentColumn = 0;
				state.currentRow++;
			}

			JPanel spacerPanel = new JPanel();
			spacerPanel.setOpaque( false );
			GridBagConstraints spacerConstraints = new GridBagConstraints();
			spacerConstraints.gridy = state.currentRow;
			spacerConstraints.weighty = 1.0f;
			container.add( spacerPanel, spacerConstraints );
			state.currentColumn = mNumberOfColumns;
		}
	}

	public void onEndBuild( SwingMetawidget metawidget ) {

		// Buttons

		Facet buttonsFacet = metawidget.getFacet( "buttons" );

		if ( buttonsFacet != null ) {
			State state = getState( metawidget );
			if ( state.currentColumn > 0 ) {
				state.currentColumn = 0;
				state.currentRow++;
			}

			GridBagConstraints buttonConstraints = new GridBagConstraints();
			buttonConstraints.fill = GridBagConstraints.BOTH;
			buttonConstraints.anchor = GridBagConstraints.WEST;
			buttonConstraints.gridy = state.currentRow;
			buttonConstraints.gridwidth = GridBagConstraints.REMAINDER;

			metawidget.add( buttonsFacet, buttonConstraints );
		}
	}

	//
	// Protected methods
	//

	protected void layoutBeforeChild( JComponent child, String labelText, String elementName, Map<String, String> attributes, JComponent container, SwingMetawidget metawidget ) {

		State state = getState( container );

		// Add label

		if ( SimpleLayoutUtils.needsLabel( labelText, elementName ) ) {
			JLabel label = new JLabel();
			label.setName( attributes.get( NAME ) + LABEL_NAME_SUFFIX );

			if ( mLabelFont != null ) {
				label.setFont( mLabelFont );
			}

			if ( mLabelForeground != null ) {
				label.setForeground( mLabelForeground );
			}

			label.setHorizontalAlignment( mLabelAlignment );

			// Required

			StrippedMnemonicAndFirstIndex strippedMnemonicAndFirstIndex = SimpleLayoutUtils.stripMnemonic( labelText );
			String labelTextToUse = strippedMnemonicAndFirstIndex.getStrippedMnemonic();

			if ( mRequiredText != null && TRUE.equals( attributes.get( REQUIRED ) ) && !TRUE.equals( attributes.get( READ_ONLY ) ) && !metawidget.isReadOnly() ) {
				if ( mRequiredAlignment == SwingConstants.CENTER ) {
					labelTextToUse += mRequiredText;
				} else if ( mRequiredAlignment == SwingConstants.LEFT ) {
					labelTextToUse = mRequiredText + labelTextToUse;
				}
			}

			if ( mLabelSuffix != null ) {
				labelTextToUse += mLabelSuffix;
			}

			label.setText( labelTextToUse );

			// Mnemonic

			label.setLabelFor( child );

			int mnemonicIndex = strippedMnemonicAndFirstIndex.getFirstIndex();

			if ( mnemonicIndex != -1 && mSupportMnemonics ) {
				label.setDisplayedMnemonic( labelTextToUse.charAt( mnemonicIndex ) );
				label.setDisplayedMnemonicIndex( mnemonicIndex );
			}

			// GridBagConstraints

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

			if ( state.currentColumn == 0 ) {
				labelConstraints.insets = state.defaultLabelInsetsFirstColumn;
			} else {
				labelConstraints.insets = state.defaultLabelInsetsRemainderColumns;
			}

			container.add( label, labelConstraints );
		}
	}

	/**
	 * @param child
	 *            component being laid out
	 */

	protected void layoutAfterChild( JComponent child, Map<String, String> attributes, JComponent container, SwingMetawidget metawidget ) {

		State state = getState( container );

		if ( mRequiredAlignment != SwingConstants.RIGHT ) {
			return;
		}

		if ( attributes == null || !TRUE.equals( attributes.get( REQUIRED ) ) || TRUE.equals( attributes.get( READ_ONLY ) ) || metawidget.isReadOnly() ) {
			return;
		}

		JLabel star = new JLabel();
		star.setText( mRequiredText );

		GridBagConstraints starConstraints = new GridBagConstraints();
		starConstraints.gridx = ( state.currentColumn * 3 ) + 2;
		starConstraints.gridy = state.currentRow;
		starConstraints.anchor = GridBagConstraints.NORTHWEST;

		if ( state.currentColumn == 0 ) {
			starConstraints.insets = state.defaultLabelInsetsFirstColumn;
		} else {
			starConstraints.insets = state.defaultLabelInsetsRemainderColumns;
		}

		container.add( star, starConstraints );
	}

	protected boolean willFillHorizontally( JComponent component, Map<String, String> attributes ) {

		if ( component instanceof JScrollPane ) {
			return true;
		}

		if ( component instanceof SwingMetawidget ) {
			return true;
		}

		return SimpleLayoutUtils.isSpanAllColumns( attributes );
	}

	protected boolean willFillVertically( JComponent component, Map<String, String> attributes ) {

		if ( attributes != null && TRUE.equals( attributes.get( LARGE ) ) ) {
			return true;
		}

		if ( component instanceof JScrollPane ) {
			return true;
		}

		return false;
	}

	/**
	 * Sets the fill constraints for this component. Defaults to
	 * <code>GridBagConstraints.BOTH</code>, unless the <code>JComponent</code> is
	 * a <code>JButton</code>.
	 * <p>
	 * Clients can override this method to change how the fill constraints are set. For example, you
	 * may not want any fill constraints if the <code>JComponent</code> has
	 * <code>getPreferredSize() != null</code>.
	 */

	protected void setFillConstraints( JComponent component, GridBagConstraints componentConstraints ) {

		if ( component instanceof JButton ) {
			return;
		}

		componentConstraints.fill = GridBagConstraints.BOTH;
	}

	//
	// Private methods
	//

	/**
	 * Get the number of columns to use in the layout.
	 * <p>
	 * Nested Metawidgets are always just single column.
	 */

	private int getEffectiveNumberOfColumns( SwingMetawidget metawidget ) {

		if ( metawidget.getParent() instanceof SwingMetawidget ) {
			return 1;
		}

		return mNumberOfColumns;
	}

	private State getState( JComponent container ) {

		State state = (State) container.getClientProperty( GridBagLayout.class );

		if ( state == null ) {
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

	/* package private */static class State {

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
