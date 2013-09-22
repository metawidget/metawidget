// Metawidget (licensed under LGPL)
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
