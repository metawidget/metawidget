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

package org.metawidget.inspectionresultprocessor.jsp;

import static org.metawidget.inspector.jsp.JspInspectionResultConstants.*;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;

import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessorException;
import org.metawidget.inspectionresultprocessor.impl.BaseInspectionResultProcessor;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.CollectionUtils;

/**
 * Processes the inspection result and evaluates any expressions of the form <code>${...}</code>
 * using JSP EL.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JspInspectionResultProcessor
	extends BaseInspectionResultProcessor<MetawidgetTag> {

	//
	// Private statics
	//

	private static final Pattern	PATTERN_EXPRESSION	= Pattern.compile( "\\$\\{([^\\}]+)\\}" );

	//
	// Protected methods
	//

	@Override
	protected void processAttributes( Map<String, String> attributes, MetawidgetTag metawidgetTag ) {

		// For each attribute value...

		for ( Map.Entry<String, String> entry : attributes.entrySet() ) {

			String key = entry.getKey();
			String value = entry.getValue();

			// ...except ones that are *expected* to be EL expressions...

			if ( JSP_LOOKUP.equals( key ) ) {
				continue;
			}

			// ...that contains an EL expression...

			Matcher matcher = PATTERN_EXPRESSION.matcher( value );
			int matchOffset = 0;

			while ( matcher.find() ) {

				String expression = matcher.group( 0 );

				// ...evaluate it...

				PageContext pageContext = metawidgetTag.getPageContext();
				ExpressionEvaluator expressionEvaluator;

				try {
					expressionEvaluator = pageContext.getExpressionEvaluator();
				} catch ( Exception e ) {
					throw InspectorException.newException( "ExpressionEvaluator requires JSP 2.0", e );
				}

				try {
					VariableResolver variableResolver = pageContext.getVariableResolver();
					Object valueObject = expressionEvaluator.evaluate( expression, Object.class, variableResolver, null );
					String valueObjectAsString;

					if ( valueObject == null ) {

						// Support the default case (when the String is just one EL)

						if ( matcher.start() == 0 && matcher.end() == value.length() ) {
							value = null;
							break;
						}

						valueObjectAsString = "";
					} else {

						// Support the default case (when the String is just one EL)

						if ( matcher.start() == 0 && matcher.end() == value.length() ) {
							if ( valueObject instanceof Collection<?> ) {
								value = CollectionUtils.toString( (Collection<?>) valueObject );
							} else if ( valueObject.getClass().isArray() ) {
								value = ArrayUtils.toString( valueObject );
							} else {
								value = String.valueOf( valueObject );
							}
							break;
						}

						valueObjectAsString = String.valueOf( valueObject );
					}

					// Replace multiple ELs within the String

					value = new StringBuilder( value ).replace( matcher.start() + matchOffset, matcher.end() + matchOffset, valueObjectAsString ).toString();
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
