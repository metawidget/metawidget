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

package org.metawidget.android.widget;

import android.view.View;

/**
 * Additional interface implemented by Android WidgetBuilders.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public interface AndroidValueAccessor {

	//
	// Methods
	//

	Object getValue( View view );

	/**
	 * Sets the given View to the specified value.
	 *
	 * @return true if the View was recognised and its value set
	 */

	boolean setValue( Object value, View view );
}
