// Metawidget Examples (licensed under BSD License)
//
// Copyright (c) Richard Kennard
// All rights reserved
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
// * Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
// this list of conditions and the following disclaimer in the documentation
// and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
// be used to endorse or promote products derived from this software without
// specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
// OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
// OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
// OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

package org.metawidget.integrationtest.swing.xsd;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MainTest
	extends TestCase {

	//
	// Public methods
	//

	public void testFrame() {

		JFrame frame = Main.createMainFrame();
		SwingMetawidget metawidget = (SwingMetawidget) frame.getContentPane().getComponent( 0 );
		Element root = ( (Document) metawidget.getToInspect() ).getDocumentElement();

		// Test loading

		assertEquals( "Richard", XmlUtils.getChildNamed( root, "name" ).getTextContent() );
		JTextField name = (JTextField) metawidget.getComponent( 1 );
		assertEquals( "Richard", name.getText() );
		assertEquals( "Kennard Consulting", XmlUtils.getChildNamed( root, "manufacturer" ).getTextContent() );
		JTextField manufacturer = (JTextField) metawidget.getComponent( 3 );
		assertEquals( "Kennard Consulting", manufacturer.getText() );
		assertEquals( null, XmlUtils.getChildNamed( root, "model" ) );
		JSpinner model = (JSpinner) metawidget.getComponent( 5 );
		JButton save = (JButton) metawidget.getComponent( 10 );

		// Test saving

		name.setText( "Richard1" );
		manufacturer.setText( "Kennard Consulting1" );
		model.setValue( 5 );
		save.doClick();

		assertEquals( "Richard1", XmlUtils.getChildNamed( root, "name" ).getTextContent() );
		assertEquals( "Kennard Consulting1", XmlUtils.getChildNamed( root, "manufacturer" ).getTextContent() );
		assertEquals( "5", XmlUtils.getChildNamed( root, "model" ).getTextContent() );
	}
}