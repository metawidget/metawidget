// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.vaadin.ui.widgetprocessor.binding.reflection;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

import org.metawidget.util.ClassUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.vaadin.ui.Stub;
import org.metawidget.vaadin.ui.VaadinMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;

/**
 * Action binding implementation based on reflection.
 *
 * @author Loghman Barari
 */

@SuppressWarnings( "serial" )
public class ReflectionBindingProcessor
	implements WidgetProcessor<Component, VaadinMetawidget>, Serializable {

	//
	// Public methods
	//

	public Component processWidget( final Component component, String elementName, Map<String, String> attributes, VaadinMetawidget metawidget ) {

		// Only bind to non-read-only Actions

		if ( !ACTION.equals( elementName ) ) {
			return component;
		}

		if ( component instanceof Stub ) {
			return component;
		}

		if ( !( component instanceof Button ) ) {
			throw WidgetProcessorException.newException( "ReflectionBindingProcessor only supports binding actions to Buttons" );
		}

		if ( WidgetBuilderUtils.isReadOnly( attributes ) ) {
			return component;
		}

		if ( metawidget == null ) {
			return component;
		}

		Object toInspect = metawidget.getToInspect();

		if ( toInspect == null ) {
			return component;
		}

		Button button = (Button) component;

		// Traverse to the last Object...

		String[] names = PathUtils.parsePath( metawidget.getPath() ).getNamesAsArray();

		for ( String name : names ) {
			toInspect = ClassUtils.getProperty( toInspect, name );

			if ( toInspect == null ) {
				return component;
			}
		}

		// ...and wire it up

		final Object fireActionOn = toInspect;
		final Class<?> fireActionOnClass = fireActionOn.getClass();
		final String actionName = attributes.get( NAME );

		button.addListener( new ClickListener() {

			public void buttonClick( ClickEvent event ) {

				try {
					Method method = fireActionOnClass.getMethod( actionName, (Class[]) null );
					method.invoke( fireActionOn, (Object[]) null );
				} catch ( Exception e ) {
					throw WidgetProcessorException.newException( e );
				}
			}
		} );

		return component;
	}
}
