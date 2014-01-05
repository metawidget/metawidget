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

import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to decorate widgets from different sections using a JPanel with a TitledBorder.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class TitledPanelLayoutDecorator
	extends SwingNestedSectionLayoutDecorator {

	//
	// Private statics
	//

	/**
	 * The border around the entire titled panel.
	 */

	private static final Border	OUTER_BORDER	= BorderFactory.createEmptyBorder( 5, 0, 5, 0 );

	/**
	 * The insets inside the titled panel.
	 */

	private static final Border	INNER_BORDER	= BorderFactory.createEmptyBorder( 3, 3, 3, 3 );

	//
	// Constructor
	//

	public TitledPanelLayoutDecorator( LayoutDecoratorConfig<JComponent, JComponent, SwingMetawidget> config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected JComponent createSectionWidget( JComponent previousSectionWidget, String section, Map<String, String> attributes, JComponent container, SwingMetawidget metawidget ) {

		JPanel titledPanel = new JPanel();
		titledPanel.setOpaque( false );

		// Section name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null ) {
			localizedSection = section;
		}

		titledPanel.setBorder( BorderFactory.createCompoundBorder( OUTER_BORDER, BorderFactory.createCompoundBorder( BorderFactory.createTitledBorder( localizedSection ), INNER_BORDER ) ) );

		// Add to parent container

		Map<String, String> panelAttributes = CollectionUtils.newHashMap();
		panelAttributes.put( LABEL, "" );
		panelAttributes.put( LARGE, TRUE );
		getDelegate().layoutWidget( titledPanel, PROPERTY, panelAttributes, container, metawidget );

		return titledPanel;
	}
}
