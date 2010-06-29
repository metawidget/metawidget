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

package org.metawidget.gwt.client.widgetprocessor.binding.simple;

import java.util.HashMap;
import java.util.Map;

import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a SimpleBindingProcessor prior to use. Once instantiated, WidgetProcessors are
 * immutable.
 *
 * @author Richard Kennard
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

	public Map<Class<?>, SimpleBindingProcessorAdapter<?>> getAdapters() {

		return mAdapters;
	}

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

	public <T> SimpleBindingProcessorConfig setAdapter( Class<T> forClass, SimpleBindingProcessorAdapter<T> Adapter ) {

		if ( mAdapters == null ) {
			mAdapters = new HashMap<Class<?>, SimpleBindingProcessorAdapter<?>>();
		}

		mAdapters.put( forClass, Adapter );

		return this;
	}

	public Map<Class<?>, Converter<?>> getConverters() {

		return mConverters;
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

		if ( that == null ) {
			return false;
		}

		if ( getClass() != that.getClass() ) {
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
}
