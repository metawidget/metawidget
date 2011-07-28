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

package org.metawidget.inspector.faces;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.metawidget.faces.FacesUtils;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseXmlInspector;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Element;

/**
 * Inspects <code>inspection-result-1.0.xsd</code>-compliant files (such as
 * <code>metawidget-metadata.xml</code>), in the same way as <code>XmlInspector</code>, for Java
 * Server Faces expressions. Any attributes conforming to the <code>#{...}</code> convention are
 * passed to JSF.
 * <p>
 * Note because <code>FacesXmlInspector</code> overrides attribute values, its position in a
 * <code>CompositeInspector</code> list is important (ie. it should come after
 * <code>XmlInspector</code>).
 *
 * @author Richard Kennard
 */

public class FacesXmlInspector
	extends BaseXmlInspector {

	//
	// Private statics
	//

	private final static String	THIS_ATTRIBUTE				= "this";

	private final static String	UNDERSCORE_THIS_ATTRIBUTE	= "_this";

	//
	// Private members
	//

	private boolean				mInjectThis;

	//
	// Constructors
	//

	/**
	 * Constructs a FacesXmlInspector.
	 * <p>
	 * Note FacesXmlInspector requires a config. It does not have a default constructor, because the
	 * FacesXmlInspectorConfig must be externally configured using <code>setResourceResolver</code>
	 * to support resolving resources from non-standard locations (such as <code>WEB-INF</code).
	 */

	public FacesXmlInspector( FacesXmlInspectorConfig config ) {

		super( config );

		mInjectThis = config.isInjectThis();
	}

	//
	// Public methods
	//

	@Override
	public Element inspectAsDom( Object toInspect, String type, String... names ) {

		FacesContext context = FacesContext.getCurrentInstance();

		if ( context == null ) {
			throw InspectorException.newException( "FacesContext not available to FacesXmlInspector" );
		}

		Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

		try {
			if ( mInjectThis ) {
				requestMap.put( THIS_ATTRIBUTE, toInspect );
				requestMap.put( UNDERSCORE_THIS_ATTRIBUTE, toInspect );
			}

			return super.inspectAsDom( toInspect, type, names );
		} finally {

			// THIS_ATTRIBUTE and UNDERSCORE_THIS_ATTRIBUTE should not be available outside of our particular evaluation

			if ( mInjectThis ) {
				requestMap.remove( THIS_ATTRIBUTE );
				requestMap.remove( UNDERSCORE_THIS_ATTRIBUTE );
			}
		}
	}

	//
	// Protected methods
	//

	@Override
	protected Map<String, String> inspectProperty( Element toInspect ) {

		if ( PROPERTY.equals( toInspect.getNodeName() ) ) {
			return inspect( toInspect );
		}

		return null;
	}

	@Override
	protected Map<String, String> inspectAction( Element toInspect ) {

		if ( ACTION.equals( toInspect.getNodeName() ) ) {
			return inspect( toInspect );
		}

		return null;
	}

	//
	// Private methods
	//

	// TODO: clean this up by turning it into an InspectionResultProcessor

	@SuppressWarnings( "deprecation" )
	private Map<String, String> inspect( Element toInspect ) {

		Map<String, String> attributes = XmlUtils.getAttributesAsMap( toInspect );

		// For each attribute value...

		for ( Map.Entry<String, String> entry : CollectionUtils.newArrayList( attributes.entrySet() ) ) {

			String key = entry.getKey();
			String value = entry.getValue();

			// ...except ones that are *expected* to be EL expressions...

			if ( FACES_LOOKUP.equals( key ) || FACES_SUGGEST.equals( key ) || FACES_EXPRESSION.equals( key ) || FACES_AJAX_ACTION.equals( key ) ) {

				if ( mInjectThis ) {
					String unwrappedExpression = FacesUtils.unwrapExpression( value );

					if ( unwrappedExpression.startsWith( THIS_ATTRIBUTE + StringUtils.SEPARATOR_DOT ) ) {
						throw InspectorException.newException( "Expression '" + value + "' (for '" + key + "') must not contain '" + THIS_ATTRIBUTE + "' (see Metawidget Reference Guide)" );
					}

					if ( unwrappedExpression.startsWith( UNDERSCORE_THIS_ATTRIBUTE + StringUtils.SEPARATOR_DOT ) ) {
						throw InspectorException.newException( "Expression '" + value + "' (for '" + key + "') must not contain '" + UNDERSCORE_THIS_ATTRIBUTE + "' (see Metawidget Reference Guide)" );
					}
				}

				continue;
			}

			// ...that looks like an EL expression...

			if ( !FacesUtils.isExpression( value ) ) {
				continue;
			}

			// Sanity checks

			if ( !mInjectThis ) {
				String unwrappedExpression = FacesUtils.unwrapExpression( value );

				if ( unwrappedExpression.startsWith( THIS_ATTRIBUTE + StringUtils.SEPARATOR_DOT ) ) {
					throw InspectorException.newException( "Expression for '" + value + "' contains '" + THIS_ATTRIBUTE + "', but " + FacesXmlInspectorConfig.class.getSimpleName() + ".setInjectThis is 'false'" );
				}

				if ( unwrappedExpression.startsWith( UNDERSCORE_THIS_ATTRIBUTE + StringUtils.SEPARATOR_DOT ) ) {
					throw InspectorException.newException( "Expression for '" + value + "' contains '" + UNDERSCORE_THIS_ATTRIBUTE + "', but " + FacesXmlInspectorConfig.class.getSimpleName() + ".setInjectThis is 'false'" );
				}
			}

			// ...evaluate it...

			try {
				FacesContext context = FacesContext.getCurrentInstance();
				value = StringUtils.quietValueOf( context.getApplication().createValueBinding( value ).getValue( context ) );

			} catch ( Exception e ) {

				// We have found it helpful to include the actual expression we were trying to
				// evaluate

				throw InspectorException.newException( "Unable to getValue of " + value, e );
			}

			// ...and replace it

			attributes.put( key, value );
		}

		return attributes;
	}
}
