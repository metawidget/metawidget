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

package org.metawidget.pipeline.base;

import java.util.Map;

import javax.swing.JComponent;

import junit.framework.TestCase;

import org.metawidget.pipeline.w3c.W3CPipeline;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;

/**
 * @author Richard Kennard
 */

public class BasePipelineTest
	extends TestCase {

	//
	// Public methods
	//

	public void testBuildCompoundWidget()
		throws Exception {

		W3CPipeline<JComponent, JComponent, JComponent> pipeline = new W3CPipeline<JComponent, JComponent, JComponent>() {

			@Override
			protected void configure() {

				// Do nothing
			}

			@Override
			protected Map<String, String> getAdditionalAttributes( JComponent widget ) {

				return null;
			}

			@Override
			protected JComponent buildNestedMetawidget( Map<String, String> attributes )
				throws Exception {

				return null;
			}

			@Override
			protected JComponent getPipelineOwner() {

				return null;
			}
		};

		// Bad entity

		Document document = XmlUtils.documentFromString( "<inspection-result><property type=\"foo\"/></inspection-result>" );

		try {
			pipeline.buildWidgets( document.getDocumentElement() );
			assertTrue( false );
		} catch( Exception e ) {
			assertEquals( "Top-level element name should be entity, not property", e.getMessage() );
		}

		// Bad child

		document = XmlUtils.documentFromString( "<inspection-result><entity type=\"foo\"><bar/></entity></inspection-result>" );

		try {
			pipeline.buildCompoundWidget( XmlUtils.getElementAt( document.getDocumentElement(), 0 ));
			assertTrue( false );
		} catch( Exception e ) {
			assertEquals( "Child element #1 should be property or action, not bar", e.getMessage() );
		}

		// Missing name attribute

		document = XmlUtils.documentFromString( "<inspection-result><entity type=\"foo\"><property/></entity></inspection-result>" );

		try {
			pipeline.buildCompoundWidget( XmlUtils.getElementAt( document.getDocumentElement(), 0 ));
			assertTrue( false );
		} catch( Exception e ) {
			assertEquals( "Child element #1 has no @name", e.getMessage() );
		}
	}
}
