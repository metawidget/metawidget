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
import java.util.Set;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.internal.databinding.BindingStatus;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.widgetprocessor.binding.BindingConverter;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.ObjectUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.iface.AdvancedWidgetProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

/**
 * Property binding implementation based on <code>eclipse.core.databinding</code>.
 * <p>
 * This implementation does <em>not</em> require JFace. JFace is separate from
 * <code>eclipse.core.databinding</code>, as discussed here
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=153630.
 * <p>
 * <p>
 * Note: <code>eclipse.core.databinding</code> does not bind <em>actions</em>, such as invoking a
 * method when a <code>Button</code> is pressed. For that, see
 * <code>ReflectionBindingProcessor</code> and <code>MetawidgetActionStyle</code>.
 *
 * @author Richard Kennard
 */

public class DataBindingProcessor
	implements AdvancedWidgetProcessor<Control, SwtMetawidget>, BindingConverter {

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

	public DataBindingProcessor() {

		this( new DataBindingProcessorConfig() );
	}

	public DataBindingProcessor( DataBindingProcessorConfig config ) {

		// Register converters

		IConverter[] converters = config.getConverters();

		if ( converters != null ) {
			for ( IConverter converter : converters ) {
				mConverters.put( new ConvertFromTo( (Class<?>) converter.getFromType(), (Class<?>) converter.getToType() ), converter );
			}
		}
	}

	//
	// Public methods
	//

	public void onStartBuild( SwtMetawidget metawidget ) {

		metawidget.setData( DataBindingProcessor.class.getName(), null );
	}

	public Control processWidget( Control control, String elementName, Map<String, String> attributes, SwtMetawidget metawidget ) {

		if ( ACTION.equals( elementName ) ) {
			return control;
		}

		// Nested Metawidgets are not bound, only remembered

		if ( control instanceof SwtMetawidget ) {

			State state = getState( metawidget );

			if ( state.nestedMetawidgets == null ) {
				state.nestedMetawidgets = CollectionUtils.newHashSet();
			}

			state.nestedMetawidgets.add( (SwtMetawidget) control );
			return control;
		}

		String controlProperty = metawidget.getValueProperty( control );

		if ( controlProperty == null ) {
			return control;
		}

		// Observe the control

		State state = getState( metawidget );
		Realm realm = state.bindingContext.getValidationRealm();
		IObservableValue observeTarget = BeanProperties.value( controlProperty ).observe( realm, control );
		UpdateValueStrategy targetToModel;

		// (NO_SETTER model values are one-way only)

		if ( TRUE.equals( attributes.get( NO_SETTER ) ) ) {
			targetToModel = new UpdateValueStrategy( UpdateValueStrategy.POLICY_NEVER );
		} else {
			targetToModel = new UpdateValueStrategy( UpdateValueStrategy.POLICY_ON_REQUEST );
		}

		// Observe the model

		Object toInspect = metawidget.getToInspect();
		String propertyName = PathUtils.parsePath( metawidget.getInspectionPath() ).getNames().replace( StringUtils.SEPARATOR_FORWARD_SLASH_CHAR, StringUtils.SEPARATOR_DOT_CHAR );

		if ( PROPERTY.equals( elementName ) ) {
			if ( propertyName.length() > 0 ) {
				propertyName += StringUtils.SEPARATOR_DOT_CHAR;
			}

			propertyName += attributes.get( NAME );
		}

		// (use PojoObservables so that the model needn't implement PropertyChangeListener)

		IObservableValue observeModel = PojoObservables.observeValue( realm, toInspect, propertyName );
		UpdateValueStrategy modelToTarget = new UpdateValueStrategy( UpdateValueStrategy.POLICY_ON_REQUEST );

		// Add converters

		targetToModel.setConverter( getConverter( (Class<?>) observeTarget.getValueType(), (Class<?>) observeModel.getValueType() ) );
		modelToTarget.setConverter( getConverter( (Class<?>) observeModel.getValueType(), (Class<?>) observeTarget.getValueType() ) );

		// Bind it

		state.bindingContext.bindValue( observeTarget, observeModel, targetToModel, modelToTarget );

		return control;
	}

	public Object convertFromString( String value, Class<?> expectedType ) {

		// TODO: ???

		return value;
	}

	public void onEndBuild( SwtMetawidget metawidget ) {

		State state = getState( metawidget );
		state.bindingContext.updateTargets();
	}

	public void save( final SwtMetawidget metawidget ) {

		// Our bindings

		State state = getState( metawidget );
		state.bindingContext.updateModels();

		for ( Object validationStatusProvider : state.bindingContext.getValidationStatusProviders() ) {
			Binding binding = (Binding) validationStatusProvider;
			BindingStatus bindingStatus = (BindingStatus) binding.getValidationStatus().getValue();

			if ( bindingStatus.isOK() ) {
				continue;
			}

			throw WidgetProcessorException.newException( bindingStatus.getException() );
		}

		// Nested Metawidgets

		if ( state.nestedMetawidgets != null ) {
			for ( SwtMetawidget nestedMetawidget : state.nestedMetawidgets ) {
				save( nestedMetawidget );
			}
		}
	}

	//
	// Private methods
	//

	private State getState( SwtMetawidget metawidget ) {

		State state = (State) metawidget.getData( DataBindingProcessor.class.getName() );

		if ( state == null ) {
			state = new State();
			state.bindingContext = new DataBindingContext( getRealm( metawidget.getDisplay() ) );

			metawidget.setData( DataBindingProcessor.class.getName(), state );

		}

		return state;
	}

	/**
	 * From org.eclipse.jface.databinding.swt.SWTObservables (EPLv1)
	 */

	private Realm getRealm( final Display display ) {

		synchronized ( mRealms ) {
			for ( DisplayRealm realm : mRealms ) {
				if ( realm.mDisplay == display ) {
					return realm;
				}
			}
			DisplayRealm realm = new DisplayRealm( display );
			mRealms.add( realm );
			return realm;
		}
	}

	/**
	 * Gets the IConverter for the given Class (if any).
	 * <p>
	 * Includes traversing superclasses of the given <code>sourceClass</code> for a suitable
	 * IConverter, so for example registering a IConverter for <code>Number.class</code> will match
	 * <code>Integer.class</code>, <code>Double.class</code> etc., unless a more subclass-specific
	 * IConverter is also registered.
	 */

	private IConverter getConverter( Class<?> sourceClass, Class<?> targetClass ) {

		Class<?> sourceClassTraversal = sourceClass;

		while ( sourceClassTraversal != null ) {
			IConverter converter = mConverters.get( new ConvertFromTo( sourceClassTraversal, targetClass ) );

			if ( converter != null ) {
				return converter;
			}

			sourceClassTraversal = sourceClassTraversal.getSuperclass();
		}

		return null;
	}

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	/* package private */static class State {

		/* package private */DataBindingContext	bindingContext;

		/* package private */Set<SwtMetawidget>	nestedMetawidgets;
	}

	/**
	 * From org.eclipse.jface.databinding.swt.SWTObservables (EPLv1)
	 */

	static class DisplayRealm
		extends Realm {

		//
		// Private members
		//

		Display	mDisplay;

		//
		// Constructor
		//

		DisplayRealm( Display display ) {

			mDisplay = display;
		}

		//
		// Public methods
		//

		@Override
		public boolean isCurrent() {

			return Display.getCurrent() == mDisplay;
		}

		// Do not override equals/hashCode, we are not going to be comparing this or hashing it
	}

	/* package private */static final class ConvertFromTo {

		//
		// Private members
		//

		private Class<?>	mSource;

		private Class<?>	mTarget;

		//
		// Constructor
		//

		public ConvertFromTo( Class<?> source, Class<?> target ) {

			mSource = source;
			mTarget = target;
		}

		//
		// Public methods
		//

		@Override
		public boolean equals( Object that ) {

			if ( this == that ) {
				return true;
			}

			if ( !ObjectUtils.nullSafeClassEquals( this, that ) ) {
				return false;
			}

			if ( !ObjectUtils.nullSafeEquals( mSource, ( (ConvertFromTo) that ).mSource ) ) {
				return false;
			}

			if ( !ObjectUtils.nullSafeEquals( mTarget, ( (ConvertFromTo) that ).mTarget ) ) {
				return false;
			}

			return true;
		}

		@Override
		public int hashCode() {

			int hashCode = 1;
			hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mSource.hashCode() );
			hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mTarget.hashCode() );

			return hashCode;
		}
	}
}
