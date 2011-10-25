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

package org.metawidget.statically.faces.component.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.StringWriter;
import java.util.Date;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.statically.faces.StaticFacesMetawidgetTests;
import org.metawidget.statically.faces.component.html.StaticHtmlMetawidget;
import org.metawidget.statically.faces.component.html.widgetbuilder.HtmlInputText;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public class StandardConverterProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetProcessor()
		throws Exception {

		StandardConverterProcessor processor = new StandardConverterProcessor();

		HtmlInputText htmlInputText = new HtmlInputText();

		// Actions get no Converters

		processor.processWidget( htmlInputText, ACTION, null, null );
		StaticFacesMetawidgetTests.assertWidgetEquals( htmlInputText, "<h:inputText/>\r\n" );

		// Empty attributes get no Converters

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "foo" );
		processor.processWidget( htmlInputText, PROPERTY, attributes, null );
		StaticFacesMetawidgetTests.assertWidgetEquals( htmlInputText, "<h:inputText/>\r\n" );

		// Implicit DateTimeConverter

		attributes.put( TYPE, Date.class.getName() );
		processor.processWidget( htmlInputText, PROPERTY, attributes, null );
		StaticFacesMetawidgetTests.assertWidgetEquals( htmlInputText, "<h:inputText>\r\n\t<f:convertDateTime/>\r\n</h:inputText>\r\n" );
		htmlInputText = new HtmlInputText();

		// DateTimeConverter

		attributes.put( DATE_STYLE, "full" );
		attributes.put( TIME_STYLE, "medium" );
		attributes.put( LOCALE, "UK" );
		attributes.put( DATETIME_PATTERN, "dd/MM/yyyy" );
		attributes.put( TIME_ZONE, "Australia/Sydney" );
		attributes.put( DATETIME_TYPE, "date" );
		processor.processWidget( htmlInputText, PROPERTY, attributes, null );
		StaticFacesMetawidgetTests.assertWidgetEquals( htmlInputText, "<h:inputText>\r\n\t<f:convertDateTime dateStyle=\"full\" locale=\"UK\" pattern=\"dd/MM/yyyy\" timeStyle=\"medium\" timeZone=\"Australia/Sydney\" type=\"date\"/>\r\n</h:inputText>\r\n" );

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

		htmlInputText = new HtmlInputText();
		processor.processWidget( htmlInputText, PROPERTY, attributes, null );
		StaticFacesMetawidgetTests.assertWidgetEquals( htmlInputText, "<h:inputText>\r\n\t<f:convertNumber currencyCode=\"AUD\" currencySymbol=\"$\" groupingUsed=\"true\" locale=\"AU\" maxFractionDigits=\"1\" maxIntegerDigits=\"5\" minFractionDigits=\"0\" minIntegerDigits=\"2\" pattern=\"#0.00\" type=\"currency\"/>\r\n</h:inputText>\r\n" );
	}

	public void testSimpleType() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setValueExpression( "#{foo}" );
		metawidget.setPath( Foo.class.getName() );

		StringWriter writer = new StringWriter();
		metawidget.write( writer );

		String result = "<h:panelGrid columns=\"2\">\r\n" +
				"\t<h:outputLabel for=\"fooBar\" id=\"fooBar-label\" value=\"Bar:\"/>\r\n" +
				"\t<h:inputText id=\"fooBar\" value=\"#{foo.bar}\">\r\n" +
				"\t\t<f:convertDateTime dateStyle=\"yyyy-MM-dd\"/>\r\n" +
				"\t</h:inputText>\r\n" +
				"\t<h:outputLabel for=\"fooBaz\" id=\"fooBaz-label\" value=\"Baz:\"/>\r\n" +
				"\t<h:inputText id=\"fooBaz\" value=\"#{foo.baz}\">\r\n" +
				"\t\t<f:convertNumber currencySymbol=\"$\"/>\r\n" +
				"\t</h:inputText>\r\n" +
				"</h:panelGrid>\r\n";

		assertEquals( result, writer.toString() );
	}

	//
	// Inner class
	//

	public static class Foo {

		@UiAttribute( name = DATE_STYLE, value = "yyyy-MM-dd" )
		public Date	bar;

		@UiAttribute( name = CURRENCY_SYMBOL, value = "$" )
		public int	baz;
	}
}
