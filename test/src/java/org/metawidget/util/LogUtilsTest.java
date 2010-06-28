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

import junit.framework.TestCase;

import org.metawidget.util.LogUtils.Log;

/**
 * @author Richard Kennard
 */

public class LogUtilsTest
	extends TestCase
{
	//
	// Public statics
	//

	public static String getLastTraceMessage()
	{
		return LogUtils.LAST_TRACE_MESSAGE;
	}

	public static String getLastDebugMessage()
	{
		return LogUtils.LAST_DEBUG_MESSAGE;
	}

	public static String getLastInfoMessage()
	{
		return LogUtils.LAST_INFO_MESSAGE;
	}

	public static void clearLastWarnMessage()
	{
		LogUtils.LAST_WARN_MESSAGE = null;
	}

	public static String getLastWarnMessage()
	{
		return LogUtils.LAST_WARN_MESSAGE;
	}

	//
	// Public methods
	//

	public void testLogger()
		throws Exception
	{
		// At the very least, test that calling the logging methods doesn't throw an Exception

		Log log = LogUtils.getLog( LogUtilsTest.class );

		// Note: this test will fail if trace is not enabled (eg. when running inside Eclipse)

		assertTrue( log.isTraceEnabled() );
		log.trace( "trace {0}", 1 );
		log.trace( "trace", new Throwable() );
		assertEquals( "trace 1", getLastTraceMessage() );
		assertTrue( log.isDebugEnabled() );
		log.debug( "debug {0}", 2 );
		log.debug( "debug", new Throwable() );
		assertEquals( "debug 2", getLastDebugMessage() );
		assertTrue( log.isInfoEnabled() );
		log.info( "info {0}", 3 );
		log.info( "info", new Throwable() );
		assertEquals( "info 3", getLastInfoMessage() );
		assertTrue( log.isWarnEnabled() );
		log.warn( "warn {0}", 4 );
		log.warn( "warn", new Throwable() );
		assertEquals( "warn 4", getLastWarnMessage() );
		assertTrue( log.isErrorEnabled() );
		log.error( "error {0}", 5 );
		log.error( "error", new Throwable() );
	}
}
