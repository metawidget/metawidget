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

package org.metawidget.swt.widgetprocessor.binding.reflection;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.lang.reflect.Method;
import java.util.Map;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.metawidget.swt.Stub;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

/**
 * Action binding implementation based on reflection.
 * <p>
 * This is the typical SWT approach to binding UI buttons to Java objects using
 * <code>AbstractActions</code> and reflection.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ReflectionBindingProcessor
	implements WidgetProcessor<Control, SwtMetawidget> {

	//
	// Public methods
	//

	public Control processWidget( Control component, String elementName, Map<String, String> attributes, SwtMetawidget metawidget ) {

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

		String[] names = PathUtils.parsePath( metawidget.getInspectionPath() ).getNamesAsArray();

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

		button.addSelectionListener( new SelectionAdapter() {

			@Override
			public void widgetSelected( SelectionEvent e ) {

				try {
					final Method parameterlessActionMethod = fireActionOnClass.getMethod( actionName, (Class[]) null );
					parameterlessActionMethod.invoke( fireActionOn, (Object[]) null );
				} catch ( Exception exception ) {
					throw WidgetProcessorException.newException( exception );
				}
			}
		} );

		return component;
	}
}
