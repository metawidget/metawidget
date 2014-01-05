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

package org.metawidget.integrationtest.faces.quirks.model.richfaces;

import static org.metawidget.inspector.InspectionResultConstants.*;

import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLookup;
import org.metawidget.inspector.annotation.UiReadOnly;
import org.metawidget.inspector.faces.UiFacesAjax;

/**
 * Models an entity that tests some RichFaces-AJAX-specific quirks.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class RichFacesAjaxQuirks {

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
	@UiFacesAjax( event = "onchange" )
	public String getSelect() {

		return mSelect;
	}

	public void setSelect( String select ) {

		mSelect = select;
	}

	@UiAttribute( name = HIDDEN, value = "#{_this.select != 'Show'}" )
	@UiFacesAjax( event = "onclick", action = "#{richQuirksAjax.updateLabel}" )
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

	public void setLabel( String label ) {

		mLabel = label;
	}

	public void updateLabel() {

		mLabel = "clicked";
	}
}
