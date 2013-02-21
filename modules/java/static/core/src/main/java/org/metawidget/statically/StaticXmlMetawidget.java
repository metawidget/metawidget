// Metawidget (licensed under LGPL)
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

package org.metawidget.statically;

import java.util.Map;

import org.metawidget.util.CollectionUtils;

/**
 * Base Metawidget for static code generation in XML environments.
 *
 * @author Richard Kennard
 */

public abstract class StaticXmlMetawidget
	extends StaticMetawidget
	implements StaticXmlWidget {

	//
	// Private members
	//

	private Map<String, String>	mAttributes;

	//
	// Public methods
	//

	public void putAttribute( String name, String value ) {

		if ( mAttributes == null ) {

			// (use TreeMap for consistent unit tests)

			mAttributes = CollectionUtils.newTreeMap();
		}

		mAttributes.put( name, value );
	}

	public String getAttribute( String name ) {

		if ( mAttributes == null ) {
			return null;
		}

		return mAttributes.get( name );
	}

	public String getPrefix() {

		return "m";
	}

	public Map<String, String> getAdditionalNamespaceURIs() {

		// Metawidgets should never have additional namespaces

		return null;
	}

	public String getTextContent() {

		// Metawidgets should never have text content

		throw new UnsupportedOperationException();
	}

	public void setTextContent( String textContent ) {

		// Metawidgets should never have text content

		throw new UnsupportedOperationException();
	}

	public String getNamespaceURI() {

		// Metawidgets should never be output (kind of the point of being static)

		return null;
	}

	/**
	 * Recurse over all children and retrieve the namespaces they use.
	 *
	 * @return map of prefix and namespace
	 */

	public Map<String, String> getNamespaces() {

		Map<String, String> namespaces = CollectionUtils.newHashMap();
		populateNamespaces( this, namespaces );
		return namespaces;
	}

	//
	// Private methods
	//

	private void populateNamespaces( StaticXmlWidget xmlWidget, Map<String, String> namespaces ) {

		for ( StaticWidget child : xmlWidget.getChildren() ) {

			StaticXmlWidget xmlChild = (StaticXmlWidget) child;
			String prefix = xmlChild.getPrefix();
			String namespaceURI = xmlChild.getNamespaceURI();

			if ( prefix != null && namespaceURI != null ) {
				namespaces.put( prefix, namespaceURI );
			}

			if ( xmlChild.getAdditionalNamespaceURIs() != null ) {
				namespaces.putAll( xmlChild.getAdditionalNamespaceURIs() );
			}

			populateNamespaces( xmlChild, namespaces );
		}
	}
}
