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

package org.metawidget.faces.component.widgetprocessor;

import java.util.Map;

import javax.faces.component.ValueHolder;
import javax.faces.convert.Converter;

/**
 * Java Server Faces support: Converters
 * <p>
 * This interface should be implemented by any WidgetProcessors that set Converters (eg.
 * StandardConverterProcessor). It is used by WidgetBuilders who need early access to the Converter
 * in order to convert child elements (eg. HtmlWidgetBuilder needs to convert the options it adds to
 * a UISelect).
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public interface ConverterProcessor {

	//
	// Methods
	//

	/**
	 * Get the appropriate Converter for this ValueHolder.
	 */

	Converter getConverter( ValueHolder valueHolder, Map<String, String> attributes );
}
