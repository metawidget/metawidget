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

package org.metawidget.faces.renderkit.html;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HtmlSimpleLayoutRendererTest
	extends TestCase {

	//
	// Private members
	//

	private FacesContext	mContext;

	//
	// Public methods
	//

	public void testHtmlDivLayoutRenderer()
		throws Exception {

		// Empty

		HtmlMetawidget metawidget = new HtmlMetawidget();
		HtmlSimpleLayoutRenderer renderer = new HtmlSimpleLayoutRenderer();
		renderer.encodeBegin( mContext, metawidget );
		renderer.encodeChildren( mContext, metawidget );
		renderer.encodeEnd( mContext, metawidget );

		assertEquals( "<div id=\"j_id2\" style=\"display: inline\"></div>", mContext.getResponseWriter().toString() );

		// Single component

		mContext = new MockFacesContext();
		HtmlInputText inputText = new HtmlInputText();
		inputText.setId( "foo" );
		Map<String, String> attributes = CollectionUtils.newHashMap();
		inputText.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA, attributes );
		attributes.put( NAME, "Bar" );
		metawidget.getChildren().add( inputText );
		renderer.encodeBegin( mContext, metawidget );
		renderer.encodeChildren( mContext, metawidget );
		renderer.encodeEnd( mContext, metawidget );

		assertEquals( "<div id=\"j_id2\" style=\"display: inline\"><htmlInputText id=\"foo\"></htmlInputText></div>", mContext.getResponseWriter().toString() );

		// Stub with 1 child

		mContext = new MockFacesContext();
		metawidget = new HtmlMetawidget();
		UIStub stub = new UIStub();
		stub.setId( "foo" );
		stub.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA, attributes );
		metawidget.getChildren().add( stub );
		stub.getChildren().add( inputText );
		renderer.encodeBegin( mContext, metawidget );
		renderer.encodeChildren( mContext, metawidget );
		renderer.encodeEnd( mContext, metawidget );

		assertEquals( "<div id=\"j_id2\" style=\"display: inline\"><UIStub id=\"foo\"><htmlInputText id=\"foo\"></htmlInputText></UIStub></div>", mContext.getResponseWriter().toString() );

		// Stub with multiple children

		mContext = new MockFacesContext();
		stub.getChildren().add( new HtmlInputText() );
		renderer.encodeBegin( mContext, metawidget );
		renderer.encodeChildren( mContext, metawidget );
		renderer.encodeEnd( mContext, metawidget );

		assertEquals( "<div id=\"j_id2\" style=\"display: inline\"><UIStub id=\"foo\"><htmlInputText id=\"foo\"></htmlInputText><htmlInputText></htmlInputText></UIStub></div>", mContext.getResponseWriter().toString() );
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
}
