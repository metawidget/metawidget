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

package org.metawidget.integrationtest.faces.quirks.model;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@ManagedBean( name = "validatorQuirks" )
public class ValidatorQuirks {

	//
	// Private members
	//

	private String	mFoo;

	private String	mBar;

	private String	mBaz;

	private boolean	mMessageAdded	= false;

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

	@UiHidden
	public boolean isMessageAdded() {

		return mMessageAdded;
	}

	/**
	 * Tests we are using FacesContext.isValidationFailed under JSF 2
	 */

	public void addMessage() {

		mMessageAdded = true;

		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage( null, new FacesMessage( FacesMessage.SEVERITY_ERROR, "Message Added!", null ) );
	}
}
