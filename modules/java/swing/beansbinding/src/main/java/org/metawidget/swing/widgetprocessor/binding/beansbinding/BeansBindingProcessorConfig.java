// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class BeansBindingProcessorConfig {

	//
	// Private members
	//

	private UpdateStrategy								mUpdateStrategy	= UpdateStrategy.READ_ONCE;

	private Map<ConvertFromTo<?, ?>, Converter<?, ?>>	mConverters;

	//
	// Public methods
	//

	/**
	 * Sets the UpdateStrategy for this BeansBindingProcessor.
	 *
	 * @return this, as part of a fluent interface
	 */

	public BeansBindingProcessorConfig setUpdateStrategy( UpdateStrategy updateStrategy ) {

		mUpdateStrategy = updateStrategy;

		return this;
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

	public <S, T> BeansBindingProcessorConfig setConverter( Class<S> source, Class<T> target, Converter<S, T> converter ) {

		if ( mConverters == null ) {
			mConverters = CollectionUtils.newHashMap();
		}

		mConverters.put( new ConvertFromTo<S, T>( source, target ), converter );

		return this;
	}

	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}

		if ( !ObjectUtils.nullSafeClassEquals( this, that )) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mUpdateStrategy, ( (BeansBindingProcessorConfig) that ).mUpdateStrategy ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mConverters, ( (BeansBindingProcessorConfig) that ).mConverters ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mUpdateStrategy );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mConverters );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected UpdateStrategy getUpdateStrategy() {

		return mUpdateStrategy;
	}

	protected Map<ConvertFromTo<?, ?>, Converter<?, ?>> getConverters() {

		return mConverters;
	}
}
