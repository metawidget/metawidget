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
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.PropertyHelper;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.Binding.SyncFailure;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.metawidget.MetawidgetException;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.binding.Binding;
import org.metawidget.util.ArrayUtils;
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
	public void save()
	{
		if ( mBindings == null )
			return;

		for ( org.jdesktop.beansbinding.Binding<Object, ?, ? extends Component, ?> binding : mBindings )
		{
			if ( !binding.getSourceProperty().isWriteable( binding.getSourceObject() ) )
				continue;

			SyncFailure failure = binding.save();

			if ( failure != null )
				throw new RuntimeException( failure.getConversionException() );
		}
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T> T convertFromString( String value, Class<T> type )
	{
		Converter<String, T> converter = (Converter<String, T>) CONVERTERS.get( new ConvertFromTo<String, T>( String.class, type ) );

		if ( converter == null )
			return (T) value;

		return converter.convertForward( value );
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

		org.jdesktop.beansbinding.Binding<SS, SV, TS, TV> binding;

		if ( component instanceof JTable )
		{
			// Do not bind to a JTable if it already has a model

			JTable table = (JTable) component;
			TableModel model = table.getModel();

			if ( !model.getClass().equals( DefaultTableModel.class ) || ( (DefaultTableModel) model ).getColumnCount() != 0 )
				return;

			// Display a very simple JTable by default

			JTableBinding tableBinding = SwingBindings.createJTableBinding( mUpdateStrategy, source, (BeanProperty<SS, List<Object>>) propertySource, table );
			tableBinding.addColumnBinding( new ToStringProperty<SS>() ).setColumnName( "" );
			binding = tableBinding;
			target = (Class<TV>) List.class;
		}
		else
		{
			BeanProperty<TS, TV> propertyTarget = BeanProperty.create( componentProperty );
			binding = Bindings.createAutoBinding( mUpdateStrategy, source, propertySource, component, propertyTarget );
			target = (Class<TV>) propertyTarget.getWriteType( component );
		}

		// Add a converter

		Converter<SV, TV> converter = null;

		if ( propertySource.isWriteable( source ) )
		{
			Class<SV> sourceClass = (Class<SV>) propertySource.getWriteType( source );
			converter = (Converter<SV, TV>) CONVERTERS.get( new ConvertFromTo<SV, TV>( sourceClass, target ) );
		}
		else if ( propertySource.isReadable( source ) )
		{
			// BeansBinding does not allow us to lookup the type
			// of a non-writable property

			SV value = propertySource.getValue( source );

			if ( value != null )
			{
				Class<SV> sourceClass = (Class<SV>) value.getClass();
				converter = (Converter<SV, TV>) CONVERTERS.get( new ConvertFromTo<SV, TV>( sourceClass, target ) );
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
			if ( component instanceof JTable )
				throw MetawidgetException.newException( "When binding " + ArrayUtils.toString( names ) + " to " + component.getClass() + "'s java.util.List (have you used BeansBinding.registerConverter?)", e );

			throw MetawidgetException.newException( "When binding " + ArrayUtils.toString( names ) + " to " + component.getClass() + "." + componentProperty + " (have you used BeansBinding.registerConverter?)", e );
		}

		mBindings.add( (org.jdesktop.beansbinding.Binding<Object, SV, TS, TV>) binding );
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

	/**
	 * BeansBinding Property that gets its value from the object's <code>toString</code>, as
	 * opposed to a JavaBean property (like <code>BeanProperty</code>) or an expression (like
	 * <code>ELProperty</code>).
	 * <p>
	 * Used for displaying an object's <code>toString</code> in a <code>JTableBinding</code>'s
	 * column binding.
	 */

	static class ToStringProperty<S>
		extends PropertyHelper<S, String>
	{
		@Override
		public String getValue( S source )
		{
			return source.toString();
		}

		@Override
		public Class<String> getWriteType( S source )
		{
			return null;
		}

		@Override
		public boolean isReadable( S source )
		{
			return true;
		}

		@Override
		public boolean isWriteable( S source )
		{
			return false;
		}

		@Override
		public void setValue( S source, String value )
		{
			// Not writable
		}
	}
}
