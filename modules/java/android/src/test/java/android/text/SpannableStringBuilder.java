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

package android.text;

/**
 * Dummy implementation for unit testing.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SpannableStringBuilder
	implements CharSequence {

	//
	// Private members
	//

	private CharSequence	mString;

	//
	// Constructor
	//

	public SpannableStringBuilder( CharSequence string ) {

		mString = string;
	}

	//
	// Supported public methods
	//

	@Override
	public String toString() {

		return mString.toString();
	}

	//
	// Unsupported public methods
	//

	public char charAt( int index ) {

		throw new UnsupportedOperationException();
	}

	public int length() {

		throw new UnsupportedOperationException();
	}

	public CharSequence subSequence( int start, int end ) {

		throw new UnsupportedOperationException();
	}
}
