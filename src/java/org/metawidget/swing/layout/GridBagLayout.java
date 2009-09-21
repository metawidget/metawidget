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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.metawidget.layout.iface.Layout;
import org.metawidget.layout.iface.LayoutException;
import org.metawidget.layout.impl.LayoutUtils;
import org.metawidget.swing.Facet;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to arrange widgets using <code>javax.awt.GridBagLayout</code>.
 * <p>
 * Widgets are arranged in a table, with one column for labels and another for the widget,
 * <p>
 * This implementation recognizes the following parameters:
 * <p>
 * <ul>
 * <li><code>numberOfColumns</code>
 * <li><code>labelAlignment</code> - one of <code>SwingConstants.LEFT</code>,
 * <code>SwingConstants.CENTER</code>, <code>SwingConstants.RIGHT</code>,
 * <code>SwingConstants.LEADING</code> or <code>SwingConstants.TRAILING</code>
 * <li><code>sectionStyle</code> - either <code>SECTION_AS_HEADING</code> (the default) or
 * <code>SECTION_AS_TAB</code> (render sections as tabs in a <code>JTabbedPane</code>)
 * <li><code>labelSuffix</code> - defaults to ':'
 * <li><code>starAlignment</code> - one of <code>SwingConstants.LEFT</code>,
 * <code>SwingConstants.CENTER</code> or <code>SwingConstants.RIGHT</code>.
 * </ul>
 *
 * @author Richard Kennard
 */

