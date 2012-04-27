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

package org.metawidget.vaadin.widgetprocessor.binding.simple;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.vaadin.VaadinMetawidget;
import org.metawidget.vaadin.widgetprocessor.binding.BindingConverter;
import org.metawidget.widgetprocessor.iface.AdvancedWidgetProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Component;

/**
 * Simple, Generator-based property and action binding processor.
 *
 * @author Richard Kennard
 */

public class SimpleBindingProcessor
	implements AdvancedWidgetProcessor<Component, VaadinMetawidget>, BindingConverter {

	//
	// Private members
	//

	private final Map<Class<?>, Converter<?>>	mConverters;

	//
	// Constructor
	//

	public SimpleBindingProcessor() {

		this( new SimpleBindingProcessorConfig() );
	}

	public SimpleBindingProcessor( SimpleBindingProcessorConfig config ) {

		mConverters = CollectionUtils.newWeakHashMap();

		// Default converters

		Converter<?> simpleConverter = new SimpleConverter();

		mConverters.put( char.class, simpleConverter );
		mConverters.put( Boolean.class, simpleConverter );
		mConverters.put( Character.class, simpleConverter );
		mConverters.put( Number.class, simpleConverter );

		// Custom converters

		if ( config.getConverters() != null ) {
			mConverters.putAll( config.getConverters() );
		}
	}

	//
	// Public methods
	//

	public void onStartBuild( VaadinMetawidget metawidget ) {

		metawidget.putClientProperty( SimpleBindingProcessor.class, null );
	}

	public Component processWidget( Component component, String elementName, Map<String, String> attributes, final VaadinMetawidget metawidget ) {

		// Nested metawidgets are not bound, only remembered

		if ( component instanceof VaadinMetawidget ) {
			State state = getState( metawidget );

			if ( state.nestedMetawidgets == null ) {
				state.nestedMetawidgets = new HashSet<VaadinMetawidget>();
			}

			state.nestedMetawidgets.add( (VaadinMetawidget) component );
			return component;
		}

		// SimpleBindingProcessor only binds to Property components (TextFields, Labels, etc)

		if ( !( component instanceof Property ) ) {
			return component;
		}

		// (use TYPE, not ACTUAL_TYPE, because an Enum with a value will get a type of Enum$1)

		String type = attributes.get( TYPE );

		if ( type == null ) {
			return component;
		}

		// Lookup the propertyType

		Class<?> propertyType = ClassUtils.niceForName( type );

		// ...fetch the value...

		Object value = metawidget.getToInspect();

		if ( value == null ) {
			return component;
		}

		String path = metawidget.getPath();

		if ( PROPERTY.equals( elementName ) || ACTION.equals( elementName ) ) {
			path += StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + attributes.get( NAME );
		}

		String[] names = PathUtils.parsePath( path ).getNamesAsArray();

		for ( String name : names ) {
			value = ClassUtils.getProperty( value, name );
		}

		// ...convert it (if necessary)...

		@SuppressWarnings( "unchecked" )
		final Converter<Object> converter = (Converter<Object>) getConverter( propertyType );

		// ...and set it

		try {
			if ( propertyType.isPrimitive() ) {
				propertyType = ClassUtils.getWrapperClass( propertyType );
			}

			@SuppressWarnings( "unchecked" )
			final Class<Object> withPropertyType = (Class<Object>) propertyType;

			Property property = new ObjectProperty<Object>( value, withPropertyType ) {

				@Override
				public void setValue( Object newValue )
					throws ReadOnlyException, ConversionException {

					Object convertedValue = newValue;

					if ( converter != null && convertedValue instanceof String ) {
						convertedValue = converter.convertFromString( (String) convertedValue, withPropertyType );
					}

					// (stop null Strings coming out as "null")

					if ( convertedValue == null && String.class.equals( withPropertyType ) ) {
						convertedValue = "";
					}

					super.setValue( convertedValue );
				}
			};
			( (Property.Viewer) component ).setPropertyDataSource( property );

			if ( TRUE.equals( attributes.get( NO_SETTER ) ) ) {
				return component;
			}

			State state = getState( metawidget );

			if ( state.bindings == null ) {
				state.bindings = new HashSet<Object[]>();
			}

			state.bindings.add( new Object[] { property, names } );
		} catch ( Exception e ) {
			throw WidgetProcessorException.newException( e );
		}

		return component;
	}

	public void save( VaadinMetawidget metawidget ) {

		State state = getState( metawidget );

		// Our bindings

		if ( state.bindings != null ) {
			Object toSave = metawidget.getToInspect();

			if ( toSave == null ) {
				return;
			}

			// For each bound property...

			for ( Object[] binding : state.bindings ) {
				Property property = (Property) binding[0];
				String[] names = (String[]) binding[1];

				// ...fetch the value...

				Object value = property.getValue();

				// (convert "" Strings back to null)

				if ( "".equals( value ) ) {
					value = null;
				}

				// ...and set it

				Object parent = toSave;
				int length = names.length;

				for ( int loop = 0; loop < length - 1; loop++ ) {
					parent = ClassUtils.getProperty( parent, names[loop] );

					if ( parent == null ) {
						return;
					}
				}

				ClassUtils.setProperty( parent, names[length - 1], value );
			}
		}

		// Nested metawidgets

		if ( state.nestedMetawidgets != null ) {
			for ( VaadinMetawidget nestedmetawidget : state.nestedMetawidgets ) {
				save( nestedmetawidget );
			}
		}
	}

	public void onEndBuild( VaadinMetawidget metawidget ) {

		// Do nothing
	}

	@SuppressWarnings( "unchecked" )
	public Object convertFromString( String value, Class<?> expectedType ) {

		Converter<Object> converter = (Converter<Object>) getConverter( expectedType );

		if ( converter != null ) {
			return converter.convertFromString( value, (Class<Object>) expectedType );
		}

		return value;
	}

	//
	// Private methods
	//

	/**
	 * Gets the Converter for the given Class (if any).
	 * <p>
	 * Includes traversing superclasses of the given <code>classToConvert</code> for a suitable
	 * Converter, so for example registering a Converter for <code>Number.class</code> will match
	 * <code>Integer.class</code>, <code>Double.class</code> etc., unless a more subclass-specific
	 * Converter is also registered.
	 */

	private Converter<?> getConverter( Class<?> classToConvert ) {

		Class<?> classTraversal = classToConvert;

		while ( classTraversal != null ) {
			Converter<?> converter = mConverters.get( classTraversal );

			if ( converter != null ) {
				return converter;
			}

			classTraversal = classTraversal.getSuperclass();
		}

		return null;
	}

	/* package private */State getState( VaadinMetawidget metawidget ) {

		State state = (State) metawidget.getClientProperty( SimpleBindingProcessor.class );

		if ( state == null ) {
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

	/* package private */static class State {

		/* package private */Set<Object[]>			bindings;

		/* package private */Set<VaadinMetawidget>	nestedMetawidgets;
	}
}
