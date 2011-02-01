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

package org.metawidget.faces.quirks.model.richfaces;

import static org.metawidget.inspector.InspectionResultConstants.*;

import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLookup;
import org.metawidget.inspector.annotation.UiReadOnly;
import org.metawidget.inspector.faces.UiFacesAjax;
import org.metawidget.inspector.faces.UiFacesAttribute;

/**
 * Models an entity that tests some RichFaces-AJAX-specific quirks.
 *
 * @author Richard Kennard
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

	@UiFacesAttribute( name = HIDDEN, expression = "#{this.select != 'Show'}" )
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
