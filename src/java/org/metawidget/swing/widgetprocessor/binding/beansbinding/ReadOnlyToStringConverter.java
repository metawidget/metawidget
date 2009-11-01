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

import org.jdesktop.beansbinding.Converter;
import org.metawidget.util.simple.StringUtils;

/**
 * Convenience Converter to call <code>toString</code> during <code>convertForward</code>. This allows
 * <code>convertForward</code> to generically support many types without an explicit Converter, so long
 * as they never try to <code>convertReverse</code>.
 * <p>
 * In the event this Converter is used to <code>convertReverse</code>, throws
 * <code>UnsupportedOperationException</code>.
 *
 * @author Richard Kennard
 */

public class ReadOnlyToStringConverter<SV>
	extends Converter<SV, String>
{
	//
	// Public methods
	//

	@Override
	public String convertForward( SV value )
	{
		return StringUtils.quietValueOf( value );
	}

	@Override
	public SV convertReverse( String value )
	{
		if ( value == null )
			return null;

		throw new UnsupportedOperationException( "ReadOnlyConverter cannot convertReverse( " + value.getClass().getName() + " ). Use BeansBindingProcessorConfig.setConverter to register a custom Converter" );
	}
}
