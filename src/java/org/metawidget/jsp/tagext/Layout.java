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

package org.metawidget.jsp.tagext;

import java.util.Map;

/**
 * Interface for all JSP-based layouts.
 * <p>
 * JSP Layouts are shared across all JSP-based Metawidgets, including <code>HtmlMetawidgetTag</code>
 * and <code>StrutsMetawidgetTag</code>.
 * <p>
 * Whilst the Metawidgets themselves use JSP taglibs, by the time the result is passed to the Layout
 * we deal with simple Strings of HTML. This is because JSP does not define a robust widget model
 * (specifically, tags cannot iterate over their children). Interesting comparisons can be made
 * between this class and <code>javax.faces.render.Renderer</code>.
 * <p>
 * Implementations need not be Thread-safe.
 *
 * @author Richard Kennard
 */

public interface Layout
{
	//
	// Methods
	//

	String layoutBegin( String value );

	String layoutChild( String child, Map<String, String> attributes );

	String layoutEnd();
}
