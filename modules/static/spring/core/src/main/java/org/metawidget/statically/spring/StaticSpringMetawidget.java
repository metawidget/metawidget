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

package org.metawidget.statically.spring;

import java.util.Map;

import org.metawidget.statically.StaticMetawidget;
import org.metawidget.statically.jsp.StaticJspMetawidget;
import org.metawidget.statically.jsp.StaticJspUtils;
import org.metawidget.util.ClassUtils;

/**
 * @author Richard Kennard
 */

public class StaticSpringMetawidget
	extends StaticJspMetawidget {

    //
    // Public methods
    //

    @Override
    public void initNestedMetawidget( StaticMetawidget nestedMetawidget, Map<String, String> attributes ) {

        if ( ((StaticJspMetawidget) nestedMetawidget).getValue() != null ) {
            String valueExpression = ((StaticJspMetawidget) nestedMetawidget).getValue();
            valueExpression = StaticJspUtils.unwrapExpression(valueExpression);
            ((StaticJspMetawidget) nestedMetawidget).setValue(valueExpression);
        }

        super.initNestedMetawidget(nestedMetawidget, attributes);
    }

    //
	// Protected methods
	//

	@Override
	protected String getDefaultConfiguration() {

		return ClassUtils.getPackagesAsFolderNames( StaticSpringMetawidget.class ) + "/metawidget-static-spring-default.xml";
	}
}
