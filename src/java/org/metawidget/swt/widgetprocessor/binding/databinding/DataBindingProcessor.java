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
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.widgetprocessor.binding.BindingConverter;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.ObjectUtils;
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
	implements AdvancedWidgetProcessor<Control, SwtMetawidget>, BindingConverter
{
	//
	// Private members
	//

	/**
	 * From org.eclipse.jface.databinding.swt.SWTObservables (EPLv1)
	 */

	private List<DisplayRealm>						mRealms		= CollectionUtils.newArrayList();

	private final Map<ConvertFromTo, IConverter>	mConverters	= CollectionUtils.newHashMap();

	//
	// Constructor
	//

	public DataBindingProcessor()
	{
		this( new DataBindingProcessorConfig() );
	}

	public DataBindingProcessor( DataBindingProcessorConfig config )
	{
		// Register converters

		IConverter[] converters = config.getConverters();

		if ( converters != null )
		{
			for( IConverter converter : converters )
			{
				mConverters.put( new ConvertFromTo( (Class<?>) converter.getFromType(), (Class<?>) converter.getToType() ), converter );
			}
		}
	}

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
		IObservableValue observeTarget = BeanProperties.value( controlProperty ).observe( realm, control );
		UpdateValueStrategy targetToModel = new UpdateValueStrategy( UpdateValueStrategy.POLICY_ON_REQUEST );

		// Observe the model

		Object toInspect = metawidget.getToInspect();
		String propertyName = attributes.get( NAME );

		TypeAndNames typeAndNames = PathUtils.parsePath( metawidget.getInspectionPath() );
		if ( typeAndNames.getNamesAsArray().length > 0 )
			propertyName = typeAndNames.getNames().replace( StringUtils.SEPARATOR_FORWARD_SLASH_CHAR, StringUtils.SEPARATOR_DOT_CHAR ) + StringUtils.SEPARATOR_DOT_CHAR + propertyName;

		// (use PojoObservables so that the model needn't implement PropertyChangeListener)

		IObservableValue observeModel = PojoObservables.observeValue( realm, toInspect, propertyName );
		UpdateValueStrategy modelToTarget = new UpdateValueStrategy( UpdateValueStrategy.POLICY_ON_REQUEST );

		// Add converters

		targetToModel.setConverter( getConverter( (Class<?>) observeTarget.getValueType(), (Class<?>) observeModel.getValueType() ));
		modelToTarget.setConverter( getConverter( (Class<?>) observeModel.getValueType(), (Class<?>) observeTarget.getValueType() ));

		// Bind it

		bindingContext.bindValue( observeTarget, observeModel, targetToModel, modelToTarget );

		return control;
	}

	@Override
	public Object convertFromString( String value, Class<?> expectedType )
	{
		return value;
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

	/**
	 * From org.eclipse.jface.databinding.swt.SWTObservables (EPLv1)
	 */

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

	/**
	 * Gets the IConverter for the given Class (if any).
	 * <p>
	 * Includes traversing superclasses of the given Class for a suitable IConverter, so for example
	 * registering a IConverter for <code>Number.class</code> will match <code>Integer.class</code>,
	 * <code>Double.class</code> etc., unless a more subclass-specific IConverter is also registered.
	 */

	private IConverter getConverter( Class<?> sourceClass, Class<?> targetClass )
	{
		Class<?> sourceClassTraversal = sourceClass;

		while ( sourceClassTraversal != null )
		{
			IConverter converter = mConverters.get( new ConvertFromTo( sourceClassTraversal, targetClass ) );

			if ( converter != null )
				return converter;

			sourceClassTraversal = sourceClassTraversal.getSuperclass();
		}

		return null;
	}

	//
	// Inner class
	//

	/**
	 * From org.eclipse.jface.databinding.swt.SWTObservables (EPLv1)
	 */

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

		/* package private */DisplayRealm( Display display )
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
				@SuppressWarnings( "synthetic-access" )
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
					@SuppressWarnings( "synthetic-access" )
					public void run()
					{
						safeRun( runnable );
					}
				};
				mDisplay.timerExec( milliseconds, safeRunnable );
			}
		}

		@Override
		public boolean equals( Object that )
		{
			if ( this == that )
				return true;

			if ( that == null )
				return false;

			if ( getClass() != that.getClass() )
				return false;

			if ( !ObjectUtils.nullSafeEquals( mDisplay, ( (DisplayRealm) that ).mDisplay ) )
				return false;

			return true;
		}

		@Override
		public int hashCode()
		{
			int hashCode = 1;
			hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mDisplay );

			return hashCode;
		}
	}

	/* package private */final static class ConvertFromTo
	{
		//
		// Private members
		//

		private Class<?>	mSource;

		private Class<?>	mTarget;

		//
		// Constructor
		//

		public ConvertFromTo( Class<?> source, Class<?> target )
		{
			mSource = source;
			mTarget = target;
		}

		//
		// Public methods
		//

		@Override
		public boolean equals( Object that )
		{
			if ( this == that )
				return true;

			if ( that == null )
				return false;

			if ( getClass() != that.getClass() )
				return false;

			if ( !ObjectUtils.nullSafeEquals( mSource, ( (ConvertFromTo) that ).mSource ) )
				return false;

			if ( !ObjectUtils.nullSafeEquals( mTarget, ( (ConvertFromTo) that ).mTarget ) )
				return false;

			return true;
		}

		@Override
		public int hashCode()
		{
			int hashCode = 1;
			hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mSource.hashCode() );
			hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mTarget.hashCode() );

			return hashCode;
		}
	}
}
