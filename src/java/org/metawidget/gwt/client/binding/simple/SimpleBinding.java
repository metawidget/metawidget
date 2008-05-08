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

package org.metawidget.gwt.client.binding.simple;

import java.util.HashMap;
import java.util.Map;

import org.metawidget.gwt.client.binding.Binding;
import org.metawidget.gwt.client.binding.Converter;
import org.metawidget.gwt.client.ui.GwtMetawidget;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author Richard Kennard
 */

public class SimpleBinding
	extends Binding
{
	//
	//
	// Private statics
	//
	//

	private final static Map<Class<?>, SimpleBindingAdapter<?>>	ADAPTERS	= new HashMap<Class<?>, SimpleBindingAdapter<?>>();

	private final static Map<Class<?>, Converter<?>>			CONVERTERS	= new HashMap<Class<?>, Converter<?>>();

	//
	//
	// Public statics
	//
	//

	public static <T> void registerAdapter( Class<T> forClass, SimpleBindingAdapter<T> adapter )
	{
		ADAPTERS.put( forClass, adapter );
	}

	public static <T> void registerConverter( Class<T> forClass, Converter<T> converter )
	{
		CONVERTERS.put( forClass, converter );
	}

	//
	//
	// Private members
	//
	//

	//
	//
	// Constructor
	//
	//

	public SimpleBinding( GwtMetawidget metawidget )
	{
		super( metawidget );
	}

	//
	//
	// Public methods
	//
	//

	@Override
	public void bind( Widget widget, String... names )
	{
		Object toInspect = getMetawidget().getToInspect();

		if ( toInspect == null )
			return;

		// From the adapter...

		Class<?> classToBindTo = toInspect.getClass();
		@SuppressWarnings( "unchecked" )
		SimpleBindingAdapter<Object> adapter = (SimpleBindingAdapter<Object>) getAdapter( classToBindTo );

		if ( adapter == null )
			throw new RuntimeException( "Don't know how to bind to a " + classToBindTo );

		// ...fetch the value...

		Object value = adapter.getProperty( toInspect, names );

		// ...convert it (if necessary)...

		if ( value != null )
		{
			@SuppressWarnings( "unchecked" )
			Converter<Object> converter = (Converter<Object>) getConverter( value.getClass() );

			if ( converter != null )
				value = converter.convertForWidget( widget, value );
		}

		// ...and set it

		getMetawidget().setValue( value, widget );
	}

	@Override
	public void save()
	{
	}

	//
	//
	// Private methods
	//
	//

	private SimpleBindingAdapter<?> getAdapter( Class<?> classToBindTo )
	{
		Class<?> classTraversal = classToBindTo;

		while ( classTraversal != null )
		{
			SimpleBindingAdapter<?> adapter = ADAPTERS.get( classTraversal );

			if ( adapter != null )
				return adapter;

			classTraversal = classTraversal.getSuperclass();
		}

		return null;
	}

	private Converter<?> getConverter( Class<?> classToConvert )
	{
		Class<?> classTraversal = classToConvert;

		while ( classTraversal != null )
		{
			Converter<?> converter = CONVERTERS.get( classTraversal );

			if ( converter != null )
				return converter;

			classTraversal = classTraversal.getSuperclass();
		}

		return null;
	}
}
