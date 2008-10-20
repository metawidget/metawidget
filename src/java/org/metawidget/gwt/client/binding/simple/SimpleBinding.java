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

import static org.metawidget.util.simple.StringUtils.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.metawidget.gwt.client.binding.Binding;
import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.GwtUtils;
import org.metawidget.gwt.client.ui.Stub;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * Simple, Generator-based two-way binding implementation.
 *
 * @author Richard Kennard
 */

public class SimpleBinding
	extends Binding
{
	//
	// Private statics
	//

	private final static Map<Class<?>, SimpleBindingAdapter<?>>	ADAPTERS	= new HashMap<Class<?>, SimpleBindingAdapter<?>>();

	private final static Map<Class<?>, Converter<?>>			CONVERTERS	= new HashMap<Class<?>, Converter<?>>();

	static
	{
		// Register default converters

		Converter<?> simpleConverter = new SimpleConverter();

		@SuppressWarnings( "unchecked" )
		Converter<Boolean> booleanConverter = (Converter<Boolean>) simpleConverter;
		registerConverter( Boolean.class, booleanConverter );

		@SuppressWarnings( "unchecked" )
		Converter<Character> characterConverter = (Converter<Character>) simpleConverter;
		registerConverter( Character.class, characterConverter );

		@SuppressWarnings( "unchecked" )
		Converter<Number> numberConverter = (Converter<Number>) simpleConverter;
		registerConverter( Number.class, numberConverter );
	}

	//
	// Public statics
	//

	/**
	 * Registers the given SimpleBindingAdapter for the given Class.
	 * <p>
	 * Adapters also apply to subclasses of the given Class. So for example registering an Adapter
	 * for <code>Contact.class</code> will match <code>PersonalContact.class</code>,
	 * <code>BusinessContact.class</code> etc., unless a more subclass-specific Adapter is also
	 * registered
	 */

	public static <T> void registerAdapter( Class<T> forClass, SimpleBindingAdapter<T> adapter )
	{
		ADAPTERS.put( forClass, adapter );
	}

	/**
	 * Registers the given Converter for the given Class.
	 * <p>
	 * Converters also apply to subclasses of the given Class. So for example registering a
	 * Converter for <code>Number.class</code> will match <code>Integer.class</code>,
	 * <code>Double.class</code> etc., unless a more subclass-specific Converter is also
	 * registered.
	 */

	public static <T> void registerConverter( Class<T> forClass, Converter<T> converter )
	{
		CONVERTERS.put( forClass, converter );
	}

	//
	// Private members
	//

	private Set<Object[]>	mBindings;

	//
	// Constructor
	//

	public SimpleBinding( GwtMetawidget metawidget )
	{
		super( metawidget );
	}

	//
	// Public methods
	//

	@Override
	public void bind( Widget widget, Map<String, String> attributes, String... names )
	{
		// SimpleBinding doesn't bind to Stubs or FlexTables

		if ( widget instanceof Stub || widget instanceof FlexTable )
			return;

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

		Class<?> propertyType = adapter.getPropertyType( toInspect, names );

		@SuppressWarnings( "unchecked" )
		Converter<Object> converter = (Converter<Object>) getConverter( propertyType );

		if ( converter != null )
			value = converter.convertForWidget( widget, value );

		// ...and set it

		try
		{
			getMetawidget().setValue( value, widget );

			if ( adapter.isPropertyReadOnly( toInspect, names ) )
				return;

			if ( mBindings == null )
				mBindings = new HashSet<Object[]>();

			mBindings.add( new Object[] { widget, names, converter, propertyType } );
		}
		catch ( Exception e )
		{
			Window.alert( GwtUtils.toString( names, SEPARATOR_DOT_CHAR ) + ": " + e.getMessage() );
		}
	}

	@Override
	public void rebind()
	{
		if ( mBindings == null )
			return;

		GwtMetawidget metawidget = getMetawidget();
		Object toInspect = metawidget.getToInspect();

		if ( toInspect == null )
			return;

		// From the adapter...

		Class<?> classToBindTo = toInspect.getClass();
		@SuppressWarnings( "unchecked" )
		SimpleBindingAdapter<Object> adapter = (SimpleBindingAdapter<Object>) getAdapter( classToBindTo );

		if ( adapter == null )
			throw new RuntimeException( "Don't know how to rebind to a " + classToBindTo );

		// ...for each bound property...

		for ( Object[] binding : mBindings )
		{
			Widget widget = (Widget) binding[0];
			String[] names = (String[]) binding[1];
			@SuppressWarnings( "unchecked" )
			Converter<Object> converter = (Converter<Object>) binding[2];

			// ...fetch the value...

			Object value = adapter.getProperty( toInspect, names );

			// ...convert it (if necessary)...

			if ( converter != null )
				value = converter.convertForWidget( widget, value );

			// ...and set it

			metawidget.setValue( value, widget );
		}
	}

	@Override
	public void save()
	{
		if ( mBindings == null )
			return;

		Object toInspect = getMetawidget().getToInspect();

		if ( toInspect == null )
			return;

		// From the adapter...

		Class<?> classToBindTo = toInspect.getClass();
		@SuppressWarnings( "unchecked" )
		SimpleBindingAdapter<Object> adapter = (SimpleBindingAdapter<Object>) getAdapter( classToBindTo );

		if ( adapter == null )
			throw new RuntimeException( "Don't know how to save to a " + classToBindTo );

		GwtMetawidget metawidget = getMetawidget();

		// ...for each bound property...

		for ( Object[] binding : mBindings )
		{
			Widget widget = (Widget) binding[0];
			String[] names = (String[]) binding[1];
			@SuppressWarnings( "unchecked" )
			Converter<Object> converter = (Converter<Object>) binding[2];
			Class<?> type = (Class<?>) binding[3];

			// ...fetch the value...

			Object value = metawidget.getValue( widget );

			// ...convert it (if necessary)...

			if ( value != null && converter != null )
				value = converter.convertFromWidget( widget, value, type );

			// ...and set it

			adapter.setProperty( toInspect, value, names );
		}
	}

	//
	// Private methods
	//

	/**
	 * Gets the Adapter for the given class (if any).
	 * <p>
	 * Includes traversing superclasses of the given Class for a suitable Converter, so for example
	 * registering an Adapter for <code>Contact.class</code> will match
	 * <code>PersonalContact.class</code>, <code>BusinessContact.class</code> etc., unless a
	 * more subclass-specific Adapter is also registered.
	 */

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

	/**
	 * Gets the Converter for the given Class (if any).
	 * <p>
	 * Includes traversing superclasses of the given Class for a suitable Converter, so for example
	 * registering a Converter for <code>Number.class</code> will match <code>Integer.class</code>,
	 * <code>Double.class</code> etc., unless a more subclass-specific Converter is also
	 * registered.
	 */

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
