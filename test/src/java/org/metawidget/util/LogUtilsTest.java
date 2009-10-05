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
		log.trace( "trace" );
		log.trace( "trace", new Throwable() );
		assertTrue( log.isDebugEnabled() );
		log.debug( "debug" );
		log.debug( "debug", new Throwable() );
		assertTrue( log.isInfoEnabled() );
		log.info( "info" );
		log.info( "info", new Throwable() );
		assertTrue( log.isWarnEnabled() );
		log.warn( "warn" );
		log.warn( "warn", new Throwable() );
		assertTrue( log.isErrorEnabled() );
		log.error( "error" );
		log.error( "error", new Throwable() );
	}
}
