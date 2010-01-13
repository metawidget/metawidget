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
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class MetawidgetPipelineTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testIndentation()
		throws Exception
	{
		new Pipeline().testIndentation();
	}

	/**
	 * Returning null from an InspectionResultProcessor should cancel things gracefully.
	 */

	public void testInspectionResultProcessorReturningNull()
		throws Exception
	{
		final List<String> called = CollectionUtils.newArrayList();

		Pipeline pipeline = new Pipeline()
		{
			@Override
			public Element inspect( Object toInspect, String type, String... names )
			{
				return super.processInspectionResult( null );
			}
		};

		pipeline.addInspectionResultProcessor( new InspectionResultProcessor<Element, Object>()
		{

			@Override
			public Element processInspectionResult( Element element, Object metawidget )
			{
				called.add( "InspectionResultProcessor #1" );
				return null;
			}
		} );
		pipeline.addInspectionResultProcessor( new InspectionResultProcessor<Element, Object>()
		{

			@Override
			public Element processInspectionResult( Element element, Object metawidget )
			{
				called.add( "InspectionResultProcessor #2" );
				return null;
			}
		} );

		assertTrue( null == pipeline.inspect( null, null ));
		assertTrue( called.size() == 1 );
		assertTrue( "InspectionResultProcessor #1".equals( called.get( 0 ) ) );
		assertTrue( !called.contains( "InspectionResultProcessor #2" ) );
	}

	/**
	 * Returning null from a WidgetProcessor should cancel things gracefully.
	 */

	public void testWidgetProcessorReturningNull()
		throws Exception
	{
		final List<String> called = CollectionUtils.newArrayList();

		// This little test harness reinforces the minimal relationship between Inspectors
		// and WidgetBuilders. Here, we are returning Strings not real GUI widgets.

		Pipeline pipeline = new Pipeline()
		{
			@Override
			protected void buildCompoundWidget( Element element )
				throws Exception
			{
				called.add( "buildCompoundWidget" );
				super.buildCompoundWidget( element );
			}

			@Override
			protected void addWidget( Object widget, String elementName, Map<String, String> attributes )
			{
				called.add( "addWidget" );
				super.addWidget( widget, elementName, attributes );
			}
		};

		pipeline.setWidgetBuilder( new WidgetBuilder<Object, Object>()
		{
			@Override
			public Object buildWidget( String elementName, Map<String, String> attributes, Object metawidget )
			{
				// Return null if no type (this will kick us into buildCompoundWidget)

				return attributes.get( TYPE );
			}
		} );

		pipeline.addWidgetProcessor( new WidgetProcessor<Object, Object>()
		{
			@Override
			public Object processWidget( Object widget, String elementName, Map<String, String> attributes, Object metawidget )
			{
				called.add( "WidgetProcessor #1" );
				return null;
			}
		} );

		pipeline.addWidgetProcessor( new WidgetProcessor<Object, Object>()
		{
			@Override
			public Object processWidget( Object widget, String elementName, Map<String, String> attributes, Object metawidget )
			{
				called.add( "WidgetProcessor #2" );
				return null;
			}
		} );

		// Top-level widget

		pipeline.buildWidgets( (Element) XmlUtils.documentFromString( "<inspection-result><entity type=\"foo\"/></inspection-result>" ).getFirstChild() );

		assertTrue( called.size() == 1 );
		assertTrue( "WidgetProcessor #1".equals( called.get( 0 ) ) );
		assertTrue( !called.contains( "WidgetProcessor #2" ) );

		// Property-level widget

		called.clear();
		pipeline.buildWidgets( (Element) XmlUtils.documentFromString( "<inspection-result><entity><property name=\"foo\" type=\"foo\"/></entity></inspection-result>" ).getFirstChild() );

		assertTrue( called.size() == 2 );
		assertTrue( "buildCompoundWidget".equals( called.get( 0 ) ) );
		assertTrue( "WidgetProcessor #1".equals( called.get( 1 ) ) );
		assertTrue( !called.contains( "WidgetProcessor #2" ) );
	}

	/**
	 * Stubs with null attributes should not error
	 */

	public void testStubWithNullAttributes()
		throws Exception
	{
		final List<String> called = CollectionUtils.newArrayList();

		Pipeline pipeline = new Pipeline()
		{
			@Override
			protected void buildCompoundWidget( Element element )
				throws Exception
			{
				called.add( "buildCompoundWidget" );
				super.buildCompoundWidget( element );
			}

			@Override
			protected void addWidget( Object widget, String elementName, Map<String, String> attributes )
			{
				called.add( "addWidget" );
				super.addWidget( widget, elementName, attributes );
			}

			@Override
			protected Map<String, String> getAdditionalAttributes( Object widget )
			{
				called.add( "nullAttributes" );
				return null;
			}
		};

		pipeline.setWidgetBuilder( new WidgetBuilder<Object, Object>()
		{
			@Override
			public Object buildWidget( String elementName, Map<String, String> attributes, Object metawidget )
			{
				// Will return null for the top-level (because no NAME), which will trigger
				// buildCompoundWidget, and will return not-null for first property (because has a
				// NAME)

				return attributes.get( NAME );
			}
		} );

		// Top-level widget

		pipeline.buildWidgets( (Element) XmlUtils.documentFromString( "<inspection-result><entity><property name=\"foo\"/></entity></inspection-result>" ).getFirstChild() );

		assertTrue( called.size() == 3 );
		assertTrue( "buildCompoundWidget".equals( called.get( 0 ) ) );
		assertTrue( "nullAttributes".equals( called.get( 1 ) ) );
		assertTrue( "addWidget".equals( called.get( 2 ) ) );
	}

	//
	// Inner class
	//

	static class Pipeline
		extends W3CPipeline<Object, Object>
	{
		//
		// Public methods
		//

		public void testIndentation()
			throws Exception
		{
			Element element = getChildAt( getDocumentElement( "<foo><bar>baz</bar></foo>" ), 0 );
			assertTrue( "bar".equals( element.getNodeName() ) );

			element = getChildAt( getDocumentElement( "<foo>		<bar>baz</bar></foo>" ), 0 );
			assertTrue( "bar".equals( element.getNodeName() ) );

			element = getChildAt( getDocumentElement( "<foo>		<bar>baz</bar></foo>" ), 1 );
			assertTrue( null == element );
		}

		//
		// Protected methods
		//

		@Override
		protected void startBuild()
		{
			// Do nothing
		}

		@Override
		protected void addWidget( Object widget, String elementName, Map<String, String> attributes )
		{
			// Do nothing
		}

		@Override
		protected Map<String, String> getAdditionalAttributes( Object widget )
		{
			return null;
		}

		@Override
		protected Object buildNestedMetawidget( Map<String, String> attributes )
			throws Exception
		{
			return null;
		}

		@Override
		protected void endBuild()
		{
			// Do nothing
		}

		@Override
		protected Object getPipelineOwner()
		{
			return null;
		}

		@Override
		protected W3CPipeline<Object, Object> getNestedPipeline( Object metawidget )
		{
			return null;
		}
	}
}
