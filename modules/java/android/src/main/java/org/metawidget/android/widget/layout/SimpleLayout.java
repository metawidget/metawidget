// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.android.widget.layout;

import java.util.Map;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.layout.iface.Layout;

import android.view.View;
import android.view.ViewGroup;

/**
 * Layout to simply output components one after another, with no labels and no structure.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SimpleLayout
	implements Layout<View, ViewGroup, AndroidMetawidget> {

	public void layoutWidget( View view, String elementName, Map<String, String> attributes, ViewGroup container, AndroidMetawidget metawidget ) {

		container.addView( view );
	}
}
