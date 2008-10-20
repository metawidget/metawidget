package org.metawidget.example.swing.console;

import groovy.ui.Console;

import javax.swing.JApplet;

public class GroovyConsoleApplet
	extends JApplet
{
	//
	// Private statics
	//

	private static final long	serialVersionUID	= 1L;

	//
	// Private members
	//

	private Console				mConsole;

	//
	// Public methods
	//

	@Override
	public void start()
	{
		mConsole = new Console();
		mConsole.getInputArea().setText( getParameter( "script" ) );
		mConsole.run();
	}

	@Override
	public void stop()
	{
		mConsole.exit();
	}
}
