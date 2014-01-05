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

import java.util._
import javax.swing._
import javax.persistence._
import org.hibernate.validator._
import scala.annotation.target._

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
