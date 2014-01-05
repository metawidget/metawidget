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

package org.metawidget.gwt.client.ui.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.HashMap;
import java.util.Map;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.util.simple.StringUtils;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

/**
 * Layout to decorate widgets from different sections using a Label.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class LabelLayoutDecorator
	extends GwtFlatSectionLayoutDecorator {

	//
	// Private members
	//

	private String	mStyleName;

	//
	// Constructor
	//

	public LabelLayoutDecorator( LabelLayoutDecoratorConfig config ) {

		super( config );

		mStyleName = config.getStyleName();
	}

	//
	// Protected methods
	//

	@Override
	protected void addSectionWidget( String section, int level, Panel container, GwtMetawidget metawidget ) {

		Label label = new Label();
		label.setStyleName( mStyleName );

		// Section name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null ) {
			localizedSection = section;
		}

		label.setText( localizedSection );

		// Add to parent container

		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put( LABEL, "" );
		attributes.put( WIDE, TRUE );

		getDelegate().layoutWidget( label, PROPERTY, attributes, container, metawidget );
	}
}
