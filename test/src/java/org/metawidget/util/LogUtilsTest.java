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

import java.util.Date;

import junit.framework.TestCase;

import org.metawidget.util.LogUtils.Log;

/**
 * @author Richard Kennard
 */

// TODO: test java.util.Logging version

public class LogUtilsTest
	extends TestCase {

	//
	// Public statics
	//

	public static String getLastTraceMessage() {

		return LogUtils.LAST_TRACE_MESSAGE;
	}

	public static Object[] getLastTraceArguments() {

		return LogUtils.LAST_TRACE_ARGUMENTS;
	}

	public static String getLastDebugMessage() {

		return LogUtils.LAST_DEBUG_MESSAGE;
	}

	public static Object[] getLastDebugArguments() {

		return LogUtils.LAST_DEBUG_ARGUMENTS;
	}

	public static String getLastInfoMessage() {

		return LogUtils.LAST_INFO_MESSAGE;
	}

	public static void clearLastWarnMessage() {

		LogUtils.LAST_WARN_MESSAGE = null;
	}

	public static String getLastWarnMessage() {

		return LogUtils.LAST_WARN_MESSAGE;
	}

	//
	// Public methods
	//

	public void testLogger()
		throws Exception {

		Log log = LogUtils.getLog( LogUtilsTest.class );

		// Note: this test will fail if trace is not enabled (eg. when running inside Eclipse)

		assertTrue( log.isTraceEnabled() );
		log.trace( "trace {0}", 1 );
		assertEquals( "trace 1", getLastTraceMessage() );
		log.trace( "trace", new Throwable() );

		assertTrue( log.isDebugEnabled() );
		log.debug( "debug {0}", 2 );
		assertEquals( "debug 2", getLastDebugMessage() );
		log.debug( "debug", new Throwable() );

		assertTrue( log.isInfoEnabled() );
		log.info( "info {0}", 3 );
		assertEquals( "info 3", getLastInfoMessage() );
		log.info( "info", new Throwable() );

		assertTrue( log.isWarnEnabled() );
		log.warn( "warn {0}", 4 );
		assertEquals( "warn 4", getLastWarnMessage() );
		log.warn( "warn", new Throwable() );

		assertTrue( log.isErrorEnabled() );
		log.error( "error {0}", 5 );
		log.error( "error", new Throwable() );

		// Test bad messages

		try
		{
			log.trace( "trace {0}", 1, 2 );
			assertTrue( false );
		}
		catch( RuntimeException e )
		{
			assertEquals( "Given 2 arguments to log, but no {1} in message 'trace {0}'", e.getMessage() );
		}

		try
		{
			log.debug( "debug {0}", "foo", "bar" );
			assertTrue( false );
		}
		catch( RuntimeException e )
		{
			assertEquals( "Given 2 arguments to log, but no {1} in message 'debug {0}'", e.getMessage() );
		}

		try
		{
			log.info( "info {0}", null, null );
			assertTrue( false );
		}
		catch( RuntimeException e )
		{
			assertEquals( "Given 2 arguments to log, but no {1} in message 'info {0}'", e.getMessage() );
		}

		try
		{
			log.warn( "warn {0}", true, false );
			assertTrue( false );
		}
		catch( RuntimeException e )
		{
			assertEquals( "Given 2 arguments to log, but no {1} in message 'warn {0}'", e.getMessage() );
		}

		try
		{
			log.error( "error {0}", new Date(), new Date() );
			assertTrue( false );
		}
		catch( RuntimeException e )
		{
			assertEquals( "Given 2 arguments to log, but no {1} in message 'error {0}'", e.getMessage() );
		}
	}
}
