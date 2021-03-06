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

package org.metawidget.inspector.impl;

import java.lang.annotation.Annotation;

/**
 * Convenience implementation for Traits.
 * <p>
 * Handles construction, and returning names.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class BaseTrait
	implements Trait {

	//
	// Private methods
	//

	private String	mName;

	//
	// Constructor
	//

	public BaseTrait( String name ) {

		mName = name;
	}

	//
	// Public methods
	//

	public String getName() {

		return mName;
	}

	public boolean isAnnotationPresent( Class<? extends Annotation> annotation ) {

		return getAnnotation( annotation ) != null;
	}

	@Override
	public String toString() {

		return mName;
	}
}
