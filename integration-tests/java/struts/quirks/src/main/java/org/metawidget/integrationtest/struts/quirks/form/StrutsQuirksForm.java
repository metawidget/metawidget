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

package org.metawidget.integrationtest.struts.quirks.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.struts.UiStrutsLookup;
import org.metawidget.util.CollectionUtils;

/**
 * Models an entity that tests some Struts-specific quirks.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StrutsQuirksForm
	extends ActionForm {

	//
	// Private members
	//

	private String					mLookup;

	private List<DisplayTagQuirks>	mList				= CollectionUtils.newArrayList();

	//
	// Constructor
	//

	public StrutsQuirksForm() {

		mList.add( new DisplayTagQuirks( 0, "Foo", "A Foo" ) );
		mList.add( new DisplayTagQuirks( 1, "Bar", "A Bar" ) );
		mList.add( new DisplayTagQuirks( 2, "Baz", "A Baz" ) );
	}

	//
	// Public methods
	//

	@UiStrutsLookup( name = "lookupValues", property = "values", labelName = "lookupLabels", labelProperty = "labels" )
	public String getLookup() {

		return mLookup;
	}

	public void setLookup( String lookup ) {

		mLookup = lookup;
	}

	@UiComesAfter( "lookup" )
	public List<DisplayTagQuirks> getList() {

		return mList;
	}

	public void setList( List<DisplayTagQuirks> list ) {

		mList = list;
	}
}
