// Metawidget (licensed under LGPL)
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

package org.metawidget.integrationtest.jsp.quirks.model;

import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiSection;

/**
 * @author Richard Kennard
 */

public class FieldsetLayoutQuirks {

	//
	// Private members
	//

	private String	mFoo;

	private String	mBar;

	private String	mBaz;

	private String	mAbc;

	private String	mDef;

	//
	// Public methods
	//

	@UiSection( "Section 1" )
	public String getFoo() {

		return mFoo;
	}

	public void setFoo( String foo ) {

		mFoo = foo;
	}

	@UiComesAfter( "foo" )
	public String getBar() {

		return mBar;
	}

	public void setBar( String bar ) {

		mBar = bar;
	}

	@UiSection( "Section 2" )
	@UiComesAfter( "bar" )
	public String getBaz() {

		return mBaz;
	}

	public void setBaz( String baz ) {

		mBaz = baz;
	}

	@UiComesAfter( "baz" )
	@UiSection( { "Section 3" } )
	public String getAbc() {

		return mAbc;
	}

	public void setAbc( String abc ) {

		mAbc = abc;
	}

	@UiComesAfter( "abc" )
	public String getDef() {

		return mDef;
	}

	public void setDef( String def ) {

		mDef = def;
	}
}
