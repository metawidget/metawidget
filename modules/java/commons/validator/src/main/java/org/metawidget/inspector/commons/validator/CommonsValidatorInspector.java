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

package org.metawidget.inspector.commons.validator;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.InputStream;
import java.util.Map;
import java.util.StringTokenizer;

import org.metawidget.config.iface.ResourceResolver;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseXmlInspector;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Inspector to look for metadata in validation.xml files.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class CommonsValidatorInspector
	extends BaseXmlInspector {

	//
	// Private statics
	//

	private static final String	FORMSET_ELEMENT	= "formset";

	private static final String	FIELD_ELEMENT	= "field";

	//
	// Constructor
	//

	public CommonsValidatorInspector( CommonsValidatorInspectorConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected Element getDocumentElement( ResourceResolver resolver, InputStream... files )
		throws Exception {

		Document document = XmlUtils.newDocument();
		Element root = document.createElement( FORMSET_ELEMENT );
		document.appendChild( root );

		for ( InputStream file : files ) {
			Document documentParsed = XmlUtils.parse( file );
			Element formSet = XmlUtils.getChildNamed( documentParsed.getDocumentElement(), FORMSET_ELEMENT );

			if ( formSet == null ) {
				continue;
			}

			XmlUtils.combineElements( root, formSet, getTopLevelTypeAttribute(), getNameAttribute() );
		}

		return root;
	}

	@Override
	protected Map<String, String> inspectProperty( Element toInspect ) {

		if ( !FIELD_ELEMENT.equals( toInspect.getNodeName() ) ) {
			return null;
		}

		// Name

		Map<String, String> attributes = CollectionUtils.newHashMap();

		String name = toInspect.getAttribute( getNameAttribute() );
		attributes.put( NAME, name );

		if ( toInspect.hasAttribute( "depends" ) ) {
			StringTokenizer tokenizer = new StringTokenizer( toInspect.getAttribute( "depends" ), "," );
			Element firstVar = XmlUtils.getChildNamed( toInspect, "var" );

			while ( tokenizer.hasMoreTokens() ) {
				String depends = tokenizer.nextToken();

				// Required

				if ( "required".equals( depends ) ) {
					attributes.put( REQUIRED, TRUE );
				}

				// Minimum/Maximum values

				if ( "intRange".equals( depends ) || "floatRange".equals( depends ) || "doubleRange".equals( depends ) ) {
					String min = getVarValue( firstVar, "min" );

					if ( min != null ) {
						attributes.put( MINIMUM_VALUE, min );
					}

					String max = getVarValue( firstVar, "max" );

					if ( max != null ) {
						attributes.put( MAXIMUM_VALUE, max );
					}

					if ( min == null && max == null ) {
						throw InspectorException.newException( "Property '" + name + "' depends on " + depends + " but has no var-name of min or max" );
					}
				}

				// Minimum length

				if ( "minlength".equals( depends ) ) {
					attributes.put( MINIMUM_LENGTH, getVarValue( firstVar, "minlength", name, depends ) );
				}

				// Maximum length

				if ( "maxlength".equals( depends ) ) {
					attributes.put( MAXIMUM_LENGTH, getVarValue( firstVar, "maxlength", name, depends ) );
				}
			}
		}

		return attributes;
	}

	/**
	 * Overridden to search by <code>name=</code>, not <code>type=</code>.
	 */

	@Override
	protected String getTopLevelTypeAttribute() {

		return NAME;
	}

	/**
	 * The attribute on child elements that uniquely identifies them.
	 */

	@Override
	protected String getNameAttribute() {

		return "property";
	}

	//
	// Private methods
	//

	/**
	 * Gets the (mandatory) var-value of the given var-name for the given validator.
	 */

	private String getVarValue( Element firstVar, String varName, String propertyName, String depend ) {

		String varValue = getVarValue( firstVar, varName );

		if ( varValue == null ) {
			throw InspectorException.newException( "Property '" + propertyName + "' depends on " + depend + " but has no var-name of " + varName );
		}

		return varValue;
	}

	/**
	 * Gets the (optional) var-value of the given var-name.
	 */

	private String getVarValue( Element firstVar, String varName ) {

		Element var = firstVar;

		while ( var != null ) {
			Element varNameElement = XmlUtils.getChildNamed( var, "var-name" );

			if ( varName.equals( varNameElement.getTextContent() ) ) {
				Element varValueElement = XmlUtils.getChildNamed( var, "var-value" );

				if ( varValueElement == null ) {
					throw InspectorException.newException( "Variable named '" + varName + "' has no var-value" );
				}

				return varValueElement.getTextContent();
			}

			var = XmlUtils.getSiblingNamed( var, "var" );
		}

		return null;
	}
}
