// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.swing.widgetprocessor.binding.reflection;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JComponent;

import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.widgetprocessor.iface.AdvancedWidgetProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

/**
 * Action binding implementation based on reflection.
 * <p>
 * This is the typical Swing approach to binding UI buttons to Java objects using
 * <code>AbstractActions</code> and reflection.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ReflectionBindingProcessor
	implements AdvancedWidgetProcessor<JComponent, SwingMetawidget> {

	//
	// Public methods
	//

	public void onStartBuild( SwingMetawidget metawidget ) {

		metawidget.putClientProperty( ReflectionBindingProcessor.class, null );
	}

	@SuppressWarnings( "serial" )
	public JComponent processWidget( JComponent component, String elementName, Map<String, String> attributes, SwingMetawidget metawidget ) {

		// Nested Metawidgets are not bound, only remembered

		if ( component instanceof SwingMetawidget ) {

			State state = getState( metawidget );

			if ( state.nestedMetawidgets == null ) {
				state.nestedMetawidgets = CollectionUtils.newHashSet();
			}

			state.nestedMetawidgets.add( (SwingMetawidget) component );
			return component;
		}

		// Only bind to non-read-only Actions

		if ( !ACTION.equals( elementName ) ) {
			return component;
		}

		if ( component instanceof Stub ) {
			return component;
		}

		if ( !( component instanceof AbstractButton ) ) {
			throw WidgetProcessorException.newException( "ReflectionBindingProcessor only supports binding actions to AbstractButtons" );
		}

		if ( WidgetBuilderUtils.isReadOnly( attributes ) ) {
			return component;
		}

		// Bind it

		AbstractButton button = (AbstractButton) component;
		BoundAction action = new BoundAction( button.getText(), metawidget.getToInspect(), metawidget.getPath(), attributes.get( NAME ) );
		button.setAction( action );

		// Save the binding

		State state = getState( metawidget );

		if ( state.actions == null ) {
			state.actions = CollectionUtils.newHashSet();
		}

		state.actions.add( action );
		return button;
	}

	public void onEndBuild( SwingMetawidget metawidget ) {

		// Do nothing
	}

	/**
	 * Rebinds the Metawidget to the given Object.
	 * <p>
	 * This method is an optimization that allows clients to load a new object into the binding
	 * <em>without</em> calling setToInspect, and therefore without reinspecting the object or
	 * recreating the components. It is the client's responsbility to ensure the rebound object is
	 * compatible with the original setToInspect.
	 */

	public void rebind( Object toRebind, SwingMetawidget metawidget ) {

		metawidget.updateToInspectWithoutInvalidate( toRebind );
		State state = getState( metawidget );

		// Our bindings

		if ( state.actions != null ) {
			for ( BoundAction action : state.actions ) {
				action.rebind( toRebind );
			}
		}

		// Nested Metawidgets

		if ( state.nestedMetawidgets != null ) {
			for ( SwingMetawidget nestedMetawidget : state.nestedMetawidgets ) {
				rebind( toRebind, nestedMetawidget );
			}
		}
	}

	//
	// Private methods
	//

	private State getState( SwingMetawidget metawidget ) {

		State state = (State) metawidget.getClientProperty( ReflectionBindingProcessor.class );

		if ( state == null ) {
			state = new State();
			metawidget.putClientProperty( ReflectionBindingProcessor.class, state );
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

		/* package private */Set<BoundAction>		actions;

		/* package private */Set<SwingMetawidget>	nestedMetawidgets;
	}

	static class BoundAction
		extends AbstractAction {

		//
		// Private members
		//

		private Object		mBindTo;

		private String[]	mNames;

		private Method		mAction;

		//
		// Constructor
		//

		public BoundAction( String actionName, Object bindTo, String path, String name ) {

			super( actionName );

			mBindTo = bindTo;

			if ( path == null ) {
				return;
			}

			mNames = PathUtils.parsePath( path ).getNamesAsArray();

			// Traverse to the last Object

			Object traverse = mBindTo;

			for ( String subName : mNames ) {

				if ( traverse == null ) {
					return;
				}

				traverse = ClassUtils.getProperty( traverse, subName );
			}

			try {
				// Parameterless methods

				mAction = traverse.getClass().getMethod( name, (Class[]) null );
			} catch ( NoSuchMethodException e1 ) {
				try {
					// ActionEvent-parameter based methods

					mAction = traverse.getClass().getMethod( name, ActionEvent.class );
				} catch ( NoSuchMethodException e2 ) {
					throw WidgetProcessorException.newException( e2 );
				}
			}
		}

		//
		// Public methods
		//

		public void rebind( Object toRebind ) {

			mBindTo = toRebind;
		}

		public void actionPerformed( ActionEvent event ) {

			String actionName = (String) getValue( Action.NAME );

			// Traverse to the last Object

			Object traverse = mBindTo;

			for ( String name : mNames ) {

				if ( traverse == null ) {
					return;
				}

				traverse = ClassUtils.getProperty( traverse, name );
			}

			try {
				if ( mAction.getParameterTypes().length == 0 ) {
					mAction.invoke( traverse, (Object[]) null );
				} else {
					mAction.invoke( traverse, new ActionEvent( mBindTo, 0, actionName ) );
				}
			} catch ( Exception e ) {
				throw WidgetProcessorException.newException( e );
			}
		}
	}
}
