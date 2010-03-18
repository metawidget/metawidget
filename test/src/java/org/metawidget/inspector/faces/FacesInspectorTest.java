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

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;
import junit.framework.TestCase;

import org.metawidget.faces.FacesUtils;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.TestUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class FacesInspectorTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testInspection()
	{
		FacesInspector inspector = new FacesInspector();
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ));

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Foo.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "object1" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "#{foo.bar}", property.getAttribute( FACES_LOOKUP ) );
		assertEquals( "#{foo.suggest}", property.getAttribute( FACES_SUGGEST) );
		assertEquals( "foo.component", property.getAttribute( FACES_COMPONENT ) );
		assertEquals( "foo.converter", property.getAttribute( FACES_CONVERTER_ID ) );
		assertTrue( property.getAttributes().getLength() == 5 );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "object2" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "full", property.getAttribute( DATE_STYLE ) );
		assertEquals( "medium", property.getAttribute( TIME_STYLE ) );
		assertEquals( "UK", property.getAttribute( LOCALE ) );
		assertEquals( "yyyy", property.getAttribute( DATETIME_PATTERN ) );
		assertEquals( "GMT", property.getAttribute( TIME_ZONE ) );
		assertEquals( "date", property.getAttribute( DATETIME_TYPE ) );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "object3" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "AUD", property.getAttribute( CURRENCY_CODE ) );
		assertEquals( "$", property.getAttribute( CURRENCY_SYMBOL ) );
		assertEquals( TRUE, property.getAttribute( NUMBER_USES_GROUPING_SEPARATORS ) );
		assertEquals( "AU", property.getAttribute( LOCALE ) );
		assertEquals( "3", property.getAttribute( MINIMUM_INTEGER_DIGITS ) );
		assertEquals( "100", property.getAttribute( MAXIMUM_INTEGER_DIGITS ) );
		assertEquals( "1", property.getAttribute( MINIMUM_FRACTIONAL_DIGITS ) );
		assertEquals( "2", property.getAttribute( MAXIMUM_FRACTIONAL_DIGITS ) );
		assertEquals( "#0.00", property.getAttribute( NUMBER_PATTERN ) );
		assertEquals( "currency", property.getAttribute( NUMBER_TYPE ) );

		assertTrue( entity.getChildNodes().getLength() == 3 );
	}

	public void testNoFacesContext()
	{
		try
		{
			new FacesInspector().inspect( new NoFacesContextOnPropertyFoo(), NoFacesContextOnPropertyFoo.class.getName() );
			assertTrue( false );
		}
		catch( InspectorException e )
		{
			assertEquals( "FacesContext not available to FacesInspector", e.getMessage() );
		}

		try
		{
			new FacesInspector().inspect( new NoFacesContextOnActionFoo(), NoFacesContextOnActionFoo.class.getName() );
			assertTrue( false );
		}
		catch( InspectorException e )
		{
			assertEquals( "FacesContext not available to FacesInspector", e.getMessage() );
		}
	}

	public void testUtils()
	{
		assertEquals( "foo.bar", FacesUtils.unwrapExpression( "foo.bar" ));
		assertEquals( "#{foo.bar", FacesUtils.unwrapExpression( "#{foo.bar" ));
		assertEquals( "foo.bar", FacesUtils.unwrapExpression( "#{foo.bar}" ));
		assertEquals( "foo.bar", FacesUtils.unwrapExpression( "foo.bar" ));
		assertEquals( "#{foo.bar", FacesUtils.unwrapExpression( "#{foo.bar" ));

		assertEquals( "#{foo.bar}", FacesUtils.wrapExpression( "foo.bar" ));
		assertEquals( "#{foo.bar}", FacesUtils.wrapExpression( "#{foo.bar}" ));
		assertEquals( "#{#{foo.bar}", FacesUtils.wrapExpression( "#{foo.bar" ));
	}

	public void testConfig()
	{
		TestUtils.testEqualsAndHashcode( FacesInspectorConfig.class, new FacesInspectorConfig()
		{
			// Subclass
		} );
	}

	//
	// Inner class
	//

	public static class Foo
	{
		@UiFacesLookup( "#{foo.bar}" )
		@UiFacesSuggest( "#{foo.suggest}" )
		@UiFacesComponent( "foo.component" )
		@UiFacesConverter( "foo.converter" )
		public Object	object1;

		@UiFacesDateTimeConverter( dateStyle = "full", timeStyle = "medium", locale = "UK", pattern = "yyyy", timeZone = "GMT", type = "date" )
		public Object	object2;

		@UiFacesNumberConverter( currencyCode = "AUD", currencySymbol = "$", groupingUsed = true, locale = "AU", maxFractionDigits = 2, minFractionDigits = 1, maxIntegerDigits = 100, minIntegerDigits = 3, pattern = "#0.00", type = "currency" )
		public Object	object3;
	}

	public static class DoubleConverterFoo
	{
		@UiFacesNumberConverter
		@UiFacesDateTimeConverter
		public Object getBar()
		{
			return null;
		}
	}

	public static class NoFacesContextOnPropertyFoo
	{
		@UiFacesAttribute( name="baz", expression="#{abc}" )
		public Object getBar()
		{
			return null;
		}
	}

	public static class NoFacesContextOnActionFoo
	{
		@UiAction
		@UiFacesAttribute( name="baz", expression="#{abc}" )
		public Object action()
		{
			return null;
		}
	}
}
