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
import static org.metawidget.inspector.propertytype.PropertyTypeInspectionResultConstants.*;

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
import com.vaadin.ui.Component;

/**
 * Simple property binding processor.
 *
 * @author Richard Kennard
 */

public class SimpleBindingProcessor
	implements AdvancedWidgetProcessor<Component, VaadinMetawidget>, BindingConverter {

	//
	// Private members
	//

	private final Map<ConvertFromTo, Converter<?, ?>>	mConverters	= CollectionUtils.newHashMap();

	//
	// Constructor
	//

	public SimpleBindingProcessor() {

		this( new SimpleBindingProcessorConfig() );
	}

	public SimpleBindingProcessor( SimpleBindingProcessorConfig config ) {

		// Default converters

		mConverters.put( new ConvertFromTo( String.class, Object.class ), new FromStringConverter() );
		mConverters.put( new ConvertFromTo( Object.class, String.class ), new ToStringConverter() );
		mConverters.put( new ConvertFromTo( Number.class, Number.class ), new NumberConverter() );

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

	public Component processWidget( final Component component, String elementName, Map<String, String> attributes, final VaadinMetawidget metawidget ) {

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

		// Ignore WRITE_ONLY (at least for now)

		if ( TRUE.equals( attributes.get( NO_GETTER ) ) ) {
			return component;
		}

		// (use TYPE, not ACTUAL_TYPE, because an Enum with a value will get a type of Enum$1)

		String type = attributes.get( TYPE );

		if ( type == null ) {
			return component;
		}

		// Lookup the propertyType

		Class<?> toInspectPropertyType = ClassUtils.niceForName( type );

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

		// ...and set it

		try {

			// (make long into Long, so that it matches during ObjectProperty.setValue)

			if ( toInspectPropertyType.isPrimitive() ) {
				toInspectPropertyType = ClassUtils.getWrapperClass( toInspectPropertyType );
			}

			// We *always* go via a Converter, as this is the simplest way to handle cases like:
			//
			// String: null -> "" -> null

			Property property = (Property) component;
			Class<?> componentPropertyType = property.getType();
			@SuppressWarnings( "unchecked" )
			Converter<Object, Object> setValueConverter = (Converter<Object, Object>) getConverter( toInspectPropertyType, componentPropertyType );

			if ( setValueConverter != null ) {
				value = setValueConverter.convert( value, componentPropertyType );
			}

			boolean readOnly = property.isReadOnly();
			if ( readOnly ) {
				property.setReadOnly( false );
			}

			// Note: we tried doing this via property.setPropertyDataSource, but that seems
			// incorrect because the component uses getValue/setValue internally and
			// it's not expecting getValue to return a type of toInspectPropertyType

			property.setValue( value );
			if ( readOnly ) {
				property.setReadOnly( true );
			}

			if ( TRUE.equals( attributes.get( NO_SETTER ) ) ) {
				return component;
			}

			State state = getState( metawidget );

			if ( state.bindings == null ) {
				state.bindings = new HashSet<Object[]>();
			}

			state.bindings.add( new Object[] { property, names, toInspectPropertyType } );
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
				Class<?> toInspectPropertyType = (Class<?>) binding[2];

				// ...fetch the value...

				Object value = property.getValue();

				// ...convert it if necessary...

				@SuppressWarnings( "unchecked" )
				Converter<Object, Object> getValueConverter = (Converter<Object, Object>) getConverter( property.getType(), toInspectPropertyType );

				if ( getValueConverter != null ) {
					value = getValueConverter.convert( value, toInspectPropertyType );
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

	@SuppressWarnings( { "unchecked", "rawtypes" } )
	public Object convertFromString( String value, Class<?> expectedType ) {

		if ( String.class.equals( expectedType ) ) {
			return value;
		}

		Converter<String, ?> converterFromString = getConverter( String.class, expectedType );

		if ( converterFromString != null ) {
			return converterFromString.convert( value, (Class) expectedType );
		}

		return value;
	}

	//
	// Private methods
	//

	/* package private */State getState( VaadinMetawidget metawidget ) {

		State state = (State) metawidget.getClientProperty( SimpleBindingProcessor.class );

		if ( state == null ) {
			state = new State();
			metawidget.putClientProperty( SimpleBindingProcessor.class, state );
		}

		return state;
	}

	/**
	 * Gets the Converter for the given Class (if any).
	 * <p>
	 * Includes traversing superclasses of the given <code>sourceClass</code> for a suitable
	 * Converter, so for example registering a Converter for <code>Number.class</code> will match
	 * <code>Integer.class</code>, <code>Double.class</code> etc., unless a more subclass-specific
	 * Converter is also registered.
	 */

	private <F, T> Converter<F, T> getConverter( Class<F> sourceClass, Class<T> targetClass ) {

		// Try target...

		Class<?> targetClassTraversal = targetClass;

		if ( targetClassTraversal.isPrimitive() ) {
			targetClassTraversal = ClassUtils.getWrapperClass( targetClassTraversal );
		}

		while ( targetClassTraversal != null ) {

			// ...then, within that, source

			Class<?> sourceClassTraversal = sourceClass;

			if ( sourceClassTraversal.isPrimitive() ) {
				sourceClassTraversal = ClassUtils.getWrapperClass( sourceClassTraversal );
			}

			while ( sourceClassTraversal != null ) {
				@SuppressWarnings( "unchecked" )
				Converter<F, T> converter = (Converter<F, T>) mConverters.get( new ConvertFromTo( sourceClassTraversal, targetClassTraversal ) );

				if ( converter != null ) {
					return converter;
				}

				sourceClassTraversal = sourceClassTraversal.getSuperclass();
			}

			targetClassTraversal = targetClassTraversal.getSuperclass();
		}

		return null;
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
