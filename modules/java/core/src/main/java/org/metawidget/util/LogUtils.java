// Metawidget (licensed under LGPL)
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

public final class LogUtils {

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

	public static Log getLog( Class<?> clazz ) {

		try {
			return new CommonsLog( clazz );
		} catch ( NoClassDefFoundError e ) {
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

	public interface Log {

		//
		// Methods
		//

		boolean isTraceEnabled();

		/**
		 * Log a trace message.
		 *
		 * @param debug
		 *            message to log
		 * @param arguments
		 *            array of arguments that will be merged into the message using standard
		 *            <code>java.text.MessageFormat</code> notation. As a special case, if the last
		 *            argument is a Throwable but is not referenced in the message, its stack trace
		 *            will be printed
		 */

		void trace( String trace, Object... arguments );

		boolean isDebugEnabled();

		/**
		 * Log a debug message.
		 *
		 * @param debug
		 *            message to log
		 * @param arguments
		 *            array of arguments that will be merged into the message using standard
		 *            <code>java.text.MessageFormat</code> notation. As a special case, if the last
		 *            argument is a Throwable but is not referenced in the message, its stack trace
		 *            will be printed
		 */

		void debug( String debug, Object... arguments );

		boolean isInfoEnabled();

		/**
		 * Log an info message.
		 *
		 * @param debug
		 *            message to log
		 * @param arguments
		 *            array of arguments that will be merged into the message using standard
		 *            <code>java.text.MessageFormat</code> notation. As a special case, if the last
		 *            argument is a Throwable but is not referenced in the message, its stack trace
		 *            will be printed
		 */

		void info( String info, Object... arguments );

		boolean isWarnEnabled();

		/**
		 * Log a warning message.
		 *
		 * @param debug
		 *            message to log
		 * @param arguments
		 *            array of arguments that will be merged into the message using standard
		 *            <code>java.text.MessageFormat</code> notation. As a special case, if the last
		 *            argument is a Throwable but is not referenced in the message, its stack trace
		 *            will be printed
		 */

		void warn( String warning, Object... arguments );

		boolean isErrorEnabled();

		/**
		 * Log an error message.
		 *
		 * @param debug
		 *            message to log
		 * @param arguments
		 *            array of arguments that will be merged into the message using standard
		 *            <code>java.text.MessageFormat</code> notation. As a special case, if the last
		 *            argument is a Throwable but is not referenced in the message, its stack trace
		 *            will be printed
		 */

		void error( String error, Object... arguments );
	}

	//
	// Private statics
	//

	/**
	 * Lightweight field that stores the last message sent to <code>trace</code>. Intended for unit
	 * tests.
	 */

	/* package private */static String		LAST_TRACE_MESSAGE;

	/* package private */static Object[]	LAST_TRACE_ARGUMENTS;

	/**
	 * Lightweight field that stores the last message sent to <code>Log.debug</code>. Intended for
	 * unit tests.
	 */

	/* package private */static String		LAST_DEBUG_MESSAGE;

	/* package private */static Object[]	LAST_DEBUG_ARGUMENTS;

	/**
	 * Lightweight field that stores the last message sent to <code>Log.info</code>. Intended for
	 * unit tests.
	 */

	/* package private */static String		LAST_INFO_MESSAGE;

	/**
	 * Lightweight field that stores the last message sent to <code>Log.warn</code>. Intended for
	 * unit tests.
	 */

	/* package private */static String		LAST_WARN_MESSAGE;

	/**
	 * Lightweight field that stores the last message sent to <code>Log.error</code>. Intended for
	 * unit tests.
	 */

	/* package private */static String		LAST_ERROR_MESSAGE;

	/**
	 * Logging implementation that uses <code>java.util.Logger</code>.
	 */

	private static class UtilLog
		implements Log {

		//
		// Private members
		//

		private Logger	mLogger;

		//
		// Constructor
		//

		public UtilLog( String logger ) {

			mLogger = Logger.getLogger( logger );
		}

		//
		// Public methods
		//

		public boolean isTraceEnabled() {

			return mLogger.isLoggable( Level.FINER );
		}

		public void trace( String trace, Object... arguments ) {

			LAST_TRACE_MESSAGE = log( Level.FINER, trace, arguments );
		}

		public boolean isDebugEnabled() {

			return mLogger.isLoggable( Level.FINE );
		}

		public void debug( String debug, Object... arguments ) {

			LAST_DEBUG_MESSAGE = log( Level.FINE, debug, arguments );
		}

		public boolean isInfoEnabled() {

			return mLogger.isLoggable( Level.INFO );
		}

		public void info( String info, Object... arguments ) {

			LAST_INFO_MESSAGE = log( Level.INFO, info, arguments );
		}

		public boolean isWarnEnabled() {

			return mLogger.isLoggable( Level.WARNING );
		}

		public void warn( String warning, Object... arguments ) {

			LAST_WARN_MESSAGE = log( Level.WARNING, warning, arguments );
		}

		public boolean isErrorEnabled() {

			return mLogger.isLoggable( Level.SEVERE );
		}

		public void error( String error, Object... arguments ) {

			LAST_ERROR_MESSAGE = log( Level.SEVERE, error, arguments );
		}

		//
		// Private methods
		//

		private String log( Level level, String message, Object... arguments ) {

			String logged = message;

			if ( mLogger.isLoggable( level ) ) {

				// Support fast cases with no arguments

				int lastArgument = arguments.length - 1;

				if ( lastArgument == -1 ) {
					mLogger.log( level, logged );
				} else {
					// Support cases with an unused Throwable on the end

					if ( arguments[lastArgument] instanceof Throwable && message.indexOf( "{" + lastArgument + "}" ) == -1 ) {
						if ( lastArgument > 0 ) {
							logged = MessageFormat.format( logged, arguments );
						}
						mLogger.log( level, logged, (Throwable) arguments[lastArgument] );
						lastArgument--;
					} else {
						logged = MessageFormat.format( logged, arguments );
						mLogger.log( level, logged );
					}

					if ( lastArgument != -1 && message.indexOf( "{" + lastArgument + "}" ) == -1 ) {
						throw new RuntimeException( "Given " + ( lastArgument + 1 ) + " arguments to log, but no {" + lastArgument + "} in message '" + message + "'" );
					}
				}
			}

			return logged;
		}
	}

	/**
	 * Logging implementation that uses <code>org.apache.commons.logging.Log</code>.
	 */

	private static class CommonsLog
		implements Log {

		//
		// Private members
		//

		private org.apache.commons.logging.Log	mLog;

		//
		// Constructor
		//

		public CommonsLog( Class<?> clazz ) {

			mLog = LogFactory.getLog( clazz );
		}

		//
		// Methods
		//

		public boolean isTraceEnabled() {

			return mLog.isTraceEnabled();
		}

		public void trace( String trace, Object... arguments ) {

			String logged = trace;

			if ( isTraceEnabled() ) {
				int lastArgument = arguments.length - 1;

				if ( lastArgument == -1 ) {
					mLog.trace( logged );
				} else {
					if ( arguments[lastArgument] instanceof Throwable && trace.indexOf( "{" + lastArgument + "}" ) == -1 ) {
						if ( lastArgument > 0 ) {
							logged = MessageFormat.format( logged, arguments );
						}
						mLog.trace( logged, (Throwable) arguments[lastArgument] );
						lastArgument--;
					} else {
						logged = MessageFormat.format( logged, arguments );
						mLog.trace( logged );
					}

					if ( lastArgument != -1 && trace.indexOf( "{" + lastArgument + "}" ) == -1 ) {
						throw new RuntimeException( "Given " + ( lastArgument + 1 ) + " arguments to log, but no {" + lastArgument + "} in message '" + trace + "'" );
					}
				}
			}

			LAST_TRACE_MESSAGE = logged;
			LAST_TRACE_ARGUMENTS = arguments;
		}

		public boolean isDebugEnabled() {

			return mLog.isDebugEnabled();
		}

		public void debug( String debug, Object... arguments ) {

			String logged = debug;

			if ( isDebugEnabled() ) {
				int lastArgument = arguments.length - 1;

				if ( lastArgument == -1 ) {
					mLog.debug( logged );
				} else {
					if ( arguments[lastArgument] instanceof Throwable && debug.indexOf( "{" + lastArgument + "}" ) == -1 ) {
						if ( lastArgument > 0 ) {
							logged = MessageFormat.format( logged, arguments );
						}
						mLog.debug( logged, (Throwable) arguments[lastArgument] );
						lastArgument--;
					} else {
						logged = MessageFormat.format( logged, arguments );
						mLog.debug( logged );
					}

					if ( lastArgument != -1 && debug.indexOf( "{" + lastArgument + "}" ) == -1 ) {
						throw new RuntimeException( "Given " + ( lastArgument + 1 ) + " arguments to log, but no {" + lastArgument + "} in message '" + debug + "'" );
					}
				}
			}

			LAST_DEBUG_MESSAGE = logged;
			LAST_DEBUG_ARGUMENTS = arguments;
		}

		public boolean isInfoEnabled() {

			return mLog.isInfoEnabled();
		}

		public void info( String info, Object... arguments ) {

			String logged = info;

			if ( isInfoEnabled() ) {
				int lastArgument = arguments.length - 1;

				if ( lastArgument == -1 ) {
					mLog.info( logged );
				} else {
					if ( arguments[lastArgument] instanceof Throwable && info.indexOf( "{" + lastArgument + "}" ) == -1 ) {
						if ( lastArgument > 0 ) {
							logged = MessageFormat.format( logged, arguments );
						}
						mLog.info( logged, (Throwable) arguments[lastArgument] );
						lastArgument--;
					} else {
						logged = MessageFormat.format( logged, arguments );
						mLog.info( logged );
					}

					if ( lastArgument != -1 && info.indexOf( "{" + lastArgument + "}" ) == -1 ) {
						throw new RuntimeException( "Given " + ( lastArgument + 1 ) + " arguments to log, but no {" + lastArgument + "} in message '" + info + "'" );
					}
				}
			}

			LAST_INFO_MESSAGE = logged;
		}

		public boolean isWarnEnabled() {

			return mLog.isWarnEnabled();
		}

		public void warn( String warning, Object... arguments ) {

			String logged = warning;

			if ( isWarnEnabled() ) {
				int lastArgument = arguments.length - 1;

				if ( lastArgument == -1 ) {
					mLog.warn( logged );
				} else {
					if ( arguments[lastArgument] instanceof Throwable && warning.indexOf( "{" + lastArgument + "}" ) == -1 ) {
						if ( lastArgument > 0 ) {
							logged = MessageFormat.format( logged, arguments );
						}
						mLog.warn( logged, (Throwable) arguments[lastArgument] );
						lastArgument--;
					} else {
						logged = MessageFormat.format( logged, arguments );
						mLog.warn( logged );
					}

					if ( lastArgument != -1 && warning.indexOf( "{" + lastArgument + "}" ) == -1 ) {
						throw new RuntimeException( "Given " + ( lastArgument + 1 ) + " arguments to log, but no {" + lastArgument + "} in message '" + warning + "'" );
					}
				}
			}

			LAST_WARN_MESSAGE = logged;
		}

		public boolean isErrorEnabled() {

			return mLog.isErrorEnabled();
		}

		public void error( String error, Object... arguments ) {

			if ( !isErrorEnabled() ) {
				return;
			}

			String logged = error;
			int lastArgument = arguments.length - 1;

			if ( lastArgument == -1 ) {
				mLog.error( logged );
			} else {
				if ( arguments[lastArgument] instanceof Throwable && error.indexOf( "{" + lastArgument + "}" ) == -1 ) {
					if ( lastArgument > 0 ) {
						logged = MessageFormat.format( logged, arguments );
					}
					mLog.error( logged, (Throwable) arguments[lastArgument] );
					lastArgument--;
				} else {
					logged = MessageFormat.format( logged, arguments );
					mLog.error( logged );
				}

				if ( lastArgument != -1 && error.indexOf( "{" + lastArgument + "}" ) == -1 ) {
					throw new RuntimeException( "Given " + ( lastArgument + 1 ) + " arguments to log, but no {" + lastArgument + "} in message '" + error + "'" );
				}
			}

			LAST_ERROR_MESSAGE = logged;
		}
	}

	//
	// Private constructor
	//

	private LogUtils() {

		// Can never be called
	}
}
