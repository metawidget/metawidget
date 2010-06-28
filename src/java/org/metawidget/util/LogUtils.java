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

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.logging.LogFactory;

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

		void trace( String trace, Object... arguments );

		void trace( String trace, Throwable throwable );

		boolean isDebugEnabled();

		void debug( String debug, Object... arguments );

		void debug( String debug, Throwable throwable );

		boolean isInfoEnabled();

		void info( String info, Object... arguments );

		void info( String info, Throwable throwable );

		boolean isWarnEnabled();

		void warn( String warning, Object... arguments );

		void warn( String warning, Throwable throwable );

		boolean isErrorEnabled();

		void error( String error, Object... arguments );

		void error( String error, Throwable throwable );
	}

	//
	// Private statics
	//

	/**
	 * Lightweight field that stores the last message sent to <code>trace</code>. Intended for unit
	 * tests.
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
		// Public methods
		//

		public boolean isTraceEnabled()
		{
			return mLogger.isLoggable( Level.FINER );
		}

		public void trace( String trace, Object... arguments )
		{
			LAST_TRACE_MESSAGE = log( Level.FINER, trace, arguments );
		}

		public void trace( String trace, Throwable throwable )
		{
			mLogger.log( Level.FINER, trace, throwable );
		}

		public boolean isDebugEnabled()
		{
			return mLogger.isLoggable( Level.FINE );
		}

		public void debug( String debug, Object... arguments )
		{
			LAST_DEBUG_MESSAGE = log( Level.FINE, debug, arguments );
		}

		public void debug( String debug, Throwable throwable )
		{
			mLogger.log( Level.FINE, debug, throwable );
		}

		public boolean isInfoEnabled()
		{
			return mLogger.isLoggable( Level.INFO );
		}

		public void info( String info, Object... arguments )
		{
			LAST_INFO_MESSAGE = log( Level.INFO, info, arguments );
		}

		public void info( String info, Throwable throwable )
		{
			mLogger.log( Level.INFO, info, throwable );
		}

		public boolean isWarnEnabled()
		{
			return mLogger.isLoggable( Level.WARNING );
		}

		public void warn( String warning, Object... arguments )
		{
			LAST_WARN_MESSAGE = log( Level.WARNING, warning, arguments );
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

		public void error( String error, Object... arguments )
		{
			log( Level.SEVERE, error, arguments );
		}

		public void error( String error, Throwable throwable )
		{
			mLogger.log( Level.SEVERE, error, throwable );
		}

		//
		// Private methods
		//

		private String log( Level level, String message, Object... arguments )
		{
			if ( arguments.length == 0 )
			{
				mLogger.log( level, message );
				return message;
			}

			String logged = MessageFormat.format( message, arguments );
			mLogger.log( level, logged );
			return logged;
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

		public void trace( String trace, Object... arguments )
		{
			if ( arguments.length == 0 )
			{
				LAST_TRACE_MESSAGE = trace;
				mLog.trace( trace );
			}
			else
			{
				String logged = MessageFormat.format( trace, arguments );
				LAST_TRACE_MESSAGE = logged;
				mLog.trace( logged );
			}
		}

		public void trace( String trace, Throwable throwable )
		{
			mLog.trace( trace, throwable );
		}

		public boolean isDebugEnabled()
		{
			return mLog.isDebugEnabled();
		}

		public void debug( String debug, Object... arguments )
		{
			if ( arguments.length == 0 )
			{
				LAST_DEBUG_MESSAGE = debug;
				mLog.debug( debug );
			}
			else
			{
				String logged = MessageFormat.format( debug, arguments );
				LAST_DEBUG_MESSAGE = logged;
				mLog.debug( logged );
			}
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

		public void info( String info, Object... arguments )
		{
			if ( arguments.length == 0 )
			{
				LAST_INFO_MESSAGE = info;
				mLog.info( info );
			}
			else
			{
				String logged = MessageFormat.format( info, arguments );
				LAST_INFO_MESSAGE = logged;
				mLog.info( logged );
			}
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

		public void warn( String warning, Object... arguments )
		{
			if ( arguments.length == 0 )
			{
				LAST_WARN_MESSAGE = warning;
				mLog.warn( warning );
			}
			else
			{
				String logged = MessageFormat.format( warning, arguments );
				LAST_WARN_MESSAGE = logged;
				mLog.warn( logged );
			}
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

		public void error( String error, Object... arguments )
		{
			if ( arguments.length == 0 )
			{
				mLog.error( error );
			}
			else
			{
				mLog.error( MessageFormat.format( error, arguments ) );
			}
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
