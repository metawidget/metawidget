package org.metawidget.util;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;

import junit.framework.TestCase;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.tools.shell.Global;
import org.mozilla.javascript.tools.shell.Main;

/**
 * Utility class to load Rhino, Envjs, JQuery and Jasmine, and run a Jasmine test case.
 */

public abstract class JavaScriptTestCase
	extends TestCase {

	//
	// Private members
	//

	private Context	mContext;

	private Global	mScope;

	private boolean	mInitializedEnvJs	= false;

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
		evaluateString( "runJasmine()" );
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
		mContext.setLanguageVersion( Context.VERSION_1_7 );

		// Jasmine patched for https://github.com/pivotal/jasmine/pull/136
		evaluateResource( "/js/jasmine.1.3.1.patched.js" );

		evaluateResource( "/js/jasmine-runner.js" );
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

		// Use EnvJS/JQuery for HTML support

		initializeEnvJs();

		// Load the page relative to the project root. On Windows, we need to hardcode triple
		// forward slash. Envjs defaults to a double forward slash for some reason

		String absolutePath = "file:///" + new File( filename ).getAbsolutePath().replace( '\\', '/' );
		evaluateString( "window.location = '" + absolutePath + "'" );
	}

	/**
	 * Evaluate the given resource.
	 */

	protected void evaluateResource( String resource ) {

		try {
			mContext.evaluateReader( mScope, new InputStreamReader( getClass().getResourceAsStream( resource ) ), resource, 1, null );
		} catch ( Exception e ) {
			throw new RuntimeException( e );
		}
	}

	protected void initializeEnvJs() {

		if ( mInitializedEnvJs ) {
			return;
		}

		mInitializedEnvJs = true;

		evaluateResource( "/js/env.rhino.1.2.js" );
		evaluateResource( "/js/jquery-1.8.3.js" );

		// Tell Envjs to load external scripts found in the page

		evaluateString( "Envjs.scriptTypes['text/javascript'] = true" );
	}

	//
	// Private methods
	//

	@SuppressWarnings( "unchecked" )
	private <T> T evaluateString( String toEvaluate ) {

		try {
			return (T) mContext.evaluateString( mScope, toEvaluate, toEvaluate, 1, null );
		} catch( JavaScriptException e ) {
			throw new RuntimeException( e.getMessage(), e );
		}
	}
}