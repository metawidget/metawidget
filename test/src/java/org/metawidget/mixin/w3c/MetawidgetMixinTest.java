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

package org.metawidget.mixin.w3c;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetprocessor.impl.BaseWidgetProcessor;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class MetawidgetMixinTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testIndentation()
		throws Exception
	{
		new TestMixin().testIndentation();
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

		TestMixin mixin = new TestMixin()
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

		mixin.setWidgetBuilder( new WidgetBuilder<Object, Object>()
		{
			@Override
			public Object buildWidget( String elementName, Map<String, String> attributes, Object metawidget )
			{
				// Return null if no type (this will kick us into buildCompoundWidget)

				return attributes.get( TYPE );
			}
		} );

		mixin.addWidgetProcessor( new BaseWidgetProcessor<Object, Object>()
		{

			@Override
			public Object processWidget( Object widget, String elementName, Map<String, String> attributes, Object metawidget )
			{
				called.add( "WidgetProcessor #1" );
				return null;
			}
		} );
		mixin.addWidgetProcessor( new BaseWidgetProcessor<Object, Object>()
		{

			@Override
			public Object processWidget( Object widget, String elementName, Map<String, String> attributes, Object metawidget )
			{
				called.add( "WidgetProcessor #2" );
				return null;
			}
		} );

		// Top-level widget

		mixin.buildWidgets( "<inspection-result><entity type=\"foo\"/></inspection-result>" );

		assertTrue( called.size() == 1 );
		assertTrue( "WidgetProcessor #1".equals( called.get( 0 ) ) );
		assertTrue( !called.contains( "WidgetProcessor #2" ) );

		// Property-level widget

		called.clear();
		mixin.buildWidgets( "<inspection-result><entity><property name=\"foo\" type=\"foo\"/></entity></inspection-result>" );

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

		TestMixin mixin = new TestMixin()
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
			protected Map<String, String> getStubAttributes( Object stub )
			{
				called.add( "nullAttributes" );
				return null;
			}

			@Override
			protected boolean isStub( Object widget )
			{
				called.add( "wasStub" );
				return true;
			}
		};

		mixin.setWidgetBuilder( new WidgetBuilder<Object, Object>()
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

		mixin.buildWidgets( "<inspection-result><entity><property name=\"foo\"/></entity></inspection-result>" );

		assertTrue( called.size() == 4 );
		assertTrue( "buildCompoundWidget".equals( called.get( 0 ) ) );
		assertTrue( "wasStub".equals( called.get( 1 ) ) );
		assertTrue( "nullAttributes".equals( called.get( 2 ) ) );
		assertTrue( "addWidget".equals( called.get( 3 ) ) );
	}

	//
	// Inner class
	//

	static class TestMixin
		extends MetawidgetMixin<Object, Object>
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
		protected boolean isStub( Object widget )
		{
			return false;
		}

		@Override
		protected Map<String, String> getStubAttributes( Object stub )
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
		protected Object getMixinOwner()
		{
			return null;
		}

		@Override
		protected MetawidgetMixin<Object, Object> getNestedMixin( Object metawidget )
		{
			return null;
		}
	}
}
