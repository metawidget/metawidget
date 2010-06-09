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

package org.metawidget.inspector.jsp;

import static org.metawidget.inspector.jsp.JspInspectionResultConstants.*;

import java.util.Collection;
import java.util.Map;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.Trait;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.jsp.JspUtils;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.ThreadUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Inspects annotations defined by Metawidget's JSP support (declared in this same package).
 *
 * @author Richard Kennard
 */

public class JspAnnotationInspector
	extends BaseObjectInspector
{
	//
	// Private member
	//

	private final static ThreadLocal<PageContext>	LOCAL_PAGE_CONTEXT	= ThreadUtils.newThreadLocal();

	//
	// Public statics
	//

	/**
	 * Sets the PageContext to use for this Inspection.
	 * <p>
	 * Unfortunately, JSP lacks a standardized mechanism to retrieve the <code>PageContext</code>
	 * (or the <code>HttpServletRequest</code>) statically. Many containers work around this using
	 * <code>ThreadLocal</code>-variables, but this is implementation specific.
	 * <p>
	 * Clients wishing to use <code>UiJspAttribute</code> or <code>UiJspAttributes</code> must call
	 * this static method before each inspection to inject the <code>PageContext</code>.
	 * <code>org.metawidget.jsp.tagext.MetawidgetTag</code> does this automatically.
	 */

	public static void setThreadLocalPageContext( PageContext pageContext )
	{
		LOCAL_PAGE_CONTEXT.set( pageContext );
	}

	//
	// Constructor
	//

	public JspAnnotationInspector()
	{
		this( new BaseObjectInspectorConfig() );
	}

	public JspAnnotationInspector( BaseObjectInspectorConfig config )
	{
		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected Map<String, String> inspectTrait( Trait trait )
		throws Exception
	{
		// UiJspAttributes/UiJspAttribute

		UiJspAttributes jspAttributes = trait.getAnnotation( UiJspAttributes.class );
		UiJspAttribute jspAttribute = trait.getAnnotation( UiJspAttribute.class );

		if ( jspAttributes == null && jspAttribute == null )
		{
			return null;
		}

		Map<String, String> attributes = CollectionUtils.newHashMap();
		PageContext pageContext = LOCAL_PAGE_CONTEXT.get();

		if ( pageContext == null )
		{
			throw InspectorException.newException( "ThreadLocalPageContext not set" );
		}

		ExpressionEvaluator expressionEvaluator;

		try
		{
			expressionEvaluator = pageContext.getExpressionEvaluator();
		}
		catch ( Throwable t )
		{
			throw InspectorException.newException( "ExpressionEvaluator requires JSP 2.0" );
		}

		VariableResolver variableResolver = pageContext.getVariableResolver();

		// UiJspAttribute

		if ( jspAttribute != null )
		{
			putJspAttribute( expressionEvaluator, variableResolver, attributes, jspAttribute );
		}

		// UiJspAttributes

		if ( jspAttributes != null )
		{
			for ( UiJspAttribute nestedJspAttribute : jspAttributes.value() )
			{
				putJspAttribute( expressionEvaluator, variableResolver, attributes, nestedJspAttribute );
			}
		}

		return attributes;
	}

	@Override
	protected Map<String, String> inspectProperty( Property property )
		throws Exception
	{
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// UiJspLookup

		UiJspLookup jspLookup = property.getAnnotation( UiJspLookup.class );

		if ( jspLookup != null )
		{
			attributes.put( JSP_LOOKUP, jspLookup.value() );
		}

		return attributes;
	}

	protected void putJspAttribute( ExpressionEvaluator expressionEvaluator, VariableResolver variableResolver, Map<String, String> attributes, UiJspAttribute jspAttribute )
		throws Exception
	{
		String expression = jspAttribute.expression();

		if ( !JspUtils.isExpression( expression ) )
		{
			throw InspectorException.newException( "Expression '" + expression + "' is not of the form ${...}" );
		}

		Object value = expressionEvaluator.evaluate( expression, Object.class, variableResolver, null );

		if ( value == null )
		{
			return;
		}

		// TODO: test these!

		if ( value instanceof Collection<?> )
		{
			for ( String attributeName : jspAttribute.name() )
			{
				attributes.put( attributeName, CollectionUtils.toString( (Collection<?>) value ) );
			}
		}
		else if ( value instanceof Object[] )
		{
			for ( String attributeName : jspAttribute.name() )
			{
				attributes.put( attributeName, ArrayUtils.toString( (Object[]) value ) );
			}
		}
		else
		{
			for ( String attributeName : jspAttribute.name() )
			{
				attributes.put( attributeName, StringUtils.quietValueOf( value ) );
			}
		}
	}
}
