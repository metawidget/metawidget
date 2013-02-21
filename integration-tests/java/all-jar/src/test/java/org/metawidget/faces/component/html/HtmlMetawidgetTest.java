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

package org.metawidget.faces.component.html;

import java.lang.reflect.Field;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.faces.FacesAnnotationInspector;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.pipeline.base.BasePipeline;

/**
 * HtmlMetawidgetTest test cases.
 * <p>
 * These are just some fringe-case tests. Most of the testing is done by WebTest.
 *
 * @author Richard Kennard
 */

public class HtmlMetawidgetTest
	extends TestCase {

	//
	// Private members
	//

	private MockFacesContext	mContext;

	//
	// Public methods
	//

	public void testDefaultConfig()
		throws Exception {

		HtmlMetawidget metawidget = new HtmlMetawidget();

		Field pipelineField = UIMetawidget.class.getDeclaredField( "mPipeline" );
		pipelineField.setAccessible( true );
		BasePipeline<?, ?, ?, ?> pipeline = (BasePipeline<?, ?, ?, ?>) pipelineField.get( metawidget );
		assertTrue( pipeline.getInspector() instanceof CompositeInspector );

		Field inspectorsField = CompositeInspector.class.getDeclaredField( "mInspectors" );
		inspectorsField.setAccessible( true );
		Inspector[] inspectors = (Inspector[]) inspectorsField.get( pipeline.getInspector() );

		assertTrue( inspectors[0] instanceof PropertyTypeInspector );
		assertTrue( inspectors[1] instanceof MetawidgetAnnotationInspector );
		assertTrue( inspectors[2] instanceof FacesAnnotationInspector );
		assertEquals( inspectors.length, 3 );
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
