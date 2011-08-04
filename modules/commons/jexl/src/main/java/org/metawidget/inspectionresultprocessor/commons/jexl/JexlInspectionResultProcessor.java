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

package org.metawidget.inspectionresultprocessor.commons.jexl;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessorException;
import org.metawidget.inspectionresultprocessor.impl.BaseInspectionResultProcessor;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.InspectorUtils;
import org.w3c.dom.Element;

/**
 * Processes the inspection result and evaluates any expressions of the form <code>${...}</code>
 * using JEXL.
 * 
 * @author Richard Kennard
 */

public class JexlInspectionResultProcessor<M>
	extends BaseInspectionResultProcessor<M> {

	//
	// Private statics
	//

	private static final String						THIS_ATTRIBUTE		= "this";

	private static final ThreadLocal<JexlContext>	LOCAL_CONTEXT		= new ThreadLocal<JexlContext>();

	private static final Pattern					PATTERN_EXPRESSION	= Pattern.compile( "^\\$\\{(.*)\\}$" );

	//
	// Private members
	//

	private PropertyStyle							mInjectThis;

	//
	// Constructors
	//

	/**
	 * Constructs a FacesInspectionResultProcessor.
	 */

	public JexlInspectionResultProcessor() {

		this( new JexlInspectionResultProcessorConfig() );
	}

	/**
	 * Constructs a FacesInspectionResultProcessor.
	 */

	public JexlInspectionResultProcessor( JexlInspectionResultProcessorConfig config ) {

		mInjectThis = config.getInjectThis();
	}

	@Override
	public Element processInspectionResultAsDom( Element inspectionResult, M metawidget, Object toInspect, String type, String... names ) {

		try {
			LOCAL_CONTEXT.set( createContext( metawidget ) );
			return super.processInspectionResultAsDom( inspectionResult, metawidget, toInspect, type, names );

		} finally {
			LOCAL_CONTEXT.remove();
		}
	}

	//
	// Protected methods
	//

	@Override
	protected void processEntity( Map<String, String> attributes, M metawidget, Object toInspect, String type, String... names ) {

		JexlContext context = LOCAL_CONTEXT.get();
		@SuppressWarnings( "unchecked" )
		Map<String, Object> vars = context.getVars();

		try {
			vars.put( THIS_ATTRIBUTE, InspectorUtils.traverse( mInjectThis, toInspect, type, true, names ).getLeft() );
			super.processEntity( attributes, metawidget, toInspect, type, names );

		} finally {

			// THIS_ATTRIBUTE should not be available outside of our particular
			// evaluation

			vars.remove( THIS_ATTRIBUTE );
		}
	}

	@Override
	protected void processTraits( Element entity, M metawidget, Object toInspect, String type, String... names ) {

		JexlContext context = LOCAL_CONTEXT.get();
		@SuppressWarnings( "unchecked" )
		Map<String, Object> vars = context.getVars();

		try {
			vars.put( THIS_ATTRIBUTE, InspectorUtils.traverse( mInjectThis, toInspect, type, false, names ).getLeft() );
			super.processTraits( entity, metawidget, toInspect, type, names );

		} finally {

			// THIS_ATTRIBUTE should not be available outside of our particular
			// evaluation

			vars.remove( THIS_ATTRIBUTE );
		}
	}

	@Override
	protected void processAttributes( Map<String, String> attributes, M metawidget ) {

		// For each attribute value...

		for ( Map.Entry<String, String> entry : CollectionUtils.newArrayList( attributes.entrySet() ) ) {

			String key = entry.getKey();
			String value = entry.getValue();

			// ...that looks like a JEXL expression...

			Matcher matcher = PATTERN_EXPRESSION.matcher( value );

			if ( !matcher.matches() ) {
				continue;
			}

			// ...evaluate it...

			value = matcher.group( 1 );

			try {
				Object valueObject = ExpressionFactory.createExpression( value ).evaluate( LOCAL_CONTEXT.get() );

				if ( valueObject == null ) {
					value = null;
				} else {
					value = String.valueOf( valueObject );
				}

			} catch ( Exception e ) {

				// We have found it helpful to include the actual expression we were trying to
				// evaluate

				throw InspectionResultProcessorException.newException( "Unable to evaluate " + value, e );
			}

			// ...and replace it

			attributes.put( key, value );
		}
	}

	/**
	 * Prepare the JexlContext.
	 * <p>
	 * Subclasses can override this method to control what is available in the context.
	 * 
	 * @param metawidget
	 *            the parent Metawidget. Never null. May be useful for finding object to add to the
	 *            context
	 */

	protected JexlContext createContext( M metawidget ) {

		return JexlHelper.createContext();
	}
}
