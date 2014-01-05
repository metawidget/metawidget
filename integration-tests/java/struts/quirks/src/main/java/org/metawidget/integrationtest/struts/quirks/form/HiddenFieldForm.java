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

package org.metawidget.integrationtest.struts.quirks.form;

import org.apache.struts.action.ActionForm;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;

/**
 * Models an entity that tests some Spring-specific quirks.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HiddenFieldForm
	extends ActionForm {

	//
	// Private members
	//

	private String	mHiddenNoValue;

	private String	mHiddenWithValue	= "Bar";

	//
	// Public methods
	//

	public String getReadOnlyNoValue() {

		return null;
	}

	@UiComesAfter( "readOnlyNoValue" )
	public String getReadOnlyWithValue() {

		return "Foo";
	}

	@UiComesAfter( "readOnlyWithValue" )
	@UiHidden
	public String getHiddenNoValue() {

		return mHiddenNoValue;
	}

	public void setHiddenNoValue( String hiddenNoValue ) {

		mHiddenNoValue = hiddenNoValue;
	}

	@UiComesAfter( "hiddenNoValue" )
	@UiHidden
	public String getHiddenWithValue() {

		return mHiddenWithValue;
	}

	public void setHiddenWithValue( String hiddenWithValue ) {

		mHiddenWithValue = hiddenWithValue;
	}
}
