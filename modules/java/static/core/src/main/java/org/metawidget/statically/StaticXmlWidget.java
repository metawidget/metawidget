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

package org.metawidget.statically;

import java.util.Map;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public interface StaticXmlWidget
	extends StaticWidget {

	//
	// Methods
	//

	String getAttribute( String name );

	void putAttribute( String name, String value );

	String getPrefix();

	String getNamespaceURI();

	String getTextContent();

	void setTextContent( String textContent );

	/**
	 * Some XML tags may use namespaces in addition to the one declared for their tag. This is
	 * particulary relevant when using functions within attributes. For example:
	 * <p>
	 * <tt><ns1:foo bar="#{ns2:baz()}"/></tt>
	 */

	Map<String, String> getAdditionalNamespaceURIs();
}
