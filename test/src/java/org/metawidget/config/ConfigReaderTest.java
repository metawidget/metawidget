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

package org.metawidget.config;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import junit.framework.TestCase;

import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.metawidget.example.shared.addressbook.model.Gender;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.faces.FacesInspector;
import org.metawidget.inspector.gwt.remote.server.GwtRemoteInspectorImpl;
import org.metawidget.inspector.hibernate.validator.HibernateValidatorInspector;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.propertystyle.groovy.GroovyPropertyStyle;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.jpa.JpaInspector;
import org.metawidget.inspector.jsp.JspAnnotationInspector;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.inspector.spring.SpringAnnotationInspector;
import org.metawidget.inspector.struts.StrutsAnnotationInspector;
import org.metawidget.inspector.struts.StrutsInspector;
import org.metawidget.inspector.xml.XmlInspector;
import org.metawidget.jsp.tagext.html.BaseHtmlMetawidgetTag;
import org.metawidget.jsp.tagext.html.HtmlMetawidgetTag;
import org.metawidget.jsp.tagext.html.spring.SpringMetawidgetTag;
import org.metawidget.layout.iface.Layout;
import org.metawidget.pipeline.base.BasePipeline;
import org.metawidget.pipeline.w3c.W3CPipeline;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.widgetbuilder.SwingWidgetBuilder;
import org.metawidget.swing.widgetbuilder.swingx.SwingXWidgetBuilder;
import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessor;
import org.metawidget.util.IOUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.LogUtilsTest;
import org.metawidget.util.XmlUtils.CachingContentHandler;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.xml.sax.SAXException;

/**
 * @author Richard Kennard
 */

