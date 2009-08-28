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

package org.metawidget.swing.widgetprocessor.binding.beansbinding;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Component;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.Binding.SyncFailure;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.widgetprocessor.binding.BindingConverter;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.impl.BaseWidgetProcessor;

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
 * no need to call <code>SwingMetawidget.rebind</code>. If set to <code>READ_WRITE</code>, there is
 * no need to call <code>SwingMetawidget.save</code>.
 * </ul>
 *
 * @author Richard Kennard
 */

public class BeansBindingProcessor
	extends BaseWidgetProcessor<JComponent, SwingMetawidget>
	implements BindingConverter
{
	//
	// Private statics
	//

	private final static Map<ConvertFromTo<?, ?>, Converter<?, ?>>	CONVERTERS	= Collections.synchronizedMap( new HashMap<ConvertFromTo<?, ?>, Converter<?, ?>>() );

	static
	{
		// Register default converters

		registerConverter( Byte.class, String.class, new NumberConverter<Byte>( Byte.class ) );
		registerConverter( Short.class, String.class, new NumberConverter<Short>( Short.class ) );
		registerConverter( Integer.class, String.class, new NumberConverter<Integer>( Integer.class ) );
		registerConverter( Long.class, String.class, new NumberConverter<Long>( Long.class ) );
		registerConverter( Float.class, String.class, new NumberConverter<Float>( Float.class ) );
		registerConverter( Double.class, String.class, new NumberConverter<Double>( Double.class ) );
		registerConverter( Boolean.class, String.class, new BooleanConverter() );
	}

	//
	// Public statics
	//

	public static <S, T> void registerConverter( Class<S> source, Class<T> target, Converter<S, T> converter )
	{
		CONVERTERS.put( new ConvertFromTo<S, T>( source, target ), converter );
	}

	public static <S, T> void unregisterConverter( Class<S> source, Class<T> target )
	{
		CONVERTERS.remove( new ConvertFromTo<S, T>( source, target ) );
	}

	//
	// Public methods
	//

	@Override
	public void onStartBuild( SwingMetawidget metawidget )
	{
		@SuppressWarnings( "unchecked" )
		Set<org.jdesktop.beansbinding.Binding<Object, ?, ? extends Component, ?>> bindings = (Set<org.jdesktop.beansbinding.Binding<Object, ?, ? extends Component, ?>>) metawidget.getClientProperty( BeansBindingProcessor.class );

		if ( bindings == null )
			return;

		for ( org.jdesktop.beansbinding.Binding<?, ?, ? extends Component, ?> binding : bindings )
		{
			binding.unbind();
		}

		metawidget.putClientProperty( BeansBindingProcessor.class, null );
	}

	@Override
	public void onAdd( JComponent component, Map<String, String> attributes, SwingMetawidget metawidget )
	{
		// Unwrap JScrollPanes (for JTextAreas etc)

		JComponent componentToBind = component;

		if ( componentToBind instanceof JScrollPane )
			componentToBind = (JComponent) ((JScrollPane) componentToBind).getViewport().getView();

		typesafeAdd( componentToBind, attributes, metawidget );
	}

	public void rebind( SwingMetawidget metawidget, Object toRebind )
	{
		// Our bindings

		@SuppressWarnings( "unchecked" )
		Set<org.jdesktop.beansbinding.Binding<Object, ?, ? extends Component, ?>> bindings = (Set<org.jdesktop.beansbinding.Binding<Object, ?, ? extends Component, ?>>) metawidget.getClientProperty( BeansBindingProcessor.class );

		if ( bindings != null )
		{
			Object sourceObject = toRebind;

			for ( org.jdesktop.beansbinding.Binding<Object, ?, ? extends Component, ?> binding : bindings )
			{
				binding.unbind();
				binding.setSourceObject( sourceObject );
				binding.bind();

				SyncFailure failure = binding.refresh();

				if ( failure != null )
					throw MetawidgetException.newException( failure.getType().toString() );
			}
		}

		// Nested bindings

		for ( Component component : metawidget.getComponents() )
		{
			if ( component instanceof SwingMetawidget )
				rebind( (SwingMetawidget) component, toRebind );
		}
	}

	public void save( SwingMetawidget metawidget )
	{
		// Our bindings

		@SuppressWarnings( "unchecked" )
		Set<org.jdesktop.beansbinding.Binding<Object, ?, ? extends Component, ?>> bindings = (Set<org.jdesktop.beansbinding.Binding<Object, ?, ? extends Component, ?>>) metawidget.getClientProperty( BeansBindingProcessor.class );

		if ( bindings != null )
		{
			for ( org.jdesktop.beansbinding.Binding<Object, ?, ? extends Component, ?> binding : bindings )
			{
				Object sourceObject = binding.getSourceObject();
				@SuppressWarnings( "unchecked" )
				BeanProperty<Object, Object> sourceProperty = (BeanProperty<Object, Object>) binding.getSourceProperty();

				if ( !sourceProperty.isWriteable( sourceObject ) )
					continue;

				if ( binding.getConverter() instanceof ReadOnlyToStringConverter )
					continue;

				try
				{
					SyncFailure failure = binding.save();

					if ( failure != null )
						throw MetawidgetException.newException( failure.getConversionException() );
				}
				catch ( ClassCastException e )
				{
					throw MetawidgetException.newException( "When saving from " + binding.getTargetObject().getClass().getName() + " to " + sourceProperty + " (have you used BeansBinding.registerConverter?)", e );
				}
			}
		}

		// Nested bindings

		for ( Component component : metawidget.getComponents() )
		{
			if ( component instanceof SwingMetawidget )
				save( (SwingMetawidget) component );
		}
	}

	@Override
	public Object convertFromString( String value, Class<?> expectedType )
	{
		// Try converters one way round...

		Converter<String, ?> converterFromString = getConverter( String.class, expectedType );

		if ( converterFromString != null )
			return converterFromString.convertForward( value );

		// ...and the other...

		Converter<?, String> converterToString = getConverter( expectedType, String.class );

		if ( converterToString != null )
			return converterToString.convertReverse( value );

		// ...or don't convert

		return value;
	}

	//
	// Private members
	//

	private UpdateStrategy getUpdateStrategy( SwingMetawidget metawidget )
	{
		UpdateStrategy updateStrategy = (UpdateStrategy) metawidget.getParameter( UpdateStrategy.class );

		if ( updateStrategy != null )
			return updateStrategy;

		return UpdateStrategy.READ_ONCE;
	}

	@SuppressWarnings( "unchecked" )
	private <SS, SV, TS extends Component, TV> void typesafeAdd( TS component,  Map<String, String> attributes, SwingMetawidget metawidget )
	{
		String componentProperty = metawidget.getValueProperty( component );

		if ( componentProperty == null )
			return;

		// Source property

		SS source = (SS) metawidget.getToInspect();
		String sourceProperty = PathUtils.parsePath( metawidget.getPath() ).getNames().replace( StringUtils.SEPARATOR_FORWARD_SLASH_CHAR, StringUtils.SEPARATOR_DOT_CHAR );

		if ( metawidget.isCompoundWidget() )
		{
			if ( sourceProperty.length() > 0 )
				sourceProperty += StringUtils.SEPARATOR_DOT_CHAR;

			sourceProperty += attributes.get( NAME );
		}

		BeanProperty<SS, SV> propertySource = BeanProperty.create( sourceProperty );

		Class<TV> target;

		// Create binding

		BeanProperty<TS, TV> propertyTarget = BeanProperty.create( componentProperty );
		org.jdesktop.beansbinding.Binding<SS, SV, TS, TV> binding = Bindings.createAutoBinding( getUpdateStrategy( metawidget ), source, propertySource, component, propertyTarget );
		target = (Class<TV>) propertyTarget.getWriteType( component );

		// Add a converter

		Converter<SV, TV> converter = null;

		if ( propertySource.isWriteable( source ) )
		{
			Class<SV> sourceClass = (Class<SV>) propertySource.getWriteType( source );
			converter = getConverter( sourceClass, target );
		}
		else if ( propertySource.isReadable( source ) )
		{
			// BeansBinding does not allow us to lookup the type
			// of a non-writable property

			SV value = propertySource.getValue( source );

			if ( value != null )
			{
				Class<SV> sourceClass = (Class<SV>) value.getClass();
				converter = getConverter( sourceClass, target );
			}
		}
		else
		{
			throw MetawidgetException.newException( "Property '" + sourceProperty + "' has no getter and no setter" );
		}

		// Convenience converter for labels

		if ( converter == null && component instanceof JLabel )
			converter = new ReadOnlyToStringConverter();

		binding.setConverter( converter );

		// Bind it

		try
		{
			binding.bind();
		}
		catch ( ClassCastException e )
		{
			throw MetawidgetException.newException( "When binding " + metawidget.getPath() + StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + sourceProperty + " to " + component.getClass() + "." + componentProperty + " (have you used BeansBinding.registerConverter?)", e );
		}

		// Save the binding

		Set<org.jdesktop.beansbinding.Binding<Object, ?, ? extends Component, ?>> bindings = (Set<org.jdesktop.beansbinding.Binding<Object, ?, ? extends Component, ?>>) metawidget.getClientProperty( BeansBindingProcessor.class );

		if ( bindings == null )
		{
			bindings = CollectionUtils.newHashSet();
			metawidget.putClientProperty( BeansBindingProcessor.class, bindings );
		}

		bindings.add( (org.jdesktop.beansbinding.Binding<Object, SV, TS, TV>) binding );
	}

	/**
	 * Gets the Converter for the given Class (if any).
	 * <p>
	 * Includes traversing superclasses of the given Class for a suitable Converter, so for example
	 * registering a Converter for <code>Number.class</code> will match <code>Integer.class</code>,
	 * <code>Double.class</code> etc., unless a more subclass-specific Converter is also registered.
	 * <p>
	 * Also includes traversing from primitive types to wrapper types (eg. from
	 * <code>int.class</code> to <code>Integer.class</code>), because we cannot declare a
	 * <code>org.jdesktop.beansbinding.Converter</code> for <code>int</code>s (because Java generics
	 * cannot accept primitives).
	 */

	@SuppressWarnings( "unchecked" )
	private static <SV, TV> Converter<SV, TV> getConverter( Class<SV> sourceClass, Class<TV> targetClass )
	{
		Class<SV> sourceClassTraversal = sourceClass;
		Class<TV> targetClassTraversal = targetClass;

		if ( sourceClassTraversal.isPrimitive() )
			sourceClassTraversal = (Class<SV>) ClassUtils.getWrapperClass( sourceClassTraversal );

		if ( targetClassTraversal.isPrimitive() )
			targetClassTraversal = (Class<TV>) ClassUtils.getWrapperClass( targetClassTraversal );

		while ( sourceClassTraversal != null )
		{
			Converter<SV, TV> converter = (Converter<SV, TV>) CONVERTERS.get( new ConvertFromTo<SV, TV>( sourceClassTraversal, targetClassTraversal ) );

			if ( converter != null )
				return converter;

			sourceClassTraversal = (Class<SV>) sourceClassTraversal.getSuperclass();
		}

		return null;
	}

	//
	// Inner class
	//

	private final static class ConvertFromTo<S, T>
	{
		//
		//
		// Private members
		//
		//

		private Class<S>	mSource;

		private Class<T>	mTarget;

		//
		//
		// Constructor
		//
		//

		public ConvertFromTo( Class<S> source, Class<T> target )
		{
			mSource = source;
			mTarget = target;
		}

		//
		//
		// Public methods
		//
		//

		@Override
		public boolean equals( Object that )
		{
			if ( !( that instanceof ConvertFromTo ) )
				return false;

			ConvertFromTo<?, ?> convertThat = (ConvertFromTo<?, ?>) that;

			// Source

			if ( mSource == null )
			{
				if ( convertThat.mSource != null )
					return false;
			}
			else if ( !mSource.equals( convertThat.mSource ) )
				return false;

			// Target

			if ( mTarget == null )
			{
				if ( convertThat.mTarget != null )
					return false;
			}
			else if ( !mTarget.equals( convertThat.mTarget ) )
				return false;

			return true;
		}

		@Override
		public int hashCode()
		{
			int hashCode = 0;

			if ( mSource != null )
				hashCode ^= mSource.hashCode();

			if ( mTarget != null )
				hashCode ^= mTarget.hashCode();

			return hashCode;
		}
	}
}
