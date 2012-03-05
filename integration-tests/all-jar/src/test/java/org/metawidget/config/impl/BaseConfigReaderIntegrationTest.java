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

package org.metawidget.config.impl;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import junit.framework.TestCase;

import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.metawidget.config.iface.ConfigReader;
import org.metawidget.config.iface.ResourceResolver;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.beanvalidation.BeanValidationInspector;
import org.metawidget.inspector.composite.CompositeInspector;
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
import org.metawidget.jsp.tagext.html.HtmlMetawidgetTag;
import org.metawidget.jsp.tagext.html.spring.SpringMetawidgetTag;
import org.metawidget.pipeline.base.BasePipeline;
import org.metawidget.pipeline.w3c.W3CPipeline;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.widgetbuilder.SwingWidgetBuilder;
import org.metawidget.swing.widgetbuilder.swingx.SwingXWidgetBuilder;
import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessor;
import org.metawidget.util.XmlUtils.CachingContentHandler;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.xml.sax.SAXException;

/**
 * @author Richard Kennard
 */

public class BaseConfigReaderIntegrationTest
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
		xml += "						<beanValidationInspector xmlns=\"java:org.metawidget.inspector.beanvalidation\"/>";
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
		xml += "								<resource>org/metawidget/config/metawidget-test-reader-metadata.xml</resource>";
		xml += "							</inputStream>";
		xml += "						</xmlInspector>";
		xml += "						<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\" config=\"XmlInspectorConfig\">";
		xml += "							<inputStream>";
		xml += "								<resource>org/metawidget/config/metawidget-test-reader-metadata2.xml</resource>";
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
		xml += "			</array>";
		xml += "		</inspectors>";
		xml += "	</compositeInspector>";
		xml += "</metawidget>";

		// New Point

		ConfigReader configReader = new ValidatingConfigReader();
		Point point = (Point) configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Point.class );
		assertEquals( 10, point.x );
		assertEquals( 20, point.y );

		// Existing Point

		point = new Point();
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), point );
		assertEquals( 10, point.x );
		assertEquals( 20, point.y );

		// SwingMetawidget

		SwingMetawidget metawidget1 = new SwingMetawidget();
		assertEquals( null, metawidget1.getName() );
		assertFalse( metawidget1.isOpaque() );
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), metawidget1 );
		assertTrue( "foo".equals( metawidget1.getName() ) );
		assertTrue( metawidget1.isOpaque() );

		// New SwingMetawidget with names

		metawidget1 = (SwingMetawidget) configReader.configure( new ByteArrayInputStream( xml.getBytes() ), SwingMetawidget.class, "name" );
		assertTrue( "foo".equals( metawidget1.getName() ) );
		assertFalse( metawidget1.isOpaque() );

		// Existing SwingMetawidget with names

		metawidget1.setName( "newFoo" );
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), metawidget1, "opaque" );
		assertTrue( "newFoo".equals( metawidget1.getName() ) );
		assertTrue( metawidget1.isOpaque() );

		// SwingMetawidget2

		SwingMetawidget metawidget2 = new SwingMetawidget();
		assertEquals( null, metawidget2.getName() );
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

		assertEquals( null, widgetBuilderField.get( pipeline1 ) );
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), metawidget1, "widgetBuilder" );

		@SuppressWarnings( "unchecked" )
		CompositeWidgetBuilder<JComponent, SwingMetawidget> compositeWidgetBuilder1 = (CompositeWidgetBuilder<JComponent, SwingMetawidget>) widgetBuilderField.get( pipeline1 );
		@SuppressWarnings( "unchecked" )
		CompositeWidgetBuilder<JComponent, SwingMetawidget> compositeWidgetBuilder2 = (CompositeWidgetBuilder<JComponent, SwingMetawidget>) widgetBuilderField.get( pipeline2 );

		// Will be the same, even though InputStreams

		assertEquals( compositeWidgetBuilder1, compositeWidgetBuilder2 );
		WidgetBuilder<JComponent, SwingMetawidget>[] widgetBuilders = compositeWidgetBuilder1.getWidgetBuilders();

		assertEquals( widgetBuilders.length, 2 );
		assertTrue( widgetBuilders[0] instanceof SwingXWidgetBuilder );
		assertTrue( widgetBuilders[1] instanceof SwingWidgetBuilder );

		// Test Inspector

		Field inspectorField = BasePipeline.class.getDeclaredField( "mInspector" );
		inspectorField.setAccessible( true );

		assertEquals( null, inspectorField.get( pipeline1 ) );
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), metawidget1, "inspector" );

		CompositeInspector compositeInspector1 = (CompositeInspector) inspectorField.get( pipeline1 );
		CompositeInspector compositeInspector2 = (CompositeInspector) inspectorField.get( pipeline1 );

		assertEquals( compositeInspector1, compositeInspector2 );

		Field inspectorsField = CompositeInspector.class.getDeclaredField( "mInspectors" );
		inspectorsField.setAccessible( true );
		Inspector[] inspectors = (Inspector[]) inspectorsField.get( compositeInspector1 );

		assertEquals( inspectors.length, 11 );
		assertTrue( inspectors[0] instanceof MetawidgetAnnotationInspector );
		assertTrue( inspectors[1] instanceof BeanValidationInspector );
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
		assertEquals( format, new MessageFormat( "'m'{0}{1}" ) );

		// Test re-caching of JavaBeanPropertyStyle with an embedded MessageFormat

		assertTrue( propertyStyle.get( inspectors[2] ) != propertyStyle.get( inspectors[3] ) );
		assertEquals( propertyStyle.get( inspectors[3] ), propertyStyle.get( inspectors[4] ) );
	}

	public void testCaching()
		throws Exception {

		ValidatingConfigReader configReader = new ValidatingConfigReader();
		configReader.configure( "org/metawidget/config/metawidget-test-caching.xml", SwingMetawidget.class );
		configReader.configure( "org/metawidget/config/metawidget-test-caching.xml", SwingMetawidget.class );
		configReader.configure( "org/metawidget/config/metawidget-test-caching.xml", SwingMetawidget.class );
		configReader.configure( "org/metawidget/config/metawidget-test-caching2.xml", GwtRemoteInspectorImpl.class );
		configReader.configure( "org/metawidget/config/metawidget-test-caching2.xml", GwtRemoteInspectorImpl.class );
		configReader.configure( "org/metawidget/config/metawidget-test-caching2.xml", GwtRemoteInspectorImpl.class );

		// (4 because each metawidget-allwidgets.xml contains a metawidget-metadata.xml)

		assertEquals( 4, ((CountingResourceResolver) configReader.getResourceResolver()).getOpenedResource() );
		assertEquals( 2, configReader.mResourceCache.size() );

		// Check caching paused and unpaused correctly

		CachingContentHandler cachingContentHandler = configReader.mResourceCache.get( "org/metawidget/config/metawidget-test-caching.xml/org.metawidget.swing.SwingMetawidget" );
		Field cacheField = CachingContentHandler.class.getDeclaredField( "mCache" );
		cacheField.setAccessible( true );

		@SuppressWarnings( "unchecked" )
		List<Object> cache = (List<Object>) cacheField.get( cachingContentHandler );
		assertTrue( "startDocument".equals( cache.get( 0 ).toString() ) );
		assertTrue( "startPrefixMapping  http://metawidget.org".equals( cache.get( 1 ).toString() ) );
		assertTrue( "startPrefixMapping xsi http://www.w3.org/2001/XMLSchema-instance".equals( cache.get( 2 ).toString() ) );
		assertTrue( "startElement http://metawidget.org metawidget metawidget schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd java:org.metawidget.swing http://metawidget.org/xsd/org.metawidget.swing-1.0.xsd java:org.metawidget.inspector.composite http://metawidget.org/xsd/org.metawidget.inspector.composite-1.0.xsd java:org.metawidget.inspector.xml http://metawidget.org/xsd/org.metawidget.inspector.xml-1.0.xsd\" version=\"1.0\"".equals( cache.get( 3 ).toString() ) );
		assertTrue( "ignorableWhitespace \n\n\t".equals( cache.get( 4 ).toString() ) );
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

		assertEquals( 24, cache.size() );

		// Test caching with names (should not cache things outside the name)

		configReader.configure( "org/metawidget/config/metawidget-test-names.xml", SpringMetawidgetTag.class, "layout" );
		assertEquals( 3, configReader.mResourceCache.size() );
		cachingContentHandler = configReader.mResourceCache.get( "org/metawidget/config/metawidget-test-names.xml/org.metawidget.jsp.tagext.html.spring.SpringMetawidgetTag/layout" );

		@SuppressWarnings( "unchecked" )
		List<Object> cacheWithNames = (List<Object>) cacheField.get( cachingContentHandler );
		assertEquals( "startElement java:org.metawidget.jsp.tagext.html.spring layout layout", cacheWithNames.get( 10 ).toString() );
		assertEquals( "startElement java:org.metawidget.jsp.tagext.html.layout headingTagLayoutDecorator headingTagLayoutDecorator config=\"HeadingTagLayoutDecoratorConfig\"", cacheWithNames.get( 13 ).toString() );
		assertEquals( "endElement java:org.metawidget.jsp.tagext.html.layout headingTagLayoutDecorator headingTagLayoutDecorator", cacheWithNames.get( 14 ).toString() );
		assertEquals( "endElement java:org.metawidget.jsp.tagext.html.spring layout layout", cacheWithNames.get( 17 ).toString() );

		assertEquals( 29, cacheWithNames.size() );

		// Test scenarios that we've seen fail hard

		configReader.configure( "org/metawidget/config/metawidget-test-names.xml", SpringMetawidgetTag.class, "widgetBuilder" );
		assertEquals( 4, configReader.mResourceCache.size() );
		configReader.configure( "org/metawidget/config/metawidget-test-names.xml", new SpringMetawidgetTag(), "widgetBuilder" );
		assertEquals( 4, configReader.mResourceCache.size() );

		try {
			configReader.configure( "org/metawidget/config/metawidget-test-names.xml", new SpringMetawidgetTag() );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertEquals( "org.metawidget.inspector.iface.InspectorException: java.io.FileNotFoundException: Unable to locate metawidget-metadata.xml on CLASSPATH", e.getMessage() );
		}

		assertEquals( 4, configReader.mResourceCache.size() );
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
		startXml += "<groovyPropertyStyle xmlns=\"java:org.metawidget.inspector.impl.propertystyle.groovy\" config=\"GroovyPropertyStyleConfig\"><excludeBaseType><pattern>foo</pattern></excludeBaseType></groovyPropertyStyle>";
		startXml += "</propertyStyle>";
		startXml += "</strutsAnnotationInspector>";
		startXml += "<springAnnotationInspector xmlns=\"java:org.metawidget.inspector.spring\" config=\"org.metawidget.inspector.impl.BaseObjectInspectorConfig\">";
		startXml += "<propertyStyle>";
		startXml += "<groovyPropertyStyle xmlns=\"java:org.metawidget.inspector.impl.propertystyle.groovy\" config=\"GroovyPropertyStyleConfig\"><excludeBaseType><pattern>foo</pattern></excludeBaseType></groovyPropertyStyle>";
		startXml += "</propertyStyle>";
		startXml += "</springAnnotationInspector>";
		String endXml = "</array>";
		endXml += "</inspectors>";
		endXml += "</compositeInspector>";
		endXml += "</metawidget>";

		String xml = startXml + endXml;

		ConfigReader configReader = new BaseConfigReader();
		Inspector inspector1 = (Inspector) configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
		Inspector inspector2 = (Inspector) configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );

		// Inspectors should be the same, even though InputStreams are not cached, because cached at
		// Config level

		assertEquals( inspector1, inspector2 );

		xml = startXml;
		xml += "<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\" config=\"XmlInspectorConfig\">";
		xml += "<inputStream><resource>org/metawidget/config/metawidget-tutorial-metadata.xml</resource></inputStream>";
		xml += "</xmlInspector>";
		xml += endXml;

		inspector1 = (Inspector) configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
		inspector2 = (Inspector) configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );

		// Inspectors should not be the same, because caching at Config level should be thwarted by
		// InputStream

		assertTrue( inspector1 != inspector2 );

		Field inspectorsField = CompositeInspector.class.getDeclaredField( "mInspectors" );
		inspectorsField.setAccessible( true );

		Inspector[] inspectors1 = (Inspector[]) inspectorsField.get( inspector1 );
		Inspector[] inspectors2 = (Inspector[]) inspectorsField.get( inspector2 );
		assertTrue( inspectors1 != inspectors2 );

		// Sub-inspectors should be the same, because are cached at the Config level

		assertEquals( inspectors1[0], inspectors2[0] );
		assertEquals( inspectors1[1], inspectors2[1] );
		assertEquals( inspectors1[2], inspectors2[2] );
		assertEquals( inspectors1[3], inspectors2[3] );

		// PropertyStyle should be shared across Inspectors

		Field propertyStyleField = BaseObjectInspector.class.getDeclaredField( "mPropertyStyle" );
		propertyStyleField.setAccessible( true );

		assertEquals( propertyStyleField.get( inspectors1[0] ), propertyStyleField.get( inspectors1[1] ) );
		assertEquals( propertyStyleField.get( inspectors2[0] ), propertyStyleField.get( inspectors2[0] ) );
		assertEquals( propertyStyleField.get( inspectors2[0] ), propertyStyleField.get( inspectors2[1] ) );

		assertEquals( propertyStyleField.get( inspectors1[3] ), propertyStyleField.get( inspectors1[4] ) );
		assertEquals( propertyStyleField.get( inspectors1[3] ), propertyStyleField.get( inspectors2[3] ) );
		assertEquals( propertyStyleField.get( inspectors2[3] ), propertyStyleField.get( inspectors2[4] ) );

		// Via resource

		SwingMetawidget metawidget1 = (SwingMetawidget) configReader.configure( "org/metawidget/config/metawidget-test-caching.xml", SwingMetawidget.class );
		SwingMetawidget metawidget2 = (SwingMetawidget) configReader.configure( "org/metawidget/config/metawidget-test-caching.xml", SwingMetawidget.class );

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

		assertEquals( pipeline1.getInspector(), pipeline2.getInspector() );

		// Test what got cached

		Field immutableByClassCacheField = BaseConfigReader.class.getDeclaredField( "mImmutableByClassCache" );
		immutableByClassCacheField.setAccessible( true );

		@SuppressWarnings( "unchecked" )
		Map<Class<?>, Map<Object, Object>> immutableByClassCache = (Map<Class<?>, Map<Object, Object>>) immutableByClassCacheField.get( configReader );
		assertFalse( immutableByClassCache.containsKey( Class.class ) );

		Map<Object, Object> immutableByConfigCache = immutableByClassCache.get( CompositeInspector.class );
		assertFalse( immutableByConfigCache.containsKey( BaseConfigReader.IMMUTABLE_NO_CONFIG ) );
		assertEquals( 4, immutableByConfigCache.size() );

		immutableByConfigCache = immutableByClassCache.get( StrutsAnnotationInspector.class );
		assertFalse( immutableByConfigCache.containsKey( BaseConfigReader.IMMUTABLE_NO_CONFIG ) );
		assertEquals( 1, immutableByConfigCache.size() );
		assertEquals( inspectors1[3], immutableByConfigCache.values().iterator().next() );

		immutableByConfigCache = immutableByClassCache.get( XmlInspector.class );
		assertFalse( immutableByConfigCache.containsKey( BaseConfigReader.IMMUTABLE_NO_CONFIG ) );
		assertEquals( 3, immutableByConfigCache.size() );

		immutableByConfigCache = immutableByClassCache.get( MetawidgetAnnotationInspector.class );
		assertTrue( immutableByConfigCache.containsKey( BaseConfigReader.IMMUTABLE_NO_CONFIG ) );
		assertEquals( 1, immutableByConfigCache.size() );
		assertEquals( inspectors1[1], immutableByConfigCache.get( BaseConfigReader.IMMUTABLE_NO_CONFIG ) );

		immutableByConfigCache = immutableByClassCache.get( SpringAnnotationInspector.class );
		assertFalse( immutableByConfigCache.containsKey( BaseConfigReader.IMMUTABLE_NO_CONFIG ) );
		assertEquals( 1, immutableByConfigCache.size() );
		assertEquals( inspectors1[4], immutableByConfigCache.values().iterator().next() );

		immutableByConfigCache = immutableByClassCache.get( PropertyTypeInspector.class );
		assertTrue( immutableByConfigCache.containsKey( BaseConfigReader.IMMUTABLE_NO_CONFIG ) );
		assertEquals( 1, immutableByConfigCache.size() );
		assertEquals( inspectors1[0], immutableByConfigCache.get( BaseConfigReader.IMMUTABLE_NO_CONFIG ) );

		immutableByConfigCache = immutableByClassCache.get( GroovyPropertyStyle.class );
		assertEquals( 1, immutableByConfigCache.size() );
		assertEquals( propertyStyleField.get( inspectors1[3] ), immutableByConfigCache.values().iterator().next() );

		immutableByConfigCache = immutableByClassCache.get( JavaBeanPropertyStyle.class );
		assertTrue( immutableByConfigCache.containsKey( BaseConfigReader.IMMUTABLE_NO_CONFIG ) );
		assertEquals( 1, immutableByConfigCache.size() );
		assertEquals( propertyStyleField.get( inspectors1[2] ), immutableByConfigCache.get( BaseConfigReader.IMMUTABLE_NO_CONFIG ) );

		assertEquals( 9, immutableByClassCache.size() );
	}

	public void testEnum()
		throws Exception {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<beansBindingProcessor xmlns=\"java:org.metawidget.swing.widgetprocessor.binding.beansbinding\" config=\"BeansBindingProcessorConfig\">";
		xml += "	<updateStrategy><enum>READ_WRITE</enum></updateStrategy>";
		xml += "</beansBindingProcessor>";
		xml += "</metawidget>";

		BeansBindingProcessor processor = (BeansBindingProcessor) new BaseConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), BeansBindingProcessor.class );
		Field updateStrategyField = BeansBindingProcessor.class.getDeclaredField( "mUpdateStrategy" );
		updateStrategyField.setAccessible( true );
		assertTrue( UpdateStrategy.READ_WRITE.equals( updateStrategyField.get( processor ) ) );
	}

	public void testOnlyCacheIfSuccessful() {

		ConfigReader configReader = new BaseConfigReader( new ResourceResolver() {

			private int	mOpenResource;

			@Override
			public InputStream openResource( String resource ) {

				String xml = "<?xml version=\"1.0\"?>";
				xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
				xml += "<htmlMetawidgetTag xmlns=\"java:org.metawidget.jsp.tagext.html\">";
				xml += "	<inspector>";

				if ( mOpenResource == 0 ) {
					xml += "		<compositeInspectorFoo xmlns=\"java:org.metawidget.inspector.composite\" config=\"CompositeInspectorConfig\">";
					xml += "			<inspectors>";
					xml += "				<array>";
					xml += "					<propertyTypeInspector xmlns=\"java:org.metawidget.inspector.propertytype\"/>";
					xml += "					<metawidgetAnnotationInspector xmlns=\"java:org.metawidget.inspector.annotation\"/>";
					xml += "				</array>";
					xml += "			</inspectors>";
					xml += "		</compositeInspectorFoo>";
				} else {
					xml += "		<compositeInspector xmlns=\"java:org.metawidget.inspector.composite\" config=\"CompositeInspectorConfig\">";
					xml += "			<inspectors>";
					xml += "				<array>";
					xml += "					<propertyTypeInspector xmlns=\"java:org.metawidget.inspector.propertytype\"/>";
					xml += "					<metawidgetAnnotationInspector xmlns=\"java:org.metawidget.inspector.annotation\"/>";
					xml += "				</array>";
					xml += "			</inspectors>";
					xml += "		</compositeInspector>";
				}

				mOpenResource++;

				xml += "	</inspector>";
				xml += "</htmlMetawidgetTag>";
				xml += "</metawidget>";

				return new ByteArrayInputStream( xml.getBytes() );
			}
		} );

		try {
			configReader.configure( "foo", HtmlMetawidgetTag.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertEquals( "No such tag <compositeInspectorFoo> or class org.metawidget.inspector.composite.CompositeInspectorFoo (is it on your CLASSPATH?)", e.getMessage() );
		}

		assertTrue( configReader.configure( "foo", HtmlMetawidgetTag.class ) != null );
	}

	//
	// Inner class
	//

	static class ValidatingConfigReader
		extends BaseConfigReader {

		//
		// Private members
		//

		//
		// Constructor
		//

		public ValidatingConfigReader() {

			super( new CountingResourceResolver() );

			SchemaFactory factory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
			InputStream in = getResourceResolver().openResource( "org/metawidget/config/metawidget-1.0.xsd" );

			try {
				mFactory.setSchema( factory.newSchema( new StreamSource( in ) ) );
			} catch ( SAXException e ) {
				throw MetawidgetException.newException( e );
			}
		}
	}

	static class CountingResourceResolver
		extends SimpleResourceResolver {

		private int	mOpenedResource;

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