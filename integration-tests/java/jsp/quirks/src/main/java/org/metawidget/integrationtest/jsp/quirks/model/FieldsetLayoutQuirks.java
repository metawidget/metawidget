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

package org.metawidget.integrationtest.jsp.quirks.model;

import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiSection;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
