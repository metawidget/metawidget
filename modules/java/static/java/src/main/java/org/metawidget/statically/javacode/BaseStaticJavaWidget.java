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

package org.metawidget.statically.javacode;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import org.metawidget.statically.BaseStaticWidget;
import org.metawidget.statically.StaticUtils.IndentedWriter;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class BaseStaticJavaWidget
	extends BaseStaticWidget
	implements StaticJavaWidget {

	//
	// Private members
	//

	private String		mTextContent;

	private Set<String>	mImports;

	//
	// Constructor
	//

	protected BaseStaticJavaWidget( String textContent ) {

		mTextContent = textContent;
	}

	//
	// Public methods
	//

	public Set<String> getImports() {

		return mImports;
	}

	public void putImport( String packageName ) {

		if ( mImports == null ) {
			mImports = CollectionUtils.newHashSet();
		}

		mImports.add( packageName );
	}

	@Override
	public void write( Writer writer )
		throws IOException {

		writer.append( mTextContent );

		// Without children

		if ( getChildren().isEmpty() ) {
			writer.append( ';' );
			if ( writer instanceof IndentedWriter ) {
				writer.append( "\r\n" );
			}

			return;
		}

		// With children

		writer.append( " {" );

		if ( writer instanceof IndentedWriter ) {
			writer.append( "\r\n" );
			( (IndentedWriter) writer ).indent();
		} else {
			writer.append( ' ' );
		}

		super.write( writer );

		if ( writer instanceof IndentedWriter ) {
			( (IndentedWriter) writer ).outdent();
		} else {
			writer.append( ' ' );
		}

		writer.append( '}' );

		if ( writer instanceof IndentedWriter ) {
			writer.append( "\r\n" );
		}
	}

	//
	// Protected methods
	//

	protected String getTextContent() {

		return mTextContent;
	}
}
