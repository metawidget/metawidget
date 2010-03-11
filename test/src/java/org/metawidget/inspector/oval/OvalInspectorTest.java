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

package org.metawidget.inspector.oval;

import static org.metawidget.inspector.InspectionResultConstants.*;
import junit.framework.TestCase;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.Max;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.Min;
import net.sf.oval.constraint.MinLength;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.Range;

import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class OvalInspectorTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testInspection()
	{
		OvalInspector inspector = new OvalInspector();
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Foo.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( REQUIRED ) );
		assertTrue( 2 == property.getAttributes().getLength() );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "baz" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( REQUIRED ) );
		assertEquals( "3", property.getAttribute( MINIMUM_VALUE ) );
		assertEquals( "24", property.getAttribute( MAXIMUM_VALUE ) );
		assertTrue( 4 == property.getAttributes().getLength() );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "range" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "1.5", property.getAttribute( MINIMUM_VALUE ) );
		assertEquals( "99.25", property.getAttribute( MAXIMUM_VALUE ) );
		assertEquals( "2", property.getAttribute( MINIMUM_LENGTH ) );
		assertEquals( "25", property.getAttribute( MAXIMUM_LENGTH ) );
		assertTrue( 5 == property.getAttributes().getLength() );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "forcedRange" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( REQUIRED ) );
		assertEquals( "3.1", property.getAttribute( MINIMUM_VALUE ) );
		assertEquals( "24.8", property.getAttribute( MAXIMUM_VALUE ) );
		assertEquals( "4", property.getAttribute( MINIMUM_LENGTH ) );
		assertEquals( "23", property.getAttribute( MAXIMUM_LENGTH ) );
		assertTrue( 6 == property.getAttributes().getLength() );
	}

	//
	// Inner class
	//

	public static class Foo
	{
		@NotNull
		public String	bar;

		@NotEmpty
		@Range( min = 3, max = 24 )
		public String	baz;

		@Min( 1.5 )
		@Max( 99.25 )
		@Length( min = 2, max = 25 )
		public int		range;

		@Range( min = 3.1, max = 24.8 )
		@MinLength( 4 )
		@MaxLength( 23 )
		@NotBlank
		public int		forcedRange;
	}
}
