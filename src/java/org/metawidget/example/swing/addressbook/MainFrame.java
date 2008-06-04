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

package org.metawidget.example.swing.addressbook;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

/**
 * @author Richard Kennard
 */

public class MainFrame
{
	//
	//
	// Public statics
	//
	//

	public static void main( String[] args )
	{
		JFrame frame = new JFrame( "Address Book (Metawidget Swing Example)" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().setBackground( Color.white );

		// Background

		ImagePanel backgroundPanel = new ImagePanel();
		backgroundPanel.setImage( ClassLoader.getSystemResource( "org/metawidget/example/shared/addressbook/media/background.jpg" ) );
		backgroundPanel.setLayout( new BorderLayout() );
		frame.add( backgroundPanel );

		// Create AddressBook

		new AddressBook( backgroundPanel );

		// Show

		frame.pack();
		frame.setVisible( true );
	}
}
