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

import junit.framework.TestCase;

import org.metawidget.inspector.ConfigReader2;
import org.metawidget.swing.SwingMetawidget;

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
		xml += "			<swingWidgetBuilder xmlns=\"urn:java:org.metawidget.swing.widgetbuilder\"/>";
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
		xml += "			<int>1</int>";
		xml += "		</parameter>";
		xml += "	</swingMetawidget>";
		xml += "</metawidget>";

		// Point

		Point point = new Point();
		ConfigReader2 configReader = new ConfigReader2();
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), point );

		assertTrue( 10 == point.x );
		assertTrue( 20 == point.y );

		// SwingMetawidget

		SwingMetawidget metawidget = new SwingMetawidget();
		assertTrue( null == metawidget.getName() );
		assertTrue( !metawidget.isOpaque() );
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), metawidget );

		// Test

		assertTrue( "foo".equals( metawidget.getName() ));
		assertTrue( metawidget.isOpaque() );
	}
}
