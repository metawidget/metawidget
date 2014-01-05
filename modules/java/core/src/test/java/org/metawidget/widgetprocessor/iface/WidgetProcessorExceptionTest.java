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

package org.metawidget.widgetprocessor.iface;

import junit.framework.TestCase;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class WidgetProcessorExceptionTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetProcessorException()
		throws Exception {

		Throwable throwable = new Throwable();
		assertEquals( throwable, WidgetProcessorException.newException( throwable ).getCause() );

		throwable = WidgetProcessorException.newException( "Foo" );
		assertEquals( "Foo", throwable.getMessage() );
		assertEquals( throwable, WidgetProcessorException.newException( throwable ) );
		assertEquals( "Foo", WidgetProcessorException.newException( "Foo", throwable ).getMessage() );
		assertEquals( throwable, WidgetProcessorException.newException( "Foo", throwable ).getCause() );
	}
}
