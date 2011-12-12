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

package org.metawidget.statically.faces.component;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.StaticMetawidget;
import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.faces.StaticFacesUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * @author Richard Kennard
 */

public abstract class StaticUIMetawidget
	extends StaticXmlMetawidget
	implements ValueHolder {

	//
	// Public methods
	//

	public String getValue() {

		return getAttribute( "value" );
	}

	public void setValue( String value ) {

		putAttribute( "value", value );
	}

	public void setConverter( String converter ) {

		// Do nothing
	}

	//
	// Protected methods
	//

	@Override
	protected void initNestedMetawidget( StaticMetawidget nestedMetawidget, Map<String, String> attributes ) {

		super.initNestedMetawidget( nestedMetawidget, attributes );

		String valueExpression = getValue();
		valueExpression = StaticFacesUtils.unwrapExpression( valueExpression );
		valueExpression += StringUtils.SEPARATOR_DOT_CHAR + attributes.get( NAME );
		valueExpression = StaticFacesUtils.wrapExpression( valueExpression );

		( (StaticUIMetawidget) nestedMetawidget ).setValue( valueExpression );
	}
}
