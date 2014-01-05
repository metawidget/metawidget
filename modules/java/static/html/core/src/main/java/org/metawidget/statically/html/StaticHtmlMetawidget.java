// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
