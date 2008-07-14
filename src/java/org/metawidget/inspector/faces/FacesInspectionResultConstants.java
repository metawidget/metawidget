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

package org.metawidget.inspector.faces;

/**
 * Faces-specific element and attribute names appearing in DOMs conforming to
 * inspection-result-1.0.xsd.
 *
 * @author Richard Kennard
 */

public final class FacesInspectionResultConstants
{
	//
	//
	// Public statics
	//
	//

	public final static String	FACES_LOOKUP					= "faces-lookup";

	public final static String	FACES_BINDING					= "faces-binding";

	public final static String	FACES_COMPONENT					= "faces-component";

	public final static String	FACES_CONVERTER_CLASS			= "faces-converter-class";

	public final static String	FACES_CONVERTER_ID				= "faces-converter-id";

	/**
	 * ISO 4217 currency code to be applied when formatting currencies.
	 */

	public final static String	CURRENCY_CODE					= "currency-code";

	public final static String	CURRENCY_SYMBOL					= "currency-symbol";

	/**
	 * Whether the formatted output should contain grouping separators (eg. commas).
	 */

	public final static String	NUMBER_USES_GROUPING_SEPARATORS	= "number-uses-grouping-separators";

	public final static String	LOCALE							= "locale";

	public final static String	NUMBER_PATTERN					= "number-pattern";

	/**
	 * The type of the number, such as 'currency' or 'percentage'.
	 */

	public final static String	NUMBER_TYPE						= "number-type";

	public final static String	DATE_STYLE						= "date-style";

	public final static String	TIME_STYLE						= "time-style";

	public final static String	DATETIME_PATTERN				= "datetime-pattern";

	public final static String	TIME_ZONE						= "time-zone";

	public final static String	DATETIME_TYPE					= "datetime-type";

	//
	//
	// Private constructor
	//
	//

	private FacesInspectionResultConstants()
	{
		// Can never be called
	}
}
