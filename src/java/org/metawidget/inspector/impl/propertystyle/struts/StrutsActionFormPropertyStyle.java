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

import org.apache.struts.action.ActionForm;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;

/**
 * PropertyStyle for Struts ActionForm-style properties.
 * <p>
 * ActionForm properties essentially follow JavaBean-convention, but should not include the extra
 * methods defined in the <code>ActionForm</code> class itself, such as <code>getServlet</code>,
 * <code>getServletWrapper</code> and <code>getMultipartRequestHandler</code>.
 *
 * @author Richard Kennard
 */

public class StrutsActionFormPropertyStyle
	extends JavaBeanPropertyStyle
{
	//
	// Protected methods
	//

	@Override
	protected boolean isExcludedBaseType( Class<?> clazz )
	{
		if ( ActionForm.class.equals( clazz ))
			return true;

		return super.isExcludedBaseType( clazz );
	}
}
