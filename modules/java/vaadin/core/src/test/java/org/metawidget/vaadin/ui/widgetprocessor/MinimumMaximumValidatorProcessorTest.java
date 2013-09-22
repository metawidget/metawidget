// Metawidget (licensed under LGPL)
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

package org.metawidget.vaadin.ui.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.util.CollectionUtils;

import com.vaadin.data.Validator;
import com.vaadin.ui.TextField;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class MinimumMaximumValidatorProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetProcessor()
		throws Exception {

		MinimumMaximumValidatorProcessor widgetProcessor = new MinimumMaximumValidatorProcessor();
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// bytes

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "2" );
		attributes.put( TYPE, byte.class.getName() );
		TextField textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		Validator validator = textField.getValidators().iterator().next();
		assertTrue( !isValid( validator, null ) );
		assertTrue( !isValid( validator, (byte) 1 ) );
		assertTrue( isValid( validator, (byte) 2 ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "99" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( isValid( validator, (byte) 1 ) );
		assertTrue( !isValid( validator, (byte) 100 ) );
		assertTrue( isValid( validator, (byte) 99 ) );

		// Bytes

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "2" );
		attributes.put( TYPE, Byte.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( isValid( validator, null ) );
		assertTrue( !isValid( validator, new Byte( (byte) 1 ) ) );
		assertTrue( isValid( validator, new Byte( (byte) 2 ) ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "99" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( isValid( validator, new Byte( (byte) 1 ) ) );
		assertTrue( !isValid( validator, new Byte( (byte) 100 ) ) );
		assertTrue( isValid( validator, new Byte( (byte) 99 ) ) );

		// shorts

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "3" );
		attributes.put( TYPE, short.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( !isValid( validator, null ) );
		assertTrue( !isValid( validator, (short) 2 ) );
		assertTrue( isValid( validator, (short) 3 ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "98" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( isValid( validator, (short) 2 ) );
		assertTrue( !isValid( validator, (short) 99 ) );
		assertTrue( isValid( validator, (short) 98 ) );

		// Shorts

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "3" );
		attributes.put( TYPE, Short.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( isValid( validator, null ) );
		assertTrue( !isValid( validator, new Short( (short) 2 ) ) );
		assertTrue( isValid( validator, new Short( (short) 3 ) ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "98" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( isValid( validator, new Short( (short) 2 ) ) );
		assertTrue( !isValid( validator, new Short( (short) 99 ) ) );
		assertTrue( isValid( validator, new Short( (short) 98 ) ) );

		// ints

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "4" );
		attributes.put( TYPE, int.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( !isValid( validator, null ) );
		assertTrue( !isValid( validator, 3 ) );
		assertTrue( isValid( validator, 4 ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "97" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( isValid( validator, 4 ) );
		assertTrue( !isValid( validator, 98 ) );
		assertTrue( isValid( validator, 97 ) );

		// Integers

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "4" );
		attributes.put( TYPE, Integer.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( isValid( validator, null ) );
		assertTrue( !isValid( validator, new Integer( 3 ) ) );
		assertTrue( isValid( validator, new Integer( 4 ) ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "97" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( isValid( validator, new Integer( 4 ) ) );
		assertTrue( !isValid( validator, new Integer( 98 ) ) );
		assertTrue( isValid( validator, new Integer( 97 ) ) );

		// longs

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "5" );
		attributes.put( TYPE, long.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( !isValid( validator, null ) );
		assertTrue( !isValid( validator, (long) 4 ) );
		assertTrue( isValid( validator, (long) 5 ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "96" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( isValid( validator, (long) 4 ) );
		assertTrue( !isValid( validator, (long) 97 ) );
		assertTrue( isValid( validator, (long) 96 ) );

		// Longs

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "5" );
		attributes.put( TYPE, Long.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( isValid( validator, null ) );
		assertTrue( !isValid( validator, new Long( 4 ) ) );
		assertTrue( isValid( validator, new Long( 5 ) ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "96" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( isValid( validator, new Long( 4 ) ) );
		assertTrue( !isValid( validator, new Long( 97 ) ) );
		assertTrue( isValid( validator, new Long( 96 ) ) );

		// floats

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "1.6" );
		attributes.put( TYPE, float.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( !isValid( validator, null ) );
		assertTrue( !isValid( validator, 1.5f ) );
		assertTrue( isValid( validator, 1.6f ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "-1" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( !isValid( validator, -0.5f ) );
		assertTrue( isValid( validator, -1f ) );

		// Floats

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "1.6" );
		attributes.put( TYPE, Float.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( isValid( validator, null ) );
		assertTrue( !isValid( validator, new Float( 1.5f ) ) );
		assertTrue( isValid( validator, new Float( 1.6f ) ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "-1" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( !isValid( validator, new Float( -0.5f ) ) );
		assertTrue( isValid( validator, new Float( -1f ) ) );

		// doubles

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "1.6" );
		attributes.put( TYPE, double.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( !isValid( validator, null ) );
		assertTrue( !isValid( validator, 1.5d ) );
		assertTrue( isValid( validator, 1.6d ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "-1" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( !isValid( validator, -0.5d ) );
		assertTrue( isValid( validator, -1d ) );

		// Doubles

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "1.6" );
		attributes.put( TYPE, Double.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( isValid( validator, null ) );
		assertTrue( !isValid( validator, new Double( 1.5d ) ) );
		assertTrue( isValid( validator, new Double( 1.6d ) ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "-1" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( !isValid( validator, new Double( -0.5d ) ) );
		assertTrue( isValid( validator, new Double( -1d ) ) );
	}

	//
	// Private methods
	//

	private boolean isValid( Validator validator, Object value ) {

		try {
			validator.validate( value );
			return true;
		} catch ( Validator.InvalidValueException e ) {
			return false;
		} catch ( Exception e ) {
			fail();
			throw new RuntimeException( e );
		}
	}
}
