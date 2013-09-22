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
