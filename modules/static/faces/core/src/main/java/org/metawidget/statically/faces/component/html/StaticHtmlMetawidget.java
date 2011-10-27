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

package org.metawidget.statically.faces.component.html;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.StaticMetawidget;
import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.faces.StaticFacesUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * @author Richard Kennard
 */

public class StaticHtmlMetawidget
	extends StaticXmlMetawidget {

	//
	// Private members
	//

	private Map<String, String>	mValueExpressions;

	//
	// Public methods
	//

	/**
	 * Gets the value expression with the given name.
	 * <p>
	 * API design mimics <code>UIComponent</code>.
	 */

	public String getValueExpression( String name ) {

		if ( mValueExpressions == null ) {
			return null;
		}

		return mValueExpressions.get( name );
	}

	/**
	 * Sets the value expression with the given name.
	 * <p>
	 * API design mimics <code>UIComponent</code>.
	 */

	public void setValueExpression( String name, String expression ) {

		if ( mValueExpressions == null ) {
			mValueExpressions = CollectionUtils.newHashMap();
		}

		mValueExpressions.put( name, expression );
	}

	//
	// Protected methods
	//

	@Override
	protected String getDefaultConfiguration() {

		return ClassUtils.getPackagesAsFolderNames( getClass() ) + "/metawidget-static-html-default.xml";
	}

	@Override
	protected void initNestedMetawidget( StaticMetawidget nestedMetawidget, Map<String, String> attributes ) {

		super.initNestedMetawidget( nestedMetawidget, attributes );

		String valueExpression = getValueExpression( "value" );
		valueExpression = StaticFacesUtils.unwrapExpression( valueExpression );
		valueExpression += StringUtils.SEPARATOR_DOT_CHAR + attributes.get( NAME );
		valueExpression = StaticFacesUtils.wrapExpression( valueExpression );

		( (StaticHtmlMetawidget) nestedMetawidget ).setValueExpression( "value", valueExpression );
	}
}
