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

package org.metawidget.pipeline.base;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.swing.JComponent;

import junit.framework.TestCase;

import org.metawidget.inspectionresultprocessor.sort.ComesAfterInspectionResultProcessor;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.layout.iface.Layout;
import org.metawidget.pipeline.w3c.W3CPipeline;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.w3c.dom.Document;

/**
 * @author Richard Kennard
 */

public class BasePipelineTest
	extends TestCase {

	//
	// Public methods
	//

	public void testBuildCompoundWidget()
		throws Exception {

		W3CPipeline<JComponent, JComponent, JComponent> pipeline = new MockPipeline();

		// Bad entity

		Document document = XmlUtils.documentFromString( "<inspection-result><property type=\"foo\"/></inspection-result>" );

		try {
			pipeline.buildWidgets( document.getDocumentElement() );
			assertTrue( false );
		} catch ( Exception e ) {
			assertEquals( "Top-level element name should be entity, not property", e.getMessage() );
		}

		// Bad child

		document = XmlUtils.documentFromString( "<inspection-result><entity type=\"foo\"><bar/></entity></inspection-result>" );

		try {
			pipeline.buildCompoundWidget( XmlUtils.getFirstChildElement( document.getDocumentElement() ) );
			assertTrue( false );
		} catch ( Exception e ) {
			assertEquals( "Child element #1 should be property or action, not bar", e.getMessage() );
		}

		// Missing name attribute

		document = XmlUtils.documentFromString( "<inspection-result><entity type=\"foo\"><property/></entity></inspection-result>" );

		try {
			pipeline.buildCompoundWidget( XmlUtils.getFirstChildElement( document.getDocumentElement() ) );
			assertTrue( false );
		} catch ( Exception e ) {
			assertEquals( "Child element #1 has no @name", e.getMessage() );
		}
	}

	public void testInitNestedPipeline()
		throws Exception {

		W3CPipeline<JComponent, JComponent, JComponent> pipeline = new MockPipeline();
		W3CPipeline<JComponent, JComponent, JComponent> nestedPipeline = new MockPipeline();

		PropertyTypeInspector inspector = new PropertyTypeInspector();
		ComesAfterInspectionResultProcessor<JComponent> inspectionResultProcessor = new ComesAfterInspectionResultProcessor<JComponent>();
		WidgetBuilder<JComponent, JComponent> widgetBuilder = new WidgetBuilder<JComponent, JComponent>() {

			public JComponent buildWidget( String elementName, Map<String, String> attributes, JComponent metawidget ) {

				return null;
			}
		};
		WidgetProcessor<JComponent, JComponent> widgetProcessor = new WidgetProcessor<JComponent, JComponent>() {

			public JComponent processWidget( JComponent widget, String elementName, Map<String, String> attributes, JComponent metawidget ) {

				return null;
			}
		};
		Layout<JComponent, JComponent, JComponent> layout = new Layout<JComponent, JComponent, JComponent>() {

			public void layoutWidget( JComponent widget, String elementName, Map<String, String> attributes, JComponent container, JComponent metawidget ) {

				// Do nothing
			}
		};

		pipeline.setInspector( inspector );
		pipeline.addInspectionResultProcessor( inspectionResultProcessor );
		pipeline.setWidgetBuilder( widgetBuilder );
		pipeline.addWidgetProcessor( widgetProcessor );
		pipeline.setLayout( layout );
		pipeline.initNestedPipeline( nestedPipeline, null );

		// Test elements are initialized

		assertTrue( nestedPipeline.getInspector() == inspector );
		assertTrue( nestedPipeline.getWidgetBuilder() == widgetBuilder );
		assertTrue( nestedPipeline.getLayout() == layout );

		// Test defensive copy

		assertEquals( nestedPipeline.getInspectionResultProcessors(), pipeline.getInspectionResultProcessors() );
		assertTrue( nestedPipeline.getInspectionResultProcessors() != pipeline.getInspectionResultProcessors() );
		pipeline.getInspectionResultProcessors().clear();
		assertTrue( pipeline.getInspectionResultProcessors().isEmpty() );
		assertTrue( !nestedPipeline.getInspectionResultProcessors().isEmpty() );

		// Test defensive copy

		assertEquals( nestedPipeline.getWidgetProcessors(), pipeline.getWidgetProcessors() );
		assertTrue( nestedPipeline.getWidgetProcessors() != pipeline.getWidgetProcessors() );
		pipeline.getWidgetProcessors().clear();
		assertTrue( pipeline.getWidgetProcessors().isEmpty() );
		assertTrue( !nestedPipeline.getWidgetProcessors().isEmpty() );

		// Test read only

		assertTrue( !nestedPipeline.isReadOnly() );
		pipeline.setReadOnly( true );
		pipeline.initNestedPipeline( nestedPipeline, null );
		assertTrue( nestedPipeline.isReadOnly() );
		nestedPipeline.setReadOnly( false );

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( READ_ONLY, TRUE );
		pipeline.initNestedPipeline( nestedPipeline, attributes );
		assertTrue( nestedPipeline.isReadOnly() );

		// Maximum inspection depth

		pipeline.setMaximumInspectionDepth( 100 );
		pipeline.initNestedPipeline( nestedPipeline, null );
		assertEquals( 99, nestedPipeline.getMaximumInspectionDepth() );
	}

	//
	// Inner class
	//

	/* package private */static class MockPipeline
		extends W3CPipeline<JComponent, JComponent, JComponent> {

		@Override
		protected void configure() {

			// Do nothing
		}

		@Override
		protected Map<String, String> getAdditionalAttributes( JComponent widget ) {

			return null;
		}

		@Override
		protected JComponent buildNestedMetawidget( Map<String, String> attributes )
				throws Exception {

			return null;
		}

		@Override
		protected JComponent getPipelineOwner() {

			return null;
		}
	}
}
