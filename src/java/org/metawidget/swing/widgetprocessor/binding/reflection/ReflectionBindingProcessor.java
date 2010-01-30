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

package org.metawidget.swing.widgetprocessor.binding.reflection;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JComponent;

import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

/**
 * Action binding implementation based on reflection.
 * <p>
 * This is the typical Swing approach to binding UI buttons to Java objects using
 * <code>AbstractActions</code> and reflection.
 *
 * @author Richard Kennard
 */

public class ReflectionBindingProcessor
	implements WidgetProcessor<JComponent,SwingMetawidget>
{
	//
	// Public methods
	//

	@Override
	@SuppressWarnings( "serial" )
	public JComponent processWidget( JComponent component, String elementName, Map<String, String> attributes, SwingMetawidget metawidget )
	{
		// Only bind to Actions

		if ( !ACTION.equals( elementName ))
			return component;

		if ( component instanceof Stub )
			return component;

		if ( !( component instanceof AbstractButton ))
			throw WidgetProcessorException.newException( "ReflectionBinding only supports binding actions to AbstractButtons" );

		if ( metawidget == null )
			return component;

		Object toInspect = metawidget.getToInspect();

		if ( toInspect == null )
			return component;

		AbstractButton button = (AbstractButton) component;

		// Traverse to the last Object...

		String[] names = PathUtils.parsePath( metawidget.getPath() ).getNamesAsArray();

		for( String name : names )
		{
			toInspect = ClassUtils.getProperty( toInspect, name );

			if ( toInspect == null )
				return component;
		}

		// ...and wire it up

		final Object fireActionOn = toInspect;
		final Class<?> fireActionOnClass = fireActionOn.getClass();
		final String actionName = attributes.get( NAME );

		button.setAction( new AbstractAction( button.getText() )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				try
				{
					try
					{
						// Parameterless methods

						final Method parameterlessActionMethod = fireActionOnClass.getMethod( actionName, (Class[]) null );
						parameterlessActionMethod.invoke( fireActionOn, (Object[]) null );
					}
					catch ( NoSuchMethodException exception1 )
					{
						// ActionEvent-parameter based methods

						final Method parameterizedActionMethod = fireActionOnClass.getMethod( actionName, ActionEvent.class );
						parameterizedActionMethod.invoke( fireActionOn, new ActionEvent( fireActionOn, 0, actionName ) );
					}
				}
				catch ( Exception exception2 )
				{
					throw WidgetProcessorException.newException( exception2 );
				}
			}
		} );

		return component;
	}
}
