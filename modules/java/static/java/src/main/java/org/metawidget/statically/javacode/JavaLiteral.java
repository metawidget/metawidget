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

package org.metawidget.statically.javacode;

import java.io.IOException;
import java.io.Writer;

/**
 * Represents a Java literal.
 * <p>
 * Literals will appear correctly indented, but are otherwise unprocessed (eg. no semicolon, no
 * braces). Useful for generating comment tags or annotations.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JavaLiteral
	extends BaseStaticJavaWidget {

	//
	// Constructor
	//

	public JavaLiteral( String textContent ) {

		super( textContent );
	}

	//
	// Public methods
	//

	@Override
	public void write( Writer writer )
		throws IOException {

		writer.append( getTextContent() );
	}
}
