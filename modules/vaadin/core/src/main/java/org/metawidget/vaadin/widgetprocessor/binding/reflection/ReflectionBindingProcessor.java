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

package org.metawidget.vaadin.widgetprocessor.binding.reflection;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

import org.metawidget.util.ClassUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.vaadin.Stub;
import org.metawidget.vaadin.VaadinMetawidget;
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
			throw WidgetProcessorException.newException( "ReflectionBindingProcessor only supports binding actions to AbstractButtons" );
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
