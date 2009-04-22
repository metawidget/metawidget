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

package org.metawidget.test.inspector;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import junit.framework.TestCase;

import org.metawidget.inspector.ConfigReader;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.faces.FacesInspector;
import org.metawidget.inspector.hibernate.validator.HibernateValidatorInspector;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.jpa.JpaInspector;
import org.metawidget.inspector.jsp.JspAnnotationInspector;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.inspector.spring.SpringAnnotationInspector;
import org.metawidget.inspector.struts.StrutsAnnotationInspector;
import org.metawidget.inspector.struts.StrutsInspector;
import org.metawidget.inspector.xml.XmlInspector;
import org.metawidget.mixin.base.BaseMetawidgetMixin;
import org.metawidget.mixin.w3c.MetawidgetMixin;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.widgetbuilder.SwingWidgetBuilder;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.xml.sax.SAXException;

/**
 * @author Richard Kennard
 */

public class ConfigReaderTest
	extends TestCase
{
	//
	// Private statics
	//

	private final static int	BUFFER_SIZE	= 1024 * 64;

	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public ConfigReaderTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testReader()
		throws Exception
	{
		// Configure

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\" version=\"1.0\">";
		xml += "	<point xmlns=\"urn:java:java.awt\">";
		xml += "		<location>";
		xml += "			<int>10</int>";
		xml += "			<int>20</int>";
		xml += "		</location>";
		xml += "	</point>";
		xml += "	<swingMetawidget xmlns=\"urn:java:org.metawidget.swing\">";
		xml += "		<widgetBuilder>";
		xml += "			<compositeWidgetBuilder xmlns=\"urn:java:org.metawidget.widgetbuilder.composite\" config=\"CompositeWidgetBuilderConfig\">";
		xml += "				<widgetBuilders>";
		xml += "					<list>";
		xml += "						<swingWidgetBuilder xmlns=\"urn:java:org.metawidget.swing.widgetbuilder\"/>";
		xml += "					</list>";
		xml += "				</widgetBuilders>";
		xml += "			</compositeWidgetBuilder>";
		xml += "		</widgetBuilder>";
		xml += "		<inspector>";
		xml += "			<compositeInspector xmlns=\"urn:java:org.metawidget.inspector.composite\" config=\"CompositeInspectorConfig\">";
		xml += "				<inspectors>";
		xml += "					<list>";
		xml += "						<metawidgetAnnotationInspector xmlns=\"urn:java:org.metawidget.inspector.annotation\"/>";
		xml += "						<facesInspector xmlns=\"java:org.metawidget.inspector.faces\"/>";
		xml += "						<hibernateValidatorInspector xmlns=\"java:org.metawidget.inspector.hibernate.validator\"/>";
		xml += "						<propertyTypeInspector xmlns=\"java:org.metawidget.inspector.propertytype\" config=\"org.metawidget.inspector.impl.BaseObjectInspectorConfig\">";
		xml += "							<propertyStyle>";
		xml += "								<class>org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle</class>";
		xml += "							</propertyStyle>";
		xml += "							<actionStyle>";
		xml += "								<class>org.metawidget.inspector.impl.actionstyle.metawidget.MetawidgetActionStyle</class>";
		xml += "							</actionStyle>";
		xml += "						</propertyTypeInspector>";
		xml += "						<jpaInspector xmlns=\"java:org.metawidget.inspector.jpa\"/>";
		xml += "						<jspAnnotationInspector xmlns=\"java:org.metawidget.inspector.jsp\"/>";
		xml += "						<springAnnotationInspector xmlns=\"java:org.metawidget.inspector.spring\"/>";
		xml += "						<strutsInspector xmlns=\"java:org.metawidget.inspector.struts\" config=\"StrutsInspectorConfig\">";
		xml += "							<inputStreams>";
		xml += "								<list>";
		xml += "									<resource>org/metawidget/test/inspector/struts/test-struts-config1.xml</resource>";
		xml += "									<resource>org/metawidget/test/inspector/struts/test-struts-config2.xml</resource>";
		xml += "								</list>";
		xml += "							</inputStreams>";
		xml += "						</strutsInspector>";
		xml += "						<strutsAnnotationInspector xmlns=\"java:org.metawidget.inspector.struts\"/>";
		xml += "						<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\" config=\"XmlInspectorConfig\">";
		xml += "							<inputStream>";
		xml += "								<resource>org/metawidget/example/swing/addressbook/metawidget-metadata.xml</resource>";
		xml += "							</inputStream>";
		xml += "						</xmlInspector>";
		xml += "						<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\" config=\"XmlInspectorConfig\">";
		xml += "							<inputStream>";
		xml += "								<resource>org/metawidget/example/swing/addressbook/metawidget-metadata.xml</resource>";
		xml += "							</inputStream>";
		xml += "						</xmlInspector>";
		xml += "					</list>";
		xml += "				</inspectors>";
		xml += "			</compositeInspector>";
		xml += "		</inspector>";
		xml += "		<name>";
		xml += "			<string>foo</string>";
		xml += "		</name>";
		xml += "		<opaque>";
		xml += "			<boolean>true</boolean>";
		xml += "		</opaque>";
		xml += "		<parameter>";
		xml += "			<string>a parameter</string>";
		xml += "			<int>1</int>";
		xml += "		</parameter>";
		xml += "		<parameter>";
		xml += "			<string>another parameter</string>";
		xml += "			<int>5</int>";
		xml += "		</parameter>";
		xml += "	</swingMetawidget>";
		xml += "	<compositeInspector xmlns=\"java:org.metawidget.inspector.composite\" config=\"CompositeInspectorConfig\">";
		xml += "		<inspectors>";
		xml += "			<list>";
		xml += "				<metawidgetAnnotationInspector xmlns=\"java:org.metawidget.inspector.annotation\"/>";
		xml += "				<propertyTypeInspector xmlns=\"java:org.metawidget.inspector.propertytype\"/>";
		xml += "				<java5Inspector xmlns=\"java:org.metawidget.inspector.java5\"/>";
		xml += "			</list>";
		xml += "		</inspectors>";
		xml += "	</compositeInspector>";
		xml += "</metawidget>";

		// New Point

		ConfigReader configReader = new ValidatingConfigReader();
		Point point = (Point) configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Point.class );
		assertTrue( 10 == point.x );
		assertTrue( 20 == point.y );

		// Existing Point

		point = new Point();
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), point );
		assertTrue( 10 == point.x );
		assertTrue( 20 == point.y );

		// SwingMetawidget

		SwingMetawidget metawidget1 = new SwingMetawidget();
		assertTrue( null == metawidget1.getName() );
		assertTrue( !metawidget1.isOpaque() );
		assertTrue( null == metawidget1.getParameter( "a parameter" ) );
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), metawidget1 );

		// Test

		assertTrue( "foo".equals( metawidget1.getName() ) );
		assertTrue( metawidget1.isOpaque() );
		assertTrue( 1 == (Integer) metawidget1.getParameter( "a parameter" ) );
		assertTrue( 5 == (Integer) metawidget1.getParameter( "another parameter" ) );

		// SwingMetawidget2

		SwingMetawidget metawidget2 = new SwingMetawidget();
		assertTrue( null == metawidget2.getName() );
		assertTrue( !metawidget2.isOpaque() );
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), metawidget2 );

		// Test WidgetBuilder

		Field mixinField = SwingMetawidget.class.getDeclaredField( "mMetawidgetMixin" );
		mixinField.setAccessible( true );
		@SuppressWarnings( "unchecked" )
		MetawidgetMixin<JComponent, SwingMetawidget> mixin1 = (MetawidgetMixin<JComponent, SwingMetawidget>) mixinField.get( metawidget1 );
		@SuppressWarnings( "unchecked" )
		MetawidgetMixin<JComponent, SwingMetawidget> mixin2 = (MetawidgetMixin<JComponent, SwingMetawidget>) mixinField.get( metawidget2 );

		Field widgetBuilderField = BaseMetawidgetMixin.class.getDeclaredField( "mWidgetBuilder" );
		widgetBuilderField.setAccessible( true );
		@SuppressWarnings( "unchecked" )
		CompositeWidgetBuilder<JComponent, SwingMetawidget> compositeWidgetBuilder1 = (CompositeWidgetBuilder<JComponent, SwingMetawidget>) widgetBuilderField.get( mixin1 );
		@SuppressWarnings( "unchecked" )
		CompositeWidgetBuilder<JComponent, SwingMetawidget> compositeWidgetBuilder2 = (CompositeWidgetBuilder<JComponent, SwingMetawidget>) widgetBuilderField.get( mixin2 );

		assertTrue( compositeWidgetBuilder1 == compositeWidgetBuilder2 );

		Field widgetBuildersField = CompositeWidgetBuilder.class.getDeclaredField( "mWidgetBuilders" );
		widgetBuildersField.setAccessible( true );
		@SuppressWarnings( "unchecked" )
		WidgetBuilder<JComponent, SwingMetawidget>[] widgetBuilders = (WidgetBuilder<JComponent, SwingMetawidget>[]) widgetBuildersField.get( compositeWidgetBuilder1 );

		assertTrue( widgetBuilders.length == 1 );
		assertTrue( widgetBuilders[0] instanceof SwingWidgetBuilder );

		// Test Inspector

		Field inspectorField = BaseMetawidgetMixin.class.getDeclaredField( "mInspector" );
		inspectorField.setAccessible( true );
		CompositeInspector compositeInspector1 = (CompositeInspector) inspectorField.get( mixin1 );
		CompositeInspector compositeInspector2 = (CompositeInspector) inspectorField.get( mixin1 );

		assertTrue( compositeInspector1 == compositeInspector2 );

		Field inspectorsField = CompositeInspector.class.getDeclaredField( "mInspectors" );
		inspectorsField.setAccessible( true );
		Inspector[] inspectors = (Inspector[]) inspectorsField.get( compositeInspector1 );

		assertTrue( inspectors.length == 11 );
		assertTrue( inspectors[0] instanceof MetawidgetAnnotationInspector );
		assertTrue( inspectors[1] instanceof FacesInspector );
		assertTrue( inspectors[2] instanceof HibernateValidatorInspector );
		assertTrue( inspectors[3] instanceof PropertyTypeInspector );
		assertTrue( inspectors[4] instanceof JpaInspector );
		assertTrue( inspectors[5] instanceof JspAnnotationInspector );
		assertTrue( inspectors[6] instanceof SpringAnnotationInspector );
		assertTrue( inspectors[7] instanceof StrutsInspector );
		assertTrue( inspectors[8] instanceof StrutsAnnotationInspector );
		assertTrue( inspectors[9] instanceof XmlInspector );
		assertTrue( inspectors[10] instanceof XmlInspector );

		// Inspector

		Inspector inspector1 = (Inspector) configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
		assertTrue( inspector1 instanceof CompositeInspector );

		Inspector inspector2 = (Inspector) configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
		assertTrue( inspector2 instanceof CompositeInspector );
		assertTrue( inspector1 == inspector2 );
	}

	public void testNoDefaultConstructor()
		throws Exception
	{
		// With config hint

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget>";
		xml += "	<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\"/>";
		xml += "</metawidget>";

		ConfigReader configReader = new ConfigReader();

		try
		{
			configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
			assertTrue( false );
		}
		catch ( InspectorException e )
		{
			assertTrue( "class org.metawidget.inspector.xml.XmlInspector does not have a default constructor. Did you mean config=\"XmlInspectorConfig\"?".equals( e.getMessage() ) );
		}

		// Without config hint

		xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget>";
		xml += "	<Class xmlns=\"java:java.lang\"/>";
		xml += "</metawidget>";

		try
		{
			configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Class.class );
			assertTrue( false );
		}
		catch ( InspectorException e )
		{
			assertTrue( "class java.lang.Class does not have a default constructor".equals( e.getMessage() ) );
		}
	}

	public void testBadUrl()
		throws Exception
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget>";
		xml += "	<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\" config=\"XmlInspectorConfig\">";
		xml += "		<inputStream>";
		xml += "			<url>http://foo.nowhere</url>";
		xml += "		</inputStream>";
		xml += "	</xmlInspector>";
		xml += "</metawidget>";

		ConfigReader configReader = new ConfigReader();

		try
		{
			configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
			assertTrue( false );
		}
		catch ( InspectorException e )
		{
			assertTrue( "java.net.UnknownHostException: foo.nowhere".equals( e.getMessage() ) );
		}
	}

	public void testForgottenConfigAttribute()
		throws Exception
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<propertyTypeInspector xmlns=\"java:org.metawidget.inspector.propertytype\">";
		xml += "<propertyStyle><class>org.metawidget.inspector.impl.propertystyle.groovy.GroovyPropertyStyle</class></propertyStyle>";
		xml += "</propertyTypeInspector></metawidget>";

		try
		{
			ConfigReader configReader = new ConfigReader();
			configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
			assertTrue( false );
		}
		catch ( InspectorException e )
		{
			assertTrue( "java.lang.NoSuchMethodException: class org.metawidget.inspector.propertytype.PropertyTypeInspector.setPropertyStyle( class java.lang.Class )".equals( e.getMessage() ) );
		}
	}

	public void testSupportedTypes()
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<badInspector xmlns=\"java:org.metawidget.test.inspector\" config=\"BadInspectorConfig\">";
		xml += "<int><int>3</int></int>";
		xml += "<list>";
		xml += "<list>";
		xml += "<string>foo</string>";
		xml += "<string>bar</string>";
		xml += "<class>java.lang.String</class>";
		xml += "<class>java.util.Date</class>";
		xml += "<class>java.lang.Long</class>";
		xml += "<null/>";
		xml += "</list>";
		xml += "</list>";
		xml += "<set>";
		xml += "<set>";
		xml += "<string>baz</string>";
		xml += "</set>";
		xml += "</set>";
		xml += "<boolean><boolean>true</boolean></boolean>";
		xml += "<pattern><pattern>.*?</pattern></pattern>";
		xml += "<inputStream><resource>org/metawidget/test/swing/allwidgets/metawidget.xml</resource></inputStream>";
		xml += "<resourceBundle><bundle>org.metawidget.test.shared.allwidgets.resource.Resources</bundle></resourceBundle>";
		xml += "</badInspector>";
		xml += "</metawidget>";

		BadInspector inspector = (BadInspector) new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), BadInspector.class );
		assertTrue( 3 == inspector.getInt() );

		List<Object> list = inspector.getList();
		assertTrue( "foo".equals( list.get( 0 ) ) );
		assertTrue( "bar".equals( list.get( 1 ) ) );
		assertTrue( String.class.equals( list.get( 2 ) ) );
		assertTrue( Date.class.equals( list.get( 3 ) ) );
		assertTrue( Long.class.equals( list.get( 4 ) ) );
		assertTrue( null == list.get( 5 ) );
		assertTrue( 6 == list.size() );

		Set<Object> set = inspector.getSet();
		assertTrue( "baz".equals( set.iterator().next() ) );

		assertTrue( true == inspector.isBoolean() );
		assertTrue( ".*?".equals( inspector.getPattern().toString() ) );

		ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
		streamBetween( inspector.getInputStream(), streamOut );
		assertTrue( streamOut.toString().contains( "<metawidget xmlns=\"http://metawidget.org\"" ) );

		assertTrue( "Limited textbox (i18n)".equals( inspector.getResourceBundle().getString( "limitedTextbox" ) ) );
	}

	public void testUnsupportedType()
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<badInspector xmlns=\"java:org.metawidget.test.inspector\" config=\"BadInspectorConfig\">";
		xml += "<date><date>1/1/2001</date></date>";
		xml += "</badInspector>";
		xml += "</metawidget>";

		try
		{
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), BadInspector.class );
			assertTrue( false );
		}
		catch ( InspectorException e )
		{
			assertTrue( e.getMessage().endsWith( "No such class org.metawidget.test.inspector.Date or supported tag <date>" ) );
		}
	}

	public void testInspectorExceptionDuringConstruction()
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<badInspector xmlns=\"java:org.metawidget.test.inspector\" config=\"BadInspectorConfig\">";
		xml += "<failDuringConstruction><boolean>true</boolean></failDuringConstruction>";
		xml += "</badInspector>";
		xml += "</metawidget>";

		try
		{
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), BadInspector.class );
			assertTrue( false );
		}
		catch ( InspectorException e )
		{
			assertTrue( "Failed during construction".equals( e.getCause().getMessage() ) );
		}
	}

	public void testSetterWithNoParameters()
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<badInspector xmlns=\"java:org.metawidget.test.inspector\" config=\"BadInspectorConfig\">";
		xml += "<noParameters/>";
		xml += "</badInspector>";
		xml += "</metawidget>";

		try
		{
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), BadInspector.class );
			assertTrue( false );
		}
		catch ( InspectorException e )
		{
			assertTrue( "java.lang.UnsupportedOperationException: Called setNoParameters".equals( e.getMessage() ) );
		}
	}

	public void testNoInspector()
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "</metawidget>";

		try
		{
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), BadInspector.class );
			assertTrue( false );
		}
		catch ( InspectorException e )
		{
			assertTrue( "No match for class org.metawidget.test.inspector.BadInspector within config".equals( e.getMessage() ) );
		}
	}

	public void testMultipleInspectors()
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<badInspector xmlns=\"java:org.metawidget.test.inspector\" config=\"BadInspectorConfig\"/>";
		xml += "<badInspector xmlns=\"java:org.metawidget.test.inspector\" config=\"BadInspectorConfig\"/>";
		xml += "</metawidget>";

		try
		{
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), BadInspector.class );
			assertTrue( false );
		}
		catch ( InspectorException e )
		{
			assertTrue( "Already configured a class org.metawidget.test.inspector.BadInspector, ambiguous match with class org.metawidget.test.inspector.BadInspector".equals( e.getMessage() ) );
		}
	}

	public void testMissingResource()
	{
		ConfigReader configReader = new ConfigReader();

		try
		{
			configReader.configure( (String) null, null );
			assertTrue( false );
		}
		catch ( InspectorException e )
		{
			assertTrue( "No resource specified".equals( e.getMessage() ) );
		}

		try
		{
			configReader.configure( "", null );
			assertTrue( false );
		}
		catch ( InspectorException e )
		{
			assertTrue( "No resource specified".equals( e.getMessage() ) );
		}

		try
		{
			configReader.configure( " ", null );
			assertTrue( false );
		}
		catch ( InspectorException e )
		{
			assertTrue( "No resource specified".equals( e.getMessage() ) );
		}

		try
		{
			configReader.configure( " foo", null );
			assertTrue( false );
		}
		catch ( InspectorException e )
		{
			assertTrue( "Unable to locate  foo on CLASSPATH".equals( e.getMessage() ) );
		}
	}

	public void testCaching()
	{
		ValidatingConfigReader configReader = new ValidatingConfigReader();
		configReader.configure( "org/metawidget/test/swing/allwidgets/metawidget.xml", SwingMetawidget.class );
		configReader.configure( "org/metawidget/test/swing/allwidgets/metawidget.xml", SwingMetawidget.class );
		configReader.configure( "org/metawidget/test/swing/allwidgets/metawidget.xml", SwingMetawidget.class );
		configReader.configure( "org/metawidget/test/gwt/allwidgets/metawidget.xml", Inspector.class );
		configReader.configure( "org/metawidget/test/gwt/allwidgets/metawidget.xml", Inspector.class );
		configReader.configure( "org/metawidget/test/gwt/allwidgets/metawidget.xml", Inspector.class );

		// (4 because each metawidget.xml contains a metawidget-metadata.xml)

		assertTrue( 4 == configReader.getOpenedResource() );
	}

	//
	// Private methods
	//

	private void streamBetween( InputStream in, OutputStream out )
	{
		try
		{
			int iCount;

			// (must create a local buffer for Thread-safety)

			byte[] byteData = new byte[BUFFER_SIZE];

			while ( ( iCount = in.read( byteData, 0, BUFFER_SIZE ) ) != -1 )
			{
				out.write( byteData, 0, iCount );
			}
		}
		catch ( Exception e )
		{
			throw new RuntimeException( e );
		}
		finally
		{
			try
			{
				out.close();
				in.close();
			}
			catch ( Exception e )
			{
				throw new RuntimeException( e );
			}
		}
	}

	//
	// Inner class
	//

	class ValidatingConfigReader
		extends ConfigReader
	{
		//
		// Private members
		//

		private int	mOpenedResource;

		//
		// Constructor
		//

		public ValidatingConfigReader()
		{
			SchemaFactory factory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
			InputStream in = super.openResource( "org/metawidget/inspector/metawidget-1.0.xsd" );

			try
			{
				mFactory.setSchema( factory.newSchema( new StreamSource( in ) ) );
			}
			catch ( SAXException e )
			{
				throw InspectorException.newException( e );
			}
		}

		//
		// Public methods
		//

		@Override
		public InputStream openResource( String resource )
		{
			mOpenedResource++;

			return super.openResource( resource );
		}

		public int getOpenedResource()
		{
			return mOpenedResource;
		}
	}
}