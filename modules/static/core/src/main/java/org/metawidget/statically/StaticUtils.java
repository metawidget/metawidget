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

package org.metawidget.statically;

import java.io.IOException;
import java.io.Writer;

/**
 * Utilities for working with statically generated output.
 *
 * @author Richard Kennard
 */

public final class StaticUtils {

	//
	// Public statics
	//

	/**
	 * Writer that automatically inserts indents into the written characters, based on the current
	 * indentation level.
	 */

	public static class IndentedWriter
		extends Writer {

		//
		// Private statics
		//

		private static final char[]	INDENT_CHARS	= new char[] { '\t' };

		//
		// Private members
		//

		private Writer				mWriter;

		private int					mIndent;

		private boolean				mWriteIndent = true;

		//
		// Constructor
		//

		/**
		 * Constructs a new IndentedWriter.
		 *
		 * @param writer
		 *            the writer to delegate all writes to
		 * @param initialIndent
		 *            the initialIndent that will be applied to every line
		 * @param indentFirstLine
		 *            whether to indent the first line by initialIndent
		 */

		public IndentedWriter( Writer writer, int initialIndent ) {

			mWriter = writer;
			mIndent = initialIndent;
		}

		//
		// Public methods
		//

		/**
		 * Increase the indentation level for subsequent writes.
		 */

		public void indent() {

			mIndent++;
		}

		/**
		 * Decrease the indentation level for subsequent writes.
		 */

		public void outdent() {

			if ( mIndent == 0 ) {
				return;
			}

			mIndent--;
		}

		/**
		 * Write the given characters, inserting indents as necessary.
		 */

		@Override
		public void write( char[] characters, int offset, int length )
			throws IOException {

			// For each character...

			for ( int loop = offset, end = offset + length; loop < end; loop++ ) {

				// ...write indent if necessary...

				if ( mWriteIndent ) {
					mWriteIndent = false;
					for ( int indent = 0; indent < mIndent; indent++ ) {
						mWriter.write( INDENT_CHARS, 0, 1 );
					}
				}

				// ...then write the character...

				mWriter.write( characters, loop, 1 );

				// ...and watch for next indent

				if ( characters[loop] == '\n' ) {
					mWriteIndent = true;
				}
			}
		}

		@Override
		public void flush()
			throws IOException {

			mWriter.flush();
		}

		@Override
		public void close()
			throws IOException {

			mWriter.close();
		}
	}

	//
	// Private constructor
	//

	private StaticUtils() {

		// Can never be called
	}
}
