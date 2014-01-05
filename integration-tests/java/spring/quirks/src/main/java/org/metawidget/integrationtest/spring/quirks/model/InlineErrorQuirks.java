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

/**
 * Models an entity that tests some Spring-specific quirks.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class InlineErrorQuirks {

	//
	// Private members
	//

	private String	mFoo;

	//
	// Public methods
	//

	public String getFoo() {
	
		return mFoo;
	}

	
	public void setFoo( String foo ) {
	
		mFoo = foo;
	}
	
	public void save() {
		
		throw new RuntimeException( "bar" );
	}
}
