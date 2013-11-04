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

package org.metawidget.android.widget.widgetprocessor.reflection;

import static org.metawidget.inspector.InspectionResultConstants.ACTION;
import static org.metawidget.inspector.InspectionResultConstants.NAME;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.android.widget.Stub;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.widgetprocessor.iface.AdvancedWidgetProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * Action binding implementation based on reflection.
 * <p>
 * This is the typical Android approach to binding UI buttons to Java objects using reflection.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

// TODO: HTML DisabledAttributeProcessor

public class ReflectionBindingProcessor
	implements AdvancedWidgetProcessor<View, AndroidMetawidget> {

	//
	// Public methods
	//

	public void onStartBuild( AndroidMetawidget metawidget ) {

		metawidget.putClientProperty( ReflectionBindingProcessor.class, null );
	}

	public View processWidget( View view, String elementName, Map<String, String> attributes, AndroidMetawidget metawidget ) {

		// Nested Metawidgets are not bound, only remembered

		if ( view instanceof AndroidMetawidget ) {

			State state = getState( metawidget );

			if ( state.nestedMetawidgets == null ) {
				state.nestedMetawidgets = CollectionUtils.newHashSet();
			}

			state.nestedMetawidgets.add( (AndroidMetawidget) view );
			return view;
		}

		// Only bind to non-read-only Actions

		if ( !ACTION.equals( elementName ) ) {
			return view;
		}

		if ( view instanceof Stub ) {
			return view;
		}

		if ( WidgetBuilderUtils.isReadOnly( attributes ) ) {
			return view;
		}

		// Bind it

		BoundAction action = new BoundAction( metawidget.getToInspect(), metawidget.getPath(), attributes.get( NAME ) );
		view.setOnClickListener( action );

		// Save the binding

		State state = getState( metawidget );

		if ( state.actions == null ) {
			state.actions = CollectionUtils.newHashSet();
		}

		state.actions.add( action );
		return view;
	}

	public void onEndBuild( AndroidMetawidget metawidget ) {

		// Do nothing
	}

	//
	// Private methods
	//

	private State getState( AndroidMetawidget metawidget ) {

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

		/* package private */Set<AndroidMetawidget>	nestedMetawidgets;
	}

	static class BoundAction
		implements OnClickListener {

		//
		// Private members
		//

		private Object		mBindTo;

		private String[]	mNames;

		private Method		mAction;

		//
		// Constructor
		//

		public BoundAction( Object bindTo, String path, String name ) {

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
				mAction = traverse.getClass().getMethod( name, (Class[]) null );
			} catch ( Exception e ) {
				throw WidgetProcessorException.newException( e );
			}
		}

		//
		// Public methods
		//

		public void onClick( View viewClicked ) {

			Object traverse = mBindTo;

			for ( String name : mNames ) {

				if ( traverse == null ) {
					return;
				}

				traverse = ClassUtils.getProperty( traverse, name );
			}

			try {
				mAction.invoke( traverse, (Object[]) null );
			} catch ( Exception e ) {
				throw WidgetProcessorException.newException( e );
			}
		}
	}
}
