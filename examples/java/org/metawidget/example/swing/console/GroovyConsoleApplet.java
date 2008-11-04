package org.metawidget.example.swing.console;

import groovy.ui.Console;

import java.util.regex.Matcher;

import javax.swing.JApplet;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class GroovyConsoleApplet
	extends JApplet
{
	//
	// Private statics
	//

	private static final long	serialVersionUID	= 1L;

	//
	// Package members
	//

	Console						mConsole;

	//
	// Public methods
	//

	@Override
	public void start()
	{
		// Nimbus look and feel (if available)

		try
		{
			for ( LookAndFeelInfo info : UIManager.getInstalledLookAndFeels() )
			{
				if ( "Nimbus".equals( info.getName() ) )
				{
					UIManager.setLookAndFeel( info.getClassName() );
					break;
				}
			}
		}
		catch ( Exception e )
		{
			// Okay to fail
		}

		// Create console

		mConsole = new Console();
		mConsole.run( this );

		// Initialize the Console with a script

		String script = getParameter( "script" );

		if ( script != null )
		{
			// (applet PARAM tag does not support newlines, so we roll our own convention)

			script = script.replaceAll( Matcher.quoteReplacement( "\\n" ), "\n" );
			script = script.replaceAll( Matcher.quoteReplacement( "\\t" ), "\t" );

			final String parsedScript = script;

			// (crashes unless we use .invokeLater)

			SwingUtilities.invokeLater( new Runnable()
			{
				public void run()
				{
					JTextPane inputArea = mConsole.getInputArea();
					inputArea.setText( parsedScript );
					inputArea.setCaretPosition( 0 );
				}
			} );
		}
	}

	@Override
	public void stop()
	{
		mConsole.exit();
	}
}
