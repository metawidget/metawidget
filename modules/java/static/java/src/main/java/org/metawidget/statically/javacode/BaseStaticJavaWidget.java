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
