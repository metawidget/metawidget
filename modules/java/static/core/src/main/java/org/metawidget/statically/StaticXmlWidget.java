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
