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

package org.metawidget.inspector.jexl;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;
import org.metawidget.inspector.ConfigReader;
import org.metawidget.inspector.ResourceResolver;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseXmlInspector;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.ThreadUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Element;

/**
 * Inspects XML for JEXL attributes.
 * <p>
 * Many Web environments, such as Java Server Faces and Java Server Pages, supply their own
 * Expression Language (EL) - but many desktop and mobile environments do not. JEXL is a
 * lightweight, standalone Expression Language for use in Java applications. Using JEXL, non-Web
 * environments can 'wire together' properties using ELs in much the same way as Web environments
 * can.
 * <p>
 * <code>JexlXmlInspector</code> inspects <code>inspection-result-1.0.xsd</code>-compliant
 * files (such as <code>metawidget-metadata.xml</code>), in the same way as
 * <code>XmlInspector</code>. Any attributes conforming to the <code>${...}</code> convention
 * are passed to JEXL.
 *
 * @author Richard Kennard
 */

public class JexlXmlInspector
	extends BaseXmlInspector
{
	//
	//
	// Private members
	//
	//

	private final static ThreadLocal<Object>		LOCAL_TOINSPECT	= ThreadUtils.newThreadLocal();

	private final static ThreadLocal<JexlContext>	LOCAL_CONTEXT	= ThreadUtils.newThreadLocal();

	//
	//
	// Constructors
	//
	//

	public JexlXmlInspector()
	{
		this( new JexlXmlInspectorConfig() );
	}

	public JexlXmlInspector( JexlXmlInspectorConfig config )
	{
		this( config, new ConfigReader() );
	}

	public JexlXmlInspector( ResourceResolver resolver )
	{
		this( new JexlXmlInspectorConfig(), resolver );
	}

	public JexlXmlInspector( JexlXmlInspectorConfig config, ResourceResolver resolver )
	{
		super( config, resolver );
	}

	//
	//
	// Public methods
	//
	//

	@Override
	public String inspect( Object toInspect, String type, String... names )
		throws InspectorException
	{
		LOCAL_TOINSPECT.set( toInspect );
		LOCAL_CONTEXT.remove();

		return super.inspect( toInspect, type, names );
	}

	//
	//
	// Protected methods
	//
	//

	@Override
	protected String getExtendsAttribute()
	{
		return "extends";
	}

	@Override
	protected Map<String, String> inspectProperty( Element toInspect )
	{
		if ( PROPERTY.equals( toInspect.getNodeName() ) )
			return inspect( toInspect );

		return null;
	}

	@Override
	protected Map<String, String> inspectAction( Element toInspect )
	{
		if ( ACTION.equals( toInspect.getNodeName() ) )
			return inspect( toInspect );

		return null;
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

		// Put the toInspect in under 'this'

		if ( toInspect != null )
			contextMap.put( "this", toInspect );

		return context;
	}

	//
	//
	// Private methods
	//
	//

	private Map<String, String> inspect( Element toInspect )
	{
		Map<String, String> attributes = XmlUtils.getAttributesAsMap( toInspect );

		// For each attribute value...

		for ( Map.Entry<String, String> entry : CollectionUtils.newArrayList( attributes.entrySet() ) )
		{
			String value = entry.getValue();

			// ...that looks like a value reference...

			if ( !JexlUtils.isValueReference( value ) )
				continue;

			// ...evaluate it...

			try
			{
				value = StringUtils.quietValueOf( ExpressionFactory.createExpression( JexlUtils.unwrapValueReference( value ) ).evaluate( getContext() ) );
			}
			catch ( Exception e )
			{
				throw InspectorException.newException( e );
			}

			// ...and replace it

			attributes.put( entry.getKey(), value );
		}

		return attributes;
	}
}
