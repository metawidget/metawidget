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

package org.metawidget.statically.html;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.StaticMetawidget;
import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.html.widgetbuilder.IdHolder;
import org.metawidget.util.ClassUtils;

/**
 * Metawidget for statically generating Plain Old HTML.
 *
 * @author Richard Kennard
 * @author Ryan Bradley
 */

public class StaticHtmlMetawidget
	extends StaticXmlMetawidget
	implements IdHolder {

	//
	// Public methods
	//

	/**
	 * Optional 'id' to prepend to child identifiers.
	 */

	public void setId( String id ) {

		putAttribute( "id", id );
	}

	public String getId() {

		return getAttribute( "id" );
	}

	@Override
	public void initNestedMetawidget( StaticMetawidget nestedMetawidget, Map<String, String> attributes ) {

		super.initNestedMetawidget( nestedMetawidget, attributes );

		if ( ( (StaticHtmlMetawidget) nestedMetawidget ).getId() == null ) {
			String nestedId = attributes.get( NAME );

			if ( getId() != null ) {
				nestedId = getId() + '-' + nestedId;
			}

			( (StaticHtmlMetawidget) nestedMetawidget ).setId( nestedId );
		}
	}

	//
	// Protected methods
	//

	@Override
	protected String getDefaultConfiguration() {

		return ClassUtils.getPackagesAsFolderNames( StaticHtmlMetawidget.class ) + "/metawidget-static-html-default.xml";
	}
}
