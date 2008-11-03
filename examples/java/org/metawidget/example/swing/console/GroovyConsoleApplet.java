package org.metawidget.example.swing.console;

import groovy.ui.Console;

import javax.swing.JApplet;
import javax.swing.JTextPane;

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
		mConsole = new Console();
		mConsole.run( this );

		// Initialize the Console with a script

		final String script = getParameter( "script" );

		if ( script != null )
		{
			//SwingUtilities.invokeLater( new Runnable()
			//{
				//public void run()
				//{
					JTextPane inputArea = mConsole.getInputArea();
					inputArea.setText( script );
					inputArea.setCaretPosition( 0 );
				//}
			//} );
		}
	}

	@Override
	public void stop()
	{
		mConsole.exit();
	}
}
