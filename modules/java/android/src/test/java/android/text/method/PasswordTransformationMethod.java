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

package android.text.method;

import android.graphics.Rect;
import android.view.View;

/**
 * Dummy implementation for unit testing.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class PasswordTransformationMethod
	implements TransformationMethod {

	//
	// Public statics
	//

	public static PasswordTransformationMethod getInstance() {

		return new PasswordTransformationMethod();
	}

	//
	// Public methods
	//

	public CharSequence getTransformation( CharSequence arg0, View arg1 ) {

		return null;
	}

	public void onFocusChanged( View arg0, CharSequence arg1, boolean arg2, int arg3, Rect arg4 ) {

		// Do nothing
	}
}
