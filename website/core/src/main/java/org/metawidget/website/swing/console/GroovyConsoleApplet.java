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

package org.metawidget.website.swing.console;

import groovy.ui.Console;

import java.util.regex.Matcher;

import javax.swing.JApplet;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class GroovyConsoleApplet
	extends JApplet {

	//
	// Package members
	//

	Console						mConsole;

	//
	// Public methods
	//

	@Override
	public void start() {

		// Nimbus look and feel (if available)
		//
		// Note: we tried <param name="java_arguments" value="-Djnlp.packEnabled=true
		// -Dswing.defaultlaf=com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"/>
		// but that didn't seem to work?

		try {
			for ( LookAndFeelInfo info : UIManager.getInstalledLookAndFeels() ) {
				if ( "Nimbus".equals( info.getName() ) ) {
					UIManager.setLookAndFeel( info.getClassName() );
					break;
				}
			}
		} catch ( Exception e ) {
			// Okay to fail
		}

		// Create console

		mConsole = new Console();
		mConsole.run( this );

		// Initialize the Console with a script

		String script = getParameter( "script" );

		if ( script != null ) {
			// (applet PARAM tag does not support newlines, so we roll our own convention)

			script = script.replaceAll( Matcher.quoteReplacement( "\\n" ), "\n" );
			script = script.replaceAll( Matcher.quoteReplacement( "\\t" ), "\t" );

			final String parsedScript = script;

			// (crashes unless we use .invokeLater)

			SwingUtilities.invokeLater( new Runnable() {

				@Override
				public void run() {

					JTextPane inputArea = mConsole.getInputArea();
					inputArea.setText( parsedScript );
					inputArea.setCaretPosition( 0 );
				}
			} );
		}
	}

	@Override
	public void stop() {

		mConsole.exit();
	}
}
