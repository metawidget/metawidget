package org.metawidget.statically.faces.component.html.richfaces;

import java.awt.Color;
import java.io.StringWriter;
import java.util.Date;

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
		metawidget.setValueExpression( "value", "#{foo}" );
		metawidget.setPath( Foo.class.getName() );

		StringWriter writer = new StringWriter();
		metawidget.write( writer );

		String result = "<h:panelGrid columns=\"2\">\r\n" +
				"\t<h:outputLabel for=\"fooBar\" value=\"Bar:\"/>\r\n" +
				"\t<h:inputText id=\"fooBar\" value=\"#{foo.bar}\"/>\r\n" +
				"\t<h:outputLabel for=\"fooColor\" value=\"Color:\"/>\r\n" +
				"\t<rich:colorPicker id=\"fooColor\" value=\"#{foo.color}\"/>\r\n" +
				"\t<h:outputLabel for=\"fooDate\" value=\"Date:\"/>\r\n" +
				"\t<rich:calendar id=\"fooDate\" value=\"#{foo.date}\"/>\r\n" +
				"\t<h:outputLabel for=\"fooSlider\" value=\"Slider:\"/>\r\n" +
				"\t<rich:inputNumberSlider id=\"fooSlider\" maxValue=\"8\" minValue=\"3\" value=\"#{foo.slider}\"/>\r\n" +
				"\t<h:outputLabel for=\"fooSpinner\" value=\"Spinner:\"/>\r\n" +
				"\t<rich:inputNumberSpinner cycled=\"false\" id=\"fooSpinner\" value=\"#{foo.spinner}\"/>\r\n" +
				"\t<h:outputLabel for=\"fooSpinnerWithMax\" value=\"Spinner with max:\"/>\r\n" +
				"\t<rich:inputNumberSpinner cycled=\"false\" id=\"fooSpinnerWithMax\" maxValue=\"9\" value=\"#{foo.spinnerWithMax}\"/>\r\n" +
				"\t<h:outputLabel for=\"fooSpinnerWithMin\" value=\"Spinner with min:\"/>\r\n" +
				"\t<rich:inputNumberSpinner cycled=\"false\" id=\"fooSpinnerWithMin\" minValue=\"2\" value=\"#{foo.spinnerWithMin}\"/>\r\n" +
				"</h:panelGrid>\r\n";

		assertEquals( result, writer.toString() );
	}

	//
	// Inner class
	//

	public static class Foo {

		public String	bar;

		public int		spinner;

		@Min( 2 )
		public int		spinnerWithMin;

		@Max( 9 )
		public int		spinnerWithMax;

		@Range( min = 3, max = 8 )
		public int		slider;

		public Color	color;

		public Date		date;
	}
}
