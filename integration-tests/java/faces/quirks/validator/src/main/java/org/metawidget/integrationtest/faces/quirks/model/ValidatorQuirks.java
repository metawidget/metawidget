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

package org.metawidget.integrationtest.faces.quirks.model;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;

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
