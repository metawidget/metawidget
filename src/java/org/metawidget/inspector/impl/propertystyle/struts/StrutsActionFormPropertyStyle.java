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

package org.metawidget.inspector.impl.propertystyle.struts;

import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;

/**
 * PropertyStyle for Struts ActionForm-style properties.
 * <p>
 * ActionForm properties essentially follow JavaBean-convention, but should not include the extra
 * methods defined in the <code>ActionForm</code> classes themselves, such as
 * <code>getServlet</code>, <code>getServletWrapper</code> and
 * <code>getMultipartRequestHandler</code> in <code>ActionForm</code> and
 * <code>getDynaClass()</code> in <code>DynaActionForm</code>.
 *
 * @author Richard Kennard
 */

public class StrutsActionFormPropertyStyle
	extends JavaBeanPropertyStyle
{
	//
	// Protected methods
	//

	/**
	 * Whether to exclude the given base type when searching up the model inheritance chain.
	 * <p>
	 * This can be useful when the convention or base class define properties that are
	 * framework-specific, and should be filtered out from 'real' business model properties.
	 * <p>
	 * By default, excludes any base types from the <code>org.apache.struts.*</code> packages, as
	 * well as those excluded by <code>BasePropertyStyle</code>.
	 *
	 * @return true if the property should be excluded, false otherwise
	 */

	@Override
	protected boolean isExcludedBaseType( Class<?> classToExclude )
	{
		String className = classToExclude.getName();

		// Exclude org.apache.struts.* (includes org.apache.struts.action.* and
		// org.apache.struts.validator.*)

		if ( className.startsWith( "org.apache.struts." ) )
			return true;

		return super.isExcludedBaseType( classToExclude );
	}
}
