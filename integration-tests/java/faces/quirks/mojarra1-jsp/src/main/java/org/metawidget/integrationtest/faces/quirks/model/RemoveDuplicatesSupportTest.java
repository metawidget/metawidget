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

import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiSection;

/**
 * Models an entity that tests RemoveDuplicatesSupport.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class RemoveDuplicatesSupportTest {

	//
	// Private members
	//

	private String							mFoo1;

	private RemoveDuplicatesSupportEmbedded	mEmbedded;

	//
	// Public methods
	//

	public String getFoo1() {

		return mFoo1;
	}

	public void setFoo1( String foo1 ) {

		mFoo1 = foo1;
	}

	@UiComesAfter( "foo1" )
	public RemoveDuplicatesSupportEmbedded getEmbedded() {

		return mEmbedded;
	}

	public void setEmbedded( RemoveDuplicatesSupportEmbedded embedded ) {

		mEmbedded = embedded;
	}

	public void clearEmbedded() {

		setEmbedded( null );
	}

	public void newEmbedded() {

		setEmbedded( new RemoveDuplicatesSupportEmbedded() );
	}

	//
	// Public methods
	//

	public static class RemoveDuplicatesSupportEmbedded {

		//
		// Private members
		//

		private String	mBar1;

		private String	mBar2;

		private String	mBar3;

		//
		// Public methods
		//

		public String getBar1() {

			return mBar1;
		}

		public void setBar1( String bar1 ) {

			mBar1 = bar1;
		}

		@UiSection( "More" )
		public String getBar2() {

			return mBar2;
		}

		public void setBar2( String bar2 ) {

			mBar2 = bar2;
		}

		public String getBar3() {

			return mBar3;
		}

		public void setBar3( String bar3 ) {

			mBar3 = bar3;
		}
	}
}
