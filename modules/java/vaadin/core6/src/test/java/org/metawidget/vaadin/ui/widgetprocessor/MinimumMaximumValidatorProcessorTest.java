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
import org.metawidget.vaadin.ui.widgetprocessor.MinimumMaximumValidatorProcessor;

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
		assertTrue( !validator.isValid( null ) );
		assertTrue( !validator.isValid( (byte) 1 ) );
		assertTrue( validator.isValid( (byte) 2 ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "99" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( validator.isValid( (byte) 1 ) );
		assertTrue( !validator.isValid( (byte) 100 ) );
		assertTrue( validator.isValid( (byte) 99 ) );

		// Bytes

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "2" );
		attributes.put( TYPE, Byte.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( validator.isValid( null ) );
		assertTrue( !validator.isValid( new Byte( (byte) 1 ) ) );
		assertTrue( validator.isValid( new Byte( (byte) 2 ) ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "99" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( validator.isValid( new Byte( (byte) 1 ) ) );
		assertTrue( !validator.isValid( new Byte( (byte) 100 ) ) );
		assertTrue( validator.isValid( new Byte( (byte) 99 ) ) );

		// shorts

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "3" );
		attributes.put( TYPE, short.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( !validator.isValid( null ) );
		assertTrue( !validator.isValid( (short) 2 ) );
		assertTrue( validator.isValid( (short) 3 ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "98" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( validator.isValid( (short) 2 ) );
		assertTrue( !validator.isValid( (short) 99 ) );
		assertTrue( validator.isValid( (short) 98 ) );

		// Shorts

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "3" );
		attributes.put( TYPE, Short.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( validator.isValid( null ) );
		assertTrue( !validator.isValid( new Short( (short) 2 ) ) );
		assertTrue( validator.isValid( new Short( (short) 3 ) ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "98" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( validator.isValid( new Short( (short) 2 ) ) );
		assertTrue( !validator.isValid( new Short( (short) 99 ) ) );
		assertTrue( validator.isValid( new Short( (short) 98 ) ) );

		// ints

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "4" );
		attributes.put( TYPE, int.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( !validator.isValid( null ) );
		assertTrue( !validator.isValid( 3 ) );
		assertTrue( validator.isValid( 4 ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "97" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( validator.isValid( 4 ) );
		assertTrue( !validator.isValid( 98 ) );
		assertTrue( validator.isValid( 97 ) );

		// Integers

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "4" );
		attributes.put( TYPE, Integer.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( validator.isValid( null ) );
		assertTrue( !validator.isValid( new Integer( 3 ) ) );
		assertTrue( validator.isValid( new Integer( 4 ) ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "97" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( validator.isValid( new Integer( 4 ) ) );
		assertTrue( !validator.isValid( new Integer( 98 ) ) );
		assertTrue( validator.isValid( new Integer( 97 ) ) );

		// longs

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "5" );
		attributes.put( TYPE, long.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( !validator.isValid( null ) );
		assertTrue( !validator.isValid( (long) 4 ) );
		assertTrue( validator.isValid( (long) 5 ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "96" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( validator.isValid( (long) 4 ) );
		assertTrue( !validator.isValid( (long) 97 ) );
		assertTrue( validator.isValid( (long) 96 ) );

		// Longs

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "5" );
		attributes.put( TYPE, Long.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( validator.isValid( null ) );
		assertTrue( !validator.isValid( new Long( 4 ) ) );
		assertTrue( validator.isValid( new Long( 5 ) ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "96" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( validator.isValid( new Long( 4 ) ) );
		assertTrue( !validator.isValid( new Long( 97 ) ) );
		assertTrue( validator.isValid( new Long( 96 ) ) );

		// floats

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "1.6" );
		attributes.put( TYPE, float.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( !validator.isValid( null ) );
		assertTrue( !validator.isValid( 1.5f ) );
		assertTrue( validator.isValid( 1.6f ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "-1" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( !validator.isValid( -0.5f ) );
		assertTrue( validator.isValid( -1f ) );

		// Floats

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "1.6" );
		attributes.put( TYPE, Float.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( validator.isValid( null ) );
		assertTrue( !validator.isValid( new Float( 1.5f ) ) );
		assertTrue( validator.isValid( new Float( 1.6f ) ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "-1" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( !validator.isValid( new Float( -0.5f ) ) );
		assertTrue( validator.isValid( new Float( -1f ) ) );

		// doubles

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "1.6" );
		attributes.put( TYPE, double.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( !validator.isValid( null ) );
		assertTrue( !validator.isValid( 1.5d ) );
		assertTrue( validator.isValid( 1.6d ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "-1" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( !validator.isValid( -0.5d ) );
		assertTrue( validator.isValid( -1d ) );

		// Doubles

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "1.6" );
		attributes.put( TYPE, Double.class.getName() );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( validator.isValid( null ) );
		assertTrue( !validator.isValid( new Double( 1.5d ) ) );
		assertTrue( validator.isValid( new Double( 1.6d ) ) );
		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "-1" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		validator = textField.getValidators().iterator().next();
		assertTrue( !validator.isValid( new Double( -0.5d ) ) );
		assertTrue( validator.isValid( new Double( -1d ) ) );
	}
}
