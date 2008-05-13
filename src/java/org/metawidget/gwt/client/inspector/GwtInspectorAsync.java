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

package org.metawidget.gwt.client.inspector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.xml.client.Document;

/**
 * Common interface implemented by all GWT Inspectors that are asynchronous.
 * <p>
 * GWT uses asynchronous calls for implementing AJAX. Whereas GWT's async interfaces do not
 * generally extend their non-async counterparts, <code>GwtInspectorAsync</code> <em>does</em>
 * extend <code>GwtInspector</code>, even though typically implementors will implement the
 * non-async method as an <code>UnsupportedOperationException</code>. This is so that,
 * ultimately, ever inspector is a <code>GwtInspector</code>.
 *
 * @author Richard Kennard
 */

public interface GwtInspectorAsync
	extends GwtInspector
{
	//
	//
	// Methods
	//
	//

	void inspect( Object toInspect, String type, String[] names, AsyncCallback<Document> callback );
}
