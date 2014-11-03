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
 * Utility class to load Rhino, Envjs, JQuery and Jasmine, and run a Jasmine test case. Designed to
 * integrate into JUnit and Maven.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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

	protected Object evaluateJavaScript( String filename ) {

		try {
			return mContext.evaluateReader( mScope, new FileReader( filename ), filename, 1, null );
		} catch ( Exception e ) {
			throw new RuntimeException( e );
		}
	}

	/**
	 * Evaluate the given HTML file.
	 *
	 * @param url
	 *            or filename
	 *            the url or filename. If the latter, file path is relative to the project root
	 */

	protected void evaluateHtml( String filename ) {

		// Use EnvJS/JQuery for HTML support

		initializeEnvJs();

		String absolutePath = filename;

		if ( !absolutePath.startsWith( "http://" ) ) {

			// Load the page relative to the project root. On Windows, we need to hardcode triple
			// forward slash. Envjs defaults to a double forward slash for some reason

			absolutePath = "file:///" + new File( filename ).getAbsolutePath().replace( '\\', '/' );
		}

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

		// Hack for unit testing Web Components

		evaluateString( "var _registeredElements = {}; HTMLElement.prototype.ownerDocument = document; document.registerElement = function( name, element ) { _registeredElements[name] = element }; document.getRegisteredElement = function( name ) { return _registeredElements[name] }" );

		// JQuery is not strictly needed here, but we saw strange side effects trying to initialize
		// it later

		evaluateResource( "/js/jquery-1.8.3.min.js" );

		// Tell Envjs to load external scripts found in any page loaded by 'window.location'

		evaluateString( "Envjs.scriptTypes['text/javascript'] = true" );
	}

	//
	// Private methods
	//

	@SuppressWarnings( "unchecked" )
	private <T> T evaluateString( String toEvaluate ) {

		try {
			return (T) mContext.evaluateString( mScope, toEvaluate, toEvaluate, 1, null );
		} catch ( JavaScriptException e ) {
			throw new RuntimeException( e.getMessage(), e );
		}
	}
}