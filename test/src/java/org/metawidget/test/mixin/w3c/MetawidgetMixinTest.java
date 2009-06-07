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

package org.metawidget.test.mixin.w3c;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.mixin.w3c.MetawidgetMixin;
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

	public void testMixin()
		throws Exception
	{
		new TestMixin().testIndentation();
	}

	//
	// Inner class
	//

	static class TestMixin
		extends MetawidgetMixin<Object,Object>
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
			throws Exception
		{
			// Do nothing
		}

		@Override
		protected void addWidget( Object widget, String elementName, Map<String, String> attributes )
			throws Exception
		{
			// Do nothing
		}

		@Override
		protected Object getOverriddenWidget( String elementName, Map<String, String> attributes )
		{
			return null;
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
			throws Exception
		{
			// Do nothing
		}

		@Override
		protected Object getMixinOwner()
		{
			return null;
		}
	}
}
