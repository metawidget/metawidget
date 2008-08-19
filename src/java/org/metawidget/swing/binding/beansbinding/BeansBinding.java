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

package org.metawidget.swing.binding.beansbinding;

import java.awt.Component;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.Binding.SyncFailure;
import org.metawidget.MetawidgetException;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.binding.Binding;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Automatic binding implementation based on BeansBinding (JSR 295).
 * <p>
 * This implementation recognizes the following <code>SwingMetawidget.setParameter</code>
 * parameters:
 * <p>
 * <ul>
 * <li><code>UpdateStrategy.class</code> - as defined by
 * <code>org.jdesktop.beansbinding.AutoBinding.UpdateStrategy</code>. Defaults to
 * <code>READ_ONCE</code>. If set to <code>READ</code> or <code>READ_WRITE</code>, the
 * object being inspected must provide <code>PropertyChangeSupport</code>. If set to
 * <code>READ</code>, there is no need to call <code>SwingMetawidget.rebind</code>. If set to
 * <code>READ_WRITE</code>, there is no need to call <code>SwingMetawidget.save</code>.
 * </ul>
 *
 * @author Richard Kennard
 */

public class BeansBinding
	extends Binding
{
	//
	//
	// Private statics
	//
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
	}

	//
	//
	// Public statics
	//
	//

	public static <S, T> void registerConverter( Class<S> source, Class<T> target, Converter<S, T> converter )
	{
		CONVERTERS.put( new ConvertFromTo<S, T>( source, target ), converter );
	}

	//
	//
	// Private members
	//
	//

	private Set<org.jdesktop.beansbinding.Binding<Object, ?, ? extends Component, ?>>	mBindings;

	private UpdateStrategy																mUpdateStrategy;

	//
	//
	// Constructor
	//
	//

	public BeansBinding( SwingMetawidget metawidget )
	{
		super( metawidget );

		// Read parameters

		mUpdateStrategy = (UpdateStrategy) metawidget.getParameter( UpdateStrategy.class );

		if ( mUpdateStrategy == null )
			mUpdateStrategy = UpdateStrategy.READ_ONCE;
	}

	//
	//
	// Public methods
	//
	//

	@Override
	public void bind( Component component, String componentProperty, String... names )
	{
		typesafeBind( component, componentProperty, names );
	}

	@Override
	public void rebind()
	{
		if ( mBindings == null )
			return;

		Object sourceObject = getMetawidget().getToInspect();

		for ( org.jdesktop.beansbinding.Binding<Object, ?, ? extends Component, ?> binding : mBindings )
		{
			binding.unbind();
			binding.setSourceObject( sourceObject );
			binding.bind();

			SyncFailure failure = binding.refresh();

			if ( failure != null )
				throw MetawidgetException.newException( failure.getType().toString() );
		}
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public void save()
	{
		if ( mBindings == null )
			return;

		for ( org.jdesktop.beansbinding.Binding<Object, ?, ? extends Component, ?> binding : mBindings )
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

	@Override
	@SuppressWarnings( "unchecked" )
	public <T> T convertFromString( String value, Class<T> type )
	{
		// Try converters one way round...

		Converter<String, T> converterFromString = getConverter( String.class, type );

		if ( converterFromString != null )
			return converterFromString.convertForward( value );

		// ...and the other...

		Converter<T, String> converterToString = getConverter( type, String.class );

		if ( converterToString != null )
			return converterToString.convertReverse( value );

		// ...or don't convert

		return (T) value;
	}

	@Override
	public void unbind()
	{
		if ( mBindings == null )
			return;

		for ( org.jdesktop.beansbinding.Binding<?, ?, ? extends Component, ?> binding : mBindings )
		{
			binding.unbind();
		}
	}

	//
	//
	// Private members
	//
	//

	@SuppressWarnings( "unchecked" )
	private <SS, SV, TS extends Component, TV> void typesafeBind( TS component, String componentProperty, String... names )
	{
		if ( componentProperty == null )
			return;

		// Source property

		SS source = (SS) getMetawidget().getToInspect();
		String sourceProperty = ArrayUtils.toString( names, StringUtils.SEPARATOR_DOT );
		BeanProperty<SS, SV> propertySource = BeanProperty.create( sourceProperty );

		Class<TV> target;

		// Create binding

		BeanProperty<TS, TV> propertyTarget = BeanProperty.create( componentProperty );
		org.jdesktop.beansbinding.Binding<SS, SV, TS, TV> binding = Bindings.createAutoBinding( mUpdateStrategy, source, propertySource, component, propertyTarget );
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

		if ( mBindings == null )
			mBindings = CollectionUtils.newHashSet();

		// Bind it

		try
		{
			binding.bind();
		}
		catch ( ClassCastException e )
		{
			throw MetawidgetException.newException( "When binding " + ArrayUtils.toString( names ) + " to " + component.getClass() + "." + componentProperty + " (have you used BeansBinding.registerConverter?)", e );
		}

		mBindings.add( (org.jdesktop.beansbinding.Binding<Object, SV, TS, TV>) binding );
	}

	/**
	 * Gets the Converter for the given Class (if any).
	 * <p>
	 * Includes traversing superclasses of the given Class for a suitable Converter, so for example
	 * registering a Converter for <code>Number.class</code> will match <code>Integer.class</code>,
	 * <code>Double.class</code> etc., unless a more subclass-specific Converter is also
	 * registered.
	 */

	@SuppressWarnings( "unchecked" )
	private <SV, TV> Converter<SV, TV> getConverter( Class<SV> sourceClass, Class<TV> targetClass )
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
	//
	// Inner class
	//
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
