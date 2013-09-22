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

package org.metawidget.faces.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Date;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.NumberConverter;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.component.html.widgetbuilder.HtmlLookupOutputText;
import org.metawidget.faces.component.widgetprocessor.StandardConverterProcessor;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StandardConverterProcessorTest
	extends TestCase {

	//
	// Private members
	//

	private FacesContext	mContext;

	//
	// Public methods
	//

	@SuppressWarnings( "deprecation" )
	public void testWidgetProcessor()
		throws Exception {

		StandardConverterProcessor processor = new StandardConverterProcessor();

		HtmlInputText htmlInputText = new HtmlInputText();

		// Actions get no Converters

		processor.processWidget( htmlInputText, ACTION, null, null );
		assertEquals( null, htmlInputText.getConverter() );

		// Empty attributes get no Converters

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "foo" );
		processor.processWidget( htmlInputText, PROPERTY, attributes, null );
		assertEquals( null, htmlInputText.getConverter() );
		assertEquals( null, htmlInputText.getLabel() );

		// Explicit converter

		attributes.put( FACES_CONVERTER, "fooConverter" );
		processor.processWidget( htmlInputText, PROPERTY, attributes, null );
		assertEquals( "fooConverter", htmlInputText.getConverter().toString() );

		// Explicit EL-based converter

		attributes.put( FACES_CONVERTER, "#{foo.converter}" );
		htmlInputText = new HtmlInputText();
		processor.processWidget( htmlInputText, PROPERTY, attributes, null );
		assertEquals( "#{foo.converter}", htmlInputText.getValueBinding( "converter" ).getExpressionString() );
		attributes.remove( FACES_CONVERTER );

		// Implicit DateTimeConverter

		attributes.put( TYPE, Date.class.getName() );
		htmlInputText = new HtmlInputText();
		processor.processWidget( htmlInputText, PROPERTY, attributes, new HtmlMetawidget() );
		assertTrue( htmlInputText.getConverter() instanceof DateTimeConverter );
		htmlInputText = new HtmlInputText();

		// DateTimeConverter

		attributes.put( DATE_STYLE, "full" );
		attributes.put( TIME_STYLE, "medium" );
		attributes.put( LOCALE, "UK" );
		attributes.put( DATETIME_PATTERN, "dd/MM/yyyy" );
		attributes.put( TIME_ZONE, "Australia/Sydney" );
		attributes.put( DATETIME_TYPE, "date" );
		processor.processWidget( htmlInputText, PROPERTY, attributes, new HtmlMetawidget() );
		assertEquals( null, htmlInputText.getLabel() );

		DateTimeConverter dateTimeConverter = (DateTimeConverter) htmlInputText.getConverter();
		assertEquals( "full", dateTimeConverter.getDateStyle() );
		assertEquals( "medium", dateTimeConverter.getTimeStyle() );
		assertEquals( "uk", dateTimeConverter.getLocale().getLanguage() );
		assertEquals( "dd/MM/yyyy", dateTimeConverter.getPattern() );
		assertEquals( "Australia/Sydney", dateTimeConverter.getTimeZone().getID() );
		assertEquals( "date", dateTimeConverter.getType() );

		// NumberConverter

		attributes.clear();
		attributes.put( CURRENCY_CODE, "AUD" );
		attributes.put( CURRENCY_SYMBOL, "$" );
		attributes.put( NUMBER_USES_GROUPING_SEPARATORS, TRUE );
		attributes.put( LOCALE, "AU" );
		attributes.put( MINIMUM_FRACTIONAL_DIGITS, "0" );
		attributes.put( MAXIMUM_FRACTIONAL_DIGITS, "1" );
		attributes.put( MINIMUM_INTEGER_DIGITS, "2" );
		attributes.put( MAXIMUM_INTEGER_DIGITS, "5" );
		attributes.put( NUMBER_PATTERN, "#0.00" );
		attributes.put( NUMBER_TYPE, "currency" );

		// (should not overwrite existing Converter)

		processor.processWidget( htmlInputText, PROPERTY, attributes, null );
		assertEquals( dateTimeConverter, htmlInputText.getConverter() );
		htmlInputText.setConverter( null );
		processor.processWidget( htmlInputText, PROPERTY, attributes, null );

		NumberConverter numberConverter = (NumberConverter) htmlInputText.getConverter();
		assertEquals( "AUD", numberConverter.getCurrencyCode() );
		assertEquals( "$", numberConverter.getCurrencySymbol() );
		assertTrue( numberConverter.isGroupingUsed() );
		assertEquals( "au", numberConverter.getLocale().getLanguage() );
		assertEquals( 0, numberConverter.getMinFractionDigits() );
		assertEquals( 1, numberConverter.getMaxFractionDigits() );
		assertEquals( 2, numberConverter.getMinIntegerDigits() );
		assertEquals( 5, numberConverter.getMaxIntegerDigits() );
		assertEquals( "#0.00", numberConverter.getPattern() );
		assertEquals( "currency", numberConverter.getType() );
	}

	//
	// Protected methods
	//

	@Override
	protected final void setUp()
		throws Exception {

		super.setUp();

		mContext = newMockFacesContext();
	}

	protected MockFacesContext newMockFacesContext() {

		return new MockFacesContext() {

			@Override
			public UIComponent createComponent( String componentName )
				throws FacesException {

				if ( "org.metawidget.HtmlLookupOutputText".equals( componentName ) ) {
					return new HtmlLookupOutputText();
				}

				return super.createComponent( componentName );
			}
		};
	}

	@Override
	protected final void tearDown()
		throws Exception {

		super.tearDown();

		mContext.release();
	}
}
