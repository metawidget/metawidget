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

package org.metawidget.swing.actionbinding.reflection;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;

import org.metawidget.MetawidgetException;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.actionbinding.ActionBindingImpl;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.simple.PathUtils;

/**
 * Action binding implementation based on reflection.
 * <p>
 * This is the typical Swing approach to binding UI buttons to Java objects using
 * <code>AbstractActions</code> and reflection.
 * <p>
 * Implementations need not be Thread-safe.
 *
 * @author Richard Kennard
 */

public class ReflectionBinding
	extends ActionBindingImpl
{
	//
	// Constructor
	//

	public ReflectionBinding( SwingMetawidget metawidget )
	{
		super( metawidget );
	}

	//
	// Public methods
	//

	@SuppressWarnings( "serial" )
	public void bind( Component component, Map<String, String> attributes, String path )
	{
		if ( !( component instanceof AbstractButton ))
			throw MetawidgetException.newException( "ReflectionBinding only supports binding actions to AbstractButtons" );

		AbstractButton button = (AbstractButton) component;

		Object toInspect = getMetawidget().getToInspect();

		if ( toInspect == null )
			return;

		// Traverse to the last Object...

		String[] names = PathUtils.parsePath( path ).getNamesAsArray();
		int last = names.length - 1;

		for( int loop = 0; loop < last; loop++ )
		{
			toInspect = ClassUtils.getProperty( toInspect, names[loop] );

			if ( toInspect == null )
				return;
		}

		// ...and wire it up

		final Object fireActionOn = toInspect;
		final Class<?> fireActionOnClass = fireActionOn.getClass();
		final String actionName = names[last];

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
					throw MetawidgetException.newException( exception2 );
				}
			}
		} );
	}
}
