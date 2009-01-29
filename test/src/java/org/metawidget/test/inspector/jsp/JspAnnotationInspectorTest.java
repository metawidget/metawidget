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

package org.metawidget.test.inspector.jsp;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.jsp.JspInspectionResultConstants.*;
import junit.framework.TestCase;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.jsp.JspAnnotationInspector;
import org.metawidget.inspector.jsp.UiJspAttribute;
import org.metawidget.inspector.jsp.UiJspLookup;
import org.metawidget.test.util.JspUtilsTest.DummyPageContext;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class JspAnnotationInspectorTest
	extends TestCase
{
	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public JspAnnotationInspectorTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testLookup()
	{
		JspAnnotationInspector inspector = new JspAnnotationInspector();
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ) );
		assertTrue( Foo.class.getName().equals( entity.getAttribute( TYPE ) ) );
		assertTrue( !entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "object1" );
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "${foo.bar}".equals( property.getAttribute( JSP_LOOKUP ) ) );

		assertTrue( entity.getChildNodes().getLength() == 1 );
	}

	public void testAttribute()
	{
		JspAnnotationInspector inspector = new JspAnnotationInspector();

		try
		{
			inspector.inspect( new Bar(), Bar.class.getName() );
		}
		catch( InspectorException e )
		{
			assertTrue( "ThreadLocalPageContext not set".equals( e.getMessage() ));
		}

		try
		{
			JspAnnotationInspector.setThreadLocalPageContext( new DummyPageContext() );
			inspector.inspect( new Bar(), Bar.class.getName() );
		}
		catch( InspectorException e )
		{
			assertTrue( "Expression 'bad-expression' is not of the form ${...}".equals( e.getMessage() ));
		}
	}

	//
	// Inner class
	//

	public static class Foo
	{
		@UiJspLookup( "${foo.bar}" )
		public Object	object1;
	}

	public static class Bar
	{
		@UiJspAttribute( name = "baz", expression = "bad-expression" )
		public Object	object1;
	}
}
