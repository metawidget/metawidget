// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.inspector.swing;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.lang.reflect.Field;

import junit.framework.TestCase;

import org.jdesktop.application.Action;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.util.MetawidgetTestUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SwingAppFrameworkInspectorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testAnnotations() {

		SwingAppFrameworkInspector inspector = new SwingAppFrameworkInspector();

		Document document = XmlUtils.documentFromString( inspector.inspect( null, Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Foo.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Actions

		Element action = (Element) entity.getFirstChild();
		assertEquals( ACTION, action.getNodeName() );
		assertEquals( "doBar", action.getAttribute( NAME ) );
		assertEquals( "barLabel", action.getAttribute( LABEL ) );
		assertEquals( action.getAttributes().getLength(), 2 );

		assertEquals( entity.getChildNodes().getLength(), 1 );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( SwingAppFrameworkInspectorConfig.class, new SwingAppFrameworkInspectorConfig() {
			// Subclass
		} );
	}

	public void testLazyInitialisation()
		throws Exception {

		SwingAppFrameworkInspectorConfig config = new SwingAppFrameworkInspectorConfig();

		Field actionStyle = BaseObjectInspectorConfig.class.getDeclaredField( "mActionStyle" );
		actionStyle.setAccessible( true );
		assertEquals( null, actionStyle.get( config ) );
		assertTrue( config.getActionStyle() != null );
		assertEquals( null, actionStyle.get( config ) );
	}

	//
	// Inner class
	//

	public static class Foo {

		@Action( name = "barLabel" )
		public boolean doBar() {

			return true;
		}
	}
}
