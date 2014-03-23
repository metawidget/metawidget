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

package org.metawidget.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Utilities for working with Java I/O.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public final class IOUtils {

	//
	// Public statics
	//

	/**
	 * Streams all data between the given two streams, then closes both streams.
	 * <p>
	 * Note: this implementation uses NIO.
	 */

	public static void streamBetween( InputStream in, OutputStream out ) {

		streamBetween( in, out, true, true );
	}

	/**
	 * Streams all data between the given two streams. Optionally closes both streams (it is
	 * sometimes important <em>not</em> to close the streams, such as when reading and writing
	 * multiple entries in a ZIP).
	 * <p>
	 * Note: this implementation uses NIO.
	 */

	public static void streamBetween( InputStream in, OutputStream out, boolean closeIn, boolean closeOut ) {

		try {
			try {
				try {
					// (must create a local buffer for Thread-safety)

					ByteBuffer buffer = ByteBuffer.allocate( BUFFER_SIZE );
					WritableByteChannel channelOut = Channels.newChannel( out );
					ReadableByteChannel channelIn = Channels.newChannel( in );

					while ( channelIn.read( buffer ) != -1 ) {
						buffer.flip();
						channelOut.write( buffer );
						buffer.clear();
					}
				} catch ( Exception e ) {
					throw new RuntimeException( e );
				} finally {

					if ( closeOut ) {
						// Separate blocks for out.close() and in.close(), as noted
						// by Bloch:
						// http://mail.openjdk.java.net/pipermail/coin-dev/2009-February/000011.html

						out.close();
					}
				}
			} finally {

				if ( closeIn ) {
					in.close();
				}
			}
		} catch ( Exception e ) {
			// Convert to unchecked Exception

			throw new RuntimeException( e );
		}
	}

	//
	// Private statics
	//

	/**
	 * Note: while the buffer <em>size</em> can be a shared non-Thread-safe static, the buffer
	 * itself must not be!
	 */

	private static final int	BUFFER_SIZE	= 1024 * 64;

	//
	// Private constructor
	//

	private IOUtils() {

		// Can never be called
	}
}
