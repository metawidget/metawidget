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

package org.metawidget.inspector.xml;

import java.io.InputStream;

import org.metawidget.inspector.impl.BaseXmlInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.w3c.dom.Document;

/**
 * Configures an XmlInspector prior to use. Once instantiated, Inspectors are immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class XmlInspectorConfig
	extends BaseXmlInspectorConfig {

	//
	// Constructor
	//

	public XmlInspectorConfig() {

		setDefaultFile( "metawidget-metadata.xml" );
	}

	//
	// Public methods
	//

	/**
	 * Overridden to provide a covariant return type for our fluent interface.
	 */

	@Override
	public XmlInspectorConfig setInputStream( InputStream stream ) {

		return (XmlInspectorConfig) super.setInputStream( stream );
	}

	/**
	 * Overridden to provide a covariant return type for our fluent interface.
	 */

	@Override
	public XmlInspectorConfig setDocuments( Document... documents ) {

		return (XmlInspectorConfig) super.setDocuments( documents );
	}

	/**
	 * Overridden to provide a covariant return type for our fluent interface.
	 */

	@Override
	public XmlInspectorConfig setRestrictAgainstObject( PropertyStyle restrictAgainstObject ) {

		return (XmlInspectorConfig) super.setRestrictAgainstObject( restrictAgainstObject );
	}

	/**
	 * Overridden to provide a covariant return type for our fluent interface.
	 */

	@Override
	public XmlInspectorConfig setInferInheritanceHierarchy( boolean inferInheritanceHierarchy ) {

		return (XmlInspectorConfig) super.setInferInheritanceHierarchy( inferInheritanceHierarchy );
	}

	/**
	 * Overridden to provide a covariant return type for our fluent interface.
	 */

	@Override
	public XmlInspectorConfig setValidateAgainstClasses( PropertyStyle validateAgainstClasses ) {

		return (XmlInspectorConfig) super.setValidateAgainstClasses( validateAgainstClasses );
	}
}
