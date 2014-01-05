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
