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

package org.metawidget.statically.jsp;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.StaticMetawidget;
import org.metawidget.statically.html.StaticHtmlMetawidget;
import org.metawidget.statically.html.widgetbuilder.ValueHolder;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * @author Richard Kennard
 */

public class StaticJspMetawidget
	extends StaticHtmlMetawidget
	implements ValueHolder {

	//
	// Public methods
	//

	/**
	 * The value argument used as the starting point for this Metawidget.
	 * <p>
	 * Note: because we are working statically, <tt>setValue</tt> and <tt>setPath</tt> must both be
	 * called. The former is the binding value that will be written into the generated output (see
	 * <tt>NameProcessor</tt>, <tt>PathProcessor</tt> etc). The latter is the actual path that
	 * should be inspected. Because we are working statically we cannot determine these
	 * automatically.
	 */

	public String getValue() {

		return getAttribute( "value" );
	}

	public void setValue( String value ) {

		putAttribute( "value", value );
	}

	@Override
	public void initNestedMetawidget( StaticMetawidget nestedMetawidget, Map<String, String> attributes ) {

		if ( ( (StaticJspMetawidget) nestedMetawidget ).getValue() == null ) {

			String valueExpression = StaticJspUtils.unwrapExpression( getValue() );
			valueExpression += StringUtils.SEPARATOR_DOT_CHAR + attributes.get( NAME );
			valueExpression = StaticJspUtils.wrapExpression( valueExpression );
			( (StaticJspMetawidget) nestedMetawidget ).setValue( valueExpression );
		}

		super.initNestedMetawidget( nestedMetawidget, attributes );
	}

	//
	// Protected methods
	//

	@Override
	protected String getDefaultConfiguration() {

		return ClassUtils.getPackagesAsFolderNames( StaticJspMetawidget.class ) + "/metawidget-static-jsp-default.xml";
	}
}
