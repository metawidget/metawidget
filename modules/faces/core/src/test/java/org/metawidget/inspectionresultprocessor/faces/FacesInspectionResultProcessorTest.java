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

package org.metawidget.inspectionresultprocessor.faces;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessorException;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.MetawidgetTestUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class FacesInspectionResultProcessorTest
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
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspectionResultProcessor/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"Foo\">";
		xml += "<property name=\"bar1\" value-is-el=\"#{baz1}\" value-is-text=\"text\"/>";
		xml += "<property name=\"bar2\" value-is-null=\"#{null}\"/>";
		xml += "<action name=\"bar3\" value-is-el=\"#{baz2}\" value-is-text=\"text\"/>";
		xml += "</entity></inspection-result>";

		FacesInspectionResultProcessor inspectionResultProcessor = new FacesInspectionResultProcessor();

		String result = inspectionResultProcessor.processInspectionResult( xml, null, null, null );
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
		assertEquals( "result of #{baz1}", property.getAttribute( "value-is-el" ) );
		assertEquals( "text", property.getAttribute( "value-is-text" ) );
		assertTrue( 3 == property.getAttributes().getLength() );

		property = XmlUtils.getNextSiblingElement( property );
		assertEquals( PROPERTY, property.getNodeName() );
		assertTrue( !property.hasAttribute( "value-is-null" ) );
		assertTrue( 1 == property.getAttributes().getLength() );

		// Actions

		Element action = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar3" );
		assertEquals( ACTION, action.getNodeName() );
		assertEquals( "result of #{baz2}", action.getAttribute( "value-is-el" ) );
		assertEquals( "text", action.getAttribute( "value-is-text" ) );
		assertTrue( 3 == action.getAttributes().getLength() );

		assertTrue( entity.getChildNodes().getLength() == 3 );

		// Test '_this' check

		xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspectionResultProcessor/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"Foo\">";
		xml += "<property name=\"bar1\" value-is-el=\"#{_this.baz}\" value-is-text=\"text\"/>";
		xml += "<action name=\"bar2\" value-is-el=\"#{_this.baz}\" value-is-text=\"text\"/>";
		xml += "</entity></inspection-result>";
		inspectionResultProcessor = new FacesInspectionResultProcessor( new FacesInspectionResultProcessorConfig() );

		UIMetawidget metawidget = new HtmlMetawidget();

		try {
			inspectionResultProcessor.processInspectionResult( xml, metawidget, null, "Foo" );
			assertTrue( false );
		} catch ( InspectionResultProcessorException e ) {
			assertEquals( "Expression for '#{_this.baz}' contains '_this', but FacesInspectionResultProcessorConfig.setInjectThis is null", e.getMessage() );
		}

		// Test the other '_this' check

		xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspectionResultProcessor/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"Foo\">";
		xml += "<property name=\"bar1\" faces-lookup=\"#{_this.baz}\"/>";
		xml += "</entity></inspection-result>";
		inspectionResultProcessor = new FacesInspectionResultProcessor( new FacesInspectionResultProcessorConfig().setInjectThis( new JavaBeanPropertyStyle() ) );

		try {
			inspectionResultProcessor.processInspectionResult( xml, metawidget, null, "Foo" );
			assertTrue( false );
		} catch ( InspectionResultProcessorException e ) {
			assertEquals( "Expression '#{_this.baz}' (for 'faces-lookup') must not contain '_this' (see Metawidget Reference Guide)", e.getMessage() );
		}
	}

	public void testConfig()
		throws Exception {

		MetawidgetTestUtils.testEqualsAndHashcode( FacesInspectionResultProcessorConfig.class, new FacesInspectionResultProcessorConfig() {
			// Subclass
		} );
	}

	public void testThisInjection() {

		final List<String> injected = CollectionUtils.newArrayList();

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspectionResultProcessor/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"Foo\">";
		xml += "<property name=\"bar\"/>";
		xml += "</entity></inspection-result>";

		// Without injection

		FacesInspectionResultProcessor inspectionResultProcessor = new FacesInspectionResultProcessor( new FacesInspectionResultProcessorConfig() ) {

			@Override
			protected void processAttributes( Map<String, String> attributes, UIMetawidget metawidget ) {

				if ( attributes.containsKey( TYPE )) {
					injected.add( "attributes: " + attributes.get( TYPE ) + ": " + FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get( "_this" ) );
				} else {
					injected.add( "attributes: " + attributes.get( NAME ) + ": " + FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get( "_this" ) );
				}
			}
		};

		inspectionResultProcessor.processInspectionResult( xml, null, new Foo(), Foo.class.getName() );

		assertEquals( "attributes: Foo: null", injected.get( 0 ) );
		assertEquals( "attributes: bar: null", injected.get( 1 ) );
		assertEquals( 2, injected.size() );

		// With injection

		injected.clear();

		inspectionResultProcessor = new FacesInspectionResultProcessor( new FacesInspectionResultProcessorConfig().setInjectThis( new JavaBeanPropertyStyle() ) ) {

			@Override
			public Element processInspectionResultAsDom( Element inspectionResult, UIMetawidget givenMetawidget, Object toInspect, String type, String... names ) {

				try {

					injected.add( "before: " + FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get( "_this" ) );

					return super.processInspectionResultAsDom( inspectionResult, givenMetawidget, toInspect, type, names );

				} finally {

					injected.add( "after: " + FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get( "_this" ) );
				}
			}

			@Override
			protected void processAttributes( Map<String, String> attributes, UIMetawidget metawidget ) {

				if ( attributes.containsKey( TYPE )) {
					injected.add( "attributes: " + attributes.get( TYPE ) + ": " + FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get( "_this" ) );
				} else {
					injected.add( "attributes: " + attributes.get( NAME ) + ": " + FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get( "_this" ) );
				}
			}
		};

		inspectionResultProcessor.processInspectionResult( xml, null, new Foo(), Foo.class.getName() );

		assertEquals( "before: null", injected.get( 0 ) );
		assertEquals( "attributes: Foo: null", injected.get( 1 ) );
		assertTrue( injected.get( 2 ).startsWith( "attributes: bar: " + Foo.class.getName() ) );
		assertEquals( "after: null", injected.get( 3 ) );
		assertEquals( 4, injected.size() );

		// With parent injection

		ParentFoo parentFoo = new ParentFoo();
		parentFoo.foo = new Foo();

		injected.clear();
		inspectionResultProcessor.processInspectionResult( xml, null, parentFoo, ParentFoo.class.getName(), "foo" );

		assertEquals( "before: null", injected.get( 0 ) );
		assertTrue( injected.get( 1 ).startsWith( "attributes: Foo: " + ParentFoo.class.getName() ) );
		assertTrue( injected.get( 2 ).startsWith( "attributes: bar: " + Foo.class.getName() ) );
		assertEquals( "after: null", injected.get( 3 ) );
		assertEquals( 4, injected.size() );
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

	public static class ParentFoo {

		public Foo	foo;
	}
}
