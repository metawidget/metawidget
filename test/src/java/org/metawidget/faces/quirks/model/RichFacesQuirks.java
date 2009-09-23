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

package org.metawidget.faces.quirks.model;

import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiSection;


/**
 * Models an entity that tests some RichFaces-specific quirks.
 *
 * @author Richard Kennard
 */

public class RichFacesQuirks
{
	//
	// Private members
	//

	private String	mFoo1;

	private String	mFoo2;

	private String	mBar1;

	private String	mBar2;

	private String	mBaz1;

	private String	mBaz2;

	private String	mBaz3;

	private String	mAbc;

	private String	mDef1;

	private String	mDef2;

	//
	// Public methods
	//

	public String getFoo1()
	{
		return mFoo1;
	}

	public void setFoo1( String foo1 )
	{
		mFoo1 = foo1;
	}

	@UiComesAfter( "foo1" )
	public String getFoo2()
	{
		return mFoo2;
	}

	public void setFoo2( String foo2 )
	{
		mFoo2 = foo2;
	}

	@UiComesAfter( "foo2" )
	@UiSection( "bar" )
	public String getBar1()
	{
		return mBar1;
	}

	public void setBar1( String bar1 )
	{
		mBar1 = bar1;
	}

	@UiComesAfter( "bar1" )
	public String getBar2()
	{
		return mBar2;
	}

	public void setBar2( String bar2 )
	{
		mBar2 = bar2;
	}

	@UiComesAfter( "bar2" )
	@UiSection( "baz" )
	public String getBaz1()
	{
		return mBaz1;
	}

	public void setBaz1( String baz1 )
	{
		mBaz1 = baz1;
	}

	@UiComesAfter( "baz1" )
	public String getBaz2()
	{
		return mBaz2;
	}

	public void setBaz2( String baz2 )
	{
		mBaz2 = baz2;
	}

	@UiComesAfter( "baz2" )
	public String getBaz3()
	{
		return mBaz3;
	}

	public void setBaz3( String baz3 )
	{
		mBaz3 = baz3;
	}

	@UiComesAfter( "baz3" )
	@UiSection( "" )
	public String getAbc()
	{
		return mAbc;
	}

	public void setAbc( String abc )
	{
		mAbc = abc;
	}

	@UiComesAfter( "abc" )
	@UiSection( "dEf" )
	public String getDef1()
	{
		return mDef1;
	}

	public void setDef1( String def1 )
	{
		mDef1 = def1;
	}

	@UiComesAfter( "def1" )
	public String getDef2()
	{
		return mDef2;
	}

	public void setDef2( String def2 )
	{
		mDef2 = def2;
	}

	@UiAction
	@UiComesAfter( "def2" )
	@UiSection( "" )
	public void save()
	{
		// Will save in session
	}
}
