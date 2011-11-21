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
import static org.metawidget.inspector.faces.StaticFacesInspectionResultConstants.*;

import java.util.Date;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.statically.faces.component.html.StaticHtmlMetawidget;
import org.metawidget.statically.faces.component.html.layout.HtmlPanelGrid;
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
		assertEquals( "<h:inputText/>", htmlInputText.toString() );

		// Empty attributes get no Converters

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "foo" );
		processor.processWidget( htmlInputText, PROPERTY, attributes, null );
		assertEquals( "<h:inputText/>", htmlInputText.toString() );

		// Implicit DateTimeConverter

		attributes.put( TYPE, Date.class.getName() );
		processor.processWidget( htmlInputText, PROPERTY, attributes, null );
		assertEquals( "<h:inputText><f:convertDateTime/></h:inputText>", htmlInputText.toString() );
		htmlInputText = new HtmlInputText();

		// DateTimeConverter

		attributes.put( DATE_STYLE, "full" );
		attributes.put( TIME_STYLE, "medium" );
		attributes.put( LOCALE, "UK" );
		attributes.put( DATETIME_PATTERN, "dd/MM/yyyy" );
		attributes.put( TIME_ZONE, "Australia/Sydney" );
		attributes.put( DATETIME_TYPE, "date" );
		processor.processWidget( htmlInputText, PROPERTY, attributes, null );
		assertEquals( "<h:inputText><f:convertDateTime dateStyle=\"full\" locale=\"UK\" pattern=\"dd/MM/yyyy\" timeStyle=\"medium\" timeZone=\"Australia/Sydney\" type=\"date\"/></h:inputText>", htmlInputText.toString() );

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
		assertEquals( "<h:inputText><f:convertNumber currencyCode=\"AUD\" currencySymbol=\"$\" groupingUsed=\"true\" locale=\"AU\" maxFractionDigits=\"1\" maxIntegerDigits=\"5\" minFractionDigits=\"0\" minIntegerDigits=\"2\" pattern=\"#0.00\" type=\"currency\"/></h:inputText>", htmlInputText.toString() );

		// Explicit Converter

		htmlInputText = new HtmlInputText();
		attributes.put( FACES_CONVERTER_ID, "#{foo}" );
		processor.processWidget( htmlInputText, PROPERTY, attributes, null );
		assertEquals( "<h:inputText converter=\"#{foo}\"/>", htmlInputText.toString() );
	}

	public void testSimpleType() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setValue( "#{foo}" );
		metawidget.setPath( Foo.class.getName() );

		String result = "<h:panelGrid columns=\"3\">\r\n" +
				"\t<h:outputLabel for=\"fooBar\" value=\"Bar:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"fooBar\" value=\"#{foo.bar}\">\r\n" +
				"\t\t\t<f:convertDateTime dateStyle=\"yyyy-MM-dd\"/>\r\n" +
				"\t\t</h:inputText>\r\n" +
				"\t\t<h:message for=\"fooBar\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"fooBaz\" value=\"Baz:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"fooBaz\" value=\"#{foo.baz}\">\r\n" +
				"\t\t\t<f:convertNumber currencySymbol=\"$\"/>\r\n" +
				"\t\t</h:inputText>\r\n" +
				"\t\t<h:message for=\"fooBaz\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"</h:panelGrid>\r\n";

		assertEquals( result, metawidget.toString() );

		Map<String, String> namespaces = metawidget.getNamespaces();
		assertEquals( "http://java.sun.com/jsf/html", namespaces.get( "h" ) );
		assertEquals( "http://java.sun.com/jsf/core", namespaces.get( "f" ) );
		assertEquals( 2, namespaces.size() );
	}

	public void testConverterNotSupported()
		throws Exception {

		// Supports Converter

		StandardConverterProcessor processor = new StandardConverterProcessor();

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( LOCALE, "AU" );

		HtmlInputText htmlInputText = new HtmlInputText();
		processor.processWidget( htmlInputText, PROPERTY, attributes, null );
		assertEquals( "<h:inputText/>", htmlInputText.toString() );

		// Does not support Converter

		HtmlPanelGrid htmlPanelGrid = new HtmlPanelGrid();
		processor.processWidget( htmlPanelGrid, PROPERTY, null, null );
		assertEquals( "<h:panelGrid/>", htmlPanelGrid.toString() );
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
