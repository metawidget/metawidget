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

package org.metawidget.test.inspector.impl.propertystyle.groovy;

import javax.swing.*;

import javax.persistence.*;
import org.hibernate.validator.*;

/**
 * @author Richard Kennard
 */

class GroovyFoo
	extends JDialog
{
	@Column( nullable = false )
	String		foo

	List<Date>	bar

	boolean 	baz
	
	protected	boolean	mInaccessibleProperty;
	
	@NotNull
	public String getMethodFoo()
	{
		return null;
	}

	@Length( min = 5 )
	public void setMethodBar( String methodBar )
	{
	}
	
	public List<String> getMethodBaz()
	{
		return null;
	}
	
	public void setMethodAbc( List<Boolean> methodAbc )
	{
	}
	
	protected boolean getInaccessibleProperty()
	{
		return mInaccessibleProperty;
	}
	
	protected void setInaccessibleProperty( boolean inaccessibleProperty )
	{
		mInaccessibleProperty = inaccessibleProperty;	
	}
}
