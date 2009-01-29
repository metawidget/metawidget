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

package org.metawidget.test.inspector.faces;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.NumberConverter;

import junit.framework.TestCase;

import org.metawidget.faces.FacesUtils;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.faces.FacesInspector;
import org.metawidget.inspector.faces.UiFacesAttribute;
import org.metawidget.inspector.faces.UiFacesComponent;
import org.metawidget.inspector.faces.UiFacesConverter;
import org.metawidget.inspector.faces.UiFacesDateTimeConverter;
import org.metawidget.inspector.faces.UiFacesLookup;
import org.metawidget.inspector.faces.UiFacesNumberConverter;
import org.metawidget.inspector.iface.InspectorException;
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
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public FacesInspectorTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testInspection()
	{
		FacesInspector inspector = new FacesInspector();
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ));

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ) );
		assertTrue( Foo.class.getName().equals( entity.getAttribute( TYPE ) ) );
		assertTrue( !entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "object1" );
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "#{foo.bar}".equals( property.getAttribute( FACES_LOOKUP ) ) );
		assertTrue( "foo.component".equals( property.getAttribute( FACES_COMPONENT ) ) );
		assertTrue( "foo.converter".equals( property.getAttribute( FACES_CONVERTER_ID ) ) );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "object2" );
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( DateTimeConverter.class.getName().equals( property.getAttribute( FACES_CONVERTER_CLASS ) ) );
		assertTrue( "full".equals( property.getAttribute( DATE_STYLE ) ) );
		assertTrue( "medium".equals( property.getAttribute( TIME_STYLE ) ) );
		assertTrue( "UK".equals( property.getAttribute( LOCALE ) ) );
		assertTrue( "yyyy".equals( property.getAttribute( DATETIME_PATTERN ) ) );
		assertTrue( "GMT".equals( property.getAttribute( TIME_ZONE ) ) );
		assertTrue( "date".equals( property.getAttribute( DATETIME_TYPE ) ) );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "object3" );
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( NumberConverter.class.getName().equals( property.getAttribute( FACES_CONVERTER_CLASS ) ) );
		assertTrue( "AUD".equals( property.getAttribute( CURRENCY_CODE ) ) );
		assertTrue( "$".equals( property.getAttribute( CURRENCY_SYMBOL ) ) );
		assertTrue( TRUE.equals( property.getAttribute( NUMBER_USES_GROUPING_SEPARATORS ) ) );
		assertTrue( "AU".equals( property.getAttribute( LOCALE ) ) );
		assertTrue( "3".equals( property.getAttribute( MINIMUM_INTEGER_DIGITS ) ) );
		assertTrue( "100".equals( property.getAttribute( MAXIMUM_INTEGER_DIGITS ) ) );
		assertTrue( "1".equals( property.getAttribute( MINIMUM_FRACTIONAL_DIGITS ) ) );
		assertTrue( "2".equals( property.getAttribute( MAXIMUM_FRACTIONAL_DIGITS ) ) );
		assertTrue( "#0.00".equals( property.getAttribute( NUMBER_PATTERN ) ) );
		assertTrue( "currency".equals( property.getAttribute( NUMBER_TYPE ) ) );

		assertTrue( entity.getChildNodes().getLength() == 3 );
	}

	public void testDoubleConverter()
	{
		try
		{
			new FacesInspector().inspect( new DoubleConverterFoo(), DoubleConverterFoo.class.getName() );
			assertTrue( false );
		}
		catch( InspectorException e )
		{
			assertTrue( "Property bar cannot define both UiFacesDateTimeConverter and another converter".equals( e.getMessage() ));
		}
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
			assertTrue( "FacesContext not available to FacesInspector".equals( e.getMessage() ));
		}

		try
		{
			new FacesInspector().inspect( new NoFacesContextOnActionFoo(), NoFacesContextOnActionFoo.class.getName() );
			assertTrue( false );
		}
		catch( InspectorException e )
		{
			assertTrue( "FacesContext not available to FacesInspector".equals( e.getMessage() ));
		}
	}

	public void testUtils()
	{
		assertTrue( "foo.bar".equals( FacesUtils.unwrapValueReference( "foo.bar" )));
		assertTrue( "#{foo.bar".equals( FacesUtils.unwrapValueReference( "#{foo.bar" )));
		assertTrue( "foo.bar".equals( FacesUtils.unwrapValueReference( "#{foo.bar}" )));
		assertTrue( "foo.bar".equals( FacesUtils.unwrapValueReference( "foo.bar" )));
		assertTrue( "#{foo.bar".equals( FacesUtils.unwrapValueReference( "#{foo.bar" )));

		assertTrue( "#{foo.bar}".equals( FacesUtils.wrapValueReference( "foo.bar" )));
		assertTrue( "#{foo.bar}".equals( FacesUtils.wrapValueReference( "#{foo.bar}" )));
		assertTrue( "#{#{foo.bar}".equals( FacesUtils.wrapValueReference( "#{foo.bar" )));
	}

	//
	// Inner class
	//

	public static class Foo
	{
		@UiFacesLookup( "#{foo.bar}" )
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
