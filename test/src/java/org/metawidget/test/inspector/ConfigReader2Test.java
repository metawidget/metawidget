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
import java.lang.reflect.Field;

import javax.swing.JComponent;

import junit.framework.TestCase;

import org.metawidget.inspector.ConfigReader2;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.faces.FacesInspector;
import org.metawidget.inspector.hibernate.validator.HibernateValidatorInspector;
import org.metawidget.inspector.iface.Inspector;
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

/**
 * @author Richard Kennard
 */

public class ConfigReader2Test
	extends TestCase
{
	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public ConfigReader2Test( String name )
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
		xml += "<metawidget>";
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
		xml += "							<files>";
		xml += "								<list>";
		xml += "									<string>org/metawidget/test/inspector/struts/test-struts-config1.xml</string>";
		xml += "									<string>org/metawidget/test/inspector/struts/test-struts-config2.xml</string>";
		xml += "								</list>";
		xml += "							</files>";
		xml += "						</strutsInspector>";
		xml += "						<strutsAnnotationInspector xmlns=\"java:org.metawidget.inspector.struts\"/>";
		xml += "						<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\" config=\"XmlInspectorConfig\">";
		xml += "							<file>";
		xml += "								<string>org/metawidget/example/swing/addressbook/metawidget-metadata.xml</string>";
		xml += "							</file>";
		xml += "						</xmlInspector>";
		xml += "						<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\" config=\"XmlInspectorConfig\">";
		xml += "							<inputStream>";
		xml += "								<stream>org/metawidget/example/swing/addressbook/metawidget-metadata.xml</stream>";
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
		xml += "</metawidget>";

		// New Point

		ConfigReader2 configReader = new ConfigReader2();
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
		assertTrue( null == metawidget1.getParameter( "a parameter" ));
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), metawidget1 );

		// Test

		assertTrue( "foo".equals( metawidget1.getName() ) );
		assertTrue( metawidget1.isOpaque() );
		assertTrue( 1 == (Integer) metawidget1.getParameter( "a parameter" ));
		assertTrue( 5 == (Integer) metawidget1.getParameter( "another parameter" ));

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
		CompositeWidgetBuilder<JComponent,SwingMetawidget> compositeWidgetBuilder1 = (CompositeWidgetBuilder<JComponent,SwingMetawidget>) widgetBuilderField.get( mixin1 );
		@SuppressWarnings( "unchecked" )
		CompositeWidgetBuilder<JComponent,SwingMetawidget> compositeWidgetBuilder2 = (CompositeWidgetBuilder<JComponent,SwingMetawidget>) widgetBuilderField.get( mixin2 );

		assertTrue( compositeWidgetBuilder1 == compositeWidgetBuilder2 );

		Field widgetBuildersField = CompositeWidgetBuilder.class.getDeclaredField( "mWidgetBuilders" );
		widgetBuildersField.setAccessible( true );
		@SuppressWarnings("unchecked")
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
	}
}
