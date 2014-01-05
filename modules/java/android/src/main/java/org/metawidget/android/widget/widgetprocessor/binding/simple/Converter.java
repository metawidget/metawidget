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

package org.metawidget.android.widget.widgetprocessor.binding.simple;

import android.view.View;

/**
 * Interface for <code>SimpleBindingProcessor</code> Converters.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public interface Converter<T> {

	//
	// Methods
	//

	/**
	 * Convert the given value (as returned by the given View) into the given class
	 *
	 * @param intoClass
	 *            the class to convert into. Useful for handling subclasses (eg. see
	 *            NumberConverter)
	 */

	T convertFromView( View widget, Object value, Class<?> intoClass );

	/**
	 * Convert the given value to a form that can be displayed by the given View.
	 */

	Object convertForView( View widget, T value );
}
