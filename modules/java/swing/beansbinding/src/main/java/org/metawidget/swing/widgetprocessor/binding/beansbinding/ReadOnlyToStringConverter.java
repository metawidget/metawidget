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

import org.jdesktop.beansbinding.Converter;
import org.metawidget.util.simple.StringUtils;

/**
 * Convenience Converter to call <code>toString</code> during <code>convertForward</code>. This
 * allows <code>convertForward</code> to generically support many types without an explicit
 * Converter, so long as they never try to <code>convertReverse</code>.
 * <p>
 * In the event this Converter is used to <code>convertReverse</code>, throws
 * <code>UnsupportedOperationException</code>.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ReadOnlyToStringConverter<T>
	extends Converter<T, String> {

	//
	// Public methods
	//

	@Override
	public String convertForward( T value ) {

		return StringUtils.quietValueOf( value );
	}

	@Override
	public T convertReverse( String value ) {

		if ( value == null ) {
			return null;
		}

		throw new UnsupportedOperationException( "ReadOnlyConverter cannot convertReverse( " + value.getClass().getName() + " ). Use BeansBindingProcessorConfig.setConverter to register a custom Converter" );
	}
}