public class GridBagLayout
	implements Layout<JComponent, SwingMetawidget>
{
	//
	// Public statics
	//

	public final static int		SECTION_AS_HEADING		= 0;

	public final static int		SECTION_AS_TAB			= 1;

	//
	// Private statics
	//

	private final static int	SMALL_GAP				= 3;

	private final static int	MEDIUM_GAP				= 5;

	private final static Insets	INSETS_COMPONENT		= new Insets( 0, 0, SMALL_GAP, 0 );

	private final static Insets	INSETS_SECTION_LABEL	= new Insets( 0, 0, 0, MEDIUM_GAP );

	private final static Border	BORDER_SECTION			= BorderFactory.createEmptyBorder( MEDIUM_GAP, 0, MEDIUM_GAP, 0 );

	private final static Border	BORDER_TAB				= BorderFactory.createEmptyBorder( SMALL_GAP, SMALL_GAP, SMALL_GAP, SMALL_GAP );

	//
	// Public methods
	//

	public void startLayout( SwingMetawidget metawidget )
	{
		metawidget.putClientProperty( GridBagLayout.class, null );
		State state = getState( metawidget );

		// Read parameters

		Object numberOfColumns = metawidget.getParameter( "numberOfColumns" );

		if ( numberOfColumns == null || metawidget.getParent() instanceof SwingMetawidget )
		{
			state.numberOfColumns = 1;
		}
		else
		{
			state.numberOfColumns = (Integer) numberOfColumns;

			if ( state.numberOfColumns < 1 )
				throw LayoutException.newException( "numberOfColumns must be >= 1" );
		}

		Object labelAlignment = metawidget.getParameter( "labelAlignment" );

		if ( labelAlignment == null )
			state.labelAlignment = SwingConstants.LEFT;
		else
			state.labelAlignment = (Integer) labelAlignment;

		Object sectionStyle = metawidget.getParameter( "sectionStyle" );

		if ( sectionStyle == null )
			state.sectionStyle = SECTION_AS_HEADING;
		else
			state.sectionStyle = (Integer) sectionStyle;

		Object labelSuffix = metawidget.getParameter( "labelSuffix" );

		if ( labelSuffix == null )
			state.labelSuffix = ":";
		else
			state.labelSuffix = (String) labelSuffix;

		Object starAlignment = metawidget.getParameter( "starAlignment" );

		if ( starAlignment == null )
			state.starAlignment = SwingConstants.CENTER;
		else
			state.starAlignment = (Integer) starAlignment;

		// Calculate default label inset
		//
		// We top align all our labels, not just those belonging to 'tall' components,
		// so that tall components, regular components and nested Metawidget components all line up.
		// However, we still want the JLabels to be middle aligned for one-line components (such as
		// JTextFields), so we top inset them a bit

		java.awt.GridBagLayout layoutManager = new java.awt.GridBagLayout();
		metawidget.setLayout( layoutManager );

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

	public void layoutChild( JComponent component, Map<String, String> attributes, SwingMetawidget metawidget )
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

		GridBagConstraints componentConstraints = new GridBagConstraints();

		if ( !( component instanceof JButton ) )
			componentConstraints.fill = GridBagConstraints.BOTH;

		componentConstraints.anchor = GridBagConstraints.WEST;
		componentConstraints.gridx = state.currentColumn * ( state.starAlignment == SwingConstants.RIGHT ? 3 : 2 );

		if ( labelText != null )
		{
			componentConstraints.gridx++;
		}
		else
		{
			componentConstraints.gridwidth = 2;
		}

		componentConstraints.gridy = state.currentRow;
		componentConstraints.weightx = 1.0f / state.numberOfColumns;
		componentConstraints.insets = INSETS_COMPONENT;

		if ( spanAllColumns )
		{
			componentConstraints.gridwidth = ( state.starAlignment == SwingConstants.RIGHT ? ( state.numberOfColumns * 3 - componentConstraints.gridx - 1 ) : GridBagConstraints.REMAINDER );
			state.currentColumn = state.numberOfColumns - 1;
		}

		// Hack for spacer row (see JavaDoc for state.mNeedSpacerRow): assume components
		// embedded in a JScrollPane are their own spacer row

		if ( component instanceof JScrollPane )
		{
			componentConstraints.weighty = 1.0f;

			if ( state.panelCurrent == null )
				state.needParentSpacerRow = false;
			else
				state.needPanelSpacerRow = false;
		}

		// Add to either current panel or direct to the Metawidget

		if ( state.panelCurrent == null )
			metawidget.add( component, componentConstraints );
		else
			state.panelCurrent.add( component, componentConstraints );

		layoutAfterChild( component, attributes, metawidget );

		state.currentColumn++;

		if ( state.currentColumn >= state.numberOfColumns )
		{
			state.currentColumn = 0;
			state.currentRow++;
		}
	}

	public void endLayout( SwingMetawidget metawidget )
	{
		sectionEnd( metawidget );

		// Buttons

		State state = getState( metawidget );
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

			if ( state.needParentSpacerRow )
			{
				buttonConstraints.weighty = 1.0f;
				state.needParentSpacerRow = false;
			}

			metawidget.add( buttonsFacet, buttonConstraints );

			state.currentRow++;
		}

		// Spacer row: see JavaDoc for state.mNeedParentSpacerRow

		if ( state.needParentSpacerRow )
		{
			if ( state.currentColumn > 0 )
			{
				state.currentColumn = 0;
				state.currentRow++;
			}

			JPanel panelSpacer = new JPanel();
			panelSpacer.setOpaque( false );
			GridBagConstraints constraintsSpacer = new GridBagConstraints();
			constraintsSpacer.gridy = state.currentRow;
			constraintsSpacer.weighty = 1.0f;
			metawidget.add( panelSpacer, constraintsSpacer );
		}
	}

	//
	// Protected methods
	//

	protected String layoutBeforeChild( JComponent component, String labelText, Map<String, String> attributes, SwingMetawidget metawidget )
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
			label.setHorizontalAlignment( state.labelAlignment );

			// Required

			String labelTextToUse = labelText;

			if ( attributes != null && TRUE.equals( attributes.get( REQUIRED ) ) && !TRUE.equals( attributes.get( READ_ONLY ) ) && !metawidget.isReadOnly() )
			{
				if ( state.starAlignment == SwingConstants.CENTER )
					labelTextToUse += "*";
				else if ( state.starAlignment == SwingConstants.LEFT )
					labelTextToUse = "*" + labelTextToUse;
			}

			labelTextToUse += state.labelSuffix;
			label.setText( labelTextToUse );

			GridBagConstraints labelConstraints = new GridBagConstraints();
			labelConstraints.gridx = state.currentColumn * ( state.starAlignment == SwingConstants.RIGHT ? 3 : 2 );
			labelConstraints.gridy = state.currentRow;
			labelConstraints.fill = GridBagConstraints.HORIZONTAL;
			labelConstraints.weightx = 0.1f / state.numberOfColumns;

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

		switch ( state.sectionStyle )
		{
			case SECTION_AS_TAB:

				JTabbedPane tabbedPane;

				if ( state.panelCurrent == null )
				{
					tabbedPane = new JTabbedPane();
					tabbedPane.setBorder( BORDER_SECTION );

					state.panelInsertedRow = state.currentRow;

					GridBagConstraints constraintsTabbedPane = new GridBagConstraints();
					constraintsTabbedPane.fill = GridBagConstraints.BOTH;
					constraintsTabbedPane.anchor = GridBagConstraints.WEST;
					constraintsTabbedPane.gridy = state.panelInsertedRow;
					constraintsTabbedPane.gridwidth = GridBagConstraints.REMAINDER;
					constraintsTabbedPane.weighty = 1.0f;
					state.needParentSpacerRow = false;

					metawidget.add( tabbedPane, constraintsTabbedPane );
				}
				else
				{
					tabbedPane = (JTabbedPane) state.panelCurrent.getParent();
					sectionEnd( metawidget );
				}

				state.panelCurrent = new JPanel();
				state.panelCurrent.setBorder( BORDER_TAB );
				state.panelCurrent.setName( localizedSection );
				state.panelCurrent.setLayout( new java.awt.GridBagLayout() );
				state.needPanelSpacerRow = true;

				tabbedPane.add( state.panelCurrent );

				state.currentRow = 0;
				state.currentColumn = 0;
				break;

			default:

				// Separator panel (a GridBagLayout within a GridBagLayout)

				JPanel panelSection = new JPanel();
				panelSection.setBorder( BORDER_SECTION );
				panelSection.setLayout( new java.awt.GridBagLayout() );
				panelSection.setOpaque( false );

				GridBagConstraints constraintsLabel = new GridBagConstraints();
				constraintsLabel.insets = INSETS_SECTION_LABEL;
				JLabel labelSection = new JLabel( localizedSection );
				panelSection.add( labelSection, constraintsLabel );

				GridBagConstraints constraintsSeparator = new GridBagConstraints();
				constraintsSeparator.fill = GridBagConstraints.HORIZONTAL;
				constraintsSeparator.weightx = 1.0;
				constraintsSeparator.anchor = GridBagConstraints.EAST;
				panelSection.add( new JSeparator( SwingConstants.HORIZONTAL ), constraintsSeparator );

				GridBagConstraints constraintsSection = new GridBagConstraints();
				constraintsSection.fill = GridBagConstraints.HORIZONTAL;
				constraintsSection.gridy = state.currentRow;
				constraintsSection.gridwidth = GridBagConstraints.REMAINDER;
				metawidget.add( panelSection, constraintsSection );

				state.currentRow++;
				break;
		}
	}

	protected void layoutAfterChild( JComponent component, Map<String, String> attributes, SwingMetawidget metawidget )
	{
		State state = getState( metawidget );

		if ( state.starAlignment != SwingConstants.RIGHT )
			return;

		if ( attributes == null || !TRUE.equals( attributes.get( REQUIRED ) ) || TRUE.equals( attributes.get( READ_ONLY ) ) || metawidget.isReadOnly() )
			return;

		JLabel star = new JLabel();
		star.setText( "*" );

		GridBagConstraints starConstraints = new GridBagConstraints();
		starConstraints.gridx = ( state.currentColumn * 3 ) + 2;
		starConstraints.gridy = state.currentRow;
		starConstraints.anchor = GridBagConstraints.NORTHWEST;

		if ( state.currentColumn == 0 )
			starConstraints.insets = state.defaultLabelInsetsFirstColumn;
		else
			starConstraints.insets = state.defaultLabelInsetsRemainderColumns;

		// Add to either current panel or direct to the Metawidget

		if ( state.panelCurrent == null )
			metawidget.add( star, starConstraints );
		else
			state.panelCurrent.add( star, starConstraints );
	}

	protected void sectionEnd( SwingMetawidget metawidget )
	{
		State state = getState( metawidget );

		if ( state.panelCurrent == null )
			return;

		// Spacer row: see JavaDoc for state.mNeedParentSpacerRow

		if ( state.needPanelSpacerRow )
		{
			if ( state.currentColumn > 0 )
			{
				state.currentColumn = 0;
				state.currentRow++;
			}

			JPanel panelSpacer = new JPanel();
			panelSpacer.setOpaque( false );
			GridBagConstraints constraintsSpacer = new GridBagConstraints();
			constraintsSpacer.gridy = state.currentRow;
			constraintsSpacer.weighty = 1.0f;
			state.panelCurrent.add( panelSpacer, constraintsSpacer );
		}

		state.currentRow = state.panelInsertedRow + 1;
		state.currentColumn = 0;

		state.panelCurrent = null;
	}

	//
	// Private methods
	//

	private State getState( SwingMetawidget metawidget )
	{
		State state = (State) metawidget.getClientProperty( GridBagLayout.class );

		if ( state == null )
		{
			state = new State();
			metawidget.putClientProperty( GridBagLayout.class, state );
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
		/* package private */String		currentSection;

		/* package private */int		labelAlignment;

		/* package private */int		sectionStyle;

		/* package private */int		numberOfColumns;

		/* package private */String		labelSuffix;

		/* package private */int		starAlignment;

		/* package private */int		currentColumn;

		/* package private */int		currentRow;

		/* package private */int		panelInsertedRow;

		/* package private */JPanel		panelCurrent;

		/* package private */Insets		defaultLabelInsetsFirstColumn;

		/* package private */Insets		defaultLabelInsetsRemainderColumns;

		/**
		 * Whether a spacer row is required on the last row of the layout.
		 * <p>
		 * By default, GridBagLayouts expand to fill their vertical space, and 'align middle' their
		 * group of components. To make them 'align top' at least one of the components must have a
		 * vertical constraint weighting &gt; 0.
		 */

		/* package private */boolean	needParentSpacerRow	= true;

		/**
		 * Whether a spacer row is required on the last row of the current panel.
		 * <p>
		 * By default, GridBagLayouts expand to fill their vertical space, and 'align middle' their
		 * group of components. To make them 'align top' at least one of the components must have a
		 * vertical constraint weighting &gt; 0.
		 */

		/* package private */boolean	needPanelSpacerRow;
	}
}
