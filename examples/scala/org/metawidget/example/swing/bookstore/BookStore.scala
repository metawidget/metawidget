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

import javax.swing._

import org.metawidget.swing._
import org.metawidget.swing.propertybinding.beansbinding._
import org.metawidget.inspector.annotation._

package org.metawidget.example.swing.bookstore
{
	class Author
	{
		var firstname = "";
		var surname = "";
	}
	
	class Book( var title:String, @UiComesAfter( Array( "title" )) var ISBN:String )
	{
	  	@UiComesAfter( Array( "ISBN" ))
		var year = 2008;
		
		@UiComesAfter( Array( "year" ))
		val author = new Author();
	}
	
	object BookStore extends JFrame( "Book Store" ) 
	{
	    def main( args: Array[String] ) = 
	    {
	        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	        
	        val metawidget = new SwingMetawidget()
         	metawidget.setInspectorConfig( "org/metawidget/example/swing/bookstore/inspector-config.xml" )
	        metawidget.setToInspect( new Book( "The Reflective Practitioner", "1-85742-319-4" ) )
	        
	        add( metawidget )
	        pack();
	        setVisible( true );
	    }
	}
}
