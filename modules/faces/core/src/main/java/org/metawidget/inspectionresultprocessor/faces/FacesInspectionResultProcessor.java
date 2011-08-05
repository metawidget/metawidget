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

package org.metawidget.inspectionresultprocessor.faces;

import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Map;
import java.util.regex.Matcher;

import javax.faces.context.FacesContext;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessorException;
import org.metawidget.inspectionresultprocessor.impl.BaseInspectionResultProcessor;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.InspectorUtils;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Element;

/**
 * Processes the inspection result and evaluates any expressions of the form <code>#{...}</code>
 * using JSF EL.
 * 
 * @author Richard Kennard
 */

public class FacesInspectionResultProcessor
	extends BaseInspectionResultProcessor<UIMetawidget> {

	//
	// Private statics
	//

	private final static String	UNDERSCORE_THIS_ATTRIBUTE	= "_this";

	//
	// Private members
	//

	private PropertyStyle		mInjectThis;

	//
	// Constructors
	//

	/**
	 * Constructs a FacesInspectionResultProcessor.
	 */

	public FacesInspectionResultProcessor() {

		this( new FacesInspectionResultProcessorConfig() );
	}

	/**
	 * Constructs a FacesInspectionResultProcessor.
	 */

	public FacesInspectionResultProcessor( FacesInspectionResultProcessorConfig config ) {

		mInjectThis = config.getInjectThis();
	}

	//
	// Protected methods
	//

	@Override
	protected void processEntity( Map<String, String> attributes, UIMetawidget metawidget, Object toInspect, String type, String... names ) {

		FacesContext context = FacesContext.getCurrentInstance();

		if ( context == null ) {
			throw InspectionResultProcessorException.newException( "FacesContext not available to FacesInspectionResultProcessor" );
		}

		Map<String, Object> requestMap = null;

		try {
			if ( mInjectThis != null ) {
				requestMap = context.getExternalContext().getRequestMap();
				requestMap.put( UNDERSCORE_THIS_ATTRIBUTE, InspectorUtils.traverse( mInjectThis, toInspect, type, true, names ).getLeft() );
			}

			super.processEntity( attributes, metawidget, toInspect, type, names );

		} finally {

			// UNDERSCORE_THIS_ATTRIBUTE should not be available outside of our particular
			// evaluation

			if ( requestMap != null ) {
				requestMap.remove( UNDERSCORE_THIS_ATTRIBUTE );
			}
		}
	}

	@Override
	protected void processTraits( Element entity, UIMetawidget metawidget, Object toInspect, String type, String... names ) {

		FacesContext context = FacesContext.getCurrentInstance();

		if ( context == null ) {
			throw InspectionResultProcessorException.newException( "FacesContext not available to FacesInspectionResultProcessor" );
		}

		Map<String, Object> requestMap = null;

		try {
			if ( mInjectThis != null ) {
				requestMap = context.getExternalContext().getRequestMap();
				requestMap.put( UNDERSCORE_THIS_ATTRIBUTE, InspectorUtils.traverse( mInjectThis, toInspect, type, false, names ).getLeft() );
			}

			super.processTraits( entity, metawidget, toInspect, type, names );

		} finally {

			// UNDERSCORE_THIS_ATTRIBUTE should not be available outside of our particular
			// evaluation

			if ( requestMap != null ) {
				requestMap.remove( UNDERSCORE_THIS_ATTRIBUTE );
			}
		}
	}

	@Override
	protected void processAttributes( Map<String, String> attributes, UIMetawidget metawidget ) {

		// For each attribute value...

		for ( Map.Entry<String, String> entry : attributes.entrySet() ) {

			String key = entry.getKey();
			String value = entry.getValue();

			// ...except ones that are *expected* to be EL expressions...

			if ( FACES_LOOKUP.equals( key ) || FACES_SUGGEST.equals( key ) || FACES_EXPRESSION.equals( key ) || FACES_AJAX_ACTION.equals( key ) ) {

				if ( mInjectThis != null ) {
					String unwrappedExpression = FacesUtils.unwrapExpression( value );

					if ( unwrappedExpression.startsWith( UNDERSCORE_THIS_ATTRIBUTE + StringUtils.SEPARATOR_DOT ) ) {
						throw InspectionResultProcessorException.newException( "Expression '" + value + "' (for '" + key + "') must not contain '" + UNDERSCORE_THIS_ATTRIBUTE + "' (see Metawidget Reference Guide)" );
					}
				}

				continue;
			}

			// ...that contains an EL expression...

			Matcher matcher = FacesUtils.matchExpression( value );
			int matchOffset = 0;

			while ( matcher.find() ) {

				String expression = matcher.group( 0 );

				// Sanity checks

				if ( mInjectThis == null && matcher.group( 2 ).startsWith( UNDERSCORE_THIS_ATTRIBUTE + StringUtils.SEPARATOR_DOT ) ) {
					throw InspectionResultProcessorException.newException( "Expression for '" + value + "' contains '" + UNDERSCORE_THIS_ATTRIBUTE + "', but " + FacesInspectionResultProcessorConfig.class.getSimpleName() + ".setInjectThis is null" );
				}

				// ...evaluate it...

				try {
					FacesContext context = FacesContext.getCurrentInstance();
					@SuppressWarnings( "deprecation" )
					Object valueObject = context.getApplication().createValueBinding( expression ).getValue( context );
					String valueObjectAsString;

					if ( valueObject == null ) {

						// Special support for when the String is just one EL

						if ( matcher.start() == 0 && matcher.end() == value.length() ) {
							value = null;
							break;
						}

						valueObjectAsString = "";
					} else {

						// Special support for when the String is just one EL

						if ( matcher.start() == 0 && matcher.end() == value.length() ) {
							value = String.valueOf( valueObject );
							break;
						}

						valueObjectAsString = String.valueOf( valueObject );
					}

					// Replace multiple ELs within the String

					value = new StringBuffer( value ).replace( matcher.start() + matchOffset, matcher.end() + matchOffset, valueObjectAsString ).toString();
					matchOffset += valueObjectAsString.length() - ( matcher.end() - matcher.start() );

				} catch ( Exception e ) {

					// We have found it helpful to include the actual expression we were trying to
					// evaluate

					throw InspectionResultProcessorException.newException( "Unable to evaluate " + value, e );
				}
			}

			// ...and replace it

			attributes.put( key, value );
		}
	}
}
