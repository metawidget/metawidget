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

package org.metawidget.statically;

import java.io.IOException;
import java.io.Writer;

/**
 * Utilities for working with statically generated output.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
		// Private members
		//

		private Writer	mWriter;

		private int		mIndent;

		private boolean	mWriteIndent	= true;

		//
		// Constructor
		//

		/**
		 * Constructs a new IndentedWriter with the given inital indent.
		 * <p>
		 * Note the initialIndent and carriage returns are applied to every line, including the
		 * first and last. If this is undesirable, clients can use <tt>String.trim()</tt>.
		 *
		 * @param writer
		 *            the writer to delegate all writes to
		 * @param initialIndent
		 *            the initialIndent that will be applied to every line
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
						mWriter.write( '\t' );
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

		@Override
		public String toString() {

			return mWriter.toString();
		}
	}

	/**
	 * Writer that automatically inserts a leading space before the first character.
	 */

	public static class LeadingSpaceWriter
		extends Writer {

		//
		// Private members
		//

		private Writer	mWriter;

		private boolean	mWriteLeadingSpace	= true;

		//
		// Constructor
		//

		/**
		 * Constructs a new LeadingSpaceWriter.
		 *
		 * @param writer
		 *            the writer to delegate all writes to
		 */

		public LeadingSpaceWriter( Writer writer ) {

			mWriter = writer;
		}

		//
		// Public methods
		//

		/**
		 * Write the given characters, inserting a leading space.
		 */

		@Override
		public void write( char[] characters, int offset, int length )
			throws IOException {

			for ( int loop = offset, end = offset + length; loop < end; loop++ ) {

				if ( mWriteLeadingSpace ) {
					mWriter.write( ' ' );
					mWriteLeadingSpace = false;
				}

				mWriter.write( characters, loop, 1 );
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
