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

package org.metawidget.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Facet for Android environments.
 * <p>
 * Facets differ from Stubs in that Stubs override widget creation, whereas Facets are 'decorations'
 * (such as button bars) to be recognized and arranged at the discretion of the Layout.
 * <p>
 * Note: this class extends <code>LinearLayout</code> rather than <code>FrameLayout</code>, because
 * <code>FrameLayout</code> would <em>always</em> need to have another <code>Layout</code> embedded
 * within it, whereas <code>LinearLayout</code> is occasionally useful directly.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class Facet
	extends LinearLayout {

	//
	// Private members
	//

	private String	mName;

	//
	// Constructor
	//

	public Facet( Context context ) {

		super( context );
	}

	public Facet( Context context, AttributeSet attributes ) {

		super( context, attributes );

		mName = attributes.getAttributeValue( null, "name" );
	}

	//
	// Public methods
	//

	public String getName() {

		return mName;
	}

	public void setName( String name ) {

		mName = name;
	}
}
