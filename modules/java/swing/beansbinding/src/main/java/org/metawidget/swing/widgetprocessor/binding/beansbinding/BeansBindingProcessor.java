// Metawidget (licensed under LGPL)
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

package org.metawidget.swing.widgetprocessor.binding.beansbinding;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Component;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Binding.SyncFailure;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Converter;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.widgetprocessor.binding.BindingConverter;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.simple.ObjectUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.iface.AdvancedWidgetProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

/**
 * Property binding implementation based on BeansBinding (JSR 295).
 * <p>
 * This implementation recognizes the following <code>SwingMetawidget.setParameter</code>
 * parameters:
 * <p>
 * <ul>
 * <li><code>UpdateStrategy.class</code> - as defined by
 * <code>org.jdesktop.beansbinding.AutoBinding.UpdateStrategy</code>. Defaults to
 * <code>READ_ONCE</code>. If set to <code>READ</code> or <code>READ_WRITE</code>, the object being
 * inspected must provide <code>PropertyChangeSupport</code>. If set to <code>READ</code>, there is
 * no need to call <code>BeansBindingProcessor.rebind</code>. If set to <code>READ_WRITE</code>,
 * there is no need to call <code>BeansBindingProcessor.save</code>.
 * </ul>
 * <p>
 * Note: <code>BeansBinding</code> does not bind <em>actions</em>, such as invoking a method when a
 * <code>JButton</code> is pressed. For that, see <code>ReflectionBindingProcessor</code> and
 * <code>MetawidgetActionStyle</code> or <code>SwingAppFrameworkActionStyle</code>.
 *
 * @author Richard Kennard
 */

