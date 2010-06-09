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

package org.metawidget.inspector.commons.jexl;

import java.util.Map;

import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.Trait;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.ThreadUtils;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Element;

/**
 * Inspects annotations defined by Metawidget's JEXL support (declared in this same package).
 * <p>
 * Many Web environments, such as Java Server Faces and Java Server Pages, supply their own
 * Expression Language (EL) - but most desktop and mobile environments do not. JEXL is a
 * lightweight, standalone Expression Language for use in Java applications. Using JEXL, non-Web
 * environments can 'wire together' properties using ELs in much the same way as Web environments
 * that use, say, <code>UiFacesAttribute</code>.
 * <p>
 * Expressions can use <code>this</code>, as in <code>this.retired</code>, to refer to the object
 * being inspected. Use of a <code>this</code> keyword, as opposed to the name of the class being
 * annotated, keeps JEXL EL expressions working even for subclasses.
 *
 * @author Richard Kennard
 */

public class JexlInspector
	extends BaseObjectInspector
{
	//
	// Private members
	//

	private final static ThreadLocal<Object>		LOCAL_TOINSPECT	= ThreadUtils.newThreadLocal();

	private final static ThreadLocal<JexlContext>	LOCAL_CONTEXT	= ThreadUtils.newThreadLocal();

	//
	// Constructor
	//

	public JexlInspector()
	{
		this( new BaseObjectInspectorConfig() );
	}

	public JexlInspector( BaseObjectInspectorConfig config )
	{
		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected Map<String, String> inspectParent( Object parentToInspect, Property propertyInParent )
		throws Exception
	{
		try
		{
			LOCAL_TOINSPECT.set( parentToInspect );
			return super.inspectParent( parentToInspect, propertyInParent );
		}
		finally
		{
			LOCAL_CONTEXT.remove();
			LOCAL_TOINSPECT.remove();
		}
	}

	@Override
	protected void inspect( Object toInspect, Class<?> classToInspect, Element toAddTo )
		throws Exception
	{
		try
		{
			LOCAL_TOINSPECT.set( toInspect );

			super.inspect( toInspect, classToInspect, toAddTo );
		}
		finally
		{
			LOCAL_CONTEXT.remove();
			LOCAL_TOINSPECT.remove();
		}
	}

	@Override
	protected Map<String, String> inspectTrait( Trait trait )
		throws Exception
	{
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// UiJexlAttributes/UiJexlAttribute

		putJexlAttributes( attributes, trait.getAnnotation( UiJexlAttributes.class ), trait.getAnnotation( UiJexlAttribute.class ) );

		return attributes;
	}

	protected void putJexlAttributes( Map<String, String> attributes, UiJexlAttributes jexlAttributes, UiJexlAttribute jexlAttribute )
		throws Exception
	{
		// UiJexlAttribute

		if ( jexlAttribute != null )
		{
			putJexlAttribute( jexlAttribute, attributes );
		}

		// UiJexlAttributes

		if ( jexlAttributes != null )
		{
			for ( UiJexlAttribute nestedJexlAttribute : jexlAttributes.value() )
			{
				putJexlAttribute( nestedJexlAttribute, attributes );
			}
		}
	}

	protected void putJexlAttribute( UiJexlAttribute jexlAttribute, Map<String, String> attributes )
		throws Exception
	{
		String expression = jexlAttribute.expression();

		// Likely mistake

		if ( expression.startsWith( "${" ) )
		{
			throw InspectorException.newException( "Expression '" + expression + "' should be of the form 'foo.bar', not '${foo.bar}'" );
		}

		Object value = ExpressionFactory.createExpression( expression ).evaluate( getContext() );

		if ( value == null )
		{
			return;
		}

		attributes.put( jexlAttribute.name(), StringUtils.quietValueOf( value ) );
	}

	/**
	 * Get the JexlContext. Creates one if necessary.
	 */

	protected JexlContext getContext()
	{
		JexlContext context = LOCAL_CONTEXT.get();

		if ( context == null )
		{
			context = createContext( LOCAL_TOINSPECT.get() );
			LOCAL_CONTEXT.set( context );
		}

		return context;
	}

	/**
	 * Prepare the JexlContext.
	 * <p>
	 * Subclasses can override this method to control what is available in the context.
	 */

	protected JexlContext createContext( Object toInspect )
	{
		JexlContext context = JexlHelper.createContext();
		@SuppressWarnings( "unchecked" )
		Map<String, Object> contextMap = context.getVars();

		// Put the toInspect in as 'this'

		if ( toInspect != null )
		{
			contextMap.put( "this", toInspect );
		}

		return context;
	}
}
