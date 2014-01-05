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

import java.io.Writer;
import java.util.Map;

import org.metawidget.iface.MetawidgetException;
import org.metawidget.statically.StaticUtils.IndentedWriter;
import org.metawidget.util.CollectionUtils;

/**
 * Base Metawidget for static code generation in XML environments.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class StaticXmlMetawidget
	extends StaticMetawidget
	implements StaticXmlWidget {

	//
	// Private members
	//

	private Map<String, String>	mAttributes;

	private String				mTextContent;

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

		return mTextContent;
	}

	public void setTextContent( String textContent ) {

		mTextContent = textContent;
	}

	@Override
	public void write( Writer writer, int initialIndent ) {

		super.write( writer, initialIndent );

		// FreemarkerLayout will setTextContent directly

		if ( mTextContent != null ) {

			Writer writerToUse = writer;

			if ( initialIndent >= 0 ) {
				writerToUse = new IndentedWriter( writer, initialIndent );
			}

			try {
				writerToUse.write( mTextContent );
			} catch ( Exception e ) {
				throw MetawidgetException.newException( e );
			}
		}
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
