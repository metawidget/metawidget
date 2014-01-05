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

package org.metawidget.integrationtest.spring.quirks.model;

import java.util.List;

import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class LookupBean {

	//
	// Private members
	//

	private List<LookupItem>	mItems;

	//
	// Constructor
	//

	public LookupBean() {

		mItems = CollectionUtils.newArrayList();
		mItems.add( new LookupItem( "value1", "label1" ) );
		mItems.add( new LookupItem( "value2", "label2" ) );
		mItems.add( new LookupItem( "value3", "label3" ) );
	}

	//
	// Public methods
	//

	public List<LookupItem> getItems() {

		return mItems;
	}

	//
	// Inner class
	//

	public static class LookupItem {

		//
		// Private members
		//

		private String	mValue;

		private String	mLabel;

		//
		// Constructor
		//

		public LookupItem( String value, String label ) {

			mValue = value;
			mLabel = label;
		}

		//
		// Public methods
		//

		public String getValue() {

			return mValue;
		}

		public String getLabel() {

			return mLabel;
		}
	}
}