public class ConfigReaderTest
	extends TestCase {

	//
	// Public methods
	//

	public void testReader()
		throws Exception {

		// Configure

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"";
		xml += "	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
		xml += "	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "	<point xmlns=\"java:java.awt\">";
		xml += "		<location>";
		xml += "			<int>10</int>";
		xml += "			<int>20</int>";
		xml += "		</location>";
		xml += "	</point>";
		xml += "	<swingMetawidget xmlns=\"java:org.metawidget.swing\">";
		xml += "		<widgetBuilder>";
		xml += "			<compositeWidgetBuilder xmlns=\"java:org.metawidget.widgetbuilder.composite\" config=\"CompositeWidgetBuilderConfig\">";
		xml += "				<widgetBuilders>";
		xml += "					<array>";
		xml += "						<swingXWidgetBuilder xmlns=\"java:org.metawidget.swing.widgetbuilder.swingx\"/>";
		xml += "						<swingWidgetBuilder xmlns=\"java:org.metawidget.swing.widgetbuilder\"/>";
		xml += "					</array>";
		xml += "				</widgetBuilders>";
		xml += "			</compositeWidgetBuilder>";
		xml += "		</widgetBuilder>";
		xml += "		<inspector>";
		xml += "			<compositeInspector xmlns=\"java:org.metawidget.inspector.composite\"";
		xml += "					xsi:schemaLocation=\"java:org.metawidget.inspector.composite http://metawidget.org/xsd/org.metawidget.inspector.composite-1.0.xsd\"";
		xml += "					config=\"CompositeInspectorConfig\">";
		xml += "				<inspectors>";
		xml += "					<array>";
		xml += "						<metawidgetAnnotationInspector xmlns=\"java:org.metawidget.inspector.annotation\"/>";
		xml += "						<facesInspector xmlns=\"java:org.metawidget.inspector.faces\"/>";
		xml += "						<hibernateValidatorInspector xmlns=\"java:org.metawidget.inspector.hibernate.validator\"/>";
		xml += "						<propertyTypeInspector xmlns=\"java:org.metawidget.inspector.propertytype\" config=\"org.metawidget.inspector.impl.BaseObjectInspectorConfig\">";
		xml += "							<propertyStyle>";
		xml += "								<javaBeanPropertyStyle xmlns=\"java:org.metawidget.inspector.impl.propertystyle.javabean\" config=\"JavaBeanPropertyStyleConfig\">";
		xml += "									<privateFieldConvention>";
		xml += "										<format>'m'{0}{1}</format>";
		xml += "									</privateFieldConvention>";
		xml += "								</javaBeanPropertyStyle>";
		xml += "							</propertyStyle>";
		xml += "							<actionStyle>";
		xml += "								<metawidgetActionStyle xmlns=\"java:org.metawidget.inspector.impl.actionstyle.metawidget\"/>";
		xml += "							</actionStyle>";
		xml += "						</propertyTypeInspector>";
		xml += "						<jpaInspector xmlns=\"java:org.metawidget.inspector.jpa\" config=\"JpaInspectorConfig\">";
		xml += "							<propertyStyle>";
		xml += "								<javaBeanPropertyStyle xmlns=\"java:org.metawidget.inspector.impl.propertystyle.javabean\" config=\"JavaBeanPropertyStyleConfig\">";
		xml += "									<privateFieldConvention>";
		xml += "										<format>'m'{0}{1}</format>";
		xml += "									</privateFieldConvention>";
		xml += "								</javaBeanPropertyStyle>";
		xml += "							</propertyStyle>";
		xml += "						</jpaInspector>";
		xml += "						<jspAnnotationInspector xmlns=\"java:org.metawidget.inspector.jsp\"/>";
		xml += "						<springAnnotationInspector xmlns=\"java:org.metawidget.inspector.spring\"/>";
		xml += "						<strutsInspector xmlns=\"java:org.metawidget.inspector.struts\" config=\"StrutsInspectorConfig\">";
		xml += "							<inputStreams>";
		xml += "								<array>";
		xml += "									<resource>org/metawidget/inspector/struts/test-struts-config1.xml</resource>";
		xml += "									<resource>org/metawidget/inspector/struts/test-struts-config2.xml</resource>";
		xml += "								</array>";
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
		xml += "								<resource>org/metawidget/example/swt/addressbook/metawidget-metadata.xml</resource>";
		xml += "							</inputStream>";
		xml += "						</xmlInspector>";
		xml += "					</array>";
		xml += "				</inspectors>";
		xml += "			</compositeInspector>";
		xml += "		</inspector>";
		xml += "		<name>";
		xml += "			<string>foo</string>";
		xml += "		</name>";
		xml += "		<opaque>";
		xml += "			<boolean>true</boolean>";
		xml += "		</opaque>";
		xml += "	</swingMetawidget>";
		xml += "	<compositeInspector xmlns=\"java:org.metawidget.inspector.composite\" config=\"CompositeInspectorConfig\">";
		xml += "		<inspectors>";
		xml += "			<array>";
		xml += "				<metawidgetAnnotationInspector xmlns=\"java:org.metawidget.inspector.annotation\"/>";
		xml += "				<propertyTypeInspector xmlns=\"java:org.metawidget.inspector.propertytype\"/>";
		xml += "				<java5Inspector xmlns=\"java:org.metawidget.inspector.java5\"/>";
		xml += "			</array>";
		xml += "		</inspectors>";
		xml += "	</compositeInspector>";
		xml += "</metawidget>";

		// New Point

		ConfigReader configReader = new ValidatingConfigReader();
		Point point = configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Point.class );
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
		assertFalse( metawidget1.isOpaque() );
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), metawidget1 );
		assertTrue( "foo".equals( metawidget1.getName() ) );
		assertTrue( metawidget1.isOpaque() );

		// New SwingMetawidget with names

		metawidget1 = configReader.configure( new ByteArrayInputStream( xml.getBytes() ), SwingMetawidget.class, "name" );
		assertTrue( "foo".equals( metawidget1.getName() ) );
		assertFalse( metawidget1.isOpaque() );

		// Existing SwingMetawidget with names

		metawidget1.setName( "newFoo" );
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), metawidget1, "opaque" );
		assertTrue( "newFoo".equals( metawidget1.getName() ) );
		assertTrue( metawidget1.isOpaque() );

		// SwingMetawidget2

		SwingMetawidget metawidget2 = new SwingMetawidget();
		assertTrue( null == metawidget2.getName() );
		assertFalse( metawidget2.isOpaque() );
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), metawidget2 );

		// Test WidgetBuilder

		Field pipelineField = SwingMetawidget.class.getDeclaredField( "mPipeline" );
		pipelineField.setAccessible( true );
		@SuppressWarnings( "unchecked" )
		W3CPipeline<JComponent, JComponent, SwingMetawidget> pipeline1 = (W3CPipeline<JComponent, JComponent, SwingMetawidget>) pipelineField.get( metawidget1 );
		@SuppressWarnings( "unchecked" )
		W3CPipeline<JComponent, JComponent, SwingMetawidget> pipeline2 = (W3CPipeline<JComponent, JComponent, SwingMetawidget>) pipelineField.get( metawidget2 );

		Field widgetBuilderField = BasePipeline.class.getDeclaredField( "mWidgetBuilder" );
		widgetBuilderField.setAccessible( true );

		assertTrue( null == widgetBuilderField.get( pipeline1 ) );
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), metawidget1, "widgetBuilder" );

		@SuppressWarnings( "unchecked" )
		CompositeWidgetBuilder<JComponent, SwingMetawidget> compositeWidgetBuilder1 = (CompositeWidgetBuilder<JComponent, SwingMetawidget>) widgetBuilderField.get( pipeline1 );
		@SuppressWarnings( "unchecked" )
		CompositeWidgetBuilder<JComponent, SwingMetawidget> compositeWidgetBuilder2 = (CompositeWidgetBuilder<JComponent, SwingMetawidget>) widgetBuilderField.get( pipeline2 );

		// Will be the same, even though InputStreams

		assertTrue( compositeWidgetBuilder1 == compositeWidgetBuilder2 );
		WidgetBuilder<JComponent, SwingMetawidget>[] widgetBuilders = compositeWidgetBuilder1.getWidgetBuilders();

		assertTrue( widgetBuilders.length == 2 );
		assertTrue( widgetBuilders[0] instanceof SwingXWidgetBuilder );
		assertTrue( widgetBuilders[1] instanceof SwingWidgetBuilder );

		// Test Inspector

		Field inspectorField = BasePipeline.class.getDeclaredField( "mInspector" );
		inspectorField.setAccessible( true );

		assertTrue( null == inspectorField.get( pipeline1 ) );
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), metawidget1, "inspector" );

		CompositeInspector compositeInspector1 = (CompositeInspector) inspectorField.get( pipeline1 );
		CompositeInspector compositeInspector2 = (CompositeInspector) inspectorField.get( pipeline1 );

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

		// Test MessageFormat

		Field propertyStyle = BaseObjectInspector.class.getDeclaredField( "mPropertyStyle" );
		propertyStyle.setAccessible( true );
		JavaBeanPropertyStyle javaBeanPropertyStyle = (JavaBeanPropertyStyle) propertyStyle.get( inspectors[3] );
		Field privateFieldConvention = JavaBeanPropertyStyle.class.getDeclaredField( "mPrivateFieldConvention" );
		privateFieldConvention.setAccessible( true );
		MessageFormat format = (MessageFormat) privateFieldConvention.get( javaBeanPropertyStyle );
		assertEquals( format, new MessageFormat( "'m'{0}{1}" ));

		// Test re-caching of JavaBeanPropertyStyle with an embedded MessageFormat

		assertTrue( propertyStyle.get( inspectors[2] ) != propertyStyle.get( inspectors[3] ));
		assertTrue( propertyStyle.get( inspectors[3] ) == propertyStyle.get( inspectors[4] ));
	}

	public void testNoDefaultConstructor()
		throws Exception {

		// With config hint

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget>";
		xml += "	<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\"/>";
		xml += "</metawidget>";

		ConfigReader configReader = new ConfigReader();

		try {
			configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( "class org.metawidget.inspector.xml.XmlInspector does not have a default constructor. Did you mean config=\"XmlInspectorConfig\"?".equals( e.getMessage() ) );
		}

		// With redirected config hint

		xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget>";
		xml += "	<titledPanelLayoutDecorator xmlns=\"java:org.metawidget.swing.layout\"/>";
		xml += "</metawidget>";

		try {
			configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Layout.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( "class org.metawidget.swing.layout.TitledPanelLayoutDecorator does not have a default constructor. Did you mean config=\"org.metawidget.layout.decorator.LayoutDecoratorConfig\"?".equals( e.getMessage() ) );
		}

		// Without config hint

		xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget>";
		xml += "	<class xmlns=\"java:java.lang\"/>";
		xml += "</metawidget>";

		try {
			configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Class.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( "class java.lang.Class does not have a default constructor".equals( e.getMessage() ) );
		}
	}

	public void testBadUrl()
		throws Exception {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget>";
		xml += "	<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\" config=\"XmlInspectorConfig\">";
		xml += "		<inputStream>";
		xml += "			<url>http://foo.nowhere</url>";
		xml += "		</inputStream>";
		xml += "	</xmlInspector>";
		xml += "</metawidget>";

		ConfigReader configReader = new ConfigReader();

		try {
			configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			String message = e.getMessage();

			// If, bizzarely, the host actually does resolve (maybe your ISP puts in a special
			// page), you'll get a FileNotFoundException

			assertTrue( "java.net.UnknownHostException: foo.nowhere".equals( message ) || "java.io.FileNotFoundException: http://foo.nowhere".equals( message ) );
		}
	}

	public void testBadFile()
		throws Exception {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget>";
		xml += "	<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\" config=\"XmlInspectorConfig\">";
		xml += "		<inputStream>";
		xml += "			<file>/tmp/no.such.file</file>";
		xml += "		</inputStream>";
		xml += "	</xmlInspector>";
		xml += "</metawidget>";

		ConfigReader configReader = new ConfigReader();

		try {
			configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( e.getMessage().startsWith( "java.io.FileNotFoundException:" ) );
		}
	}

	public void testForgottenConfigAttribute()
		throws Exception {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<propertyTypeInspector xmlns=\"java:org.metawidget.inspector.propertytype\">";
		xml += "<propertyStyle><groovyPropertyStyle xmlns=\"java:org.metawidget.inspector.impl.propertystyle.groovy\"/></propertyStyle>";
		xml += "</propertyTypeInspector></metawidget>";

		try {
			ConfigReader configReader = new ConfigReader();
			configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( "java.lang.NoSuchMethodException: class org.metawidget.inspector.propertytype.PropertyTypeInspector.setPropertyStyle(GroovyPropertyStyle)".equals( e.getMessage() ) );
		}
	}

	public void testLikelyMethod()
		throws Exception {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<propertyTypeInspector xmlns=\"java:org.metawidget.inspector.propertytype\" config=\"org.metawidget.inspector.impl.BaseObjectInspectorConfig\">";
		xml += "<propertyStyle><boolean>true</boolean></propertyStyle>";
		xml += "</propertyTypeInspector></metawidget>";

		try {
			ConfigReader configReader = new ConfigReader();
			configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertEquals( "java.lang.NoSuchMethodException: class org.metawidget.inspector.impl.BaseObjectInspectorConfig.setPropertyStyle(Boolean). Did you mean setPropertyStyle(PropertyStyle)?", e.getMessage() );
		}

		xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<swingMetawidget xmlns=\"java:org.metawidget.swing\">";
		xml += "<inspectionResultProcessors><list><comesAfterInspectionResultProcessor xmlns=\"java:org.metawidget.inspectionresultprocessor.sort\"/></list></inspectionResultProcessors>";
		xml += "</swingMetawidget></metawidget>";

		try {
			ConfigReader configReader = new ConfigReader();
			configReader.configure( new ByteArrayInputStream( xml.getBytes() ), SwingMetawidget.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertEquals( "java.lang.NoSuchMethodException: class org.metawidget.swing.SwingMetawidget.setInspectionResultProcessors(ArrayList). Did you mean setInspectionResultProcessors(InspectionResultProcessor[])?", e.getMessage() );
		}
	}

	public void testSupportedTypes() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestInspectorConfig\">";
		xml += "<int><int>3</int></int>";
		xml += "<constant><constant>CONSTANT_VALUE</constant></constant>";
		xml += "<externalConstant><constant>javax.swing.SwingConstants.LEFT</constant></externalConstant>";
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
		xml += "<inputStream><resource>org/metawidget/swing/allwidgets/metawidget.xml</resource></inputStream>";
		xml += "<resourceBundle><bundle>org.metawidget.shared.allwidgets.resource.Resources</bundle></resourceBundle>";
		xml += "<stringArray><array><string>foo</string><string>bar</string></array></stringArray>";
		xml += "<gender><enum>MALE</enum></gender>";
		xml += "</testInspector>";
		xml += "</metawidget>";

		TestInspector inspector = new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
		assertTrue( 3 == inspector.getInt() );
		assertTrue( TestInspectorConfig.CONSTANT_VALUE == inspector.getConstant() );
		assertTrue( SwingConstants.LEFT == inspector.getExternalConstant() );

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
		IOUtils.streamBetween( inspector.getInputStream(), streamOut );
		assertTrue( streamOut.toString().contains( "<metawidget xmlns=\"http://metawidget.org\"" ) );

		assertTrue( "Limited textbox (i18n)".equals( inspector.getResourceBundle().getString( "limitedTextbox" ) ) );

		assertTrue( 2 == inspector.getStringArray().length );
		assertTrue( "foo".equals( inspector.getStringArray()[0] ) );
		assertTrue( "bar".equals( inspector.getStringArray()[1] ) );

		assertTrue( Gender.MALE.equals( inspector.getGender() ) );
	}

	public void testUnsupportedType() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestInspectorConfig\">";
		xml += "<date><date>1/1/2001</date></date>";
		xml += "</testInspector>";
		xml += "</metawidget>";

		try {
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( e.getMessage().endsWith( "No such class org.metawidget.config.Date or supported tag <date>" ) );
		}
	}

	public void testBadNamesapce() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"org.metawidget.config\" config=\"TestInspectorConfig\">";
		xml += "<date><date>1/1/2001</date></date>";
		xml += "</testInspector>";
		xml += "</metawidget>";

		try {
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( "org.xml.sax.SAXException: Namespace 'org.metawidget.config' of element <testInspector> must start with java:".equals( e.getMessage() ) );
		}
	}

	public void testEmptyCollection() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestInspectorConfig\">";
		xml += "<list>";
		xml += "<list/>";
		xml += "</list>";
		xml += "<set>";
		xml += "<set/>";
		xml += "</set>";
		xml += "</testInspector>";
		xml += "</metawidget>";

		TestInspector inspector = new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
		assertTrue( inspector.getList().isEmpty() );
		assertTrue( inspector.getSet().isEmpty() );
	}

	public void testMetawidgetExceptionDuringConstruction() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestInspectorConfig\">";
		xml += "<failDuringConstruction><boolean>true</boolean></failDuringConstruction>";
		xml += "</testInspector>";
		xml += "</metawidget>";

		try {
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( "Failed during construction".equals( e.getCause().getMessage() ) );
		}
	}

	public void testSetterWithNoParameters() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestInspectorConfig\">";
		xml += "<noParameters/>";
		xml += "</testInspector>";
		xml += "</metawidget>";

		try {
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( "java.lang.UnsupportedOperationException: Called setNoParameters".equals( e.getMessage() ) );
		}
	}

	public void testNoInspector() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "</metawidget>";

		try {
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( "No match for class org.metawidget.config.TestInspector within config".equals( e.getMessage() ) );
		}
	}

	public void testMultipleInspectors() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestInspectorConfig\"/>";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestInspectorConfig\"/>";
		xml += "</metawidget>";

		try {
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( "Already configured a class org.metawidget.config.TestInspector, ambiguous match with class org.metawidget.config.TestInspector".equals( e.getMessage() ) );
		}
	}

	public void testMissingResource() {

		ConfigReader configReader = new ConfigReader();

		try {
			configReader.configure( (String) null, null );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( "java.io.FileNotFoundException: No resource specified".equals( e.getMessage() ) );
		}

		try {
			configReader.configure( "", null );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( "java.io.FileNotFoundException: No resource specified".equals( e.getMessage() ) );
		}

		try {
			configReader.configure( " ", null );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( "java.io.FileNotFoundException: No resource specified".equals( e.getMessage() ) );
		}

		try {
			configReader.configure( " foo", null );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( "java.io.FileNotFoundException: Unable to locate  foo on CLASSPATH".equals( e.getMessage() ) );
		}
	}

	public void testCaching()
		throws Exception {

		ValidatingConfigReader configReader = new ValidatingConfigReader();
		configReader.configure( "org/metawidget/swing/allwidgets/metawidget.xml", SwingMetawidget.class );
		configReader.configure( "org/metawidget/swing/allwidgets/metawidget.xml", SwingMetawidget.class );
		configReader.configure( "org/metawidget/swing/allwidgets/metawidget.xml", SwingMetawidget.class );
		configReader.configure( "org/metawidget/gwt/allwidgets/metawidget.xml", GwtRemoteInspectorImpl.class );
		configReader.configure( "org/metawidget/gwt/allwidgets/metawidget.xml", GwtRemoteInspectorImpl.class );
		configReader.configure( "org/metawidget/gwt/allwidgets/metawidget.xml", GwtRemoteInspectorImpl.class );

		// (4 because each metawidget.xml contains a metawidget-metadata.xml)

		assertTrue( 4 == configReader.getOpenedResource() );
		assertTrue( 2 == configReader.mResourceCache.size() );

		// Check caching paused and unpaused correctly

		CachingContentHandler cachingContentHandler = configReader.mResourceCache.get( "org/metawidget/swing/allwidgets/metawidget.xml/org.metawidget.swing.SwingMetawidget" );
		Field cacheField = CachingContentHandler.class.getDeclaredField( "mCache" );
		cacheField.setAccessible( true );

		@SuppressWarnings( "unchecked" )
		List<Object> cache = (List<Object>) cacheField.get( cachingContentHandler );
		assertTrue( "startDocument".equals( cache.get( 0 ).toString() ) );
		assertTrue( "startPrefixMapping  http://metawidget.org".equals( cache.get( 1 ).toString() ) );
		assertTrue( "startPrefixMapping xsi http://www.w3.org/2001/XMLSchema-instance".equals( cache.get( 2 ).toString() ) );
		assertTrue( "startElement http://metawidget.org metawidget metawidget schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd java:org.metawidget.swing http://metawidget.org/xsd/org.metawidget.swing-1.0.xsd java:org.metawidget.inspector.composite http://metawidget.org/xsd/org.metawidget.inspector.composite-1.0.xsd java:org.metawidget.inspector.xml http://metawidget.org/xsd/org.metawidget.inspector.xml-1.0.xsd\" version=\"1.0\"".equals( cache.get( 3 ).toString() ) );
		assertTrue( "ignorableWhitespace \n\n\t".equals( cache.get( 4 ).toString() ));
		assertTrue( "startPrefixMapping  java:org.metawidget.swing".equals( cache.get( 5 ).toString() ) );
		assertTrue( "startElement java:org.metawidget.swing swingMetawidget swingMetawidget".equals( cache.get( 6 ).toString() ) );
		assertTrue( "characters \n\t\t".equals( cache.get( 7 ).toString() ) );
		assertTrue( "startElement java:org.metawidget.swing inspector inspector".equals( cache.get( 8 ).toString() ) );
		assertTrue( "characters \n\t\t\t".equals( cache.get( 9 ).toString() ) );
		assertTrue( "startPrefixMapping  java:org.metawidget.inspector.composite".equals( cache.get( 10 ).toString() ) );
		assertTrue( "startElement java:org.metawidget.inspector.composite compositeInspector compositeInspector config=\"CompositeInspectorConfig\"".equals( cache.get( 11 ).toString() ) );
		assertTrue( "endElement java:org.metawidget.inspector.composite compositeInspector compositeInspector".equals( cache.get( 12 ).toString() ) );
		assertTrue( "endPrefixMapping ".equals( cache.get( 13 ).toString() ) );
		assertTrue( "characters \n\t\t".equals( cache.get( 14 ).toString() ) );
		assertTrue( "endElement java:org.metawidget.swing inspector inspector".equals( cache.get( 15 ).toString() ) );
		assertTrue( "characters \n\t".equals( cache.get( 16 ).toString() ) );
		assertTrue( "endElement java:org.metawidget.swing swingMetawidget swingMetawidget".equals( cache.get( 17 ).toString() ) );
		assertTrue( "endPrefixMapping ".equals( cache.get( 18 ).toString() ) );
		assertTrue( "ignorableWhitespace \n\n".equals( cache.get( 19 ).toString() ) );
		assertTrue( "endElement http://metawidget.org metawidget metawidget".equals( cache.get( 20 ).toString() ) );
		assertTrue( "endPrefixMapping ".equals( cache.get( 21 ).toString() ) );
		assertTrue( "endPrefixMapping xsi".equals( cache.get( 22 ).toString() ) );
		assertTrue( "endDocument".equals( cache.get( 23 ).toString() ) );

		assertTrue( 24 == cache.size() );

		// Test caching with names (should not cache things outside the name)

		configReader.configure( "org/metawidget/config/metawidget-test-names.xml", SpringMetawidgetTag.class, "layout" );
		assertTrue( 3 == configReader.mResourceCache.size() );
		cachingContentHandler = configReader.mResourceCache.get( "org/metawidget/config/metawidget-test-names.xml/org.metawidget.jsp.tagext.html.spring.SpringMetawidgetTag/layout" );

		@SuppressWarnings( "unchecked" )
		List<Object> cacheWithNames = (List<Object>) cacheField.get( cachingContentHandler );
		assertTrue( "startElement java:org.metawidget.jsp.tagext.html.spring layout layout".equals( cacheWithNames.get( 10 ).toString() ) );
		assertTrue( "startElement java:org.metawidget.jsp.tagext.html.layout headingTagLayoutDecorator headingTagLayoutDecorator config=\"HeadingTagLayoutDecoratorConfig\"".equals( cacheWithNames.get( 13 ).toString() ) );
		assertTrue( "endElement java:org.metawidget.jsp.tagext.html.layout headingTagLayoutDecorator headingTagLayoutDecorator".equals( cacheWithNames.get( 14 ).toString() ) );
		assertTrue( "endElement java:org.metawidget.jsp.tagext.html.spring layout layout".equals( cacheWithNames.get( 17 ).toString() ) );

		assertTrue( 29 == cacheWithNames.size() );

		// Test scenarios that we've seen fail hard

		configReader.configure( "org/metawidget/config/metawidget-test-names.xml", SpringMetawidgetTag.class, "widgetBuilder" );
		assertTrue( 4 == configReader.mResourceCache.size() );
		configReader.configure( "org/metawidget/config/metawidget-test-names.xml", new SpringMetawidgetTag(), "widgetBuilder" );
		assertTrue( 4 == configReader.mResourceCache.size() );

		try {
			configReader.configure( "org/metawidget/config/metawidget-test-names.xml", new SpringMetawidgetTag() );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( "org.metawidget.inspector.iface.InspectorException: org.metawidget.iface.MetawidgetException: java.io.FileNotFoundException: Unable to locate metawidget-metadata.xml on CLASSPATH".equals( e.getMessage() ) );
		}

		assertTrue( 4 == configReader.mResourceCache.size() );
	}

	public void testLogging() {

		ConfigReader configReader = new ConfigReader();
		configReader.configure( "org/metawidget/config/metawidget-test-logging.xml", BaseHtmlMetawidgetTag.class, "widgetBuilder" );
		configReader.configure( "org/metawidget/config/metawidget-test-logging.xml", SpringMetawidgetTag.class, "widgetBuilder" );

		// Test it doesn't log 'Instantiated immutable class
		// org.metawidget.widgetbuilder.composite.CompositeWidgetBuilder' a second time

		if ( LogUtils.getLog( ConfigReader.class ).isDebugEnabled() ) {
			assertEquals( "Reading resource from org/metawidget/config/metawidget-test-logging.xml/org.metawidget.jsp.tagext.html.spring.SpringMetawidgetTag/widgetBuilder", LogUtilsTest.getLastDebugMessage() );
		} else {
			assertTrue( !LogUtils.getLog( ConfigReader.class ).isDebugEnabled() );
			assertEquals( "Reading resource from {0}", LogUtilsTest.getLastDebugMessage() );
			assertEquals( "org/metawidget/config/metawidget-test-logging.xml/org.metawidget.jsp.tagext.html.spring.SpringMetawidgetTag/widgetBuilder", LogUtilsTest.getLastDebugArguments()[0] );
		}
	}

	public void testImmutable()
		throws Exception {

		// Via InputStream

		String startXml = "<?xml version=\"1.0\"?>";
		startXml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		startXml += "<compositeInspector xmlns=\"java:org.metawidget.inspector.composite\" config=\"CompositeInspectorConfig\">";
		startXml += "<inspectors>";
		startXml += "<array>";
		startXml += "<propertyTypeInspector xmlns=\"java:org.metawidget.inspector.propertytype\"/>";
		startXml += "<metawidgetAnnotationInspector xmlns=\"java:org.metawidget.inspector.annotation\"/>";
		startXml += "<jspAnnotationInspector xmlns=\"java:org.metawidget.inspector.jsp\" config=\"org.metawidget.inspector.impl.BaseObjectInspectorConfig\">";
		startXml += "<propertyStyle><javaBeanPropertyStyle xmlns=\"java:org.metawidget.inspector.impl.propertystyle.javabean\"/></propertyStyle>";
		startXml += "</jspAnnotationInspector>";
		startXml += "<strutsAnnotationInspector xmlns=\"java:org.metawidget.inspector.struts\" config=\"org.metawidget.inspector.impl.BaseObjectInspectorConfig\">";
		startXml += "<propertyStyle>";
		startXml += "<groovyPropertyStyle xmlns=\"java:org.metawidget.inspector.impl.propertystyle.groovy\" config=\"org.metawidget.inspector.impl.propertystyle.BasePropertyStyleConfig\"><excludeBaseType><pattern>foo</pattern></excludeBaseType></groovyPropertyStyle>";
		startXml += "</propertyStyle>";
		startXml += "</strutsAnnotationInspector>";
		startXml += "<springAnnotationInspector xmlns=\"java:org.metawidget.inspector.spring\" config=\"org.metawidget.inspector.impl.BaseObjectInspectorConfig\">";
		startXml += "<propertyStyle>";
		startXml += "<groovyPropertyStyle xmlns=\"java:org.metawidget.inspector.impl.propertystyle.groovy\" config=\"org.metawidget.inspector.impl.propertystyle.BasePropertyStyleConfig\"><excludeBaseType><pattern>foo</pattern></excludeBaseType></groovyPropertyStyle>";
		startXml += "</propertyStyle>";
		startXml += "</springAnnotationInspector>";
		String endXml = "</array>";
		endXml += "</inspectors>";
		endXml += "</compositeInspector>";
		endXml += "</metawidget>";

		String xml = startXml + endXml;

		ConfigReader configReader = new ConfigReader();
		Inspector inspector1 = configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
		Inspector inspector2 = configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );

		// Inspectors should be the same, even though InputStreams are not cached, because cached at
		// Config level

		assertTrue( inspector1 == inspector2 );

		xml = startXml;
		xml += "<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\" config=\"XmlInspectorConfig\">";
		xml += "<inputStream><resource>org/metawidget/example/swing/tutorial/metawidget-metadata.xml</resource></inputStream>";
		xml += "</xmlInspector>";
		xml += endXml;

		inspector1 = configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
		inspector2 = configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );

		// Inspectors should not be the same, because caching at Config level should be thwarted by
		// InputStream

		assertTrue( inspector1 != inspector2 );

		Field inspectorsField = CompositeInspector.class.getDeclaredField( "mInspectors" );
		inspectorsField.setAccessible( true );

		Inspector[] inspectors1 = (Inspector[]) inspectorsField.get( inspector1 );
		Inspector[] inspectors2 = (Inspector[]) inspectorsField.get( inspector2 );
		assertTrue( inspectors1 != inspectors2 );

		// Sub-inspectors should be the same, because are cached at the Config level

		assertTrue( inspectors1[0] == inspectors2[0] );
		assertTrue( inspectors1[1] == inspectors2[1] );
		assertTrue( inspectors1[2] == inspectors2[2] );
		assertTrue( inspectors1[3] == inspectors2[3] );

		// PropertyStyle should be shared across Inspectors

		Field propertyStyleField = BaseObjectInspector.class.getDeclaredField( "mPropertyStyle" );
		propertyStyleField.setAccessible( true );

		assertTrue( propertyStyleField.get( inspectors1[0] ) == propertyStyleField.get( inspectors1[1] ) );
		assertTrue( propertyStyleField.get( inspectors2[0] ) == propertyStyleField.get( inspectors2[0] ) );
		assertTrue( propertyStyleField.get( inspectors2[0] ) == propertyStyleField.get( inspectors2[1] ) );

		assertTrue( propertyStyleField.get( inspectors1[3] ) == propertyStyleField.get( inspectors1[4] ) );
		assertTrue( propertyStyleField.get( inspectors1[3] ) == propertyStyleField.get( inspectors2[3] ) );
		assertTrue( propertyStyleField.get( inspectors2[3] ) == propertyStyleField.get( inspectors2[4] ) );

		// Via resource

		SwingMetawidget metawidget1 = configReader.configure( "org/metawidget/swing/allwidgets/metawidget.xml", SwingMetawidget.class );
		SwingMetawidget metawidget2 = configReader.configure( "org/metawidget/swing/allwidgets/metawidget.xml", SwingMetawidget.class );

		assertTrue( metawidget1 != metawidget2 );

		Field pipelineField = SwingMetawidget.class.getDeclaredField( "mPipeline" );
		pipelineField.setAccessible( true );
		@SuppressWarnings( "unchecked" )
		W3CPipeline<JComponent, JComponent, SwingMetawidget> pipeline1 = (W3CPipeline<JComponent, JComponent, SwingMetawidget>) pipelineField.get( metawidget1 );
		@SuppressWarnings( "unchecked" )
		W3CPipeline<JComponent, JComponent, SwingMetawidget> pipeline2 = (W3CPipeline<JComponent, JComponent, SwingMetawidget>) pipelineField.get( metawidget2 );
		assertTrue( pipeline1 != pipeline2 );

		// Inspectors should be the same, because resources are cached even though it contains
		// InputStreams

		assertTrue( pipeline1.getInspector() == pipeline2.getInspector() );

		// Test what got cached

		Map<Class<?>, Map<Object, Object>> immutableByClassCache = configReader.mImmutableByClassCache;
		assertFalse( immutableByClassCache.containsKey( Class.class ) );

		Map<Object, Object> immutableByConfigCache = immutableByClassCache.get( CompositeInspector.class );
		assertFalse( immutableByConfigCache.containsKey( ConfigReader.IMMUTABLE_NO_CONFIG ) );
		assertTrue( 4 == immutableByConfigCache.size() );

		immutableByConfigCache = immutableByClassCache.get( StrutsAnnotationInspector.class );
		assertFalse( immutableByConfigCache.containsKey( ConfigReader.IMMUTABLE_NO_CONFIG ) );
		assertTrue( 1 == immutableByConfigCache.size() );
		assertTrue( inspectors1[3] == immutableByConfigCache.values().iterator().next() );

		immutableByConfigCache = immutableByClassCache.get( XmlInspector.class );
		assertFalse( immutableByConfigCache.containsKey( ConfigReader.IMMUTABLE_NO_CONFIG ) );
		assertTrue( 3 == immutableByConfigCache.size() );

		immutableByConfigCache = immutableByClassCache.get( MetawidgetAnnotationInspector.class );
		assertTrue( immutableByConfigCache.containsKey( ConfigReader.IMMUTABLE_NO_CONFIG ) );
		assertTrue( 1 == immutableByConfigCache.size() );
		assertTrue( inspectors1[1] == immutableByConfigCache.get( ConfigReader.IMMUTABLE_NO_CONFIG ) );

		immutableByConfigCache = immutableByClassCache.get( SpringAnnotationInspector.class );
		assertFalse( immutableByConfigCache.containsKey( ConfigReader.IMMUTABLE_NO_CONFIG ) );
		assertTrue( 1 == immutableByConfigCache.size() );
		assertTrue( inspectors1[4] == immutableByConfigCache.values().iterator().next() );

		immutableByConfigCache = immutableByClassCache.get( PropertyTypeInspector.class );
		assertTrue( immutableByConfigCache.containsKey( ConfigReader.IMMUTABLE_NO_CONFIG ) );
		assertTrue( 1 == immutableByConfigCache.size() );
		assertTrue( inspectors1[0] == immutableByConfigCache.get( ConfigReader.IMMUTABLE_NO_CONFIG ) );

		immutableByConfigCache = immutableByClassCache.get( GroovyPropertyStyle.class );
		assertTrue( 1 == immutableByConfigCache.size() );
		assertTrue( propertyStyleField.get( inspectors1[3] ) == immutableByConfigCache.values().iterator().next() );

		immutableByConfigCache = immutableByClassCache.get( JavaBeanPropertyStyle.class );
		assertTrue( immutableByConfigCache.containsKey( ConfigReader.IMMUTABLE_NO_CONFIG ) );
		assertTrue( 1 == immutableByConfigCache.size() );
		assertTrue( propertyStyleField.get( inspectors1[2] ) == immutableByConfigCache.get( ConfigReader.IMMUTABLE_NO_CONFIG ) );

		assertTrue( 9 == immutableByClassCache.size() );
	}

	public void testPatternCache()
		throws Exception {

		assertFalse( Pattern.compile( "foo" ).equals( Pattern.compile( "foo" ) ) );

		ConfigReader configReader = new ConfigReader();
		assertTrue( configReader.createNative( "pattern", null, "foo" ).equals( configReader.createNative( "pattern", null, "foo" ) ) );
	}

	public void testUppercase() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<TestInspector xmlns=\"java:org.metawidget.config\"/>";
		xml += "</metawidget>";

		try {
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( "XML node 'TestInspector' should start with a lowercase letter".equals( e.getMessage() ) );
		}
	}

	public void testBadConfigImplementation() {

		// No equals

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestNoEqualsInspectorConfig\"/>";
		xml += "</metawidget>";

		try {
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( "class org.metawidget.config.TestNoEqualsInspectorConfig does not override .equals(), so cannot cache reliably".equals( e.getMessage() ) );
		}

		// No hashCode

		xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestNoHashCodeInspectorConfig\"/>";
		xml += "</metawidget>";

		try {
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( "class org.metawidget.config.TestNoHashCodeInspectorConfig does not override .hashCode(), so cannot cache reliably".equals( e.getMessage() ) );
		}

		// Unbalanced

		xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestUnbalancedEqualsInspectorConfig\"/>";
		xml += "</metawidget>";

		try {
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );

			// assertTrue( false );
			//
			// (works running JUnit in Eclipse, but not via Ant. Does the VM cache reflection
			// results or something?)
		} catch ( MetawidgetException e ) {
			assertTrue( "class org.metawidget.config.TestNoHashCodeInspectorConfig implements .equals(), but .hashCode() is implemented by class org.metawidget.config.TestUnbalancedEqualsInspectorConfig, so cannot cache reliably".equals( e.getMessage() ) );
		}

		// No such constructor

		xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"java.lang.String\"/>";
		xml += "</metawidget>";

		try {
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( "class org.metawidget.config.TestInspector does not have a constructor that takes a class java.lang.String, as specified by your config attribute".equals( e.getMessage() ) );
		}

		// Different constructor

		xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\" config=\"java.lang.String\"/>";
		xml += "</metawidget>";

		try {
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), XmlInspector.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( "class org.metawidget.inspector.xml.XmlInspector does not have a constructor that takes a class java.lang.String, as specified by your config attribute. Did you mean config=\"XmlInspectorConfig\"?".equals( e.getMessage() ) );
		}

		// Config-less constructor

		xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<object xmlns=\"java:java.lang\" config=\"java.lang.String\"/>";
		xml += "</metawidget>";

		try {
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), Object.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( "class java.lang.Object does not have a constructor that takes a class java.lang.String, as specified by your config attribute. It only has a config-less constructor".equals( e.getMessage() ) );
		}

		// Superclass does, but subclass doesn't, but no methods

		xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestNoEqualsSubclassInspectorConfig\"/>";
		xml += "</metawidget>";

		LogUtilsTest.clearLastWarnMessage();

		new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );

		assertTrue( null == LogUtilsTest.getLastWarnMessage() );

		// Superclass does, but subclass doesn't, and has methods

		xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestNoEqualsHasMethodsSubclassInspectorConfig\"/>";
		xml += "</metawidget>";

		new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );

		assertEquals( "class org.metawidget.config.TestNoEqualsHasMethodsSubclassInspectorConfig does not override .equals() (only its superclass org.metawidget.config.TestInspectorConfig does), so may not be cached reliably", LogUtilsTest.getLastWarnMessage() );

		// Overridden, but uses super.hashCode

		xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestDumbHashCodeInspectorConfig\"/>";
		xml += "</metawidget>";

		new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );

		assertTrue( "class org.metawidget.config.TestDumbHashCodeInspectorConfig overrides .hashCode(), but it returns the same as System.identityHashCode, so cannot be cached reliably".equals( LogUtilsTest.getLastWarnMessage() ) );
	}

	public void testEnum()
		throws Exception {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<beansBindingProcessor xmlns=\"java:org.metawidget.swing.widgetprocessor.binding.beansbinding\" config=\"BeansBindingProcessorConfig\">";
		xml += "	<updateStrategy><enum>READ_WRITE</enum></updateStrategy>";
		xml += "</beansBindingProcessor>";
		xml += "</metawidget>";

		BeansBindingProcessor processor = new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), BeansBindingProcessor.class );
		Field updateStrategyField = BeansBindingProcessor.class.getDeclaredField( "mUpdateStrategy" );
		updateStrategyField.setAccessible( true );
		assertTrue( UpdateStrategy.READ_WRITE.equals( updateStrategyField.get( processor ) ) );
	}

	public void testOnlyCacheIfSuccessful() {

		ConfigReader configReader = new ConfigReader() {

			private int	mOpenResource;

			@Override
			public InputStream openResource( String resource ) {

				String xml = "<?xml version=\"1.0\"?>";
				xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
				xml += "<htmlMetawidgetTag xmlns=\"java:org.metawidget.jsp.tagext.html\">";
				xml += "	<inspector>";
				xml += "		<compositeInspector xmlns=\"java:org.metawidget.inspector.composite\" config=\"CompositeInspectorConfig\">";
				xml += "			<inspectors>";
				xml += "				<array>";
				xml += "					<propertyTypeInspector xmlns=\"java:org.metawidget.inspector.propertytype\"/>";
				xml += "					<metawidgetAnnotationInspector xmlns=\"java:org.metawidget.inspector.annotation\"/>";
				xml += "					<java5Inspector xmlns=\"java:org.metawidget.inspector.java5\"/>";
				xml += "					<facesInspector xmlns=\"java:org.metawidget.inspector.faces\"/>";
				xml += "				</array>";
				xml += "			</inspectors>";
				xml += "		</compositeInspector>";
				xml += "	</inspector>";
				xml += "	<widgetBuilder>";

				switch ( mOpenResource ) {
					case 0:
						xml += "		<compositeWidgetBuilderFoo xmlns=\"java:org.metawidget.widgetbuilder.composite\" config=\"CompositeWidgetBuilderConfig\">";
						xml += "			<widgetBuilders>";
						xml += "				<array>";
						xml += "					<overriddenWidgetBuilder xmlns=\"java:org.metawidget.faces.component.widgetbuilder\"/>";
						xml += "					<readOnlyWidgetBuilder xmlns=\"java:org.metawidget.faces.component.html.widgetbuilder\"/>";
						xml += "				</array>";
						xml += "			</widgetBuilders>";
						xml += "		</compositeWidgetBuilderFoo>";
						break;

					default:
						xml += "		<compositeWidgetBuilder xmlns=\"java:org.metawidget.widgetbuilder.composite\" config=\"CompositeWidgetBuilderConfig\">";
						xml += "			<widgetBuilders>";
						xml += "				<array>";
						xml += "					<overriddenWidgetBuilder xmlns=\"java:org.metawidget.faces.component.widgetbuilder\"/>";
						xml += "					<readOnlyWidgetBuilder xmlns=\"java:org.metawidget.faces.component.html.widgetbuilder\"/>";
						xml += "				</array>";
						xml += "			</widgetBuilders>";
						xml += "		</compositeWidgetBuilder>";
				}

				mOpenResource++;

				xml += "	</widgetBuilder>";
				xml += "</htmlMetawidgetTag>";
				xml += "</metawidget>";

				return new ByteArrayInputStream( xml.getBytes() );
			}
		};

		try {
			configReader.configure( "foo", HtmlMetawidgetTag.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertEquals( "No such class org.metawidget.widgetbuilder.composite.CompositeWidgetBuilderFoo or supported tag <compositeWidgetBuilderFoo>", e.getMessage() );
		}

		assertTrue( configReader.configure( "foo", HtmlMetawidgetTag.class ) != null );
	}

	//
	// Inner class
	//

	static class ValidatingConfigReader
		extends ConfigReader {

		//
		// Private members
		//

		private int	mOpenedResource;

		//
		// Constructor
		//

		public ValidatingConfigReader() {

			SchemaFactory factory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
			InputStream in = super.openResource( "org/metawidget/config/metawidget-1.0.xsd" );

			try {
				mFactory.setSchema( factory.newSchema( new StreamSource( in ) ) );
			} catch ( SAXException e ) {
				throw MetawidgetException.newException( e );
			}
		}

		//
		// Public methods
		//

		@Override
		public InputStream openResource( String resource ) {

			mOpenedResource++;

			return super.openResource( resource );
		}

		public int getOpenedResource() {

			return mOpenedResource;
		}
	}
}