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

package org.metawidget.jsp.tagext;

import java.lang.reflect.Field;

import junit.framework.TestCase;

import org.metawidget.config.iface.ConfigReader;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.jsp.JspAnnotationInspector;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.jsp.JspMetawidgetTests.MockPageContext;
import org.metawidget.jsp.tagext.html.HtmlMetawidgetTag;
import org.metawidget.pipeline.base.BasePipeline;
import org.metawidget.util.LogUtils;
import org.metawidget.util.LogUtilsTest;

/**
 * MetawidgetTag test cases.
 * <p>
 * These are just some fringe-case tests. Most of the testing is done by WebTest.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class MetawidgetTagTest
	extends TestCase {

	//
	// Public methods
	//

	public void testMissingConfig()
		throws Exception {

		MockPageContext pageContext = new MockPageContext();
		MetawidgetTag metawidget = new HtmlMetawidgetTag();
		metawidget.setPageContext( pageContext );

		// Should not error (just log)

		metawidget.mPipeline.configureOnce();
		assertEquals( "Could not locate metawidget.xml. This file is optional, but if you HAVE created one then Metawidget isn't finding it: java.io.FileNotFoundException: Unable to locate metawidget.xml on CLASSPATH", LogUtilsTest.getLastInfoMessage() );

		// Should have done something

		assertTrue( metawidget.getPageContext().getServletContext().getAttribute( "metawidget-config-reader" ) instanceof ConfigReader );

		// Should error

		try {
			metawidget.setConfig( "does-not-exist.xml" );
			metawidget.mPipeline.configureOnce();
			fail();
		} catch ( MetawidgetException e ) {
			assertEquals( "java.io.FileNotFoundException: Unable to locate does-not-exist.xml on CLASSPATH", e.getMessage() );
		}

		// Should not re-log

		LogUtils.getLog( MetawidgetTagTest.class ).info( "" );
		metawidget = new HtmlMetawidgetTag();
		metawidget.setPageContext( pageContext );
		metawidget.mPipeline.configureOnce();
		assertFalse( "Could not locate metawidget.xml. This file is optional, but if you HAVE created one then Metawidget isn't finding it: java.io.FileNotFoundException: Unable to locate metawidget.xml on CLASSPATH".equals( LogUtilsTest.getLastInfoMessage() ) );
	}

	public void testDefaultConfig()
		throws Exception {

		MockPageContext pageContext = new MockPageContext();
		MetawidgetTag metawidget = new HtmlMetawidgetTag();
		metawidget.setPageContext( pageContext );

		Field pipelineField = MetawidgetTag.class.getDeclaredField( "mPipeline" );
		pipelineField.setAccessible( true );
		BasePipeline<?, ?, ?, ?> pipeline = (BasePipeline<?, ?, ?, ?>) pipelineField.get( metawidget );
		assertTrue( pipeline.getInspector() instanceof CompositeInspector );

		Field inspectorsField = CompositeInspector.class.getDeclaredField( "mInspectors" );
		inspectorsField.setAccessible( true );
		Inspector[] inspectors = (Inspector[]) inspectorsField.get( pipeline.getInspector() );

		assertTrue( inspectors[0] instanceof PropertyTypeInspector );
		assertTrue( inspectors[1] instanceof MetawidgetAnnotationInspector );
		assertTrue( inspectors[2] instanceof JspAnnotationInspector );
		assertEquals( inspectors.length, 3 );
	}

	public void testNullConfig()
		throws Exception {

		MockPageContext pageContext = new MockPageContext();
		MetawidgetTag metawidget = new HtmlMetawidgetTag();
		assertEquals( null, metawidget.mPipeline.getConfig() );
		metawidget.setPageContext( pageContext );
		assertEquals( "metawidget.xml", metawidget.mPipeline.getConfig() );

		// Set null

		metawidget.setConfig( null );
		metawidget.setPageContext( pageContext );
		assertEquals( null, metawidget.mPipeline.getConfig() );

		// Un-null again

		metawidget.setConfig( "Foo" );
		metawidget.setPageContext( pageContext );
		assertEquals( "Foo", metawidget.mPipeline.getConfig() );
	}
}
