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

package org.metawidget.statically.spring;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.StaticMetawidget;
import org.metawidget.statically.jsp.StaticJspMetawidget;
import org.metawidget.statically.jsp.StaticJspUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StaticSpringMetawidget
	extends StaticJspMetawidget {

	//
	// Public methods
	//

	@Override
	public void initNestedMetawidget( StaticMetawidget nestedMetawidget, Map<String, String> attributes ) {

		if ( ( (StaticJspMetawidget) nestedMetawidget ).getValue() != null ) {

			// Overridden to setValue without calling .wrapExpression

			String valueExpression = StaticJspUtils.unwrapExpression( getValue() );
			valueExpression += StringUtils.SEPARATOR_DOT_CHAR + attributes.get( NAME );
			( (StaticJspMetawidget) nestedMetawidget ).setValue( valueExpression );
		}

		super.initNestedMetawidget( nestedMetawidget, attributes );
	}

	//
	// Protected methods
	//

	@Override
	protected String getDefaultConfiguration() {

		return ClassUtils.getPackagesAsFolderNames( StaticSpringMetawidget.class ) + "/metawidget-static-spring-default.xml";
	}
}
