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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class OvalInspectorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testInspection() {

		OvalInspector inspector = new OvalInspector();
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Foo.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( REQUIRED ) );
		assertEquals( 2, property.getAttributes().getLength() );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "baz" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( REQUIRED ) );
		assertEquals( "3", property.getAttribute( MINIMUM_VALUE ) );
		assertEquals( "24", property.getAttribute( MAXIMUM_VALUE ) );
		assertEquals( 4, property.getAttributes().getLength() );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "range" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "1.5", property.getAttribute( MINIMUM_VALUE ) );
		assertEquals( "99.25", property.getAttribute( MAXIMUM_VALUE ) );
		assertEquals( "2", property.getAttribute( MINIMUM_LENGTH ) );
		assertEquals( "25", property.getAttribute( MAXIMUM_LENGTH ) );
		assertEquals( 5, property.getAttributes().getLength() );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "forcedRange" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( REQUIRED ) );
		assertEquals( "3.1", property.getAttribute( MINIMUM_VALUE ) );
		assertEquals( "24.8", property.getAttribute( MAXIMUM_VALUE ) );
		assertEquals( "4", property.getAttribute( MINIMUM_LENGTH ) );
		assertEquals( "23", property.getAttribute( MAXIMUM_LENGTH ) );
		assertEquals( 6, property.getAttributes().getLength() );
	}

	//
	// Inner class
	//

	public static class Foo {

		@NotNull
		public String getBar() {

			return null;
		}

		public void setBar( @SuppressWarnings( "unused" )String bar ) {

			// Do nothing
		}

		@NotEmpty
		@Range( min = 3, max = 24 )
		public String getBaz() {

			return null;
		}

		public void setBaz( @SuppressWarnings( "unused" )String baz ) {

			// Do nothing
		}

		@Min( 1.5 )
		@Max( 99.25 )
		@Length( min = 2, max = 25 )
		public int getRange() {

			return 0;
		}

		public void setRange() {

			// Do nothing
		}

		@Range( min = 3.1, max = 24.8 )
		@MinLength( 4 )
		@MaxLength( 23 )
		@NotBlank
		public int getForcedRange() {

			return 0;
		}

		public void setForcedRange( @SuppressWarnings( "unused" ) int forcedRange ) {

			// Do nothing
		}
	}
}
