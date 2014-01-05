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

package org.metawidget.statically.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.statically.BaseStaticXmlWidget;
import org.metawidget.statically.StaticMetawidget;
import org.metawidget.statically.StaticXmlStub;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SimpleLayoutTest
	extends TestCase {

	//
	// Public methods
	//

	public void testSimpleLayout()
		throws Exception {

		SimpleLayout layout = new SimpleLayout();

		// Normal

		StaticMetawidget metawidget = new StaticMetawidget() {

			@Override
			protected String getDefaultConfiguration() {

				return null;
			}
		};
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "foo" );
		StaticXmlWidget container = new MyStaticXmlWidget();
		StaticXmlWidget widget = new MyStaticXmlWidget();
		layout.layoutWidget( widget, PROPERTY, attributes, container, metawidget );

		assertEquals( "<prefix:tagName><prefix:tagName/></prefix:tagName>", container.toString() );
	}

	public void testStub()
		throws Exception {

		SimpleLayout layout = new SimpleLayout();
		StaticMetawidget metawidget = new StaticMetawidget() {

			@Override
			protected String getDefaultConfiguration() {

				return null;
			}
		};

		// Empty stub

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "foo" );
		StaticXmlWidget container = new MyStaticXmlWidget();
		layout.layoutWidget( new StaticXmlStub(), PROPERTY, attributes, container, metawidget );

		assertEquals( "<prefix:tagName/>", container.toString() );

		// Stub with children

		container = new MyStaticXmlWidget();
		StaticXmlStub stub = new StaticXmlStub();
		stub.getChildren().add( new MyStaticXmlWidget() );
		layout.layoutWidget( stub, PROPERTY, attributes, container, metawidget );

		assertEquals( "<prefix:tagName><prefix:tagName/></prefix:tagName>", container.toString() );
	}

	//
	// Inner class
	//

	class MyStaticXmlWidget
		extends BaseStaticXmlWidget {

		//
		// Constructor
		//

		public MyStaticXmlWidget() {

			super( "prefix", "tagName", "namespace" );
		}
	}
}
