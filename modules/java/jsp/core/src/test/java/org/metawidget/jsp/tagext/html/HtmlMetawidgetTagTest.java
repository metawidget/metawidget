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

package org.metawidget.jsp.tagext.html;

import java.lang.reflect.Field;

import junit.framework.TestCase;

import org.metawidget.jsp.tagext.FacetTag;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.StubTag;
import org.metawidget.pipeline.base.BasePipeline;

/**
 * HtmlMetawidgetTag test cases.
 * <p>
 * These are just some fringe-case tests. Most of the testing is done by WebTest.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HtmlMetawidgetTagTest
	extends TestCase {

	//
	// Public methods
	//

	public void testMetawidget()
		throws Exception {

		HtmlMetawidgetTag metawidget = new HtmlMetawidgetTag();

		// Value without prefix

		metawidget.setValue( "foo" );
		assertEquals( "foo", metawidget.getPath() );
		assertEquals( null, metawidget.getPathPrefix() );

		// Value with prefix

		metawidget.setValue( "foo.bar" );
		assertEquals( "foo.bar", metawidget.getPath() );
		assertEquals( "foo.", metawidget.getPathPrefix() );

		metawidget.setValue( "foo.bar.baz" );
		assertEquals( "foo.bar.baz", metawidget.getPath() );
		assertEquals( "foo.bar.", metawidget.getPathPrefix() );
	}

	public void testLifecyle()
		throws Exception {

		// We must use doStartTag(), not rely on super.release()

		HtmlMetawidgetTag metawidget = new HtmlMetawidgetTag();

		FacetTag facetTag = new FacetTag();
		Field savedBodyContentField = FacetTag.class.getDeclaredField( "mSavedBodyContent" );
		savedBodyContentField.setAccessible( true );
		savedBodyContentField.set( facetTag, "abc" );
		metawidget.setFacet( "foo", facetTag );

		StubTag stubTag = new HtmlStubTag();
		savedBodyContentField = StubTag.class.getDeclaredField( "mSavedBodyContent" );
		savedBodyContentField.setAccessible( true );
		savedBodyContentField.set( stubTag, "ghi" );
		metawidget.setStub( "baz", stubTag );

		Field facets = MetawidgetTag.class.getDeclaredField( "mFacets" );
		facets.setAccessible( true );
		Field stubs = MetawidgetTag.class.getDeclaredField( "mStubs" );
		stubs.setAccessible( true );
		Field pipelineField = MetawidgetTag.class.getDeclaredField( "mPipeline" );
		pipelineField.setAccessible( true );
		BasePipeline<?,?,?,?> pipeline = (BasePipeline<?,?,?,?>) pipelineField.get( metawidget );
		Field needsConfiguringField = BasePipeline.class.getDeclaredField( "mNeedsConfiguring" );
		needsConfiguringField.setAccessible( true );
		needsConfiguringField.set( pipeline, false );

		assertTrue( null != facets.get( metawidget ) );
		assertTrue( null != stubs.get( metawidget ) );
		assertTrue( false == (Boolean) needsConfiguringField.get( pipeline ) );

		// Should reset facets and stubs

		metawidget.doStartTag();
		assertEquals( null, facets.get( metawidget ) );
		assertEquals( null, stubs.get( metawidget ) );
		assertTrue( true == (Boolean) needsConfiguringField.get( pipeline ) );
	}
}
