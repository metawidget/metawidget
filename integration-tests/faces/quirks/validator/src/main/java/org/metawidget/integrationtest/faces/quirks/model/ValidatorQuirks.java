package org.metawidget.integrationtest.faces.quirks.model;

import javax.faces.bean.ManagedBean;

import org.metawidget.inspector.annotation.UiComesAfter;

@ManagedBean( name = "validatorQuirks" )
public class ValidatorQuirks {

	//
	// Private members
	//

	private String	mFoo;

	private String	mBar;

	private String	mBaz;

	//
	// Public methods
	//

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

	@UiComesAfter( "bar" )
	public String getBaz() {

		return mBaz;
	}

	public void setBaz( String baz ) {

		mBaz = baz;
	}

	public void save() {

		// Do nothing
	}
}
