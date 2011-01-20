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

public class LogUtilsTest
	extends TestCase {

	//
	// Constructor
	//

	public LogUtilsTest( String name ) {

		super( name );
	}

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

		if ( log.isTraceEnabled() ) {

			log.trace( "trace {0}", 1 );

			// Note: cannot use assertEquals because must stay JDK 1.4 compatible

			assertTrue( "trace 1".equals( getLastTraceMessage() ));
			log.trace( "trace {0}", "1t", new Throwable() );
			assertTrue( "trace 1t".equals( getLastTraceMessage() ));

			assertTrue( log.isDebugEnabled() );
			log.debug( "debug {0}", 2 );
			assertTrue( "debug 2".equals( getLastDebugMessage() ));
			log.debug( "debug {0}", "2t", new Throwable() );
			assertTrue( "debug 2t".equals( getLastDebugMessage() ));
		}

		assertTrue( log.isInfoEnabled() );
		log.info( "info {0}", 3 );
		assertTrue( "info 3".equals( getLastInfoMessage() ));
		log.info( "info {0}", "3t", new Throwable() );
		assertTrue( "info 3t".equals( getLastInfoMessage() ));

		assertTrue( log.isWarnEnabled() );
		log.warn( "warn {0}", 4 );
		assertTrue( "warn 4".equals( getLastWarnMessage() ));
		log.warn( "warn {0}", "4t", new Throwable() );
		assertTrue( "warn 4t".equals( getLastWarnMessage() ));

		assertTrue( log.isErrorEnabled() );
		log.error( "error {0}", 5 );
		log.error( "error {0}", "5t", new Throwable() );

		// Test bad messages

		if ( log.isTraceEnabled() ) {
			try
			{
				log.trace( "trace {0}", 1, 2 );
				assertTrue( false );
			}
			catch( RuntimeException e )
			{
				assertTrue( "Given 2 arguments to log, but no {1} in message 'trace {0}'".equals( e.getMessage() ));
			}

			try
			{
				log.debug( "debug {0}", "foo", "bar" );
				assertTrue( false );
			}
			catch( RuntimeException e )
			{
				assertTrue( "Given 2 arguments to log, but no {1} in message 'debug {0}'".equals( e.getMessage() ));
			}
		}

		try
		{
			log.info( "info {0}", null, null );
			assertTrue( false );
		}
		catch( RuntimeException e )
		{
			assertTrue( "Given 2 arguments to log, but no {1} in message 'info {0}'".equals( e.getMessage() ));
		}

		try
		{
			log.warn( "warn {0}", true, false );
			assertTrue( false );
		}
		catch( RuntimeException e )
		{
			assertTrue( "Given 2 arguments to log, but no {1} in message 'warn {0}'".equals( e.getMessage() ));
		}

		try
		{
			log.error( "error {0}", new Date(), new Date() );
			assertTrue( false );
		}
		catch( RuntimeException e )
		{
			assertTrue( "Given 2 arguments to log, but no {1} in message 'error {0}'".equals( e.getMessage() ));
		}
	}
}
