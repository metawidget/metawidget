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

import java.util.Date;

import junit.framework.TestCase;

import org.metawidget.util.LogUtils.Log;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

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

	public static String getLastErrorMessage() {

		return LogUtils.LAST_ERROR_MESSAGE;
	}

	//
	// Public methods
	//

	public void testLogger()
		throws Exception {

		Log log = LogUtils.getLog( LogUtilsTest.class );

		if ( log.isTraceEnabled() ) {

			log.trace( "trace {0}", 1 );

			assertEquals( "trace 1", getLastTraceMessage() );
			log.trace( "trace {0}", "1t", new Throwable() );
			assertEquals( "trace 1t", getLastTraceMessage() );
			log.trace( "trace", new Throwable() );
			assertEquals( "trace", getLastTraceMessage() );

			assertTrue( log.isDebugEnabled() );
			log.debug( "debug {0}", 2 );
			assertEquals( "debug 2", getLastDebugMessage() );
			log.debug( "debug {0}", "2t", new Throwable() );
			assertEquals( "debug 2t", getLastDebugMessage() );
			log.debug( "debug", new Throwable() );
			assertEquals( "debug", getLastDebugMessage() );
		}

		assertTrue( log.isInfoEnabled() );
		log.info( "info {0}", 3 );
		assertEquals( "info 3", getLastInfoMessage() );
		log.info( "info {0}", "3t", new Throwable() );
		assertEquals( "info 3t", getLastInfoMessage() );
		log.info( "info", new Throwable() );
		assertEquals( "info", getLastInfoMessage() );

		assertTrue( log.isWarnEnabled() );
		log.warn( "warn {0}", 4 );
		assertEquals( "warn 4", getLastWarnMessage() );
		log.warn( "warn {0}", "4t", new Throwable() );
		assertEquals( "warn 4t", getLastWarnMessage() );
		log.warn( "warn", new Throwable() );
		assertEquals( "warn", getLastWarnMessage() );

		assertTrue( log.isErrorEnabled() );
		log.error( "error {0}", 5 );
		assertEquals( "error 5", getLastErrorMessage() );
		log.error( "error {0}", "5t", new Throwable() );
		assertEquals( "error 5t", getLastErrorMessage() );
		log.error( "error", new Throwable() );
		assertEquals( "error", getLastErrorMessage() );

		// Test bad messages

		if ( log.isTraceEnabled() ) {
			try
			{
				log.trace( "trace {0}", 1, 2 );
				fail();
			}
			catch( RuntimeException e )
			{
				assertTrue( "Given 2 arguments to log, but no {1} in message 'trace {0}'".equals( e.getMessage() ));
			}

			try
			{
				log.debug( "debug {0}", "foo", "bar" );
				fail();
			}
			catch( RuntimeException e )
			{
				assertTrue( "Given 2 arguments to log, but no {1} in message 'debug {0}'".equals( e.getMessage() ));
			}
		}

		try
		{
			log.info( "info {0}", null, null );
			fail();
		}
		catch( RuntimeException e )
		{
			assertTrue( "Given 2 arguments to log, but no {1} in message 'info {0}'".equals( e.getMessage() ));
		}

		try
		{
			log.warn( "warn {0}", true, false );
			fail();
		}
		catch( RuntimeException e )
		{
			assertTrue( "Given 2 arguments to log, but no {1} in message 'warn {0}'".equals( e.getMessage() ));
		}

		try
		{
			log.error( "error {0}", new Date(), new Date() );
			fail();
		}
		catch( RuntimeException e )
		{
			assertTrue( "Given 2 arguments to log, but no {1} in message 'error {0}'".equals( e.getMessage() ));
		}
	}
}
