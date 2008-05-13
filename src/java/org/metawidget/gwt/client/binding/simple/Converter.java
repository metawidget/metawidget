package org.metawidget.gwt.client.binding.simple;

import com.google.gwt.user.client.ui.Widget;

public abstract class Converter<T>
{
	//
	//
	// Methods
	//
	//

	/**
	 * Convert the given value (as returned by the given Widget) into the given class
	 *
	 * @param intoClass	the class to convert into. Useful for handling subclasses (eg. see NumberConverter)
	 */

	public abstract T convertFromWidget( Widget widget, Object value, Class<?> intoClass );

	/**
	 * Convert the given value to a form that can be displayed by the given Widget.
	 * <p>
	 * By default, uses <code>String.valueOf</code>.
	 */

	public Object convertForWidget( Widget widget, T value )
	{
		if ( value == null )
			return null;

		return String.valueOf( value );
	}
}
