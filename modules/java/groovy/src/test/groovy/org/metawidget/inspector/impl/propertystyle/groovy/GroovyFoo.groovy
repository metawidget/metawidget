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

package org.metawidget.inspector.impl.propertystyle.groovy;

import javax.swing.*;

import javax.persistence.*;
import org.hibernate.validator.*;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

class GroovyFoo
	extends JDialog
{
	@Column( nullable = false )
	String		foo
	
	String		FOo

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
