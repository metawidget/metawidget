// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

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
