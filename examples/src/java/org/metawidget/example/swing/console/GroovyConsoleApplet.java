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

package org.metawidget.example.swing.console;

import groovy.ui.Console;

import java.util.regex.Matcher;

import javax.swing.JApplet;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * @author Richard Kennard
 */

public class GroovyConsoleApplet
	extends JApplet
{
	//
	// Private statics
	//

	private final static long	serialVersionUID	= 1L;

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
		//
		// Note: we tried <param name="java_arguments" value="-Djnlp.packEnabled=true
		// -Dswing.defaultlaf=com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"/>
		// but that didn't seem to work?

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
