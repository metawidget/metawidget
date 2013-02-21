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
 * @author Richard Kennard
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
