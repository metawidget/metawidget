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

package org.metawidget.test.inspector.commons.jexl;

import static org.metawidget.inspector.InspectionResultConstants.*;
import junit.framework.TestCase;

import org.metawidget.inspector.commons.jexl.JexlInspector;
import org.metawidget.inspector.commons.jexl.UiJexlAttribute;
import org.metawidget.inspector.commons.jexl.UiJexlAttributes;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class JexlInspectorTest
	extends TestCase
{
	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public JexlInspectorTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testAnnotations()
	{
		JexlInspector inspector = new JexlInspector();
		assertTrue( null == inspector.inspect( (Object) null, Foo.class.getName() ));

		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ));

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ) );
		assertTrue( Foo.class.getName().equals( entity.getAttribute( TYPE ) ) );
		assertTrue( !entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar1" );
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "from-baz".equals( property.getAttribute( "value-is-el" ) ) );
		assertTrue( "text".equals( property.getAttribute( "value-is-text" ) ) );
		assertTrue( !property.hasAttribute( "condition-is-false" ) );
		assertTrue( 3 == property.getAttributes().getLength() );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar2" );
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "was set".equals( property.getAttribute( "condition-is-true" ) ) );
		assertTrue( 2 == property.getAttributes().getLength() );

		assertTrue( entity.getChildNodes().getLength() == 2 );
	}

	//
	// Inner class
	//

	public static class Foo
	{
		@UiJexlAttributes( {
			@UiJexlAttribute( name = "value-is-el", expression="this.baz" ),
			@UiJexlAttribute( name = "value-is-text", expression="'text'" ),
			@UiJexlAttribute( name = "condition-is-false", expression="if ( !this.conditionResult ) 'was set'" )
		} )
		public String bar1;

		@UiJexlAttribute( name = "condition-is-true", expression="if ( this.conditionResult ) { 'was set'; }" )
		public String bar2;

		public String getBaz()
		{
			return "from-baz";
		}

		public boolean isConditionResult()
		{
			return true;
		}
	}
}
