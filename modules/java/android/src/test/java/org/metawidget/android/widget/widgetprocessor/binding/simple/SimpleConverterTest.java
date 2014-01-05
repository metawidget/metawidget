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

import junit.framework.TestCase;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SimpleConverterTest
	extends TestCase {

	//
	// Public methods
	//

	public void testConvertFromView()
		throws Exception {

		Converter<Object> converter = new SimpleConverter();

		// Based on type of value

		assertEquals( Boolean.TRUE, converter.convertFromView( new EditText( null ), true, Boolean.class ) );
		assertEquals( Boolean.TRUE, converter.convertFromView( new EditText( null ), "true", Boolean.class ) );

		// Primitives

		assertEquals( (byte) 1, converter.convertFromView( new EditText( null ), "1", byte.class ) );
		assertEquals( (short) 1, converter.convertFromView( new EditText( null ), "1", short.class ) );
		assertEquals( 1, converter.convertFromView( new EditText( null ), "1", int.class ) );
		assertEquals( 1l, converter.convertFromView( new EditText( null ), "1", long.class ) );
		assertEquals( 1f, converter.convertFromView( new EditText( null ), "1", float.class ) );
		assertEquals( 1d, converter.convertFromView( new EditText( null ), "1", double.class ) );
		assertEquals( true, converter.convertFromView( new EditText( null ), "true", boolean.class ) );
		assertEquals( 'a', converter.convertFromView( new EditText( null ), "a", char.class ) );

		// Wrappers

		assertEquals( (byte) 1, converter.convertFromView( new EditText( null ), "1", Byte.class ) );
		assertEquals( (short) 1, converter.convertFromView( new EditText( null ), "1", Short.class ) );
		assertEquals( 1, converter.convertFromView( new EditText( null ), "1", Integer.class ) );
		assertEquals( 1l, converter.convertFromView( new EditText( null ), "1", Long.class ) );
		assertEquals( 1f, converter.convertFromView( new EditText( null ), "1", Float.class ) );
		assertEquals( 1d, converter.convertFromView( new EditText( null ), "1", Double.class ) );
		assertEquals( true, converter.convertFromView( new EditText( null ), "true", Boolean.class ) );
		assertEquals( 'a', converter.convertFromView( new EditText( null ), "a", Character.class ) );
	}

	public void testConvertForView()
		throws Exception {

		Converter<Object> converter = new SimpleConverter();

		assertEquals( "1", converter.convertForView( new EditText( null ), 1 ) );
		assertEquals( "true", converter.convertForView( new EditText( null ), true ) );
		assertEquals( true, converter.convertForView( new CheckBox( null ), true ) );
	}
}
