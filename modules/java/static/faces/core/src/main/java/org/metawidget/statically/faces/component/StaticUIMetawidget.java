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

package org.metawidget.statically.faces.component;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.StaticMetawidget;
import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.faces.StaticFacesUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class StaticUIMetawidget
	extends StaticXmlMetawidget
	implements ValueHolder {

	//
	// Public methods
	//

	/**
	 * The value binding expression used as the starting point for this Metawidget.
	 * <p>
	 * Note: because we are working statically, <tt>setValue</tt> and <tt>setPath</tt> must both be
	 * called. The former is the binding value that will be written into the generated output (see
	 * <tt>StandardBindingProcessor</tt>). The latter is the actual path that should be inspected.
	 * Because we are working statically we cannot determine these automatically.
	 */

	public String getValue() {

		return getAttribute( "value" );
	}

	public void setValue( String value ) {

		putAttribute( "value", value );
	}

	public void setConverter( String converter ) {

		// Do nothing
	}

	@Override
	public void initNestedMetawidget( StaticMetawidget nestedMetawidget, Map<String, String> attributes ) {

		super.initNestedMetawidget( nestedMetawidget, attributes );

		if ( ( (StaticUIMetawidget) nestedMetawidget ).getValue() == null ) {

			String valueExpression = getValue();
			valueExpression = StaticFacesUtils.unwrapExpression( valueExpression );
			valueExpression += StringUtils.SEPARATOR_DOT_CHAR + attributes.get( NAME );
			valueExpression = StaticFacesUtils.wrapExpression( valueExpression );

			( (StaticUIMetawidget) nestedMetawidget ).setValue( valueExpression );
		}
	}
}
