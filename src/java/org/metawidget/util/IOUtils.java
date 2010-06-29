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
 * @author Richard Kennard
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
					// Separate blocks for out.close() and in.close(), as noted
					// by Bloch:
					// http://mail.openjdk.java.net/pipermail/coin-dev/2009-February/000011.html

					out.close();
				}
			} finally {
				in.close();
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

	private final static int	BUFFER_SIZE	= 1024 * 64;

	//
	// Private constructor
	//

	private IOUtils() {

		// Can never be called
	}
}
