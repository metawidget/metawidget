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

package org.metawidget.statically.faces.component.html.richfaces;

import java.awt.Color;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;

import junit.framework.TestCase;

import org.hibernate.validator.Max;
import org.hibernate.validator.Min;
import org.hibernate.validator.Range;
import org.metawidget.statically.faces.component.html.StaticHtmlMetawidget;

public class RichFacesWidgetBuilderTest
	extends TestCase {

	//
	// Public methods
	//

	public void testRichFacesWidgets() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setConfig( "org/metawidget/statically/faces/component/html/richfaces/metawidget.xml" );
		metawidget.setValue( "#{foo}" );
		metawidget.setPath( Foo.class.getName() );

		StringWriter writer = new StringWriter();
		metawidget.write( writer );

		String result = "<h:panelGrid columns=\"3\">\r\n" +
				"\t<h:outputLabel for=\"fooBar\" value=\"Bar:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"fooBar\" value=\"#{foo.bar}\"/>\r\n" +
				"\t\t<h:message for=\"fooBar\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"fooColor\" value=\"Color:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<rich:colorPicker id=\"fooColor\" value=\"#{foo.color}\"/>\r\n" +
				"\t\t<h:message for=\"fooColor\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"fooDate\" value=\"Date:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<rich:calendar id=\"fooDate\" value=\"#{foo.date}\"/>\r\n" +
				"\t\t<h:message for=\"fooDate\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"fooSlider\" value=\"Slider:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<rich:inputNumberSlider id=\"fooSlider\" maxValue=\"8\" minValue=\"3\" value=\"#{foo.slider}\"/>\r\n" +
				"\t\t<h:message for=\"fooSlider\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"fooSpinner\" value=\"Spinner:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<rich:inputNumberSpinner cycled=\"false\" id=\"fooSpinner\" maxValue=\"999999999\" value=\"#{foo.spinner}\"/>\r\n" +
				"\t\t<h:message for=\"fooSpinner\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"fooSpinnerWithMax\" value=\"Spinner With Max:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<rich:inputNumberSpinner cycled=\"false\" id=\"fooSpinnerWithMax\" maxValue=\"9\" value=\"#{foo.spinnerWithMax}\"/>\r\n" +
				"\t\t<h:message for=\"fooSpinnerWithMax\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"fooSpinnerWithMin\" value=\"Spinner With Min:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<rich:inputNumberSpinner cycled=\"false\" id=\"fooSpinnerWithMin\" maxValue=\"999999999\" minValue=\"2\" value=\"#{foo.spinnerWithMin}\"/>\r\n" +
				"\t\t<h:message for=\"fooSpinnerWithMin\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"</h:panelGrid>\r\n";

		writer = new StringWriter();
		metawidget.write( writer, 0 );
		assertEquals( result, writer.toString() );

		Map<String, String> namespaces = metawidget.getNamespaces();
		assertEquals( "http://java.sun.com/jsf/html", namespaces.get( "h" ) );
		assertEquals( "http://richfaces.org/rich", namespaces.get( "rich" ) );
		assertEquals( 2, namespaces.size() );
	}

	//
	// Inner class
	//

	public static class Foo {

		public String getBar() {

			return null;
		}

		public void setBar( @SuppressWarnings( "unused" ) String bar ) {

			// Do nothing
		}

		public int getSpinner() {

			return 0;
		}

		public void setSpinner( @SuppressWarnings( "unused" ) int spinner ) {

			// Do nothing
		}

		@Min( 2 )
		public int getSpinnerWithMin() {

			return 0;
		}

		public void setSpinnerWithMin( @SuppressWarnings( "unused" ) int spinnerWithMin ) {

			// Do nothing
		}

		@Max( 9 )
		public int getSpinnerWithMax() {

			return 0;
		}

		public void setSpinnerWithMax( @SuppressWarnings( "unused" ) int spinnerWithMax ) {

			// Do nothing
		}

		@Range( min = 3, max = 8 )
		public int getSlider() {

			return 0;
		}

		public void setSlider( @SuppressWarnings( "unused" ) int slider ) {

			// Do nothing
		}

		public Color getColor() {

			return null;
		}

		public void setColor( @SuppressWarnings( "unused" ) Color color ) {

			// Do nothing
		}

		public Date getDate() {

			return null;
		}

		public void setDate( @SuppressWarnings( "unused" ) Date date ) {

			// Do nothing
		}
	}
}
