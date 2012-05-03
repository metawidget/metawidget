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

package org.metawidget.pipeline.w3c;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessorException;
import org.metawidget.inspectionresultprocessor.sort.ComesAfterInspectionResultProcessor;
import org.metawidget.swing.widgetprocessor.binding.reflection.ReflectionBindingProcessor;
import org.metawidget.swing.widgetprocessor.validator.jgoodies.JGoodiesValidatorProcessor;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class MetawidgetPipelineTest
	extends TestCase {

	//
	// Public methods
	//

	public void testIndentation()
		throws Exception {

		new Pipeline().testIndentation();
	}

	/**
	 * Returning null from an InspectionResultProcessor should cancel things gracefully.
	 */

	public void testInspectionResultProcessorReturningNull()
		throws Exception {

		final List<String> called = CollectionUtils.newArrayList();

		Pipeline pipeline = new Pipeline() {

			@Override
			public Element inspectAsDom( Object toInspect, String type, String... names ) {

				return super.processInspectionResult( null, toInspect, type, names );
			}
		};
		pipeline.addInspectionResultProcessor( new InspectionResultProcessor<Object>() {

			@Override
			public String processInspectionResult( String inspectionResult, Object metawidget, Object toInspect, String type, String... names ) {

				called.add( "InspectionResultProcessor #1" );
				return null;
			}
		} );
		pipeline.addInspectionResultProcessor( new InspectionResultProcessor<Object>() {

			@Override
			public String processInspectionResult( String inspectionResult, Object metawidget, Object toInspect,  String type, String... names ) {

				called.add( "InspectionResultProcessor #2" );
				return null;
			}
		} );

		assertEquals( null, pipeline.inspectAsDom( null, null ) );
		assertEquals( called.size(), 1 );
		assertEquals( "InspectionResultProcessor #1", called.get( 0 ) );
		assertFalse( called.contains( "InspectionResultProcessor #2" ) );
	}

	/**
	 * Returning null from a WidgetProcessor should cancel things gracefully.
	 */

	public void testWidgetProcessorReturningNull()
		throws Exception {

		final List<String> called = CollectionUtils.newArrayList();

		// This little test harness reinforces the minimal relationship between Inspectors
		// and WidgetBuilders. Here, we are returning Strings not real GUI widgets.

		Pipeline pipeline = new Pipeline() {

			@Override
			protected void buildCompoundWidget( Element element )
				throws Exception {

				called.add( "buildCompoundWidget" );
				super.buildCompoundWidget( element );
			}

			@Override
			protected void layoutWidget( Object widget, String elementName, Map<String, String> attributes ) {

				called.add( "layoutWidget" );
				super.layoutWidget( widget, elementName, attributes );
			}
		};

		pipeline.setWidgetBuilder( new WidgetBuilder<Object, Object>() {

			@Override
			public Object buildWidget( String elementName, Map<String, String> attributes, Object metawidget ) {

				// Return null if no type (this will kick us into buildCompoundWidget)

				return attributes.get( TYPE );
			}
		} );

		pipeline.addWidgetProcessor( new WidgetProcessor<Object, Object>() {

			@Override
			public Object processWidget( Object widget, String elementName, Map<String, String> attributes, Object metawidget ) {

				called.add( "WidgetProcessor #1" );
				return null;
			}
		} );

		pipeline.addWidgetProcessor( new WidgetProcessor<Object, Object>() {

			@Override
			public Object processWidget( Object widget, String elementName, Map<String, String> attributes, Object metawidget ) {

				called.add( "WidgetProcessor #2" );
				return null;
			}
		} );

		// Top-level widget

		pipeline.buildWidgets( XmlUtils.documentFromString( "<inspection-result><entity type=\"foo\"/></inspection-result>" ).getDocumentElement() );

		assertEquals( called.size(), 1 );
		assertEquals( "WidgetProcessor #1", called.get( 0 ) );
		assertFalse( called.contains( "WidgetProcessor #2" ) );

		// Property-level widget

		called.clear();
		pipeline.buildWidgets( XmlUtils.documentFromString( "<inspection-result><entity><property name=\"foo\" type=\"foo\"/></entity></inspection-result>" ).getDocumentElement() );

		assertEquals( called.size(), 2 );
		assertEquals( "buildCompoundWidget", called.get( 0 ) );
		assertEquals( "WidgetProcessor #1", called.get( 1 ) );
		assertFalse( called.contains( "WidgetProcessor #2" ) );
	}

	/**
	 * Stubs with null attributes should not error
	 */

	public void testStubWithNullAttributes()
		throws Exception {

		final List<String> called = CollectionUtils.newArrayList();

		Pipeline pipeline = new Pipeline() {

			@Override
			protected void buildCompoundWidget( Element element )
				throws Exception {

				called.add( "buildCompoundWidget" );
				super.buildCompoundWidget( element );
			}

			@Override
			protected void layoutWidget( Object widget, String elementName, Map<String, String> attributes ) {

				called.add( "addWidget" );
				super.layoutWidget( widget, elementName, attributes );
			}

			@Override
			protected Map<String, String> getAdditionalAttributes( Object widget ) {

				called.add( "nullAttributes" );
				return null;
			}
		};

		pipeline.setWidgetBuilder( new WidgetBuilder<Object, Object>() {

			@Override
			public Object buildWidget( String elementName, Map<String, String> attributes, Object metawidget ) {

				// Will return null for the top-level (because no NAME), which will trigger
				// buildCompoundWidget, and will return not-null for first property (because has a
				// NAME)

				return attributes.get( NAME );
			}
		} );

		// Top-level widget

		pipeline.buildWidgets( XmlUtils.documentFromString( "<inspection-result><entity><property name=\"foo\"/></entity></inspection-result>" ).getDocumentElement() );

		assertEquals( called.size(), 3 );
		assertEquals( "buildCompoundWidget", called.get( 0 ) );
		assertEquals( "nullAttributes", called.get( 1 ) );
		assertEquals( "addWidget", called.get( 2 ) );
	}

	public void testDuplicateInspectionResultProcessors() {

		InspectionResultProcessor<Object> inspectionResultProcessor1 = new ComesAfterInspectionResultProcessor<Object>();

		Pipeline pipeline = new Pipeline();
		assertEquals( null, pipeline.getInspectionResultProcessors() );
		pipeline.removeInspectionResultProcessor( inspectionResultProcessor1 );
		assertEquals( null, pipeline.getInspectionResultProcessors() );

		pipeline.addInspectionResultProcessor( inspectionResultProcessor1 );

		// Haven't found a need to implement pipeline.getInspectionResultProcessor()!

		try {
			pipeline.addInspectionResultProcessor( inspectionResultProcessor1 );
			fail();
		} catch ( InspectionResultProcessorException e ) {
			assertEquals( "List of InspectionResultProcessors already contains class org.metawidget.inspectionresultprocessor.sort.ComesAfterInspectionResultProcessor", e.getMessage() );
		}

		pipeline.removeInspectionResultProcessor( inspectionResultProcessor1 );
		pipeline.addInspectionResultProcessor( inspectionResultProcessor1 );

		assertEquals( 1, pipeline.getInspectionResultProcessors().size() );
	}

	@SuppressWarnings( { "rawtypes", "unchecked" } )
	public void testDuplicateWidgetProcessors() {

		WidgetProcessor<Object, Object> widgetProcessor1 = (WidgetProcessor) new ReflectionBindingProcessor();
		WidgetProcessor<Object, Object> widgetProcessor2 = (WidgetProcessor) new JGoodiesValidatorProcessor();

		Pipeline pipeline = new Pipeline();
		assertEquals( null, pipeline.getWidgetProcessors() );
		pipeline.removeWidgetProcessor( widgetProcessor1 );
		assertEquals( null, pipeline.getWidgetProcessors() );

		pipeline.addWidgetProcessor( widgetProcessor1 );
		pipeline.addWidgetProcessor( widgetProcessor2 );

		assertEquals( widgetProcessor1, pipeline.getWidgetProcessor( WidgetProcessor.class ) );
		assertEquals( widgetProcessor1, pipeline.getWidgetProcessor( ReflectionBindingProcessor.class ) );
		assertEquals( widgetProcessor2, pipeline.getWidgetProcessor( JGoodiesValidatorProcessor.class ) );

		try {
			pipeline.addWidgetProcessor( widgetProcessor1 );
			fail();
		} catch ( WidgetProcessorException e ) {
			assertEquals( "List of WidgetProcessors already contains class org.metawidget.swing.widgetprocessor.binding.reflection.ReflectionBindingProcessor", e.getMessage() );
		}

		pipeline.removeWidgetProcessor( widgetProcessor1 );
		pipeline.addWidgetProcessor( widgetProcessor1 );

		assertEquals( 2, pipeline.getWidgetProcessors().size() );
	}

	//
	// Inner class
	//

	static class Pipeline
		extends W3CPipeline<Object, Object, Object> {

		//
		// Public methods
		//

		public void testIndentation()
			throws Exception {

			Element element = getFirstChildElement( stringToElement( "<foo><bar>baz</bar></foo>" ) );
			assertEquals( "bar", element.getNodeName() );

			element = getFirstChildElement( stringToElement( "<foo>		<bar>baz</bar></foo>" ) );
			assertEquals( "bar", element.getNodeName() );

			element = getNextSiblingElement( getFirstChildElement( stringToElement( "<foo>		<bar>baz</bar></foo>" ) ) );
			assertEquals( null, element );
		}

		//
		// Protected methods
		//

		@Override
		protected Object getPipelineOwner() {

			return null;
		}

		@Override
		protected String getDefaultConfiguration() {

			return null;
		}

		@Override
		protected void configure() {

			// Do nothing
		}

		@Override
		protected void startBuild() {

			// Do nothing
		}

		@Override
		protected void layoutWidget( Object widget, String elementName, Map<String, String> attributes ) {

			// Do nothing
		}

		@Override
		protected Map<String, String> getAdditionalAttributes( Object widget ) {

			return null;
		}

		@Override
		protected Object buildNestedMetawidget( Map<String, String> attributes )
			throws Exception {

			return null;
		}

		@Override
		protected void endBuild() {

			// Do nothing
		}
	}
}
