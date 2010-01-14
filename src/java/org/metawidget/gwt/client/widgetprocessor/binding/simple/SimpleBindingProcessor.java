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

package org.metawidget.gwt.client.widgetprocessor.binding.simple;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.Stub;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.iface.AdvancedWidgetProcessor;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Simple, Generator-based property and action binding processor.
 *
 * @author Richard Kennard
 */

public class SimpleBindingProcessor
	implements AdvancedWidgetProcessor<Widget, GwtMetawidget>
{
	//
	// Private members
	//

	private final Map<Class<?>, SimpleBindingProcessorAdapter<?>>	mAdapters;

	private final Map<Class<?>, Converter<?>>						mConverters;

	//
	// Constructor
	//

	public SimpleBindingProcessor()
	{
		this( new SimpleBindingProcessorConfig() );
	}

	public SimpleBindingProcessor( SimpleBindingProcessorConfig config )
	{
		// Custom adapters

		if ( config.getAdapters() == null )
			mAdapters = null;
		else
			mAdapters = new HashMap<Class<?>, SimpleBindingProcessorAdapter<?>>( config.getAdapters() );

		// Default converters

		mConverters = new HashMap<Class<?>, Converter<?>>();

		Converter<?> simpleConverter = new SimpleConverter();

		@SuppressWarnings( "unchecked" )
		Converter<Boolean> booleanConverter = (Converter<Boolean>) simpleConverter;
		mConverters.put( Boolean.class, booleanConverter );

		@SuppressWarnings( "unchecked" )
		Converter<Character> characterConverter = (Converter<Character>) simpleConverter;
		mConverters.put( Character.class, characterConverter );

		@SuppressWarnings( "unchecked" )
		Converter<Number> numberConverter = (Converter<Number>) simpleConverter;
		mConverters.put( Number.class, numberConverter );

		// Custom converters

		if ( config.getConverters() != null )
			mConverters.putAll( config.getConverters() );
	}

	//
	// Public methods
	//

	@Override
	public void onStartBuild( GwtMetawidget metawidget )
	{
		metawidget.putClientProperty( SimpleBindingProcessor.class, null );
	}

	@Override
	public Widget processWidget( Widget widget, String elementName, Map<String, String> attributes, final GwtMetawidget metawidget )
	{
		// Nested Metawidgets are not bound, only remembered

		if ( widget instanceof GwtMetawidget )
		{
			State state = getState( metawidget );

			if ( state.nestedMetawidgets == null )
				state.nestedMetawidgets = new HashSet<GwtMetawidget>();

			state.nestedMetawidgets.add( (GwtMetawidget) widget );
			return widget;
		}

		// SimpleBindingProcessor doesn't bind to Stubs or FlexTables

		if ( widget instanceof Stub || widget instanceof FlexTable )
			return widget;

		String path = metawidget.getPath();

		if ( PROPERTY.equals( elementName ) || ACTION.equals( elementName ) )
			path += StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + attributes.get( NAME );

		final String[] names = PathUtils.parsePath( path ).getNamesAsArray();

		// Bind actions

		if ( ACTION.equals( elementName ) )
		{
			if ( !( widget instanceof FocusWidget ) )
				throw new RuntimeException( "SimpleBindingProcessor only supports binding actions to FocusWidgets - '" + attributes.get( NAME ) + "' is using a " + widget.getClass().getName() );

			// Bind the action

			FocusWidget focusWidget = (FocusWidget) widget;
			focusWidget.addClickHandler( new ClickHandler()
			{
				public void onClick( ClickEvent event )
				{
					// Use the adapter...

					Object toInvokeOn = metawidget.getToInspect();

					if ( toInvokeOn == null )
						return;

					Class<?> classToBindTo = toInvokeOn.getClass();
					SimpleBindingProcessorAdapter<Object> adapter = getAdapter( classToBindTo );

					if ( adapter == null )
						throw new RuntimeException( "Don't know how to bind to a " + classToBindTo );

					// ...to invoke the action

					adapter.invokeAction( toInvokeOn, names );
				}
			} );

			return widget;
		}

		// From the adapter...

		Object toInspect = metawidget.getToInspect();

		if ( toInspect == null )
			return widget;

		Class<?> classToBindTo = toInspect.getClass();
		SimpleBindingProcessorAdapter<Object> adapter = getAdapter( classToBindTo );

		if ( adapter == null )
			throw new RuntimeException( "Don't know how to bind to a " + classToBindTo );

		// ...fetch the value...

		Object value = adapter.getProperty( toInspect, names );

		// ...convert it (if necessary)...

		Class<?> propertyType = adapter.getPropertyType( toInspect, names );
		Converter<Object> converter = getConverter( propertyType );

		if ( converter != null )
			value = converter.convertForWidget( widget, value );

		// ...and set it

		try
		{
			metawidget.setValue( value, widget );

			if ( TRUE.equals( attributes.get( NO_SETTER ) ) )
				return widget;

			State state = getState( metawidget );

			if ( state.bindings == null )
				state.bindings = new HashSet<Object[]>();

			state.bindings.add( new Object[] { widget, names, converter, propertyType } );
		}
		catch ( Exception e )
		{
			Window.alert( path + ": " + e.getMessage() );
		}

		return widget;
	}

	/**
	 * Rebinds the Metawidget to the given Object.
	 * <p>
	 * This method is an optimization that allows clients to load a new object into the binding
	 * <em>without</em> calling setToInspect, and therefore without reinspecting the object or
	 * recreating the components. It is the client's responsbility to ensure the rebound object is
	 * compatible with the original setToInspect.
	 */

	public void rebind( Object toRebind, GwtMetawidget metawidget )
	{
		metawidget.updateToInspectWithoutInvalidate( toRebind );

		State state = getState( metawidget );

		// Our bindings

		if ( state.bindings != null )
		{
			// From the adapter...

			Class<?> classToRebind = toRebind.getClass();
			SimpleBindingProcessorAdapter<Object> adapter = getAdapter( classToRebind );

			if ( adapter == null )
				throw new RuntimeException( "Don't know how to rebind to a " + classToRebind );

			// ...for each bound property...

			for ( Object[] binding : state.bindings )
			{
				Widget widget = (Widget) binding[0];
				String[] names = (String[]) binding[1];
				@SuppressWarnings( "unchecked" )
				Converter<Object> converter = (Converter<Object>) binding[2];

				// ...fetch the value...

				Object value = adapter.getProperty( toRebind, names );

				// ...convert it (if necessary)...

				if ( converter != null )
					value = converter.convertForWidget( widget, value );

				// ...and set it

				metawidget.setValue( value, widget );
			}
		}

		// Nested bindings

		if ( state.nestedMetawidgets != null )
		{
			for ( GwtMetawidget nestedMetawidget : state.nestedMetawidgets )
			{
				rebind( toRebind, nestedMetawidget );
			}
		}
	}

	public void save( GwtMetawidget metawidget )
	{
		State state = getState( metawidget );

		// Our bindings

		if ( state.bindings != null )
		{
			Object toSave = metawidget.getToInspect();

			if ( toSave == null )
				return;

			// From the adapter...

			Class<?> classToBindTo = toSave.getClass();
			SimpleBindingProcessorAdapter<Object> adapter = getAdapter( classToBindTo );

			if ( adapter == null )
				throw new RuntimeException( "Don't know how to save to a " + classToBindTo );

			// ...for each bound property...

			for ( Object[] binding : state.bindings )
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

				adapter.setProperty( toSave, value, names );
			}
		}

		// Nested bindings

		if ( state.nestedMetawidgets != null )
		{
			for ( GwtMetawidget nestedMetawidget : state.nestedMetawidgets )
			{
				save( nestedMetawidget );
			}
		}
	}

	@Override
	public void onEndBuild( GwtMetawidget metawidget )
	{
		// Do nothing
	}

	//
	// Protected methods
	//

	/**
	 * Gets the Adapter for the given class (if any).
	 * <p>
	 * Includes traversing superclasses of the given Class for a suitable Converter, so for example
	 * registering an Adapter for <code>Contact.class</code> will match
	 * <code>PersonalContact.class</code>, <code>BusinessContact.class</code> etc., unless a more
	 * subclass-specific Adapter is also registered.
	 */

	protected <T extends SimpleBindingProcessorAdapter<?>> T getAdapter( Class<?> classToBindTo )
	{
		if ( mAdapters == null )
			return null;

		Class<?> classTraversal = classToBindTo;

		while ( classTraversal != null )
		{
			@SuppressWarnings( "unchecked" )
			T adapter = (T) mAdapters.get( classTraversal );

			if ( adapter != null )
				return adapter;

			classTraversal = classTraversal.getSuperclass();
		}

		return null;
	}

	//
	// Private methods
	//

	/**
	 * Gets the Converter for the given Class (if any).
	 * <p>
	 * Includes traversing superclasses of the given Class for a suitable Converter, so for example
	 * registering a Converter for <code>Number.class</code> will match <code>Integer.class</code>,
	 * <code>Double.class</code> etc., unless a more subclass-specific Converter is also registered.
	 */

	private <T extends Converter<?>> T getConverter( Class<?> classToConvert )
	{
		Class<?> classTraversal = classToConvert;

		while ( classTraversal != null )
		{
			@SuppressWarnings( "unchecked" )
			T converter = (T) mConverters.get( classTraversal );

			if ( converter != null )
				return converter;

			classTraversal = classTraversal.getSuperclass();
		}

		return null;
	}

	/* package private */State getState( GwtMetawidget metawidget )
	{
		State state = (State) metawidget.getClientProperty( SimpleBindingProcessor.class );

		if ( state == null )
		{
			state = new State();
			metawidget.putClientProperty( SimpleBindingProcessor.class, state );
		}

		return state;
	}

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	/* package private */static class State
	{
		/* package private */Set<Object[]>		bindings;

		/* package private */Set<GwtMetawidget>	nestedMetawidgets;
	}
}
