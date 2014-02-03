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
//   this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
//   be used to endorse or promote products derived from this software without
//   specific prior written permission.
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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;

import org.metawidget.config.impl.SimpleResourceResolver;
import org.metawidget.inspectionresultprocessor.xsd.XmlSchemaToJavaTypeMappingProcessor;
import org.metawidget.inspector.wsdl.WsdlInspector;
import org.metawidget.inspector.xsd.XmlSchemaInspectorConfig;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;

public class Main {

	//
	// Public statics
	//

	public static void main( String[] args ) {

		JFrame frame = createMainFrame();
		frame.setVisible( true );
	}

	//
	// Private statics
	//

	/**
	 * Refactored out for unit tests.
	 */

	/*package private*/static JFrame createMainFrame() {

		// Metawidget (an OIM)

		final SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new WsdlInspector(
				new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver()
						.openResource( "org/metawidget/integrationtest/swing/xsd/endorsementSearch.wsdl" ) ) ) );
		metawidget.addInspectionResultProcessor( new XmlSchemaToJavaTypeMappingProcessor<SwingMetawidget>() );
		metawidget.addWidgetProcessor( new DocumentBindingProcessor() );
		String xml = "<endorsingBoarder><name>Richard</name><manufacturer>Kennard Consulting</manufacturer></endorsingBoarder>";

		// Domain model (the 'O' in 'OIM')

		final Document document = XmlUtils.documentFromString( xml );
		metawidget.setToInspect( document );
		metawidget.setPath( "GetEndorsingBoarder" );

		// UI (the 'I' in 'OIM')

		JFrame frame = new JFrame( "Metawidget WSDL" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add( metawidget );
		frame.setSize( 400, 210 );
		metawidget.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );

		metawidget.add( new JButton( new AbstractAction( "Save" ) {

			public void actionPerformed( ActionEvent e ) {

				metawidget.getWidgetProcessor( DocumentBindingProcessor.class ).save( metawidget );
				System.out.println( XmlUtils.documentToString( document, true ) );
			}
		} ) );

		return frame;
	}
}