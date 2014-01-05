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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.inspector.InspectionResultConstants;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseXmlInspector;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Element;

/**
 * Inspects <code>inspection-result-1.0.xsd</code>-compliant files (such as
 * <code>metawidget-metadata.xml</code>).
 * <p>
 * XmlInspector is a very simple Inspector: it takes as its input XML in the same format that
 * Inspectors usually output. It can be useful for declaring 'ad hoc' UI entities that do not map to
 * any Java class, as well as for declaring UI-specific attributes for existing Java classes (ie. if
 * you prefer not to use annotations, or if you want to introduce additional 'virtual' properties).
 * Some attributes accept multiple values, such as <code>lookup</code>. These can be
 * supplied as a comma-separated string. The values will be trimmed for whitespace. If the values
 * themselves contain commas, they can be escaped with the <code>\</code> character.
 * <p>
 * Note when using <code>XmlInspector</code> you should still try to avoid duplicating UI metadata
 * that already exists in other parts of your application. For example, if you are also using
 * <code>PropertyTypeInspector</code> in your <code>CompositeInspector</code> there is no need to
 * duplicate the names and types of properties. Also, if you are using
 * <code>PropertyTypeInspector</code> and <code>XmlInspector</code> together, please read the
 * JavaDoc for <code>restrictAgainstObject</code>.
 * <p>
 * <code>XmlInspector</code> does add some niceties beyond <code>inspection-result-1.0.xsd</code>.
 * It supports an <code>extends</code> attribute to allow one <code>entity</code> to inherit from
 * another. It also supports nested entities, for example:
 * <p>
 * <code>&lt;entity type="Person"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;property name="surname"/&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;property name="address"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="street"/&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="postcode"/&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;/property&gt;<br/>
 * &lt;/entity&gt;</code>
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class XmlInspector
	extends BaseXmlInspector {

	//
	// Constructors
	//

	/**
	 * Constructs an XmlInspector.
	 * <p>
	 * Note XmlInspector requires a config. It does not have a default constructor, because the
	 * XmlInspectorConfig must be externally configured using <code>setResourceResolver</code> to
	 * support resolving resources from non-standard locations (such as <code>WEB-INF</code).
	 */

	public XmlInspector( XmlInspectorConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected String getExtendsAttribute() {

		return "extends";
	}

	@Override
	protected Map<String, String> inspectProperty( Element toInspect ) {

		if ( PROPERTY.equals( toInspect.getNodeName() ) ) {
			Map<String, String> attributes = XmlUtils.getAttributesAsMap( toInspect );

			// Warn about some common typos

			if ( attributes.containsKey( "readonly" ) ) {
				throw InspectorException.newException( "Attribute named 'readonly' should be '" + InspectionResultConstants.READ_ONLY + "'" );
			}

			if ( attributes.containsKey( "dontexpand" ) ) {
				throw InspectorException.newException( "Attribute named 'dontexpand' should be '" + InspectionResultConstants.DONT_EXPAND + "'" );
			}

			// All good

			return attributes;
		}

		return null;
	}

	@Override
	protected Map<String, String> inspectAction( Element toInspect ) {

		if ( ACTION.equals( toInspect.getNodeName() ) ) {
			return XmlUtils.getAttributesAsMap( toInspect );
		}

		return null;
	}
}
