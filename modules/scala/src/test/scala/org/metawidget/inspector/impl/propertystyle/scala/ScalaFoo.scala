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

import java.util._
import javax.swing._
import javax.persistence._
import org.hibernate.validator._
import scala.annotation.target._

/**
 * @author Richard Kennard
 */

package org.metawidget.inspector.impl.propertystyle.scala
{
	class ScalaFoo( @(Column @field)( nullable = false ) var foo:String, @(NotNull @field) var bar:List[Date], val baz:Boolean, private var mInaccessibleProperty:Boolean )
		extends JDialog
	{
		def getIgnoredProperty():String = { return null }
	
		protected def getInaccessibleProperty():Boolean = {	return mInaccessibleProperty }
		
		protected def setInaccessibleProperty(inaccessibleProperty:Boolean) = {	mInaccessibleProperty = inaccessibleProperty }
	}
}
