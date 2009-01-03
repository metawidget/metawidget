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

import org.metawidget.swing._
import org.metawidget.inspector.annotation._
import scala.swing._
import scala.swing.event._

package org.metawidget.example.swing.bookstore
{
	object BookStore extends SimpleGUIApplication
	{
		var book = new Book( "The Reflective Practitioner", "1-85742-319-4" )
		def top = new MainFrame{
				val metawidget = new SwingMetawidget()
				metawidget.setInspectorConfig( "org/metawidget/example/swing/bookstore/inspector-config.xml" )
				metawidget.setToInspect( book )

				contents=new BorderPanel {
					add(Component.wrap( metawidget ),BorderPanel.Position.Center)
					add(new Button("Save") {
					reactions += {
						case ButtonClicked(_) =>
						  println(book) // Make sure it was saved correclty
					}
				},BorderPanel.Position.South)
			}
		}
	}
	class Author
	{
		var firstname = "";
		var surname = "";
		override def toString = firstname+ " "+surname
	}

	class Book( var title:String, @UiComesAfter( Array( "title" )) var ISBN:String )
	{
		@UiComesAfter( Array( "ISBN" ))
		var year = 2008;

		@UiComesAfter( Array( "year" ))
		val author = new Author();
		override def toString = title + " by "+author+", "+year
	}
}
