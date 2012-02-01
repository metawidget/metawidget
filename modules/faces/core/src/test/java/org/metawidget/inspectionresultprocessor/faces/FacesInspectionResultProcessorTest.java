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
		xml += "<property name=\"bar2\" value-is-null=\"#{null}\" value-is-embedded-el=\"first #{abc} middle #{null}#{def} last\"/>";
		xml += "<action name=\"bar3\" value-is-el=\"#{baz2}\" value-is-text=\"text\"/>";
		xml += "</entity></inspection-result>";

		FacesInspectionResultProcessor inspectionResultProcessor = new FacesInspectionResultProcessor();

		String result = inspectionResultProcessor.processInspectionResult( xml, null, null, null );
		Document document = XmlUtils.documentFromString( result );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "Foo", entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getFirstChildElement( entity );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar1", property.getAttribute( NAME ) );
		assertEquals( "result of #{baz1}", property.getAttribute( "value-is-el" ) );
		assertEquals( "text", property.getAttribute( "value-is-text" ) );
		assertTrue( 3 == property.getAttributes().getLength() );

		property = XmlUtils.getNextSiblingElement( property );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar2", property.getAttribute( NAME ) );
		assertTrue( !property.hasAttribute( "value-is-null" ) );
		assertEquals( "first result of #{abc} middle result of #{def} last", property.getAttribute( "value-is-embedded-el" ) );
		assertTrue( 2 == property.getAttributes().getLength() );

		// Actions

		Element action = XmlUtils.getNextSiblingElement( property );
		assertEquals( ACTION, action.getNodeName() );
		assertEquals( "bar3", action.getAttribute( NAME ) );
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

	public void testIgnoredAttributes() {

		UIMetawidget metawidget = new HtmlMetawidget();

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspectionResultProcessor/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"Foo\">";
		xml += "<property name=\"bar\" not-ignored=\"#{1+1}\" faces-lookup=\"#{faces.lookup}\" faces-lookup-item-label=\"#{faces.lookup.item.label}\" faces-lookup-item-value=\"#{faces.lookup.item.value}\" faces-suggest=\"#{faces.suggest}\" faces-expression=\"#{faces.expression}\" faces-ajax-action=\"#{faces.ajax.action}\"/>";
		xml += "</entity></inspection-result>";
		FacesInspectionResultProcessor inspectionResultProcessor = new FacesInspectionResultProcessor( new FacesInspectionResultProcessorConfig().setInjectThis( new JavaBeanPropertyStyle() ) );

		String result = inspectionResultProcessor.processInspectionResult( xml, metawidget, null, "Foo" );
		Document document = XmlUtils.documentFromString( result );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "Foo", entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getFirstChildElement( entity );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar", property.getAttribute( NAME ) );
		assertEquals( "result of #{1+1}", property.getAttribute( "not-ignored" ) );
		assertEquals( "#{faces.lookup}", property.getAttribute( "faces-lookup" ) );
		assertEquals( "#{faces.lookup.item.label}", property.getAttribute( "faces-lookup-item-label" ) );
		assertEquals( "#{faces.lookup.item.value}", property.getAttribute( "faces-lookup-item-value" ) );
		assertEquals( "#{faces.suggest}", property.getAttribute( "faces-suggest" ) );
		assertEquals( "#{faces.expression}", property.getAttribute( "faces-expression" ) );
		assertEquals( "#{faces.ajax.action}", property.getAttribute( "faces-ajax-action" ) );
		assertEquals( 8, property.getAttributes().getLength() );

		assertEquals( 1, entity.getChildNodes().getLength() );
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

				if ( attributes.containsKey( TYPE ) ) {
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

				if ( attributes.containsKey( TYPE ) ) {
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
		parentFoo.setFoo( new Foo() );

		injected.clear();
		inspectionResultProcessor.processInspectionResult( xml, null, parentFoo, ParentFoo.class.getName(), "foo" );

		assertEquals( "before: null", injected.get( 0 ) );
		assertTrue( injected.get( 1 ).startsWith( "attributes: Foo: " + ParentFoo.class.getName() ) );
		assertTrue( injected.get( 2 ).startsWith( "attributes: bar: " + Foo.class.getName() ) );
		assertEquals( "after: null", injected.get( 3 ) );
		assertEquals( 4, injected.size() );
	}

	public void testArrays() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"Company\">";
		xml += "<property name=\"employee\" lookup=\"#{array1,}\" lookup2=\"#{collection1,}\"/>";
		xml += "</entity></inspection-result>";

		FacesInspectionResultProcessor inspectionResultProcessor = new FacesInspectionResultProcessor( new FacesInspectionResultProcessorConfig() );

		String result = inspectionResultProcessor.processInspectionResult( xml, null, null, "Company" );
		Document document = XmlUtils.documentFromString( result );
		Element entity = XmlUtils.getFirstChildElement( document.getDocumentElement() );
		Element property = XmlUtils.getFirstChildElement( entity );
		assertEquals( "employee", property.getAttribute( NAME ) );
		assertEquals( "#{array1\\,},#{array1\\,}", property.getAttribute( "lookup" ) );
		assertEquals( "#{collection1\\,},#{collection1\\,}", property.getAttribute( "lookup2" ) );
		assertEquals( 3, property.getAttributes().getLength() );

		assertEquals( entity.getChildNodes().getLength(), 1 );
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

		public Object getObject1() {

			return null;
		}

		public void setObject1( @SuppressWarnings( "unused" ) Object object1 ) {

			// Do nothing
		}

		public Object getObject2() {

			return null;
		}

		public void setObject2( @SuppressWarnings( "unused" ) Object object2 ) {

			// Do nothing
		}

		public Object getObject3() {

			return null;
		}

		public void setObject3( @SuppressWarnings( "unused" ) Object object3 ) {

			// Do nothing
		}

		public String getFoo() {

			return null;
		}

		public void setFoo( @SuppressWarnings( "unused" ) String foo ) {

			// Do nothing
		}

		public String getBar() {

			return null;
		}

		public void setBar( @SuppressWarnings( "unused" ) String bar ) {

			// Do nothing
		}
	}

	public static class ParentFoo {

		private Foo mFoo;

		public Foo getFoo() {

			return mFoo;
		}

		public void setFoo( Foo foo ) {

			mFoo = foo;
		}
	}
}
