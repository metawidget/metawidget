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

import static org.metawidget.inspector.InspectionResultConstants.*;

import javax.faces.bean.ManagedBean;
import javax.faces.event.AjaxBehaviorEvent;

import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLookup;
import org.metawidget.inspector.annotation.UiReadOnly;
import org.metawidget.inspector.faces.UiFacesAjax;

/**
 * Models an entity that tests some JSF2-AJAX-specific quirks.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@ManagedBean
public class AjaxQuirks {

	//
	// Private members
	//

	private String	mSelect;

	private boolean	mCheckbox;

	private String	mLabel;

	//
	// Public methods
	//

	@UiLookup( { "Hide", "Show" } )
	@UiFacesAjax
	public String getSelect() {

		return mSelect;
	}

	public void setSelect( String select ) {

		mSelect = select;
	}

	@UiAttribute( name = HIDDEN, value = "#{ajaxQuirks.select != 'Show'}" )
	@UiFacesAjax( action = "#{ajaxQuirks.updateLabel}" )
	@UiComesAfter( "select" )
	public boolean isCheckbox() {

		return mCheckbox;
	}

	public void setCheckbox( boolean checkbox ) {

		mCheckbox = checkbox;
	}

	@UiComesAfter( "checkbox" )
	@UiReadOnly
	public String getLabel() {

		return mLabel;
	}

	/**
	 * @param event	not used, but required by the spec
	 */

	public void updateLabel( AjaxBehaviorEvent event ) {

		mLabel = "clicked";
	}
}
