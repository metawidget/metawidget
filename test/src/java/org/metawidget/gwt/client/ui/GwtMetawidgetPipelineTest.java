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

package org.metawidget.gwt.client.ui;

import java.util.Map;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.xml.client.Element;

/**
 * @author Richard Kennard
 */

public class GwtMetawidgetPipelineTest
	extends GWTTestCase
{
	//
	// Public methods
	//

	@Override
	public String getModuleName()
	{
		return "org.metawidget.gwt.GwtMetawidgetTest";
	}

	public void testPipeline()
		throws Exception
	{
		new Pipeline().testIndentation();
	}

	//
	// Inner class
	//

	static class Pipeline
		extends GwtPipeline<Object, Object, GwtMetawidget>
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
		protected Map<String, String> getAdditionalAttributes( Object stub )
		{
			return null;
		}

		@Override
		protected GwtMetawidget buildNestedMetawidget( Map<String, String> attributes )
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
		protected GwtMetawidget getPipelineOwner()
		{
			return null;
		}
	}
}