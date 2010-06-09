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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilities for working with Logging.
 * <p>
 * Note: we're not trying to create <em>another</em> logging framework here! We're just trying to
 * use Commons Logging where available, and java.util Logging where it's not. Most web containers
 * will prefer Commons Logging, but we don't want to have to ship commons-logging.jar with, say, a
 * Swing applet.
 *
 * @author Richard Kennard
 */

public final class LogUtils
{
	//
	// Public statics
	//

	/**
	 * Where possible, returns an implementation of Commons Logging. For those applications that do
	 * not use Commons Logging, returns an implementation of java.util Logging.
	 * <p>
	 * In general, Commons Logging is the better choice. However, that introduces a mandatory JAR
	 * dependency which we want to avoid.
	 * <p>
	 * Note: we're not trying to create <em>another</em> logging framework here! We're just trying
	 * to use Commons Logging where available, and java.util Logging where it's not. Most web
	 * containers will prefer Commons Logging, but we don't want to have to ship commons-logging.jar
	 * with, say, a Swing applet.
	 */

	public static Log getLog( Class<?> clazz )
	{
		try
		{
			return new CommonsLog( clazz );
		}
		catch ( NoClassDefFoundError e )
		{
			return new UtilLog( clazz.getName() );
		}
	}

	/**
	 * Common logging interface.
	 * <p>
	 * Note: we're not trying to create <em>another</em> logging framework here! We're just trying
	 * to use Commons Logging where available, and java.util Logging where it's not. Most web
	 * containers will prefer Commons Logging, but we don't want to have to ship commons-logging.jar
	 * with, say, a Swing applet.
	 */

	public interface Log
	{
		//
		// Methods
		//

		boolean isTraceEnabled();

		void trace( String trace );

		void trace( String trace, Throwable throwable );

		boolean isDebugEnabled();

		void debug( String debug );

		void debug( String debug, Throwable throwable );

		boolean isInfoEnabled();

		void info( String info );

		void info( String info, Throwable throwable );

		boolean isWarnEnabled();

		void warn( String warning );

		void warn( String warning, Throwable throwable );

		boolean isErrorEnabled();

		void error( String error );

		void error( String error, Throwable throwable );
	}

	//
	// Private statics
	//

	/**
	 * Lightweight field that stores the last message sent to <code>trace</code>. Intended for
	 * unit tests.
	 */

	/* package private */static String	LAST_TRACE_MESSAGE;

	/**
	 * Lightweight field that stores the last message sent to <code>Log.debug</code>. Intended for
	 * unit tests.
	 */

	/* package private */static String	LAST_DEBUG_MESSAGE;

	/**
	 * Lightweight field that stores the last message sent to <code>Log.info</code>. Intended for
	 * unit tests.
	 */

	/* package private */static String	LAST_INFO_MESSAGE;

	/**
	 * Lightweight field that stores the last message sent to <code>Log.warn</code>. Intended for
	 * unit tests.
	 */

	/* package private */static String	LAST_WARN_MESSAGE;

	/**
	 * Logging implementation that uses <code>java.util.Logger</code>.
	 */

	private static class UtilLog
		implements Log
	{
		//
		// Private members
		//

		private Logger	mLogger;

		//
		// Constructor
		//

		public UtilLog( String logger )
		{
			mLogger = Logger.getLogger( logger );
		}

		//
		// Methods
		//

		public boolean isTraceEnabled()
		{
			return mLogger.isLoggable( Level.FINER );
		}

		public void trace( String trace )
		{
			LAST_TRACE_MESSAGE = trace;

			mLogger.finer( trace );
		}

		public void trace( String trace, Throwable throwable )
		{
			mLogger.log( Level.FINER, trace, throwable );
		}

		public boolean isDebugEnabled()
		{
			return mLogger.isLoggable( Level.FINE );
		}

		public void debug( String debug )
		{
			LAST_DEBUG_MESSAGE = debug;

			mLogger.fine( debug );
		}

		public void debug( String debug, Throwable throwable )
		{
			mLogger.log( Level.FINE, debug, throwable );
		}

		public boolean isInfoEnabled()
		{
			return mLogger.isLoggable( Level.INFO );
		}

		public void info( String info )
		{
			LAST_INFO_MESSAGE = info;

			mLogger.info( info );
		}

		public void info( String info, Throwable throwable )
		{
			mLogger.log( Level.INFO, info, throwable );
		}

		public boolean isWarnEnabled()
		{
			return mLogger.isLoggable( Level.WARNING );
		}

		public void warn( String warning )
		{
			LAST_WARN_MESSAGE = warning;

			mLogger.warning( warning );
		}

		public void warn( String warning, Throwable throwable )
		{
			LAST_WARN_MESSAGE = warning;

			mLogger.log( Level.WARNING, warning, throwable );
		}

		public boolean isErrorEnabled()
		{
			return mLogger.isLoggable( Level.SEVERE );
		}

		public void error( String error )
		{
			mLogger.log( Level.SEVERE, error );
		}

		public void error( String error, Throwable throwable )
		{
			mLogger.log( Level.SEVERE, error, throwable );
		}
	}

	/**
	 * Logging implementation that uses <code>org.apache.commons.logging.Log</code>.
	 */

	private static class CommonsLog
		implements Log
	{
		//
		// Private members
		//

		private org.apache.commons.logging.Log	mLog;

		//
		// Constructor
		//

		public CommonsLog( Class<?> clazz )
		{
			mLog = LogFactory.getLog( clazz );
		}

		//
		// Methods
		//

		public boolean isTraceEnabled()
		{
			return mLog.isTraceEnabled();
		}

		public void trace( String trace )
		{
			LAST_TRACE_MESSAGE = trace;

			mLog.trace( trace );
		}

		public void trace( String trace, Throwable throwable )
		{
			mLog.trace( trace, throwable );
		}

		public boolean isDebugEnabled()
		{
			return mLog.isDebugEnabled();
		}

		public void debug( String debug )
		{
			LAST_DEBUG_MESSAGE = debug;

			mLog.debug( debug );
		}

		public void debug( String debug, Throwable throwable )
		{
			LAST_DEBUG_MESSAGE = debug;

			mLog.debug( debug, throwable );
		}

		public boolean isInfoEnabled()
		{
			return mLog.isInfoEnabled();
		}

		public void info( String info )
		{
			LAST_INFO_MESSAGE = info;

			mLog.info( info );
		}

		public void info( String info, Throwable throwable )
		{
			LAST_INFO_MESSAGE = info;

			mLog.info( info, throwable );
		}

		public boolean isWarnEnabled()
		{
			return mLog.isWarnEnabled();
		}

		public void warn( String warning )
		{
			LAST_WARN_MESSAGE = warning;

			mLog.warn( warning );
		}

		public void warn( String warning, Throwable throwable )
		{
			LAST_WARN_MESSAGE = warning;

			mLog.warn( warning, throwable );
		}

		public boolean isErrorEnabled()
		{
			return mLog.isErrorEnabled();
		}

		public void error( String error )
		{
			mLog.error( error );
		}

		public void error( String error, Throwable throwable )
		{
			mLog.error( error, throwable );
		}
	}

	//
	// Private constructor
	//

	private LogUtils()
	{
		// Can never be called
	}
}
