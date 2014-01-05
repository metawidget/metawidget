// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.android.widget.widgetprocessor.binding.simple;

import static org.metawidget.inspector.InspectionResultConstants.ACTION;
import static org.metawidget.inspector.InspectionResultConstants.NAME;
import static org.metawidget.inspector.InspectionResultConstants.PROPERTY;
import static org.metawidget.inspector.InspectionResultConstants.TRUE;
import static org.metawidget.inspector.InspectionResultConstants.TYPE;
import static org.metawidget.inspector.propertytype.PropertyTypeInspectionResultConstants.NO_GETTER;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.android.widget.widgetprocessor.binding.BindingConverter;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.iface.AdvancedWidgetProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Simple property binding processor.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SimpleBindingProcessor
	implements AdvancedWidgetProcessor<View, AndroidMetawidget>, BindingConverter {

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

		// Default converters

		mConverters = CollectionUtils.newWeakHashMap();

		Converter<?> simpleConverter = new SimpleConverter();
		mConverters.put( Boolean.class, simpleConverter );
		mConverters.put( Character.class, simpleConverter );
		mConverters.put( Number.class, simpleConverter );
		mConverters.put( byte.class, simpleConverter );
		mConverters.put( short.class, simpleConverter );
		mConverters.put( int.class, simpleConverter );
		mConverters.put( long.class, simpleConverter );
		mConverters.put( float.class, simpleConverter );
		mConverters.put( double.class, simpleConverter );
		mConverters.put( boolean.class, simpleConverter );
		mConverters.put( char.class, simpleConverter );

		// Custom converters

		if ( config.getConverters() != null ) {
			mConverters.putAll( config.getConverters() );
		}
	}

	//
	// Public methods
	//

	public void onStartBuild( AndroidMetawidget metawidget ) {

		metawidget.putClientProperty( SimpleBindingProcessor.class, null );
	}

	public View processWidget( final View view, String elementName, Map<String, String> attributes, final AndroidMetawidget metawidget ) {

		// Nested metawidgets are not bound, only remembered

		if ( view instanceof AndroidMetawidget ) {
			State state = getState( metawidget );

			if ( state.nestedMetawidgets == null ) {
				state.nestedMetawidgets = CollectionUtils.newHashSet();
			}

			state.nestedMetawidgets.add( (AndroidMetawidget) view );
			return view;
		}

		// Ignore WRITE_ONLY (at least for now)

		if ( TRUE.equals( attributes.get( NO_GETTER ) ) ) {
			return view;
		}

		// (use TYPE, not ACTUAL_TYPE, because an Enum with a value will get a type of Enum$1)

		String type = attributes.get( TYPE );

		if ( type == null ) {
			return view;
		}

		// SimpleBindingProcessor only binds to simple components

		if ( !( view instanceof CheckBox ) && !( view instanceof TextView ) && !( view instanceof DatePicker ) && !( view instanceof Spinner ) ) {
			return view;
		}

		// Fetch the value...

		Object value = metawidget.getToInspect();

		if ( value == null ) {
			return view;
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

		Class<?> propertyType = ClassUtils.niceForName( type );
		Converter<Object> converter = getConverter( propertyType );

		if ( converter != null ) {
			value = converter.convertForView( view, value );
		}

		// ...and set it

		try {

			metawidget.setValue( value, view );

			// If read-only, can be no save()

			if ( WidgetBuilderUtils.isReadOnly( attributes ) ) {
				return view;
			}

			State state = getState( metawidget );

			if ( state.bindings == null ) {
				state.bindings = new HashSet<Object[]>();
			}

			state.bindings.add( new Object[] { view, names, converter, propertyType } );
		} catch ( Exception e ) {
			throw WidgetProcessorException.newException( e );
		}

		return view;
	}

	public void save( AndroidMetawidget metawidget ) {

		State state = getState( metawidget );

		// Our bindings

		if ( state.bindings != null ) {
			Object toSave = metawidget.getToInspect();

			if ( toSave == null ) {
				return;
			}

			// For each bound property...

			for ( Object[] binding : state.bindings ) {
				View view = (View) binding[0];
				String[] names = (String[]) binding[1];
				@SuppressWarnings( "unchecked" )
				Converter<Object> converter = (Converter<Object>) binding[2];
				Class<?> propertyType = (Class<?>) binding[3];

				// ...fetch the value...

				Object value = metawidget.getValue( view );

				// ...convert it (if necessary)...

				if ( converter != null ) {
					value = converter.convertFromView( view, value, propertyType );
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
			for ( AndroidMetawidget nestedMetawidget : state.nestedMetawidgets ) {
				save( nestedMetawidget );
			}
		}
	}

	public void onEndBuild( AndroidMetawidget metawidget ) {

		// Do nothing
	}

	public Object convertFromString( String value, Class<?> expectedType ) {

		Converter<?> converterFromString = getConverter( expectedType );

		if ( converterFromString != null ) {
			return converterFromString.convertFromView( null, value, expectedType );
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

	private <T extends Converter<?>> T getConverter( Class<?> classToConvert ) {

		Class<?> classTraversal = classToConvert;

		while ( classTraversal != null ) {
			@SuppressWarnings( "unchecked" )
			T converter = (T) mConverters.get( classTraversal );

			if ( converter != null ) {
				return converter;
			}

			classTraversal = classTraversal.getSuperclass();
		}

		return null;
	}

	/* package private */State getState( AndroidMetawidget metawidget ) {

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

		/* package private */Set<AndroidMetawidget>	nestedMetawidgets;
	}
}
