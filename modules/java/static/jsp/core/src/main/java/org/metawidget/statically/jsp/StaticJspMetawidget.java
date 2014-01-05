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

package org.metawidget.statically.jsp;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.StaticMetawidget;
import org.metawidget.statically.html.StaticHtmlMetawidget;
import org.metawidget.statically.html.widgetbuilder.ValueHolder;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
