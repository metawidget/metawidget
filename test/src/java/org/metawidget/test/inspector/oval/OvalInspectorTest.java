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

package org.metawidget.test.inspector.oval;

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

import org.metawidget.inspector.oval.OvalInspector;
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
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public OvalInspectorTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testInspection()
	{
		OvalInspector inspector = new OvalInspector();
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ) );
		assertTrue( Foo.class.getName().equals( entity.getAttribute( TYPE ) ) );
		assertTrue( !entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar" );
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( TRUE.equals( property.getAttribute( REQUIRED ) ) );
		assertTrue( 2 == property.getAttributes().getLength() );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "baz" );
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( TRUE.equals( property.getAttribute( REQUIRED ) ) );
		assertTrue( 2 == property.getAttributes().getLength() );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "range" );
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "1.0".equals( property.getAttribute( MINIMUM_VALUE ) ) );
		assertTrue( "99.0".equals( property.getAttribute( MAXIMUM_VALUE ) ) );
		assertTrue( "2".equals( property.getAttribute( MINIMUM_LENGTH ) ) );
		assertTrue( "25".equals( property.getAttribute( MAXIMUM_LENGTH ) ) );
		assertTrue( 5 == property.getAttributes().getLength() );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "forcedRange" );
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( TRUE.equals( property.getAttribute( REQUIRED ) ) );
		assertTrue( "3".equals( property.getAttribute( MINIMUM_VALUE ) ) );
		assertTrue( "24".equals( property.getAttribute( MAXIMUM_VALUE ) ) );
		assertTrue( "4".equals( property.getAttribute( MINIMUM_LENGTH ) ) );
		assertTrue( "23".equals( property.getAttribute( MAXIMUM_LENGTH ) ) );
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
		public String	baz;

		@Min( 1 )
		@Max( 99 )
		@Length( min = 2, max = 25 )
		public int		range;

		@Range( min = 3, max = 24 )
		@MinLength( 4 )
		@MaxLength( 23 )
		@NotBlank
		public int		forcedRange;
	}
}
