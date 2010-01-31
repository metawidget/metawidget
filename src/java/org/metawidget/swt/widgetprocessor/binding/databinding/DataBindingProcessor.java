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

package org.metawidget.swt.widgetprocessor.binding.databinding;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.util.simple.PathUtils.TypeAndNames;
import org.metawidget.widgetprocessor.iface.AdvancedWidgetProcessor;

/**
 * Property binding implementation based on <code>eclipse.core.databinding</code>.
 * <p>
 * This implementation does <em>not</em> require JFace. JFace is separate from
 * <code>eclipse.core.databinding</code>, as discussed here
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=153630.
 *
 * @author Richard Kennard
 */

public class DataBindingProcessor
	implements AdvancedWidgetProcessor<Control, SwtMetawidget>
{
	//
	// Public methods
	//

	@Override
	public void onStartBuild( SwtMetawidget metawidget )
	{
		Realm realm = getRealm( metawidget.getDisplay() );
		metawidget.setData( DataBindingProcessor.class.getName(), new DataBindingContext( realm ) );
	}

	@Override
	public Control processWidget( Control control, String elementName, Map<String, String> attributes, SwtMetawidget metawidget )
	{
		String controlProperty = metawidget.getValueProperty( control );

		if ( controlProperty == null )
			return control;

		// Observe the control

		DataBindingContext bindingContext = (DataBindingContext) metawidget.getData( DataBindingProcessor.class.getName() );
		Realm realm = bindingContext.getValidationRealm();
		IObservableValue observeControl = BeanProperties.value( controlProperty ).observe( realm, control );

		// Observe the model

		Object toInspect = metawidget.getToInspect();
		String propertyName = attributes.get( NAME );

		TypeAndNames typeAndNames = PathUtils.parsePath( metawidget.getInspectionPath() );
		if ( typeAndNames.getNamesAsArray().length > 0 )
			propertyName = typeAndNames.getNames().replace( StringUtils.SEPARATOR_FORWARD_SLASH_CHAR, StringUtils.SEPARATOR_DOT_CHAR ) + StringUtils.SEPARATOR_DOT_CHAR + propertyName;

		IObservableValue observeModel = BeanProperties.value( propertyName ).observe( realm, toInspect );

		// Bind it

		UpdateValueStrategy targetToModel = new UpdateValueStrategy( UpdateValueStrategy.POLICY_ON_REQUEST );
		UpdateValueStrategy modelToTarget = new UpdateValueStrategy( UpdateValueStrategy.POLICY_ON_REQUEST );
		bindingContext.bindValue( observeControl, observeModel, targetToModel, modelToTarget );

		return control;
	}

	@Override
	public void onEndBuild( SwtMetawidget metawidget )
	{
		DataBindingContext bindingContext = (DataBindingContext) metawidget.getData( DataBindingProcessor.class.getName() );
		bindingContext.updateTargets();
	}

	public void save( final SwtMetawidget metawidget )
	{
		DataBindingContext bindingContext = (DataBindingContext) metawidget.getData( DataBindingProcessor.class.getName() );
		bindingContext.updateModels();
	}

	//
	// Private methods
	//

	private List<DisplayRealm>	mRealms	= CollectionUtils.newArrayList();

	private Realm getRealm( final Display display )
	{
		synchronized ( mRealms )
		{
			for ( DisplayRealm realm : mRealms )
			{
				if ( realm.mDisplay == display )
					return realm;
			}
			DisplayRealm realm = new DisplayRealm( display );
			mRealms.add( realm );
			return realm;
		}
	}

	//
	// Inner class
	//

	private static class DisplayRealm
		extends Realm
	{
		//
		// Private members
		//

		/* package private */Display	mDisplay;

		//
		// Constructor
		//

		/* package private */ DisplayRealm( Display display )
		{
			mDisplay = display;
		}

		//
		// Public methods
		//

		@Override
		public boolean isCurrent()
		{
			return Display.getCurrent() == mDisplay;
		}

		@Override
		public void asyncExec( final Runnable runnable )
		{
			Runnable safeRunnable = new Runnable()
			{
				public void run()
				{
					safeRun( runnable );
				}
			};
			if ( !mDisplay.isDisposed() )
			{
				mDisplay.asyncExec( safeRunnable );
			}
		}

		@Override
		public void timerExec( int milliseconds, final Runnable runnable )
		{
			if ( !mDisplay.isDisposed() )
			{
				Runnable safeRunnable = new Runnable()
				{
					public void run()
					{
						safeRun( runnable );
					}
				};
				mDisplay.timerExec( milliseconds, safeRunnable );
			}
		}

		@Override
		public int hashCode()
		{
			return ( mDisplay == null ) ? 0 : mDisplay.hashCode();
		}

		@Override
		public boolean equals( Object obj )
		{
			if ( this == obj )
				return true;
			if ( obj == null )
				return false;
			if ( getClass() != obj.getClass() )
				return false;
			final DisplayRealm other = (DisplayRealm) obj;
			if ( mDisplay == null )
			{
				if ( other.mDisplay != null )
					return false;
			}
			else if ( !mDisplay.equals( other.mDisplay ) )
				return false;
			return true;
		}
	}
}
