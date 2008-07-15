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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.metawidget.util.ArrayUtils;

/**
 * Utilities for working with Java Server Faces.
 *
 * @author Richard Kennard
 */

public final class FacesUtils
{
	//
	//
	// Public statics
	//
	//

	public static boolean isValueReference( String binding )
	{
		return PATTERN_BINDING.matcher( binding ).matches();
	}
	/**
	 * @return the original String, not wrapped in #{...}. If the original String was not wrapped,
	 *         returns the original String
	 */

	public static String unwrapValueReference( String binding )
	{
		Matcher matcher = PATTERN_BINDING.matcher( binding );

		if ( !matcher.matches() )
			return binding;

		return matcher.group( 3 );
	}

	/**
	 * @return the original String, wrapped in #{...}. If the original String was already wrapped,
	 *         returns the original String
	 */

	public static String wrapValueReference( String binding )
	{
		if ( isValueReference( binding ))
			return binding;

		return BINDING_START + unwrapValueReference( binding ) + BINDING_END;
	}

	/**
	 * Finds the child component of the given component that is both rendered and has the given
	 * value binding.
	 * <p>
	 * Note: this method does <em>not</em> recurse into sub-children.
	 */

	public static UIComponent findRenderedComponentWithValueBinding( UIComponent component, ValueBinding binding )
	{
		String expressionString = binding.getExpressionString();

		// Try to find a child...

		@SuppressWarnings( "unchecked" )
		List<UIComponent> children = component.getChildren();

		for ( UIComponent child : children )
		{
			// ...with the binding we're interested in

			ValueBinding valueBinding = child.getValueBinding( "value" );

			if ( valueBinding == null )
				continue;

			// (note: ValueBinding.equals() does not compare expression strings)

			if ( expressionString.equals( valueBinding.getExpressionString() ) )
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

		@SuppressWarnings( "unchecked" )
		List<UIComponent> children = component.getChildren();

		for ( UIComponent child : children )
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
		if ( !component.isRendered() )
			return;

		component.encodeBegin( context );

		if ( component.getRendersChildren() )
			component.encodeChildren( context );
		else
			renderChildren( context, component );

		component.encodeEnd( context );
	}

	@SuppressWarnings( "unchecked" )
	public static void copyAttributes( UIComponent from, UIComponent to )
	{
		to.getAttributes().putAll( from.getAttributes() );
	}

	public static void copyParameters( UIComponent from, UIComponent to, String... exclude )
	{
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		UIViewRoot viewRoot = context.getViewRoot();

		// For each child parameter...

		@SuppressWarnings( "unchecked" )
		List<UIComponent> fromChildren = from.getChildren();

		@SuppressWarnings( "unchecked" )
		List<UIComponent> toChildren = to.getChildren();

		for ( UIComponent component : fromChildren )
		{
			if ( !( component instanceof UIParameter ) )
				continue;

			// ...that is not excluded...

			UIParameter parameter = (UIParameter) component;

			String name = parameter.getName();

			if ( ArrayUtils.contains( exclude, name ) )
				continue;

			// ...create a copy

			UIParameter parameterCopy = (UIParameter) application.createComponent( "javax.faces.Parameter" );
			parameterCopy.setId( viewRoot.createUniqueId() );

			parameterCopy.setName( name );
			parameterCopy.setValue( parameter.getValue() );

			toChildren.add( parameterCopy );
		}
	}

	//
	//
	// Private statics
	//
	//

	private static void renderChildren( FacesContext context, UIComponent component )
		throws IOException
	{
		@SuppressWarnings( "unchecked" )
		List<UIComponent> children = component.getChildren();

		for ( UIComponent componentChild : children )
		{
			render( context, componentChild );
		}
	}

	private final static Pattern	PATTERN_BINDING	= Pattern.compile( "((#|\\$)\\{)([^}]*)(\\})" );

	private final static String		BINDING_START	= "#{";

	private final static String		BINDING_END		= "}";

	//
	//
	// Private constructor
	//
	//

	private FacesUtils()
	{
		// Can never be called
	}
}
