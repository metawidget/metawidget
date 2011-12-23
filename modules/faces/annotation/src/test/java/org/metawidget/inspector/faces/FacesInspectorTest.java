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

import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.FacesUtils;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class FacesInspectorTest
	extends TestCase {

	//
	// Private members
	//

	private FacesContext	mContext;

	//
	// Public methods
	//

	public void testInspection() {

		// Must support both config-less/config-based constructor

		FacesInspector inspector = new FacesInspector();
		inspector = new FacesInspector( new BaseObjectInspectorConfig() );

		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Foo.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "object1" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "#{foo.bar}", property.getAttribute( FACES_LOOKUP ) );
		assertTrue( !property.hasAttribute( FACES_LOOKUP_VAR ) );
		assertTrue( !property.hasAttribute( FACES_LOOKUP_ITEM_VALUE ) );
		assertTrue( !property.hasAttribute( FACES_LOOKUP_ITEM_LABEL ) );
		assertEquals( "#{foo.suggest}", property.getAttribute( FACES_SUGGEST ) );
		assertEquals( "foo.component", property.getAttribute( FACES_COMPONENT ) );
		assertEquals( "foo.converter", property.getAttribute( FACES_CONVERTER_ID ) );
		assertEquals( "foo", property.getAttribute( FACES_AJAX_EVENT ) );
		assertEquals( "#{bar}", property.getAttribute( FACES_AJAX_ACTION ) );
		assertEquals( 7, property.getAttributes().getLength() );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "object2" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "full", property.getAttribute( DATE_STYLE ) );
		assertEquals( "medium", property.getAttribute( TIME_STYLE ) );
		assertEquals( "UK", property.getAttribute( LOCALE ) );
		assertEquals( "yyyy", property.getAttribute( DATETIME_PATTERN ) );
		assertEquals( "GMT", property.getAttribute( TIME_ZONE ) );
		assertEquals( "date", property.getAttribute( DATETIME_TYPE ) );
		assertTrue( 7 == property.getAttributes().getLength() );

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
		assertTrue( 11 == property.getAttributes().getLength() );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "complexLookup" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "#{foo.bar}", property.getAttribute( FACES_LOOKUP ) );
		assertEquals( "_fooBar", property.getAttribute( FACES_LOOKUP_VAR ) );
		assertEquals( "#{_fooBar.value}", property.getAttribute( FACES_LOOKUP_ITEM_VALUE ) );
		assertEquals( "#{_fooBar.label}", property.getAttribute( FACES_LOOKUP_ITEM_LABEL ) );
		assertEquals( 5, property.getAttributes().getLength() );

		assertTrue( entity.getChildNodes().getLength() == 4 );
	}

	public void testUtils() {

		assertEquals( "foo.bar", FacesUtils.unwrapExpression( "foo.bar" ) );
		assertEquals( "#{foo.bar", FacesUtils.unwrapExpression( "#{foo.bar" ) );
		assertEquals( "foo.bar", FacesUtils.unwrapExpression( "#{foo.bar}" ) );
		assertEquals( "foo.bar", FacesUtils.unwrapExpression( "foo.bar" ) );
		assertEquals( "#{foo.bar", FacesUtils.unwrapExpression( "#{foo.bar" ) );

		assertEquals( "#{foo.bar}", FacesUtils.wrapExpression( "foo.bar" ) );
		assertEquals( "#{foo.bar}", FacesUtils.wrapExpression( "#{foo.bar}" ) );
		assertEquals( "#{#{foo.bar}", FacesUtils.wrapExpression( "#{foo.bar" ) );
	}

	//
	// Protected methods
	//

	@Override
	protected final void setUp()
		throws Exception {

		super.setUp();

		mContext = new MockFacesContext();
	}

	@Override
	protected final void tearDown()
		throws Exception {

		super.tearDown();

		mContext.release();
	}

	//
	// Inner class
	//

	public static class ParentFoo {

		public Foo	fooInParent	= new Foo();
	}

	public static class Foo {

		@UiFacesLookup( value = "#{foo.bar}" )
		@UiFacesSuggest( "#{foo.suggest}" )
		@UiFacesComponent( "foo.component" )
		@UiFacesConverter( "foo.converter" )
		@UiFacesAjax( event = "foo", action = "#{bar}" )
		public Object	object1;

		@UiFacesDateTimeConverter( dateStyle = "full", timeStyle = "medium", locale = "UK", pattern = "yyyy", timeZone = "GMT", type = "date" )
		public Object	object2;

		@UiFacesNumberConverter( currencyCode = "AUD", currencySymbol = "$", groupingUsed = true, locale = "AU", maxFractionDigits = 2, minFractionDigits = 1, maxIntegerDigits = 100, minIntegerDigits = 3, pattern = "#0.00", type = "currency" )
		public Object	object3;

		@UiFacesLookup( value = "#{foo.bar}", var = "_fooBar", itemLabel = "#{_fooBar.label}", itemValue = "#{_fooBar.value}" )
		public Object	complexLookup;
	}

	public static class DoubleConverterFoo {

		@UiFacesNumberConverter
		@UiFacesDateTimeConverter
		public Object getBar() {

			return null;
		}
	}

	public static class BadExpression1a {

		@UiFacesLookup( "foo" )
		public Object getFoo() {

			return null;
		}
	}

	public static class BadExpression1b {

		@UiFacesLookup( "#{_this.foo}" )
		public Object getFoo() {

			return null;
		}
	}

	public static class BadExpression2a {

		@UiFacesSuggest( "bar" )
		public Object getFoo() {

			return null;
		}
	}

	public static class BadExpression2b {

		@UiFacesSuggest( "#{_this.bar}" )
		public Object getFoo() {

			return null;
		}
	}

	public static class BadExpression3a {

		@UiFacesAjax( event = "anEvent", action = "baz" )
		public Object getFoo() {

			return null;
		}
	}

	public static class BadExpression3b {

		@UiFacesAjax( event = "anEvent", action = "#{_this.baz}" )
		public Object getFoo() {

			return null;
		}
	}
}
