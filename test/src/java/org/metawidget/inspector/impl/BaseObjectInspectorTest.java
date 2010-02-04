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

package org.metawidget.inspector.impl;

import static org.metawidget.inspector.InspectionResultConstants.*;
import junit.framework.TestCase;

import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.annotation.UiLabel;
import org.metawidget.inspector.annotation.UiMasked;
import org.metawidget.util.TestUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class BaseObjectInspectorTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testConfig()
	{
		TestUtils.testEqualsAndHashcode( BaseObjectInspectorConfig.class, new BaseObjectInspectorConfig()
		{
			// Subclass
		} );
	}

	/**
	 * Test that parent properties <em>and</em> parent traits get merged in properly.
	 */

	public void testInspectParent()
	{
		MetawidgetAnnotationInspector inspector = new MetawidgetAnnotationInspector();
		Document document = XmlUtils.documentFromString( inspector.inspect( new PropertyAndTraitAnnotation(), PropertyAndTraitAnnotation.class.getName(), "foo" ));

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ));

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ));
		assertTrue( String.class.getName().equals( entity.getAttribute( TYPE ) ));
		assertTrue( "foo".equals( entity.getAttribute( NAME ) ));
		assertTrue( TRUE.equals( entity.getAttribute( MASKED ) ));
		assertTrue( "Foo".equals( entity.getAttribute( LABEL ) ));
		assertTrue( 4 == entity.getAttributes().getLength() );

		assertTrue( 0 == entity.getChildNodes().getLength() );

		// Test null parent doesn't throw NullPointerException

		assertTrue( null == inspector.inspect( null, PropertyAndTraitAnnotation.class.getName(), "foo" ));
	}

	public void testNullPropertyStyle()
	{
		MetawidgetAnnotationInspector inspector = new MetawidgetAnnotationInspector();

		// Shoud fail hard

		try
		{
			assertTrue( inspector.getProperties( null ).isEmpty() );
			assertTrue( false );
		}
		catch( NullPointerException e )
		{
			// true
		}

		// Should fail gracefully

		BaseObjectInspectorConfig config = new BaseObjectInspectorConfig();
		config.setPropertyStyle( null );

		inspector = new MetawidgetAnnotationInspector( config );
		assertTrue( inspector.getProperties( null ).isEmpty() );
	}

	public void testNullActionStyle()
	{
		MetawidgetAnnotationInspector inspector = new MetawidgetAnnotationInspector();

		// Shoud fail hard

		try
		{
			assertTrue( inspector.getActions( null ).isEmpty() );
			assertTrue( false );
		}
		catch( NullPointerException e )
		{
			// true
		}

		// Should fail gracefully

		BaseObjectInspectorConfig config = new BaseObjectInspectorConfig();
		config.setActionStyle( null );

		inspector = new MetawidgetAnnotationInspector( config );
		assertTrue( inspector.getActions( null ).isEmpty() );
	}

	//
	// Inner class
	//

	public static class PropertyAndTraitAnnotation
	{
		@UiMasked
		@UiLabel( "Foo" )
		public String foo;
	}
}
