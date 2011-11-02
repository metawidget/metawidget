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

package org.metawidget.statically.faces.component;

import java.io.Writer;

import org.metawidget.statically.BaseStaticXmlWidget;

/**
 * @author Richard Kennard
 */

public class StaticStub
	extends BaseStaticXmlWidget {

	//
	// Constructor
	//

	public StaticStub() {

		// Null namespace: stubs should never be output (kind of the point of being static)

		super( "m", "stub", null );
	}

	//
	// Public methods
	//

	@Override
	public void write( Writer writer ) {

		// Write nothing
	}
}
