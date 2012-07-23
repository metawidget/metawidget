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

package org.metawidget.integrationtest.faces.quirks.model;

import static org.metawidget.inspector.InspectionResultConstants.*;

import javax.faces.event.AjaxBehaviorEvent;

import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLookup;
import org.metawidget.inspector.annotation.UiReadOnly;
import org.metawidget.inspector.faces.UiFacesAjax;

/**
 * Models an entity that tests some JSF2-AJAX-specific quirks.
 *
 * @author Richard Kennard
 */

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
	@UiFacesAjax( event = "change" )
	public String getSelect() {

		return mSelect;
	}

	public void setSelect( String select ) {

		mSelect = select;
	}

	@UiAttribute( name = HIDDEN, value = "#{ajaxQuirks.select != 'Show'}" )
	@UiFacesAjax( event = "click", action = "#{ajaxQuirks.updateLabel}" )
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

	/**
	 * @param event	not used, but required by the spec
	 */

	public void updateLabel( AjaxBehaviorEvent event ) {

		mLabel = "clicked";
	}
}
