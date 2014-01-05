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

package org.metawidget.gwt.client.widgetprocessor.binding.simple;

import java.util.HashMap;
import java.util.Map;

import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a SimpleBindingProcessor prior to use. Once instantiated, WidgetProcessors are
 * immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SimpleBindingProcessorConfig {

	//
	// Private members
	//

	private Map<Class<?>, SimpleBindingProcessorAdapter<?>>	mAdapters;

	private Map<Class<?>, Converter<?>>						mConverters;

	//
	// Public methods
	//

	/**
	 * Sets an Adapter for the given Class.
	 * <p>
	 * Adapters also apply to subclasses of the given Class. So for example registering an Adapter
	 * for <code>Contact.class</code> will match <code>PersonalContact.class</code>,
	 * <code>BusinessContact.class</code> etc., unless a more subclass-specific Adapter is also
	 * registered
	 * <p>
	 * Note: this is not a JavaBean 'setter': multiple different Adapters can be set by calling
	 * <code>setAdapter</code> multiple times with different source classes.
	 *
	 * @return this, as part of a fluent interface
	 */

	public <T> SimpleBindingProcessorConfig setAdapter( Class<T> forClass, SimpleBindingProcessorAdapter<T> adapter ) {

		if ( mAdapters == null ) {
			
			// WeakHashMap would be better here, but not supported by GWT:
			// http://code.google.com/webtoolkit/doc/latest/RefJreEmulation.html#Package_java_util
			
			mAdapters = new HashMap<Class<?>, SimpleBindingProcessorAdapter<?>>();
		}

		mAdapters.put( forClass, adapter );

		return this;
	}

	/**
	 * Sets a Converter for the given Class.
	 * <p>
	 * Converters also apply to subclasses of the given Class. So for example registering a
	 * Converter for <code>Number.class</code> will match <code>Integer.class</code>,
	 * <code>Double.class</code> etc., unless a more subclass-specific Converter is also registered.
	 * <p>
	 * Note: this is not a JavaBean 'setter': multiple different Converters can be set by calling
	 * <code>setConverter</code> multiple times with different source classes.
	 *
	 * @return this, as part of a fluent interface
	 */

	public <T> SimpleBindingProcessorConfig setConverter( Class<T> forClass, Converter<T> converter ) {

		if ( mConverters == null ) {
			
			// WeakHashMap would be better here, but not supported by GWT:
			// http://code.google.com/webtoolkit/doc/latest/RefJreEmulation.html#Package_java_util
			
			mConverters = new HashMap<Class<?>, Converter<?>>();
		}

		mConverters.put( forClass, converter );

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

		if ( !ObjectUtils.nullSafeEquals( mAdapters, ( (SimpleBindingProcessorConfig) that ).mAdapters ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mConverters, ( (SimpleBindingProcessorConfig) that ).mConverters ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mAdapters );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mConverters );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected Map<Class<?>, SimpleBindingProcessorAdapter<?>> getAdapters() {

		return mAdapters;
	}

	protected Map<Class<?>, Converter<?>> getConverters() {

		return mConverters;
	}
}
