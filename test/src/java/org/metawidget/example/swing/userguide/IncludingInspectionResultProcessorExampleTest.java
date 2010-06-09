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

package org.metawidget.example.swing.userguide;

import static org.metawidget.inspector.InspectionResultConstants.*;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import junit.framework.TestCase;

import org.metawidget.example.swing.tutorial.Person;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class IncludingInspectionResultProcessorExampleTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testIncludingInspectionResultProcessorExample()
		throws Exception
	{
		Person person = new Person();

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.addInspectionResultProcessor( new IncludingInspectionResultProcessor() );
		metawidget.putClientProperty( "include", new String[]{ "age", "retired" } );
		metawidget.setToInspect( person );

		assertTrue( metawidget.getComponent( 0 ) instanceof JLabel );
		assertTrue( metawidget.getComponent( 1 ) instanceof JSpinner );
		assertEquals( "age", metawidget.getComponent( 1 ).getName() );
		assertTrue( metawidget.getComponent( 2 ) instanceof JLabel );
		assertTrue( metawidget.getComponent( 3 ) instanceof JCheckBox );
		assertEquals( "retired", metawidget.getComponent( 3 ).getName() );
		assertTrue( metawidget.getComponent( 4 ) instanceof JPanel );
		assertTrue( 5 == metawidget.getComponentCount() );
	}

	//
	// Inner class
	//

	static class IncludingInspectionResultProcessor
		implements InspectionResultProcessor<SwingMetawidget>
	{
		@Override
		public String processInspectionResult( String inspectionResult, SwingMetawidget metawidget )
		{
			String[] includes = (String[]) metawidget.getClientProperty( "include" );
			Document document = XmlUtils.documentFromString( inspectionResult );
			Element entity = (Element) document.getDocumentElement().getFirstChild();
			int propertiesToCleanup = entity.getChildNodes().getLength();

			// Pull out the names in order

			for( String include : includes )
			{
				Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, include );

				if ( property == null )
				{
					continue;
				}

				entity.appendChild( property );
				propertiesToCleanup--;
			}

			// Cleanup the rest

			for( int loop = 0; loop < propertiesToCleanup; loop++ )
			{
				entity.removeChild( entity.getFirstChild() );
			}

			return XmlUtils.documentToString( document, false );
		}
	}
}
