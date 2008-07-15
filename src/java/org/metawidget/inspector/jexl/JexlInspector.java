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

import java.util.Map;

import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;
import org.metawidget.MetawidgetException;
import org.metawidget.faces.FacesUtils;
import org.metawidget.inspector.impl.BasePropertyInspector;
import org.metawidget.inspector.impl.BasePropertyInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Inspects annotations defined by Metawidget's JEXL support (declared in this same package).
 * <p>
 * Many Web environments, such as Java Server Faces and Java Server Pages, supply their own
 * Expression Language (EL) - but many desktop and mobile environments do not. JEXL is a
 * lightweight, standalone Expression Language for use in Java applications. Using JEXL, non-Web
 * environments can 'wire together' properties using ELs in much the same way as Web environments
 * can use, say, <code>UiFacesAttribute</code>.
 *
 * @author Richard Kennard
 */

public class JexlInspector
	extends BasePropertyInspector
{
	//
	//
	// Constructor
	//
	//

	public JexlInspector()
	{
		this( new BasePropertyInspectorConfig() );
	}

	public JexlInspector( BasePropertyInspectorConfig config )
	{
		super( config );
	}

	//
	//
	// Protected methods
	//
	//

	@Override
	protected Map<String, String> inspect( Property property, Object toInspect )
		throws Exception
	{
		if ( toInspect == null )
			return null;

		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Prepare JEXL

		JexlContext context = JexlHelper.createContext();
		@SuppressWarnings( "unchecked" )
		Map<String, Object> contextMap = context.getVars();
		contextMap.put( StringUtils.lowercaseFirstLetter( toInspect.getClass().getSimpleName() ), toInspect );

		// UiJexlAttribute

		UiJexlAttribute JexlAttribute = property.getAnnotation( UiJexlAttribute.class );

		if ( JexlAttribute != null )
		{
			putJexlAttribute( context, attributes, JexlAttribute );
		}

		// UiJexlAttributes

		UiJexlAttributes JexlAttributes = property.getAnnotation( UiJexlAttributes.class );

		if ( JexlAttributes != null )
		{
			for ( UiJexlAttribute nestedJexlAttribute : JexlAttributes.value() )
			{
				putJexlAttribute( context, attributes, nestedJexlAttribute );
			}
		}

		return attributes;
	}

	protected void putJexlAttribute( JexlContext context, Map<String, String> attributes, UiJexlAttribute jexlAttribute )
		throws Exception
	{
		// Optional condition

		String condition = jexlAttribute.condition();

		if ( !"".equals( condition ) )
		{
			if ( !FacesUtils.isValueReference( condition ) )
				throw MetawidgetException.newException( "Condition '" + condition + "' is not of the form ${...}" );

			Object conditionResult = ExpressionFactory.createExpression( JexlUtils.unwrapValueReference( condition ) ).evaluate( context );

			if ( !Boolean.TRUE.equals( conditionResult ) )
				return;
		}

		// Optionally expression-based

		String value = jexlAttribute.value();

		if ( JexlUtils.isValueReference( value ) )
			value = StringUtils.quietValueOf( ExpressionFactory.createExpression( JexlUtils.unwrapValueReference( value ) ).evaluate( context ) );

		// Set the value

		attributes.put( jexlAttribute.name(), StringUtils.quietValueOf( value ) );
	}
}
