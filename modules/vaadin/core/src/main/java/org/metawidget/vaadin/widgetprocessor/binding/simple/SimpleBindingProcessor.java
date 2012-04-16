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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.vaadin.VaadinMetawidget;
import org.metawidget.vaadin.widgetprocessor.binding.BindingConverter;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

import com.vaadin.data.Buffered;
import com.vaadin.data.Property.Viewer;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * Property binding implementation.
 * Note: <code>SimpleBindingProcessor</code> does not bind <em>actions</em>, such as invoking a
 * method when a <code>Button</code> is pressed. For that, see
 * <code>ReflectionBindingProcessor</code>
 *
 * @author Loghman Barari
 */

public class SimpleBindingProcessor
	implements WidgetProcessor<Component, VaadinMetawidget>, BindingConverter {

	//
	// Private members
	//

	private final Map<Class<?>, Converter<?>>	mConverters	= CollectionUtils.newHashMap();

	//
	// Constructor
	//

	public SimpleBindingProcessor() {

		this( new SimpleBindingProcessorConfig() );
	}

	public SimpleBindingProcessor( SimpleBindingProcessorConfig config ) {

		// Default converters

		registerConverter( Character.class, new CharacterConverter() );

		// Custom converters

		if ( config.getConverters() != null ) {
			mConverters.putAll( config.getConverters() );
		}
	}

	//
	// Public methods
	//

	public Component processWidget( Component component, String elementName, Map<String, String> attributes, VaadinMetawidget metawidget ) {

		if ( !PROPERTY.equals( elementName ) ) {
			return component;
		}

		if ( !( component instanceof Viewer ) ) {
			return component;
		}

		if ( !( attributes.containsKey( TYPE ) ) ) {
			return component;
		}

		Property property;

		String propertyName = attributes.get( NAME );

		Object toInspect = metawidget.getToInspect();

		// Traverse to the last Object...

		String[] names = PathUtils.parsePath( metawidget.getPath() ).getNamesAsArray();

		for ( String name : names ) {
			toInspect = ClassUtils.getProperty( toInspect, name );

			if ( toInspect == null ) {
				return component;
			}
		}

		try {
			Field field = toInspect.getClass().getField( propertyName );
			property = new FieldProperty( toInspect, field );
		} catch ( Exception e ) {
			property = new MethodProperty( toInspect, propertyName );
		}

		String lookup = attributes.get( LOOKUP );
		if ( ( lookup != null ) && ( !"".equals( lookup ) ) && ( component instanceof Label ) ) {
			( (Label) component ).setValue( property.getValue() );
		} else {
			property.setConvertor( this.getConverter( property.getType() ) );
			( (Viewer) component ).setPropertyDataSource( property );
		}

		component.requestRepaint();

		return component;
	}

	public void save( VaadinMetawidget metawidget ) {

		save( (AbstractComponentContainer) metawidget );
	}

	public Object convertFromString( String value, Class<?> expectedType ) {

		// Try converters one way round...

		Converter<?> converter = getConverter( expectedType );

		if ( converter != null ) {
			return converter.convert( value );
		}
		Constructor<?> constructor;
		try {
			constructor = expectedType.getConstructor( new Class[] { String.class } );

			// Creates new object from the string
			return constructor.newInstance( new Object[] { value.toString() } );

		} catch ( Exception e ) {
			// e.printStackTrace();
		}

		// ...or don't convert

		return value;
	}

	//
	// Private methods
	//

	private <T> void registerConverter( Class<T> clazz, Converter<T> converter ) {

		mConverters.put( clazz, converter );
	}

	/**
	 * Gets the Converter for the given Class (if any).
	 */

	@SuppressWarnings( "unchecked" )
	private <T> Converter<T> getConverter( Class<T> classType ) {

		if ( classType.isPrimitive() ) {
			classType = (Class<T>) ClassUtils.getWrapperClass( classType );
		}

		while ( classType != null ) {
			Converter<T> converter = (Converter<T>) mConverters.get( classType );

			if ( converter != null ) {
				return converter;
			}

			classType = (Class<T>) classType.getSuperclass();
		}

		return null;
	}

	private void save( AbstractComponentContainer componentContainer ) {

		Iterator<Component> iterator = componentContainer.getComponentIterator();

		while ( iterator.hasNext() ) {

			Component component = iterator.next();

			if ( component instanceof AbstractComponentContainer ) {

				// Nested Metawidgets

				save( (AbstractComponentContainer) component );
			} else if ( component instanceof Buffered ) {

				( (Buffered) component ).commit();
			}
		}
	}
}
