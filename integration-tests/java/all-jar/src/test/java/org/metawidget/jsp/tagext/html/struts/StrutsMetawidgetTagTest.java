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

package org.metawidget.jsp.tagext.html.struts;

import java.lang.reflect.Field;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.commons.validator.CommonsValidatorInspector;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.inspector.struts.StrutsAnnotationInspector;
import org.metawidget.inspector.struts.StrutsInspector;
import org.metawidget.jsp.JspMetawidgetTests.MockPageContext;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.pipeline.base.BasePipeline;

/**
 * StrutsMetawidgetTag test cases.
 * <p>
 * These are just some fringe-case tests. Most of the testing is done by WebTest.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StrutsMetawidgetTagTest
	extends TestCase {

	//
	// Public methods
	//

	public void testDefaultConfig()
		throws Exception {

		MockPageContext pageContext = new MockPageContext();
		StrutsMetawidgetTag metawidget = new StrutsMetawidgetTag();
		metawidget.setPageContext( pageContext );

		Field pipelineField = MetawidgetTag.class.getDeclaredField( "mPipeline" );
		pipelineField.setAccessible( true );
		BasePipeline<?, ?, ?, ?> pipeline = (BasePipeline<?, ?, ?, ?>) pipelineField.get( metawidget );
		assertTrue( pipeline.getInspector() instanceof CompositeInspector );

		Field inspectorsField = CompositeInspector.class.getDeclaredField( "mInspectors" );
		inspectorsField.setAccessible( true );
		Inspector[] inspectors = (Inspector[]) inspectorsField.get( pipeline.getInspector() );

		assertTrue( inspectors[0] instanceof StrutsInspector );
		assertTrue( inspectors[1] instanceof PropertyTypeInspector );
		assertTrue( inspectors[2] instanceof MetawidgetAnnotationInspector );
		assertTrue( inspectors[3] instanceof StrutsAnnotationInspector );
		assertTrue( inspectors[4] instanceof CommonsValidatorInspector );
		assertEquals( inspectors.length, 5 );
	}
}
