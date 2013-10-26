// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.config.iface;


/**
 * Interface to indicate a <code>xxxConfig</code> class (ie. an <code>InspectorConfig</code>, a
 * <code>LayoutConfig</code> etc) needs a ResourceResolver. <code>ConfigReader</code> automatically
 * recognises classes implementing <code>NeedsResourceResolver</code> and passes itself to them.
 * <p>
 * Note: this class is not located under <code>org.metawidget.iface</code>, because GWT does not
 * like <code>ResourceResolver</code>'s <code>java.io.InputStream</code>.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public interface NeedsResourceResolver {

	//
	// Methods
	//

	/**
	 * Set the <code>ResourceResolver</code> for this class.
	 * <p>
	 * Note: we tried removing this interface, and having <code>ConfigReader</code> look for
	 * <code>xxxConfig</code> classes that took a <code>ResourceResolver</code> parameter in their
	 * constructor instead. This had disadvantages:
	 * <p>
	 * <ul>
	 * <li>you start dictating how a class' constructor must look, which degrades the POJO approach</li>
	 * <li>you end up using reflection to determine what constructor parameters there are. In
	 * general, an API should favour interfaces over reflection</li>
	 * <li>you require clients to think about <code>ResourceResolver</code> when constructing the class
	 * programmatically. They can pass null, but this really calls for a default constructor. But if
	 * you have a default constructor, all subclasses must implement two constructors, and many must
	 * therefore make two calls to <code>setDefaultFile</code></li>
	 * </ul>
	 */

	void setResourceResolver( ResourceResolver resourceResolver );
}
