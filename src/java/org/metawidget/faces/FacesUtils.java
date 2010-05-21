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

package org.metawidget.faces;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.Application;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

/**
 * Utilities for working with Java Server Faces.
 *
 * @author Richard Kennard
 */

@SuppressWarnings( "deprecation" )
public final class FacesUtils
{
	//
	// Public statics
	//

	/**
	 * Return <code>true</code> if the specified value conforms to the syntax requirements of a
	 * value binding expression.
	 * <p>
	 * This method is a mirror of the one in <code>UIComponentTag.isValueReference</code>, but that
	 * one is deprecated so may be removed in the future.
	 *
	 * @param value
	 *            The value to evaluate
	 * @throws NullPointerException
	 *             if <code>value</code> is <code>null</code>
	 */

	public static boolean isExpression( String value )
	{
		return PATTERN_EXPRESSION.matcher( value ).matches();
	}

	/**
	 * @return the original String, not wrapped in #{...}. If the original String was not wrapped,
	 *         returns the original String
	 */

	public static String unwrapExpression( String value )
	{
		Matcher matcher = PATTERN_EXPRESSION.matcher( value );

		if ( !matcher.matches() )
			return value;

		return matcher.group( 3 );
	}

	/**
	 * @return the original String, wrapped in #{...}. If the original String was already wrapped,
	 *         returns the original String
	 */

	public static String wrapExpression( String value )
	{
		if ( isExpression( value ) )
			return value;

		return EXPRESSION_START + unwrapExpression( value ) + EXPRESSION_END;
	}

	/**
	 * Finds the child component of the given component that is both rendered and has the given
	 * value expression.
	 * <p>
	 * Note: this method does <em>not</em> recurse into sub-children.
	 */

	public static UIComponent findRenderedComponentWithValueBinding( UIComponent component, String expressionString )
	{
		// Try to find a child...

		for ( UIComponent child : component.getChildren() )
		{
			// ...with the binding we're interested in

			ValueBinding childValueBinding = child.getValueBinding( "value" );

			if ( childValueBinding == null )
				continue;

			// (note: ValueBinding.equals() does not compare expression strings)

			if ( expressionString.equals( childValueBinding.getExpressionString() ) )
			{
				if ( child.isRendered() )
					return child;
			}
		}

		return null;
	}

	/**
	 * Finds the child component of the given component that is both rendered and has the given
	 * method expression.
	 * <p>
	 * Note: this method does <em>not</em> recurse into sub-children.
	 */

	public static UIComponent findRenderedComponentWithMethodBinding( UIComponent component, String expressionString )
	{
		// Try to find a child...

		for ( UIComponent child : component.getChildren() )
		{
			if ( !( child instanceof ActionSource ) )
				continue;

			// ...with the binding we're interested in

			MethodBinding childMethodBinding = ( (ActionSource) child ).getAction();

			if ( childMethodBinding == null )
				continue;

			// (note: MethodBinding.equals() does not compare expression strings)

			if ( expressionString.equals( childMethodBinding.getExpressionString() ) )
			{
				if ( child.isRendered() )
					return child;
			}
		}

		return null;
	}

	public static UIParameter findParameterWithName( UIComponent component, String name )
	{
		// Try to find a child parameter...

		for ( UIComponent child : component.getChildren() )
		{
			if ( !( child instanceof UIParameter ) )
				continue;

			// ...with the name we're interested in

			UIParameter parameter = (UIParameter) child;

			if ( name.equals( parameter.getName() ) )
				return parameter;
		}

		return null;
	}

	public static void render( FacesContext context, UIComponent component )
		throws IOException
	{
		if ( component == null || !component.isRendered() )
			return;

		component.encodeBegin( context );

		if ( component.getRendersChildren() )
			component.encodeChildren( context );
		else
			renderChildren( context, component );

		component.encodeEnd( context );
	}

	public static void copyParameters( UIComponent from, UIComponent to )
	{
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		UIViewRoot viewRoot = context.getViewRoot();

		// For each child parameter...

		for ( UIComponent component : from.getChildren() )
		{
			if ( !( component instanceof UIParameter ) )
				continue;

			// ...create a copy

			UIParameter parameterCopy = (UIParameter) application.createComponent( "javax.faces.Parameter" );
			parameterCopy.setId( viewRoot.createUniqueId() );

			UIParameter parameter = (UIParameter) component;
			parameterCopy.setName( parameter.getName() );
			parameterCopy.setValue( parameter.getValue() );

			to.getChildren().add( parameterCopy );
		}
	}

	/**
	 * Gets whether
	 * <code>&lt;param-name&gt;org.metawidget.faces.component.USE_SYSTEM_EVENTS&lt;/param-name&gt;</code>
	 * is set to <code>true</code>.
	 */

	public static boolean isUseSystemEvents()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();

		return "true".equals( externalContext.getInitParameter( "org.metawidget.faces.component.USE_SYSTEM_EVENTS" ) );
	}

	//
	// Private statics
	//

	private static void renderChildren( FacesContext context, UIComponent component )
		throws IOException
	{
		for ( UIComponent componentChild : component.getChildren() )
		{
			render( context, componentChild );
		}
	}

	/**
	 * Match #{...} and ${...}. This mirrors the approach in
	 * <code>UIComponentTag.isValueReference</code>, but that one is deprecated so may be removed in
	 * the future.
	 * <p>
	 * Like <code>UIComponentTag.isValueReference</code> we allow nested #{...} blocks, because this
	 * can still be a legitimate value reference:
	 * <p>
	 * <code>
	 * #{!empty bar ? '' : '#{foo}'}
	 * </code>
	 */

	private final static Pattern	PATTERN_EXPRESSION	= Pattern.compile( "((#|\\$)\\{)(.*)(\\})" );

	private final static String		EXPRESSION_START	= "#{";

	private final static String		EXPRESSION_END		= "}";

	//
	// Private constructor
	//

	private FacesUtils()
	{
		// Can never be called
	}
}