public class BeansBindingProcessor
	implements AdvancedWidgetProcessor<JComponent, SwingMetawidget>, BindingConverter {

	//
	// Private members
	//

	private final UpdateStrategy							mUpdateStrategy;

	private final Map<ConvertFromTo<?, ?>, Converter<?, ?>>	mConverters	= CollectionUtils.newHashMap();

	//
	// Constructor
	//

	public BeansBindingProcessor() {

		this( new BeansBindingProcessorConfig() );
	}

	public BeansBindingProcessor( BeansBindingProcessorConfig config ) {

		mUpdateStrategy = config.getUpdateStrategy();

		// Default converters

		registerConverter( Byte.class, String.class, new NumberConverter<Byte>( Byte.class ) );
		registerConverter( Short.class, String.class, new NumberConverter<Short>( Short.class ) );
		registerConverter( Integer.class, String.class, new NumberConverter<Integer>( Integer.class ) );
		registerConverter( Long.class, String.class, new NumberConverter<Long>( Long.class ) );
		registerConverter( Float.class, String.class, new NumberConverter<Float>( Float.class ) );
		registerConverter( Double.class, String.class, new NumberConverter<Double>( Double.class ) );
		registerConverter( Boolean.class, String.class, new BooleanConverter() );

		// Custom converters (defensive copy)

		if ( config.getConverters() != null ) {
			mConverters.putAll( config.getConverters() );
		}
	}

	//
	// Public methods
	//

	public void onStartBuild( SwingMetawidget metawidget ) {

		State state = getState( metawidget );

		if ( state.bindings != null ) {
			for ( org.jdesktop.beansbinding.Binding<?, ?, ? extends Component, ?> binding : state.bindings ) {
				binding.unbind();
			}
		}

		metawidget.putClientProperty( BeansBindingProcessor.class, null );
	}

	public JComponent processWidget( JComponent component, String elementName, Map<String, String> attributes, SwingMetawidget metawidget ) {

		JComponent componentToBind = component;

		// Unwrap JScrollPanes (for JTextAreas etc)

		if ( componentToBind instanceof JScrollPane ) {
			componentToBind = (JComponent) ( (JScrollPane) componentToBind ).getViewport().getView();
		}

		// Nested Metawidgets are not bound, only remembered

		if ( componentToBind instanceof SwingMetawidget ) {

			State state = getState( metawidget );

			if ( state.nestedMetawidgets == null ) {
				state.nestedMetawidgets = CollectionUtils.newHashSet();
			}

			state.nestedMetawidgets.add( (SwingMetawidget) component );
			return component;
		}

		typesafeAdd( componentToBind, elementName, attributes, metawidget );

		return component;
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

		if ( state.bindings != null ) {
			for ( org.jdesktop.beansbinding.Binding<Object, ?, ? extends Component, ?> binding : state.bindings ) {
				binding.unbind();
				binding.setSourceObject( toRebind );
				binding = processBinding( binding, metawidget );

				if ( binding == null ) {
					continue;
				}

				binding.bind();

				SyncFailure failure = binding.refresh();

				if ( failure != null ) {
					throw WidgetProcessorException.newException( failure.getType().toString() );
				}
			}
		}

		// Nested Metawidgets

		if ( state.nestedMetawidgets != null ) {
			for ( SwingMetawidget nestedMetawidget : state.nestedMetawidgets ) {
				rebind( toRebind, nestedMetawidget );
			}
		}
	}

	public void save( SwingMetawidget metawidget ) {

		if ( UpdateStrategy.READ_WRITE.equals( mUpdateStrategy ) ) {
			throw WidgetProcessorException.newException( "Should not call save() when using " + UpdateStrategy.READ_WRITE );
		}

		State state = getState( metawidget );

		// Our bindings

		if ( state.bindings != null ) {
			for ( org.jdesktop.beansbinding.Binding<Object, ?, ? extends Component, ?> binding : state.bindings ) {
				Object sourceObject = binding.getSourceObject();
				@SuppressWarnings( "unchecked" )
				BeanProperty<Object, Object> sourceProperty = (BeanProperty<Object, Object>) binding.getSourceProperty();

				if ( !sourceProperty.isWriteable( sourceObject ) ) {
					continue;
				}

				if ( binding.getConverter() instanceof ReadOnlyToStringConverter<?> ) {
					continue;
				}

				try {
					SyncFailure failure = binding.save();

					if ( failure != null ) {
						throw WidgetProcessorException.newException( failure.getConversionException() );
					}
				} catch ( ClassCastException e ) {
					throw WidgetProcessorException.newException( "When saving from " + binding.getTargetObject().getClass() + " to " + sourceProperty + " (have you used BeansBindingProcessorConfig.setConverter?)", e );
				}
			}
		}

		// Nested Metawidgets

		if ( state.nestedMetawidgets != null ) {
			for ( SwingMetawidget nestedMetawidget : state.nestedMetawidgets ) {
				save( nestedMetawidget );
			}
		}
	}

	public Object convertFromString( String value, Class<?> expectedType ) {

		if ( String.class.equals( expectedType ) ) {
			return value;
		}

		// Try converters one way round...

		Converter<String, ?> converterFromString = getConverter( String.class, expectedType );

		if ( converterFromString != null ) {
			return converterFromString.convertForward( value );
		}

		// ...and the other...

		Converter<?, String> converterToString = getConverter( expectedType, String.class );

		if ( converterToString != null ) {
			return converterToString.convertReverse( value );
		}

		// ...or don't convert

		return value;
	}

	public void onEndBuild( SwingMetawidget metawidget ) {

		// Do nothing
	}

	//
	// Protected methods
	//

	/**
	 * Process the given Binding prior to calling <code>binding.bind()</code>. Source, target and
	 * converters will have already been initialized.
	 * <p>
	 * Clients can subclass this <code>WidgetProcessor</code> and override this method to manipulate
	 * the binding, for example to add a <code>BindingListener</code>.
	 *
	 * @param metawidget
	 *            the owning Metawidget. May be useful for clients
	 * @return the processed Binding, or null to abort binding this property
	 */

	protected <SS, SV, TS extends Component, TV> Binding<SS, SV, TS, TV> processBinding( Binding<SS, SV, TS, TV> binding, SwingMetawidget metawidget ) {

		return binding;
	}

	@SuppressWarnings( { "unchecked", "rawtypes" } )
	protected <SS, SV, TS extends Component, TV> Binding typesafeAdd( TS component, String elementName, Map<String, String> attributes, SwingMetawidget metawidget ) {

		String componentProperty = metawidget.getValueProperty( component );

		if ( componentProperty == null ) {
			return null;
		}

		// Source property

		SS source = (SS) metawidget.getToInspect();
		String sourceProperty = PathUtils.parsePath( metawidget.getPath() ).getNames().replace( StringUtils.SEPARATOR_FORWARD_SLASH_CHAR, StringUtils.SEPARATOR_DOT_CHAR );

		if ( PROPERTY.equals( elementName ) ) {
			if ( sourceProperty.length() > 0 ) {
				sourceProperty += StringUtils.SEPARATOR_DOT_CHAR;
			}

			sourceProperty += attributes.get( NAME );
		}

		BeanProperty<SS, SV> propertySource = BeanProperty.create( sourceProperty );

		Class<TV> targetClass;

		// Create binding

		BeanProperty<TS, TV> propertyTarget = BeanProperty.create( componentProperty );

		org.jdesktop.beansbinding.Binding<SS, SV, TS, TV> binding = Bindings.createAutoBinding( mUpdateStrategy, source, propertySource, component, propertyTarget );
		targetClass = (Class<TV>) propertyTarget.getWriteType( component );

		// Add a converter

		Converter<SV, TV> converter = getConverter( propertySource, source, targetClass, attributes );

		// Convenience converter for READ_ONLY fields (not just based on 'component instanceof
		// JLabel', because the user may override a DONT_EXPAND to be a non-editable JTextField)
		//
		// See https://sourceforge.net/projects/metawidget/forums/forum/747623/topic/3460563

		if ( converter == null && WidgetBuilderUtils.isReadOnly( attributes ) && targetClass.equals( String.class ) ) {
			converter = new ReadOnlyToStringConverter();
		}

		binding.setConverter( converter );
		binding = processBinding( binding, metawidget );

		if ( binding == null ) {
			return null;
		}

		// Bind it

		try {
			binding.bind();
		} catch ( ClassCastException e ) {
			throw WidgetProcessorException.newException( "When binding " + metawidget.getPath() + StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + sourceProperty + " to " + component.getClass() + "." + componentProperty + " (have you used BeansBindingProcessorConfig.setConverter?)", e );
		}

		// Save the binding

		State state = getState( metawidget );

		if ( state.bindings == null ) {
			state.bindings = CollectionUtils.newHashSet();
		}

		state.bindings.add( (org.jdesktop.beansbinding.Binding<Object, SV, TS, TV>) binding );

		return binding;
	}

	/**
	 * Get a converter for the given source class. Clients can override this method to provide
	 * customized converter implementations.
	 *
	 * @param attributes
	 *            the attributes of the given business property. May be useful if you want to
	 *            lookup, say, 'actual-class'
	 */

	@SuppressWarnings( "unchecked" )
	protected <SV, SS, TV> Converter<SV, TV> getConverter( BeanProperty<SS, SV> propertySource, SS source, Class<TV> targetClass, Map<String, String> attributes ) {

		// Determine sourceClass

		Class<SV> sourceClass = null;

		if ( propertySource.isWriteable( source ) ) {
			sourceClass = (Class<SV>) propertySource.getWriteType( source );
		} else if ( propertySource.isReadable( source ) ) {

			// BeansBinding does not allow us to lookup the type of a non-writable property

			SV value = propertySource.getValue( source );

			if ( value != null ) {
				sourceClass = (Class<SV>) value.getClass();
			} else {
				return null;
			}
		} else {
			throw WidgetProcessorException.newException( "Property '" + propertySource + "' has no getter and no setter (or parent is null)" );
		}

		return getConverter( sourceClass, targetClass );
	}

	protected State getState( SwingMetawidget metawidget ) {

		State state = (State) metawidget.getClientProperty( BeansBindingProcessor.class );

		if ( state == null ) {
			state = new State();
			metawidget.putClientProperty( BeansBindingProcessor.class, state );
		}

		return state;
	}

	//
	// Private methods
	//

	private <S, T> void registerConverter( Class<S> source, Class<T> target, Converter<S, T> converter ) {

		mConverters.put( new ConvertFromTo<S, T>( source, target ), converter );
	}

	/**
	 * Gets the Converter for the given Class (if any).
	 */

	@SuppressWarnings( "unchecked" )
	private <SV, TV> Converter<SV, TV> getConverter( Class<SV> sourceClass, Class<TV> targetClass ) {

		Class<SV> sourceClassTraversal = sourceClass;
		Class<TV> targetClassTraversal = targetClass;

		if ( sourceClassTraversal.isPrimitive() ) {
			sourceClassTraversal = (Class<SV>) ClassUtils.getWrapperClass( sourceClassTraversal );
		}

		if ( targetClassTraversal.isPrimitive() ) {
			targetClassTraversal = (Class<TV>) ClassUtils.getWrapperClass( targetClassTraversal );
		}

		while ( sourceClassTraversal != null ) {
			Converter<SV, TV> converter = (Converter<SV, TV>) mConverters.get( new ConvertFromTo<SV, TV>( sourceClassTraversal, targetClassTraversal ) );

			if ( converter != null ) {
				return converter;
			}

			sourceClassTraversal = (Class<SV>) sourceClassTraversal.getSuperclass();
		}

		return null;
	}

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	protected static class State {

		public Set<org.jdesktop.beansbinding.Binding<Object, ?, ? extends Component, ?>>	bindings;

		public Set<SwingMetawidget>															nestedMetawidgets;
	}

	/* package private */static final class ConvertFromTo<S, T> {

		//
		// Private members
		//

		private Class<S>	mSource;

		private Class<T>	mTarget;

		//
		// Constructor
		//

		public ConvertFromTo( Class<S> source, Class<T> target ) {

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

			if ( that == null ) {
				return false;
			}

			if ( getClass() != that.getClass() ) {
				return false;
			}

			if ( !ObjectUtils.nullSafeEquals( mSource, ( (ConvertFromTo<?, ?>) that ).mSource ) ) {
				return false;
			}

			if ( !ObjectUtils.nullSafeEquals( mTarget, ( (ConvertFromTo<?, ?>) that ).mTarget ) ) {
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
