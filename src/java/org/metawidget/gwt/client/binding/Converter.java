package org.metawidget.gwt.client.binding;

import com.google.gwt.user.client.ui.Widget;

public abstract class Converter<T>
{
	//
	//
	// Methods
	//
	//

	/**
	 * Convert the given value (as returned by the given Widget) into its original form.
	 */

	public abstract T convertFromWidget( Widget widget, String value );

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
