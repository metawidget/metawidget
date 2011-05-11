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
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.MetawidgetTestUtils;
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

		FacesInspector inspector = new FacesInspector();
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

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
		assertEquals( "#{foo.suggest}", property.getAttribute( FACES_SUGGEST ) );
		assertEquals( "foo.component", property.getAttribute( FACES_COMPONENT ) );
		assertEquals( "foo.converter", property.getAttribute( FACES_CONVERTER_ID ) );
		assertEquals( "foo", property.getAttribute( FACES_AJAX_EVENT ) );
		assertEquals( "#{bar}", property.getAttribute( FACES_AJAX_ACTION ) );
		assertTrue( 7 == property.getAttributes().getLength() );

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

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "foo" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "result of #{foo1}", property.getAttribute( "foo1" ) );
		assertEquals( "result of #{foo1}", property.getAttribute( "foo2" ) );
		assertTrue( 3 == property.getAttributes().getLength() );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "#{array1},#{array1}", property.getAttribute( "array" ) );
		assertEquals( "#{collection1},#{collection1}", property.getAttribute( "collection" ) );
		assertTrue( 3 == property.getAttributes().getLength() );

		assertTrue( entity.getChildNodes().getLength() == 5 );
	}

	public void testBadExpression() {

		FacesInspector inspector = new FacesInspector( new FacesInspectorConfig().setInjectThis( true ));

		try {
			inspector.inspect( null, BadExpression1a.class.getName() );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "Expression 'foo' (for 'faces-lookup') is not of the form #{...}", e.getMessage() );
		}

		try {
			inspector.inspect( null, BadExpression1b.class.getName() );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "Expression '#{this.foo}' (for 'faces-lookup') must not contain 'this' (see Metawidget Reference Guide)", e.getMessage() );
		}

		try {
			inspector.inspect( null, BadExpression1c.class.getName() );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "Expression '#{_this.foo}' (for 'faces-lookup') must not contain '_this' (see Metawidget Reference Guide)", e.getMessage() );
		}

		try {
			inspector.inspect( null, BadExpression2a.class.getName() );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "Expression 'bar' (for 'faces-suggest') is not of the form #{...}", e.getMessage() );
		}

		try {
			inspector.inspect( null, BadExpression2b.class.getName() );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "Expression '#{this.bar}' (for 'faces-suggest') must not contain 'this' (see Metawidget Reference Guide)", e.getMessage() );
		}

		try {
			inspector.inspect( null, BadExpression2c.class.getName() );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "Expression '#{_this.bar}' (for 'faces-suggest') must not contain '_this' (see Metawidget Reference Guide)", e.getMessage() );
		}

		try {
			inspector.inspect( null, BadExpression3a.class.getName() );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "Expression 'baz' (for 'faces-ajax-action') is not of the form #{...}", e.getMessage() );
		}

		try {
			inspector.inspect( null, BadExpression3b.class.getName() );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "Expression '#{this.baz}' (for 'faces-ajax-action') must not contain 'this' (see Metawidget Reference Guide)", e.getMessage() );
		}

		try {
			inspector.inspect( null, BadExpression3c.class.getName() );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "Expression '#{_this.baz}' (for 'faces-ajax-action') must not contain '_this' (see Metawidget Reference Guide)", e.getMessage() );
		}
	}

	public void testBadEvaluation() {

		try {
			new FacesInspector().inspect( null, BadEvaluation1.class.getName() );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "Unable to getValue of #{error}", e.getMessage() );
			assertEquals( "Forced error", e.getCause().getMessage() );
		}

		try {
			new FacesInspector().inspect( null, BadEvaluation2.class.getName() );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "Expression for '#{this.error}' contains 'this', but FacesInspectorConfig.setInjectThis is 'false'", e.getMessage() );
		}

		try {
			new FacesInspector().inspect( null, BadEvaluation2d.class.getName() );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "Expression for '#{_this.error}' contains '_this', but FacesInspectorConfig.setInjectThis is 'false'", e.getMessage() );
		}
	}

	public void testNoFacesContext() {

		mContext.release();

		try {
			new FacesInspector().inspect( new NoFacesContextOnPropertyFoo(), NoFacesContextOnPropertyFoo.class.getName() );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "FacesContext not available to FacesInspector", e.getMessage() );
		}

		try {
			new FacesInspector().inspect( new NoFacesContextOnActionFoo(), NoFacesContextOnActionFoo.class.getName() );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "FacesContext not available to FacesInspector", e.getMessage() );
		}
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

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( FacesInspectorConfig.class, new FacesInspectorConfig() {
			// Subclass
		} );
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

	public static class Foo {

		@UiFacesLookup( "#{foo.bar}" )
		@UiFacesSuggest( "#{foo.suggest}" )
		@UiFacesComponent( "foo.component" )
		@UiFacesConverter( "foo.converter" )
		@UiFacesAjax( event = "foo", action = "#{bar}" )
		public Object	object1;

		@UiFacesDateTimeConverter( dateStyle = "full", timeStyle = "medium", locale = "UK", pattern = "yyyy", timeZone = "GMT", type = "date" )
		public Object	object2;

		@UiFacesNumberConverter( currencyCode = "AUD", currencySymbol = "$", groupingUsed = true, locale = "AU", maxFractionDigits = 2, minFractionDigits = 1, maxIntegerDigits = 100, minIntegerDigits = 3, pattern = "#0.00", type = "currency" )
		public Object	object3;

		@UiFacesAttribute( name = { "foo1", "foo2" }, expression = "#{foo1}" )
		public String	foo;

		@UiFacesAttributes( { @UiFacesAttribute( name = "array", expression = "#{array1}" ), @UiFacesAttribute( name = "collection", expression = "#{collection1}" ) } )
		public String	bar;
	}

	public static class DoubleConverterFoo {

		@UiFacesNumberConverter
		@UiFacesDateTimeConverter
		public Object getBar() {

			return null;
		}
	}

	public static class NoFacesContextOnPropertyFoo {

		@UiFacesAttribute( name = "baz", expression = "#{abc}" )
		public Object getBar() {

			return null;
		}
	}

	public static class NoFacesContextOnActionFoo {

		@UiAction
		@UiFacesAttribute( name = "baz", expression = "#{abc}" )
		public Object action() {

			return null;
		}
	}

	public static class BadEvaluation1 {

		@UiFacesAttribute( name = "foo1", expression = "#{error}" )
		public Object getFoo() {

			return null;
		}
	}

	public static class BadEvaluation2 {

		@UiFacesAttribute( name = "foo2", expression = "#{this.error}" )
		public Object getFoo() {

			return null;
		}
	}

	public static class BadEvaluation2d {

		@UiFacesAttribute( name = "foo2", expression = "#{_this.error}" )
		public Object getFoo() {

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

		@UiFacesLookup( "#{this.foo}" )
		public Object getFoo() {

			return null;
		}
	}

	public static class BadExpression1c {

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

		@UiFacesSuggest( "#{this.bar}" )
		public Object getFoo() {

			return null;
		}
	}

	public static class BadExpression2c {

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

		@UiFacesAjax( event = "anEvent", action = "#{this.baz}" )
		public Object getFoo() {

			return null;
		}
	}

	public static class BadExpression3c {

		@UiFacesAjax( event = "anEvent", action = "#{_this.baz}" )
		public Object getFoo() {

			return null;
		}
	}
}
