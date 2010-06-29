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

package org.metawidget.swing;

import java.io.ByteArrayInputStream;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.config.ConfigReader;
import org.metawidget.example.swing.tutorial.Person;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.util.LogUtilsTest;

/**
 * @author Richard Kennard
 */

public class SwingJdk14Test
	extends TestCase {

	//
	// Constructor
	//

	public SwingJdk14Test( String name ) {

		super( name );
	}

	//
	// Public methods
	//

	public void testJdk14() {

		// Check works 'out of the box' on JDK 1.4

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setToInspect( new Person() );
		assertTrue( "Age:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 1 ) instanceof JSpinner );
		assertTrue( "Name:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 3 ) instanceof JTextField );
		assertTrue( "Retired:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );
		assertTrue( metawidget.getComponent( 6 ) instanceof JPanel );
		assertTrue( 7 == metawidget.getComponentCount() );

		assertTrue( "\tInstantiated immutable class org.metawidget.swing.widgetprocessor.binding.reflection.ReflectionBindingProcessor (no config)".equals( LogUtilsTest.getLastDebugMessage() ) );

		// Check warning

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"";
		xml += "	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
		xml += "	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "	<swingMetawidget xmlns=\"java:org.metawidget.swing\">";
		xml += "		<inspector>";
		xml += "			<metawidgetAnnotationInspector xmlns=\"java:org.metawidget.inspector.annotation\" />";
		xml += "		</inspector>";
		xml += "	</swingMetawidget>";
		xml += "</metawidget>";

		try {
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), SwingMetawidget.class );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertTrue( "java.lang.NoSuchMethodException: class org.metawidget.swing.SwingMetawidget.setInspector(). Did you mean setInspector(Inspector)?".equals( e.getMessage() ) );
		}

		assertTrue( "\tNot instantiating org.metawidget.inspector.annotation.MetawidgetAnnotationInspector - wrong Java version".equals( LogUtilsTest.getLastDebugMessage() ) );
	}
}
