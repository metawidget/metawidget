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

import org.metawidget.layout.delegate.DelegateLayout;
import org.metawidget.layout.delegate.DelegateLayoutConfig;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * @author Richard Kennard
 */

public class SeparatorSectionLayout
	extends DelegateLayout<JComponent, SwingMetawidget>
{
	//
	// Private statics
	//

	/**
	 * The border around the entire separator.
	 */

	private final static Border	BORDER_SECTION			= BorderFactory.createEmptyBorder( 5, 0, 5, 0 );

	/**
	 * The insets around the separator label.
	 */

	private final static Insets	INSETS_SECTION_LABEL	= new Insets( 0, 0, 0, 5 );

	//
	// Constructor
	//

	public SeparatorSectionLayout( DelegateLayoutConfig<JComponent, SwingMetawidget> config )
	{
		super( config );
	}

	//
	// Public methods
	//

	@Override
	public void startLayout( JComponent container, SwingMetawidget metawidget )
	{
		super.startLayout( container, metawidget );
		metawidget.putClientProperty( SeparatorSectionLayout.class, null );
	}

	@Override
	public void layoutWidget( JComponent component, String elementName, Map<String, String> attributes, JComponent container, SwingMetawidget metawidget )
	{
		String section = attributes.get( SECTION );
		String currentSection = (String) container.getClientProperty( SeparatorSectionLayout.class );

		// Stay where we are?

		if ( section == null || section.equals( currentSection ) )
		{
			super.layoutWidget( component, elementName, attributes, container, metawidget );
			return;
		}

		container.putClientProperty( SeparatorSectionLayout.class, section );

		// New section

		if ( !"".equals( section ) )
		{
			JPanel separatorPanel = new JPanel();
			separatorPanel.setBorder( BORDER_SECTION );
			separatorPanel.setLayout( new java.awt.GridBagLayout() );
			separatorPanel.setOpaque( false );

			// Section name (possibly localized)

			String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

			if ( localizedSection == null )
				localizedSection = section;

			GridBagConstraints labelConstraints = new GridBagConstraints();
			labelConstraints.insets = INSETS_SECTION_LABEL;
			JLabel labelSection = new JLabel( localizedSection );
			separatorPanel.add( labelSection, labelConstraints );

			GridBagConstraints separatorConstraints = new GridBagConstraints();
			separatorConstraints.fill = GridBagConstraints.HORIZONTAL;
			separatorConstraints.weightx = 1.0;
			separatorConstraints.anchor = GridBagConstraints.EAST;
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
