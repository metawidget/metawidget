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

package org.metawidget.test.util;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import junit.framework.TestCase;

import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.JspUtils.BodyPreparer;

/**
 * @author Richard Kennard
 */

public class JspUtilsTest
	extends TestCase
{
	//
	//
	// Package-level statics
	//
	//

	final static String	NEWLINE	= System.getProperty( "line.separator" );

	//
	//
	// Public methods
	//
	//

	public void testJspUtils()
		throws Exception
	{
		Tag testTag = new Tag()
		{
			@Override
			public int doEndTag()
			{
				return 0;
			}

			@Override
			public int doStartTag()
			{
				return 0;
			}

			@Override
			public Tag getParent()
			{
				return null;
			}

			@Override
			public void release()
			{
				// Do nothing
			}

			@Override
			public void setPageContext( PageContext context )
			{
				// Do nothing
			}

			@Override
			public void setParent( Tag parent )
			{
				// Do nothing
			}

		};

		JspUtils.writeTag( null, testTag, null, new BodyPreparer()
		{
			@Override
			public void prepareBody( PageContext delegateContext )
				throws IOException
			{
				JspWriter writer = delegateContext.getOut();

				// Write

				writer.newLine();
				writer.print( true );
				writer.print( 'a' );
				writer.print( Integer.MAX_VALUE );
				writer.print( Long.MAX_VALUE );
				writer.print( Float.MAX_VALUE );
				writer.print( Double.MAX_VALUE );
				writer.print( new char[] { 'b', 'c' } );
				Object object1 = new Object();
				writer.print( object1 );
				writer.println();
				writer.println( false );
				writer.println( 'b' );
				writer.println( Integer.MIN_VALUE );
				writer.println( Long.MIN_VALUE );
				writer.println( Float.MIN_VALUE );
				writer.println( Double.MIN_VALUE );
				writer.println( new char[] { 'c', 'd' } );
				Object object2 = new Object();
				writer.println( object2 );
				writer.println( "END" );
				writer.flush();
				writer.close();

				// Verify

				String verify = NEWLINE + "truea" + Integer.MAX_VALUE + Long.MAX_VALUE + Float.MAX_VALUE + Double.MAX_VALUE + "bc" + object1;
				verify += NEWLINE + "false" + NEWLINE;
				verify += "b" + NEWLINE;
				verify += Integer.MIN_VALUE + NEWLINE;
				verify += Long.MIN_VALUE + NEWLINE;
				verify += Float.MIN_VALUE + NEWLINE;
				verify += Double.MIN_VALUE + NEWLINE;
				verify += "cd" + NEWLINE;
				verify += object2 + NEWLINE;
				verify += "END" + NEWLINE;

				assertTrue( verify.equals( writer.toString() ) );
			}
		} );
	}

	//
	//
	// Constructor
	//
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public JspUtilsTest( String name )
	{
		super( name );
	}
}
