// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SeparatorLayoutDecorator
	extends SwingFlatSectionLayoutDecorator {

	//
	// Private statics
	//

	/**
	 * The border around the entire separator.
	 */

	private static final Border	BORDER_SECTION				= BorderFactory.createEmptyBorder( 5, 0, 5, 0 );

	/**
	 * The insets around the separator label.
	 */

	private static final Insets	INSETS_SECTION_LABEL_LEFT	= new Insets( 0, 0, 0, 5 );

	private static final Insets	INSETS_SECTION_LABEL_RIGHT	= new Insets( 0, 5, 0, 0 );

	//
	// Private members
	//

	private final int			mAlignment;

	//
	// Constructor
	//

	public SeparatorLayoutDecorator( SeparatorLayoutDecoratorConfig config ) {

		super( config );

		mAlignment = config.getAlignment();
	}

	//
	// Protected methods
	//

	@Override
	protected void addSectionWidget( String section, int level, JComponent container, SwingMetawidget metawidget ) {

		JPanel separatorPanel = new JPanel();
		separatorPanel.setBorder( BORDER_SECTION );
		separatorPanel.setLayout( new java.awt.GridBagLayout() );
		separatorPanel.setOpaque( false );

		// Section name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null ) {
			localizedSection = section;
		}

		GridBagConstraints labelConstraints = new GridBagConstraints();

		GridBagConstraints separatorConstraints = new GridBagConstraints();
		separatorConstraints.fill = GridBagConstraints.HORIZONTAL;
		separatorConstraints.weightx = 1.0;

		if ( mAlignment == SwingConstants.RIGHT ) {
			separatorConstraints.gridx = 0;
			labelConstraints.gridx = 1;
			labelConstraints.insets = INSETS_SECTION_LABEL_RIGHT;
		} else {
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
