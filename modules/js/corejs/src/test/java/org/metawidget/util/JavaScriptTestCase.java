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