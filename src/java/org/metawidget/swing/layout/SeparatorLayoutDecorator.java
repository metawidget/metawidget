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

import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to decorate widgets from different sections using a JSeparator.
 *
 * @author Richard Kennard
 */

public class SeparatorLayoutDecorator
	extends SwingFlatSectionLayoutDecorator
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

	final private int			mAlignment;

	//
	// Constructor
	//

	public SeparatorLayoutDecorator( SeparatorLayoutDecoratorConfig config )
	{
		super( config );

		mAlignment = config.getAlignment();
	}

	//
	// Protected methods
	//

	@Override
	protected void addSectionWidget( String section, int level, JComponent container, SwingMetawidget metawidget )
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
		getDelegate().layoutWidget( separatorPanel, PROPERTY, separatorPanelAttributes, container, metawidget );
	}
}
