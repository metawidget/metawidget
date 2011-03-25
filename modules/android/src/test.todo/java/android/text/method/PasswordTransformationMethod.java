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

package android.text.method;

import android.graphics.Rect;
import android.view.View;

/**
 * Dummy implementation for unit testing.
 *
 * @author Richard Kennard
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

	@Override
	public CharSequence getTransformation( CharSequence arg0, View arg1 ) {

		return null;
	}

	@Override
	public void onFocusChanged( View arg0, CharSequence arg1, boolean arg2, int arg3, Rect arg4 ) {

		// Do nothing
	}
}
