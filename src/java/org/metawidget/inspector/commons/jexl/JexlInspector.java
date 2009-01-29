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
import org.metawidget.inspector.impl.actionstyle.Action;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.ThreadUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Inspects annotations defined by Metawidget's JEXL support (declared in this same package).
 * <p>
 * Many Web environments, such as Java Server Faces and Java Server Pages, supply their own
 * Expression Language (EL) - but most desktop and mobile environments do not. JEXL is a
 * lightweight, standalone Expression Language for use in Java applications. Using JEXL, non-Web
 * environments can 'wire together' properties using ELs in much the same way as Web environments
 * that use, say, <code>UiFacesAttribute</code>.
 * <p>
 * A significant difference of <code>JexlInspector</code> compared to other ELs is that the JEXL
 * EL is relative to the object being inspected, not to some global EL context. So expressions use
 * <code>this</code>, as in <code>${this.retired}</code>. Use of a <code>this</code>
 * keyword, as opposed to the name of the class being annotated, keeps JEXL EL expressions working
 * even for subclasses.
 *
 * @author Richard Kennard
 */

public class JexlInspector
	extends BaseObjectInspector
{
	//
	// Private members
	//

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
	// Public methods
	//

	@Override
	public String inspect( Object toInspect, String type, String... names )
		throws InspectorException
	{
		LOCAL_CONTEXT.remove();

		return super.inspect( toInspect, type, names );
	}

	//
	// Protected methods
	//

	@Override
	protected Map<String, String> inspectProperty( Property property, Object toInspect )
		throws Exception
	{
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// UiJexlAttributes/UiJexlAttribute

		putJexlAttributes( toInspect, attributes, property.getAnnotation( UiJexlAttributes.class ), property.getAnnotation( UiJexlAttribute.class ));

		return attributes;
	}

	@Override
	protected Map<String, String> inspectAction( Action action, Object toInspect )
		throws Exception
	{
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// UiJexlAttributes/UiJexlAttribute

		putJexlAttributes( toInspect, attributes, action.getAnnotation( UiJexlAttributes.class ), action.getAnnotation( UiJexlAttribute.class ));

		return attributes;
	}

	protected void putJexlAttributes( Object toInspect, Map<String, String> attributes, UiJexlAttributes jexlAttributes, UiJexlAttribute jexlAttribute )
		throws Exception
	{
		// UiJexlAttribute

		if ( jexlAttribute != null )
		{
			putJexlAttribute( toInspect, jexlAttribute, attributes );
		}

		// UiJexlAttributes

		if ( jexlAttributes != null )
		{
			for ( UiJexlAttribute nestedJexlAttribute : jexlAttributes.value() )
			{
				putJexlAttribute( toInspect, nestedJexlAttribute, attributes );
			}
		}
	}

	protected void putJexlAttribute( Object toInspect, UiJexlAttribute jexlAttribute, Map<String, String> attributes )
		throws Exception
	{
		String expression = jexlAttribute.expression();

		// Likely mistake

		if ( expression.startsWith( "${" ) )
			throw InspectorException.newException( "Expression '" + expression + "' should be of the form 'foo.bar', not '${foo.bar}'" );

		Object value = ExpressionFactory.createExpression( expression ).evaluate( getContext( toInspect ) );

		if ( value == null )
			return;

		attributes.put( jexlAttribute.name(), StringUtils.quietValueOf( value ) );
	}

	/**
	 * Get the JexlContext. Creates one if necessary.
	 */

	protected JexlContext getContext( Object toInspect )
	{
		JexlContext context = LOCAL_CONTEXT.get();

		if ( context == null )
		{
			context = createContext( toInspect );
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
			contextMap.put( "this", toInspect );

		return context;
	}
}
