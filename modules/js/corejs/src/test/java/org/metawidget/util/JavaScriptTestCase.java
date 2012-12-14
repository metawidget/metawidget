package org.metawidget.util;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;

import junit.framework.TestCase;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.tools.shell.Global;
import org.mozilla.javascript.tools.shell.Main;

/**
 * Utility class to load Rhino and Env.js, and run a JavaScript test case.
 */

public abstract class JavaScriptTestCase
	extends TestCase {

	//
	// Private members
	//

	private Context	mContext;

	private Global	mScope;

	//
	// Protected methods
	//

	// TODO: webjars and CDNs

	/**
	 * Runs all tests defined in the given file. Tests must be defined as methods of a 'tests'
	 * object.
	 */

	protected void run( String filename ) {

		evaluateJavaScript( filename );
		NativeObject tests = evaluateString( "tests" );

		for ( Object function : tests.getAllIds() ) {

			try {
				evaluateString( "tests." + function + "()" );
			} catch ( Exception e ) {
				fail( function + ": " + e.getMessage() );
			}
		}
	}

	/**
	 * Prepare Rhino and load some common scripts.
	 */

	@Override
	protected void setUp() {

		mContext = ContextFactory.getGlobal().enterContext();
		mScope = Main.getGlobal();
		mScope.init( mContext );
		mContext.setOptimizationLevel( -1 );
		mContext.setLanguageVersion( Context.VERSION_1_5 );

		evaluateResource( "/js/env.rhino.1.2.js" );
		evaluateResource( "/js/metawidget-testutils.js" );
	}

	/**
	 * Exit Rhino.
	 */

	@Override
	protected void tearDown() {

		Context.exit();
	}

	/**
	 * Evaluate the given Javascript file.
	 *
	 * @param filename
	 *            the filename. File path is relative to the project root
	 */

	protected void evaluateJavaScript( String filename ) {

		try {
			mContext.evaluateReader( mScope, new FileReader( filename ), filename, 0, null );
		} catch ( Exception e ) {
			throw new RuntimeException( e );
		}
	}

	/**
	 * Evaluate the given HTML file.
	 *
	 * @param filename
	 *            the filename. File path is relative to the project root
	 */

	protected void evaluateHtml( String filename ) {

		String absolutePath = "file:///" + new File( filename ).getAbsolutePath().replace( '\\', '/' );
		evaluateString( "window.location = '" + absolutePath + "'" );
	}

	//
	// Private methods
	//

	/**
	 * Evaluate the given resource.
	 */

	private void evaluateResource( String resource ) {

		try {
			mContext.evaluateReader( mScope, new InputStreamReader( getClass().getResourceAsStream( resource ) ), resource, 0, null );
		} catch ( Exception e ) {
			throw new RuntimeException( e );
		}
	}

	@SuppressWarnings( "unchecked" )
	private <T> T evaluateString( String toEvaluate ) {

		return (T) mContext.evaluateString( mScope, toEvaluate, toEvaluate, 1, null );
	}
}