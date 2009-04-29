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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;

import org.metawidget.MetawidgetException;
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
	extends BaseLayout
{
	//
	// Public statics
	//

	public final static int	SECTION_AS_HEADING	= 0;

	public final static int	SECTION_AS_TAB		= 1;

	//
	// Private members
	//

	private String			mCurrentSection;

	private int				mNumberOfColumns;

	private int				mCurrentColumn;

	private int				mCurrentRow;

	private int				mSectionStyle;

	private JPanel			mPanelCurrent;

	private int				mDefaultLabelVerticalPadding;

	//
	// Constructor
	//

	public MigLayout( SwingMetawidget metawidget )
	{
		super( metawidget );

		// Read parameters

		Object numberOfColumns = metawidget.getParameter( "numberOfColumns" );

		if ( numberOfColumns == null || metawidget.getParent() instanceof SwingMetawidget )
		{
			mNumberOfColumns = 1;
		}
		else
		{
			mNumberOfColumns = (Integer) numberOfColumns;

			if ( mNumberOfColumns < 1 )
				throw MetawidgetException.newException( "numberOfColumns must be >= 1" );
		}

		Object sectionStyle = metawidget.getParameter( "sectionStyle" );

		if ( sectionStyle == null )
			mSectionStyle = SECTION_AS_HEADING;
		else
			mSectionStyle = (Integer) sectionStyle;
	}

	//
	// Public methods
	//

	@Override
	public void layoutBegin()
	{
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
		getMetawidget().setLayout( layoutManager );

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

		mDefaultLabelVerticalPadding = (int) Math.max( 0, Math.floor(( dummyTextFieldHeight - dummyLabelHeight ) / 2 ));
	}

	public void layoutChild( Component component, Map<String, String> attributes )
	{
		// Do not render empty stubs

		if ( component instanceof Stub && ( (Stub) component ).getComponentCount() == 0 )
			return;

		// Special support for large components

		boolean largeComponent = ( component instanceof JScrollPane || component instanceof SwingMetawidget || ( attributes != null && TRUE.equals( attributes.get( LARGE ) ) ) );

		if ( largeComponent && mCurrentColumn > 0 )
		{
			mCurrentColumn = 0;
			mCurrentRow++;
		}

		// Layout a label...

		String labelText = null;

		if ( attributes != null )
			labelText = getMetawidget().getLabelString( attributes );

		layoutBeforeChild( component, labelText, attributes );

		// ...and layout the component

		CC componentConstraints = new CC();

		if ( labelText != null )
		{
			componentConstraints.cell( ( mCurrentColumn * 2 ) + 1, mCurrentRow );
		}
		else
		{
			componentConstraints.cell( mCurrentColumn * 2, mCurrentRow );
			componentConstraints.spanX( 2 );
		}

		componentConstraints.pushX( 1f ).growX();

		if ( largeComponent )
		{
			componentConstraints.spanX();
			mCurrentColumn = mNumberOfColumns;
		}

		// Assume JScrollPanes should grow vertically

		if ( component instanceof JScrollPane )
		{
			componentConstraints.growY();

			if ( mPanelCurrent == null )
				( (LC) ( (net.miginfocom.swing.MigLayout) getMetawidget().getLayout() ).getLayoutConstraints() ).fill();
			else
				( (LC) ( (net.miginfocom.swing.MigLayout) mPanelCurrent.getLayout() ).getLayoutConstraints() ).fill();
		}

		// Add to either current panel or direct to the Metawidget

		if ( mPanelCurrent == null )
			getMetawidget().add( component, componentConstraints );
		else
			mPanelCurrent.add( component, componentConstraints );

		mCurrentColumn++;

		if ( mCurrentColumn >= mNumberOfColumns )
		{
			mCurrentColumn = 0;
			mCurrentRow++;
		}
	}

	@Override
	public void layoutEnd()
	{
		sectionEnd();

		// Buttons

		Facet buttonsFacet = getMetawidget().getFacet( "buttons" );

		if ( buttonsFacet != null )
		{
			if ( mCurrentColumn > 0 )
			{
				mCurrentColumn = 0;
				mCurrentRow++;
			}

			getMetawidget().add( buttonsFacet, new CC().cell( 0, mCurrentRow ).spanX().growX() );
		}
	}

	//
	// Protected methods
	//

	protected String layoutBeforeChild( Component component, String labelText, Map<String, String> attributes )
	{
		if ( attributes != null )
		{
			// Section headings

			String section = attributes.get( SECTION );

			if ( section != null && !section.equals( mCurrentSection ) )
			{
				mCurrentSection = section;
				layoutSection( section );
			}
		}

		// Add label

		if ( labelText != null && !"".equals( labelText ) && !( component instanceof JButton ) )
		{
			JLabel label = new JLabel();

			// Required

			if ( attributes != null && TRUE.equals( attributes.get( REQUIRED ) ) && !TRUE.equals( attributes.get( READ_ONLY ) ) && !getMetawidget().isReadOnly() )
				label.setText( labelText + "*:" );
			else
				label.setText( labelText + ":" );

			CC labelConstraints = new CC();
			labelConstraints.cell( mCurrentColumn * 2, mCurrentRow );
			labelConstraints.growX();

			// Top align all labels, not just those belonging to 'tall' components,
			// so that tall components, regular components and nested Metawidget
			// components all line up

			labelConstraints.alignY( "top" );

			// Apply some vertical padding so the label lines up with the component nicely

			labelConstraints.pad( mDefaultLabelVerticalPadding, 0, mDefaultLabelVerticalPadding, 0 );

			// Add to either current panel or direct to the Metawidget

			if ( mPanelCurrent == null )
				getMetawidget().add( label, labelConstraints );
			else
				mPanelCurrent.add( label, labelConstraints );
		}

		return labelText;
	}

	protected void layoutSection( String section )
	{
		if ( "".equals( section ) )
		{
			sectionEnd();
			return;
		}

		// Section name (possibly localized)

		String localizedSection = getMetawidget().getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null )
			localizedSection = section;

		// Start a new row

		if ( mCurrentColumn > 0 )
		{
			mCurrentColumn = 0;
			mCurrentRow++;
		}

		// Insert the section

		switch ( mSectionStyle )
		{
			case SECTION_AS_TAB:

				JTabbedPane tabbedPane;

				if ( mPanelCurrent == null )
				{
					tabbedPane = new JTabbedPane();
					getMetawidget().add( tabbedPane, new CC().cell( mCurrentColumn, mCurrentRow ).spanX().grow() );
					( (LC) ( (net.miginfocom.swing.MigLayout) getMetawidget().getLayout() ).getLayoutConstraints() ).fill();
				}
				else
				{
					tabbedPane = (JTabbedPane) mPanelCurrent.getParent();
					sectionEnd();
				}

				mPanelCurrent = new JPanel();
				mPanelCurrent.setName( localizedSection );
				mPanelCurrent.setLayout( new net.miginfocom.swing.MigLayout( new LC() ) );

				tabbedPane.add( mPanelCurrent );
				break;

			default:

				// Compared to GridBagLayout, we can achieve the heading without a nested JPanel

				JLabel label = new JLabel( localizedSection );
				getMetawidget().add( label, new CC().cell( mCurrentColumn, mCurrentRow ).spanX() );
				JSeparator separator = new JSeparator( SwingConstants.HORIZONTAL );
				getMetawidget().add( separator, new CC().cell( mCurrentColumn, mCurrentRow ).growX() );

				mCurrentRow++;
				break;
		}
	}

	protected void sectionEnd()
	{
		if ( mPanelCurrent == null )
			return;

		mCurrentColumn = 0;
		mPanelCurrent = null;
	}
}
