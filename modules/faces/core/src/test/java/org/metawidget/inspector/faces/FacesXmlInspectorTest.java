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

package org.metawidget.inspector.faces;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseXmlInspectorConfig;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.MetawidgetTestUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class FacesXmlInspectorTest
	extends TestCase {

	//
	// Private members
	//

	private FacesContext	mContext;

	//
	// Public methods
	//

	public void testXml() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"Foo\">";
		xml += "<property name=\"bar1\" value-is-el=\"#{this.baz}\" value-is-text=\"text\"/>";
		xml += "<action name=\"bar2\" value-is-el=\"#{this.baz}\" value-is-text=\"text\"/>";
		xml += "</entity></inspection-result>";

		FacesXmlInspector inspector = new FacesXmlInspector( new FacesXmlInspectorConfig().setInputStream( new ByteArrayInputStream( xml.getBytes() ) ).setInjectThis( true ) );

		String result = inspector.inspect( null, "Foo" );
		Document document = XmlUtils.documentFromString( result );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "Foo", entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar1" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "result of #{this.baz}", property.getAttribute( "value-is-el" ) );
		assertEquals( "text", property.getAttribute( "value-is-text" ) );
		assertTrue( 3 == property.getAttributes().getLength() );

		// Actions

		Element action = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar2" );
		assertEquals( ACTION, action.getNodeName() );
		assertEquals( "result of #{this.baz}", action.getAttribute( "value-is-el" ) );
		assertEquals( "text", action.getAttribute( "value-is-text" ) );
		assertTrue( 3 == action.getAttributes().getLength() );

		assertTrue( entity.getChildNodes().getLength() == 2 );

		// Test 'this' check

		inspector = new FacesXmlInspector( new FacesXmlInspectorConfig().setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) );

		try {
			inspector.inspect( null, "Foo" );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "Expression for '#{this.baz}' contains 'this', but FacesXmlInspectorConfig.setInjectThis is 'false'", e.getMessage() );
		}

		// Test the other 'this' check

		xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"Foo\">";
		xml += "<property name=\"bar1\" faces-lookup=\"#{this.baz}\"/>";
		xml += "</entity></inspection-result>";
		inspector = new FacesXmlInspector( new FacesXmlInspectorConfig().setInjectThis( true ).setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) );

		try {
			inspector.inspect( null, "Foo" );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "Expression '#{this.baz}' (for 'faces-lookup') must not contain 'this' (see Metawidget Reference Guide)", e.getMessage() );
		}

		// Test '_this' check

		xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"Foo\">";
		xml += "<property name=\"bar1\" value-is-el=\"#{_this.baz}\" value-is-text=\"text\"/>";
		xml += "<action name=\"bar2\" value-is-el=\"#{_this.baz}\" value-is-text=\"text\"/>";
		xml += "</entity></inspection-result>";
		inspector = new FacesXmlInspector( new FacesXmlInspectorConfig().setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) );

		try {
			inspector.inspect( null, "Foo" );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "Expression for '#{_this.baz}' contains '_this', but FacesXmlInspectorConfig.setInjectThis is 'false'", e.getMessage() );
		}

		// Test the other '_this' check

		xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"Foo\">";
		xml += "<property name=\"bar1\" faces-lookup=\"#{_this.baz}\"/>";
		xml += "</entity></inspection-result>";
		inspector = new FacesXmlInspector( new FacesXmlInspectorConfig().setInjectThis( true ).setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) );

		try {
			inspector.inspect( null, "Foo" );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "Expression '#{_this.baz}' (for 'faces-lookup') must not contain '_this' (see Metawidget Reference Guide)", e.getMessage() );
		}
	}

	public void testConfig()
		throws Exception {

		MetawidgetTestUtils.testEqualsAndHashcode( FacesXmlInspectorConfig.class, new FacesXmlInspectorConfig() {
			// Subclass
		} );

		Field defaultFileField = BaseXmlInspectorConfig.class.getDeclaredField( "mDefaultFile" );
		defaultFileField.setAccessible( true );

		assertEquals( "metawidget-metadata.xml", defaultFileField.get( new FacesXmlInspectorConfig() ) );
	}

	public void testThisInjection() {

		final List<String> injected = CollectionUtils.newArrayList();

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"Foo\">";
		xml += "<property name=\"bar\"/>";
		xml += "</entity></inspection-result>";

		// Without injection

		FacesXmlInspector inspector = new FacesXmlInspector( new FacesXmlInspectorConfig().setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) ) {

			@Override
			protected Map<String, String> inspectProperty( Element toInspect ) {

				injected.add( "1: " + toInspect.getAttribute( NAME ) + ": " + FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get( "this" ) );
				injected.add( "2: " + toInspect.getAttribute( NAME ) + ": " + FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get( "_this" ) );
				return null;
			}
		};

		inspector.inspect( new Foo(), "Foo" );

		assertEquals( "1: bar: null", injected.get( 0 ) );
		assertEquals( "2: bar: null", injected.get( 1 ) );
		assertEquals( 2, injected.size() );

		// With injection

		injected.clear();

		inspector = new FacesXmlInspector( new FacesXmlInspectorConfig().setInjectThis( true ).setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) ) {

			@Override
			public Element inspectAsDom( Object toInspect, String type, String... names ) {

				try {

					injected.add( "1: " + FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get( "this" ) );
					injected.add( "2: " + FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get( "_this" ) );

					return super.inspectAsDom( toInspect, type, names );

				} finally {

					injected.add( "5: " + FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get( "this" ) );
					injected.add( "6: " + FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get( "_this" ) );
				}
			}

			@Override
			protected Map<String, String> inspectProperty( Element toInspect ) {

				injected.add( "3: " + toInspect.getAttribute( NAME ) + ": " + FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get( "this" ) );
				injected.add( "4: " + toInspect.getAttribute( NAME ) + ": " + FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get( "_this" ) );
				return null;
			}
		};

		inspector.inspect( new Foo(), "Foo" );

		assertEquals( "1: null", injected.get( 0 ) );
		assertEquals( "2: null", injected.get( 1 ) );
		assertTrue( injected.get( 2 ).startsWith( "3: bar: " + Foo.class.getName() ) );
		assertTrue( injected.get( 3 ).startsWith( "4: bar: " + Foo.class.getName() ) );
		assertEquals( "5: null", injected.get( 4 ) );
		assertEquals( "6: null", injected.get( 5 ) );
		assertEquals( 6, injected.size() );
	}

	//
	// Protected methods
	//

	@Override
	protected final void setUp()
		throws Exception {

		super.setUp();

		mContext = new MockFacesContext();
	}

	@Override
	protected final void tearDown()
		throws Exception {

		super.tearDown();

		mContext.release();
	}
	
	//
	// Inner class
	//
	
	public static class Foo {

		public Object	object1;

		public Object	object2;

		public Object	object3;

		public String	foo;

		public String	bar;
	}	
}
