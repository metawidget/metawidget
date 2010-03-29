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

import java.util.Map;

import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessor.ConvertFromTo;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a BeansBindingProcessor prior to use. Once instantiated, WidgetProcessors are
 * immutable.
 *
 * @author Richard Kennard
 */

public class BeansBindingProcessorConfig
{
	//
	// Private members
	//

	private UpdateStrategy								mUpdateStrategy	= UpdateStrategy.READ_ONCE;

	private Map<ConvertFromTo<?, ?>, Converter<?, ?>>	mConverters;

	//
	// Public methods
	//

	public UpdateStrategy getUpdateStrategy()
	{
		return mUpdateStrategy;
	}

	/**
	 * Sets the UpdateStrategy for this BeansBindingProcessor.
	 *
	 * @return this, as part of a fluent interface
	 */

	public BeansBindingProcessorConfig setUpdateStrategy( UpdateStrategy updateStrategy )
	{
		mUpdateStrategy = updateStrategy;

		return this;
	}

	public Map<ConvertFromTo<?, ?>, Converter<?, ?>> getConverters()
	{
		return mConverters;
	}

	/**
	 * Sets a Converter for this BeansBindingProcessor.
	 * <p>
	 * Superclasses of the given <code>sourceClass</code> will be traversed for a suitable
	 * Converter, so for example registering a Converter for <code>Number.class</code> will match
	 * <code>Integer.class</code>, <code>Double.class</code> etc., unless a more subclass-specific
	 * Converter is also registered.
	 * <p>
	 * This includes traversing from primitive types to wrapper types (eg. from
	 * <code>int.class</code> to <code>Integer.class</code>), because we cannot declare a
	 * <code>org.jdesktop.beansbinding.Converter</code> for <code>int</code>s (because Java generics
	 * cannot accept primitives).
	 * <p>
	 * Note: <code>setConverter</code> is not a JavaBean 'setter': multiple different Converters can
	 * be set by calling <code>setConverter</code> multiple times with different source and target
	 * classes.
	 * <p>
	 * Note: superclass traversal only works for the source class. The target class still needs to
	 * be an exact match.
	 *
	 * @return this, as part of a fluent interface
	 */

	public <S, T> BeansBindingProcessorConfig setConverter( Class<S> source, Class<T> target, Converter<S, T> converter )
	{
		if ( mConverters == null )
			mConverters = CollectionUtils.newHashMap();

		mConverters.put( new ConvertFromTo<S, T>( source, target ), converter );

		return this;
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

		if ( !ObjectUtils.nullSafeEquals( mUpdateStrategy, ( (BeansBindingProcessorConfig) that ).mUpdateStrategy ) )
			return false;

		if ( !ObjectUtils.nullSafeEquals( mConverters, ( (BeansBindingProcessorConfig) that ).mConverters ) )
			return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int hashCode = 1;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mUpdateStrategy );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mConverters );

		return hashCode;
	}
}
