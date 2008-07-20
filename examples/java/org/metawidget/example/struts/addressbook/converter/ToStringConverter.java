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

package org.metawidget.example.struts.addressbook.converter;

import org.apache.commons.beanutils.Converter;

/**
 * Converts Objects to a String representation.
 *
 * @author Richard Kennard
 */

public class ToStringConverter
	implements Converter
{
	//
	//
	// Public methods
	//
	//

	@SuppressWarnings( "unchecked" )
	public Object convert( Class clazz, Object value )
	{
		if ( value == null || "".equals( value ))
			return null;

		// Convert enums to their .name() form, not their .toString() form, so that we can
		// use .valueOf() in EnumConverter.
		//
		// Note: for this to be called correctly, it requires a fix for http://issues.apache.org/jira/browse/BEANUTILS-110,
		// which is only available in BeanUtils 1.8.0 and greater!

		if ( value instanceof Enum )
			return ((Enum) value).name();

		return value.toString();
	}
}
