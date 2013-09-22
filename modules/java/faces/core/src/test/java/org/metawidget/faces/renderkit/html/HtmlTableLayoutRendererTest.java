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

import javax.faces.component.html.HtmlInputHidden;
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

public class HtmlTableLayoutRendererTest
	extends TestCase {

	//
	// Private members
	//

	private FacesContext	mContext;

	//
	// Public methods
	//

	public void testHtmlTableLayoutRenderer()
		throws Exception {

		// Empty

		HtmlMetawidget metawidget = new HtmlMetawidget();
		HtmlTableLayoutRenderer renderer = new HtmlTableLayoutRenderer();
		renderer.encodeBegin( mContext, metawidget );
		renderer.encodeChildren( mContext, metawidget );
		renderer.encodeEnd( mContext, metawidget );

		assertEquals( "<table id=\"j_id2\"><tbody></tbody></table>", mContext.getResponseWriter().toString() );

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

		assertEquals( "<table id=\"j_id2\"><tbody><tr><th><htmlOutputLabel id=\"foo-label\" for=\"foo\" value=\"Bar:\"></htmlOutputLabel></th><td><htmlInputText id=\"foo\"></htmlInputText><htmlMessage id=\"j_idmw0\" for=\"foo\"></htmlMessage></td><td><div></div></td></tr></tbody></table>", mContext.getResponseWriter().toString() );

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

		assertEquals( "<table id=\"j_id2\"><tbody><tr><th><htmlOutputLabel id=\"foo-label\" for=\"foo\" value=\"Bar:\"></htmlOutputLabel></th><td><UIStub id=\"foo\"><htmlInputText id=\"foo\"></htmlInputText></UIStub></td><td><div></div></td></tr></tbody></table>", mContext.getResponseWriter().toString() );

		// Stub with multiple children

		mContext = new MockFacesContext();
		stub.getChildren().add( new HtmlInputText() );
		renderer.encodeBegin( mContext, metawidget );
		renderer.encodeChildren( mContext, metawidget );
		renderer.encodeEnd( mContext, metawidget );

		assertEquals( "<table id=\"j_id2\"><tbody><tr><th><htmlOutputLabel value=\"Bar:\"></htmlOutputLabel></th><td><UIStub id=\"foo\"><htmlInputText id=\"foo\"></htmlInputText><htmlInputText></htmlInputText></UIStub></td><td><div></div></td></tr></tbody></table>", mContext.getResponseWriter().toString() );

		// Component and a hidden field

		mContext = new MockFacesContext();
		metawidget = new HtmlMetawidget();
		inputText = new HtmlInputText();
		inputText.setId( "foo" );
		attributes = CollectionUtils.newHashMap();
		inputText.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA, attributes );
		attributes.put( NAME, "Bar" );
		attributes.put( REQUIRED, TRUE );
		metawidget.getChildren().add( inputText );
		metawidget.getChildren().add( new HtmlInputHidden() );
		renderer.encodeBegin( mContext, metawidget );
		renderer.encodeChildren( mContext, metawidget );
		renderer.encodeEnd( mContext, metawidget );

		assertEquals( "<div id=\"j_id2\"><htmlInputHidden></htmlInputHidden><table><tbody><tr><th><htmlOutputLabel id=\"foo-label\" for=\"foo\" value=\"Bar:\"></htmlOutputLabel></th><td><htmlInputText id=\"foo\"></htmlInputText><htmlMessage id=\"j_idmw0\" for=\"foo\"></htmlMessage></td><td>*</td></tr></tbody></table></div>", mContext.getResponseWriter().toString() );
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
