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
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.metawidget.layout.decorator.LayoutDecorator;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to separate widgets in different sections using a JSeparator.
 *
 * @author Richard Kennard
 */

public class SeparatorSectionLayout
	extends LayoutDecorator<JComponent, SwingMetawidget>
{
	//
	// Private statics
	//

	/**
	 * The border around the entire separator.
	 */

	private final static Border	BORDER_SECTION				= BorderFactory.createEmptyBorder( 5, 0, 5, 0 );

	/**
	 * The insets around the separator label.
	 */

	private final static Insets	INSETS_SECTION_LABEL_LEFT	= new Insets( 0, 0, 0, 5 );

	private final static Insets	INSETS_SECTION_LABEL_RIGHT	= new Insets( 0, 5, 0, 0 );

	//
	// Private members
	//

	private int					mAlignment;

	//
	// Constructor
	//

	public SeparatorSectionLayout( SeparatorSectionLayoutConfig config )
	{
		super( config );

		mAlignment = config.getAlignment();
	}

	//
	// Public methods
	//

	@Override
	public void startLayout( JComponent container, SwingMetawidget metawidget )
	{
		super.startLayout( container, metawidget );
		container.putClientProperty( SeparatorSectionLayout.class, null );
	}

	@Override
	public void layoutWidget( JComponent component, String elementName, Map<String, String> attributes, JComponent container, SwingMetawidget metawidget )
	{
		String[] sections = ArrayUtils.fromString( attributes.get( SECTION ) );
		String[] currentSections = (String[]) container.getClientProperty( SeparatorSectionLayout.class );

		// Stay where we are?

		if ( sections.length == 0 || sections.equals( currentSections ) )
		{
			super.layoutWidget( component, elementName, attributes, container, metawidget );
			return;
		}

		container.putClientProperty( SeparatorSectionLayout.class, sections );

		// For each of the new sections...

		for ( int loop = 0; loop < sections.length; loop++ )
		{
			String section = sections[loop];

			// ...that are different from our current...

			if ( "".equals( section ))
				continue;

			if ( currentSections != null && loop < currentSections.length && section.equals( currentSections[loop] ) )
				continue;

			// ...display a divider

			JPanel separatorPanel = new JPanel();
			separatorPanel.setBorder( BORDER_SECTION );
			separatorPanel.setLayout( new java.awt.GridBagLayout() );
			separatorPanel.setOpaque( false );

			// Section name (possibly localized)

			String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

			if ( localizedSection == null )
				localizedSection = section;

			GridBagConstraints labelConstraints = new GridBagConstraints();

			GridBagConstraints separatorConstraints = new GridBagConstraints();
			separatorConstraints.fill = GridBagConstraints.HORIZONTAL;
			separatorConstraints.weightx = 1.0;

			if ( mAlignment == SwingConstants.RIGHT )
			{
				separatorConstraints.gridx = 0;
				labelConstraints.gridx = 1;
				labelConstraints.insets = INSETS_SECTION_LABEL_RIGHT;
			}
			else
			{
				labelConstraints.insets = INSETS_SECTION_LABEL_LEFT;
			}

			separatorPanel.add( new JLabel( localizedSection ), labelConstraints );
			separatorPanel.add( new JSeparator( SwingConstants.HORIZONTAL ), separatorConstraints );

			// Add to parent container

			Map<String, String> separatorPanelAttributes = CollectionUtils.newHashMap();
			separatorPanelAttributes.put( LABEL, "" );
			separatorPanelAttributes.put( WIDE, TRUE );
			super.layoutWidget( separatorPanel, PROPERTY, separatorPanelAttributes, container, metawidget );
		}

		// Add component as normal

		super.layoutWidget( component, elementName, attributes, container, metawidget );
	}
}
