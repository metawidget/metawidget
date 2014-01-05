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
