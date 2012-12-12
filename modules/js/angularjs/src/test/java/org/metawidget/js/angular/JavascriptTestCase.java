package org.metawidget.js.angular;

import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

public abstract class JavascriptTestCase
	extends TestCase {

	//
	// Private members
	//

	private Context		mContext;

	private Scriptable	mScope;

	//
	// Constructor
	//

	public JavascriptTestCase() {

		mContext = Context.enter();
		mScope = mContext.initStandardObjects();

		//evaluateReader( "src/test/js/angular.min.js" );
		//evaluateReader( "target/metawidget-angularjs/lib/metawidget/angular/metawidget-angular.js" );
		//evaluateReader( "target/metawidget-angularjs/lib/metawidget/core/metawidget.js" );
		//evaluateReader( "target/metawidget-angularjs/lib/metawidget/core/metawidget-inspectors.js" );
		//evaluateReader( "target/metawidget-angularjs/lib/metawidget/core/metawidget-widgetbuilders.js" );
		//evaluateReader( "target/metawidget-angularjs/lib/metawidget/core/metawidget-widgetprocessors.js" );
		//evaluateReader( "target/metawidget-angularjs/lib/metawidget/core/metawidget-layouts.js" );
		//evaluateReader( "target/metawidget-angularjs/lib/metawidget/core/metawidget-utils.js" );
		//evaluateReader( "target/metawidget-angularjs/lib/metawidget/angular/metawidget-angular.js" );
	}

	//
	// Protected methods
	//

	protected void run( String filename )
		throws IOException {

		mContext.evaluateReader( mScope, new FileReader( filename ), filename, 1, null );

		NativeObject tests = evaluateString( "tests" );

		for ( Object function : tests.getAllIds() ) {

			try {
				evaluateString( "tests." + function + "()" );
			} catch( Exception e ) {
				fail( e.getMessage() );
			}
		}

		Context.exit();
	}

	//
	// Private methods
	//

	private void evaluateReader( String filename ) {

		try {
			mContext.evaluateReader( mScope, new FileReader( filename ), filename, 1, null );
		} catch ( Exception e ) {
			throw new RuntimeException( e );
		}
	}

	@SuppressWarnings( "unchecked" )
	private <T> T evaluateString( String toEvaluate ) {

		return (T) Context.jsToJava( mContext.evaluateString( mScope, toEvaluate, toEvaluate, 1, null ), Object.class );
	}
}