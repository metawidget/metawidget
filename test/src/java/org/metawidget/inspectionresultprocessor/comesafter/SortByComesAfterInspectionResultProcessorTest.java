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

package org.metawidget.inspectionresultprocessor.comesafter;

import junit.framework.TestCase;

import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessorException;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class SortByComesAfterInspectionResultProcessorTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testNoComesAfter()
		throws Exception
	{
		// Set up

		String inputXml = "<?xml version=\"1.0\"?>";
		inputXml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">";
		inputXml += "<entity type=\"Foo\">";
		inputXml += "<property name=\"foo\"/>";
		inputXml += "<property name=\"bar\"/>";
		inputXml += "<property name=\"baz\"/>";
		inputXml += "</entity></inspection-result>";

		Element inspectionResult = XmlUtils.documentFromString( inputXml ).getDocumentElement();

		// Run processor

		inspectionResult = new SortByComesAfterInspectionResultProcessor<SwingMetawidget>().processInspectionResult( inspectionResult, null );

		// Test result

		String outputXml = XmlUtils.documentToString( inspectionResult.getOwnerDocument(), true );

		String validateXml = "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">\n";
		validateXml += "   <entity type=\"Foo\">\n";
		validateXml += "      <property name=\"foo\"/>\n";
		validateXml += "      <property name=\"bar\"/>\n";
		validateXml += "      <property name=\"baz\"/>\n";
		validateXml += "   </entity>\n";
		validateXml += "</inspection-result>";

		assertTrue( validateXml.equals( outputXml ) );
	}

	public void testComesAfterAll()
		throws Exception
	{
		// Set up

		String inputXml = "<?xml version=\"1.0\"?>";
		inputXml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">";
		inputXml += "<entity type=\"Foo\">";
		inputXml += "<property name=\"foo\" comes-after=\"\"/>";
		inputXml += "<property name=\"bar\"/>";
		inputXml += "<property name=\"baz\"/>";
		inputXml += "</entity></inspection-result>";

		Element inspectionResult = XmlUtils.documentFromString( inputXml ).getDocumentElement();

		// Run processor

		inspectionResult = new SortByComesAfterInspectionResultProcessor<SwingMetawidget>().processInspectionResult( inspectionResult, null );

		// Test result

		String outputXml = XmlUtils.documentToString( inspectionResult.getOwnerDocument(), true );

		String validateXml = "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">\n";
		validateXml += "   <entity type=\"Foo\">\n";
		validateXml += "      <property name=\"bar\"/>\n";
		validateXml += "      <property name=\"baz\"/>\n";
		validateXml += "      <property name=\"foo\" comes-after=\"\"/>\n";
		validateXml += "   </entity>\n";
		validateXml += "</inspection-result>";

		assertTrue( validateXml.equals( outputXml ) );
	}

	public void testSingleComesAfter()
		throws Exception
	{
		// Set up

		String inputXml = "<?xml version=\"1.0\"?>";
		inputXml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">";
		inputXml += "<entity type=\"Foo\">";
		inputXml += "<property name=\"foo\" comes-after=\"bar\"/>";
		inputXml += "<property name=\"bar\"/>";
		inputXml += "<property name=\"baz\"/>";
		inputXml += "</entity></inspection-result>";

		Element inspectionResult = XmlUtils.documentFromString( inputXml ).getDocumentElement();

		// Run processor

		inspectionResult = new SortByComesAfterInspectionResultProcessor<SwingMetawidget>().processInspectionResult( inspectionResult, null );

		// Test result

		String outputXml = XmlUtils.documentToString( inspectionResult.getOwnerDocument(), true );

		String validateXml = "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">\n";
		validateXml += "   <entity type=\"Foo\">\n";
		validateXml += "      <property name=\"bar\"/>\n";
		validateXml += "      <property name=\"foo\" comes-after=\"bar\"/>\n";
		validateXml += "      <property name=\"baz\"/>\n";
		validateXml += "   </entity>\n";
		validateXml += "</inspection-result>";

		assertTrue( validateXml.equals( outputXml ) );
	}

	public void testMultipleComesAfter()
		throws Exception
	{
		// Set up

		String inputXml = "<?xml version=\"1.0\"?>";
		inputXml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">";
		inputXml += "<entity type=\"Foo\">";
		inputXml += "<property name=\"foo\" comes-after=\"bar,baz\"/>";
		inputXml += "<property name=\"bar\"/>";
		inputXml += "<property name=\"baz\"/>";
		inputXml += "<property name=\"abc\"/>";
		inputXml += "</entity></inspection-result>";

		Element inspectionResult = XmlUtils.documentFromString( inputXml ).getDocumentElement();

		// Run processor

		inspectionResult = new SortByComesAfterInspectionResultProcessor<SwingMetawidget>().processInspectionResult( inspectionResult, null );

		// Test result

		String outputXml = XmlUtils.documentToString( inspectionResult.getOwnerDocument(), true );

		String validateXml = "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">\n";
		validateXml += "   <entity type=\"Foo\">\n";
		validateXml += "      <property name=\"bar\"/>\n";
		validateXml += "      <property name=\"baz\"/>\n";
		validateXml += "      <property name=\"foo\" comes-after=\"bar,baz\"/>\n";
		validateXml += "      <property name=\"abc\"/>\n";
		validateXml += "   </entity>\n";
		validateXml += "</inspection-result>";

		assertTrue( validateXml.equals( outputXml ) );
	}

	public void testNonDeterministicComesAfter()
		throws Exception
	{
		// Set up

		String inputXml = "<?xml version=\"1.0\"?>";
		inputXml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">";
		inputXml += "<entity type=\"Foo\">";
		inputXml += "<property name=\"foo\" comes-after=\"baz\"/>";
		inputXml += "<property name=\"bar\" comes-after=\"baz\"/>";
		inputXml += "<property name=\"baz\"/>";
		inputXml += "<property name=\"abc\"/>";
		inputXml += "</entity></inspection-result>";

		Element inspectionResult = XmlUtils.documentFromString( inputXml ).getDocumentElement();

		// Run processor

		inspectionResult = new SortByComesAfterInspectionResultProcessor<SwingMetawidget>().processInspectionResult( inspectionResult, null );

		// Test result

		String outputXml = XmlUtils.documentToString( inspectionResult.getOwnerDocument(), true );

		String validateXml = "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">\n";
		validateXml += "   <entity type=\"Foo\">\n";
		validateXml += "      <property name=\"baz\"/>\n";
		validateXml += "      <property name=\"bar\" comes-after=\"baz\"/>\n";
		validateXml += "      <property name=\"foo\" comes-after=\"baz\"/>\n";
		validateXml += "      <property name=\"abc\"/>\n";
		validateXml += "   </entity>\n";
		validateXml += "</inspection-result>";

		assertTrue( validateXml.equals( outputXml ) );
	}

	public void testIteratedComesAfter()
		throws Exception
	{
		// Set up

		String inputXml = "<?xml version=\"1.0\"?>";
		inputXml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">";
		inputXml += "<entity type=\"Foo\">";
		inputXml += "<property name=\"foo\" comes-after=\"bar\"/>";
		inputXml += "<property name=\"bar\" comes-after=\"abc\"/>";
		inputXml += "<property name=\"baz\"/>";
		inputXml += "<property name=\"abc\"/>";
		inputXml += "</entity></inspection-result>";

		Element inspectionResult = XmlUtils.documentFromString( inputXml ).getDocumentElement();

		// Run processor

		inspectionResult = new SortByComesAfterInspectionResultProcessor<SwingMetawidget>().processInspectionResult( inspectionResult, null );

		// Test result

		String outputXml = XmlUtils.documentToString( inspectionResult.getOwnerDocument(), true );

		String validateXml = "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">\n";
		validateXml += "   <entity type=\"Foo\">\n";
		validateXml += "      <property name=\"baz\"/>\n";
		validateXml += "      <property name=\"abc\"/>\n";
		validateXml += "      <property name=\"bar\" comes-after=\"abc\"/>\n";
		validateXml += "      <property name=\"foo\" comes-after=\"bar\"/>\n";
		validateXml += "   </entity>\n";
		validateXml += "</inspection-result>";

		assertTrue( validateXml.equals( outputXml ) );
	}

	public void testInfiniteLoop()
	{
		try
		{
			String inputXml = "<?xml version=\"1.0\"?>";
			inputXml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">";
			inputXml += "<entity type=\"InfiniteFoo\">";
			inputXml += "<property name=\"foo\" comes-after=\"bar\"/>";
			inputXml += "<property name=\"bar\" comes-after=\"foo\"/>";
			inputXml += "</entity></inspection-result>";

			Element inspectionResult = XmlUtils.documentFromString( inputXml ).getDocumentElement();

			// Run processor

			inspectionResult = new SortByComesAfterInspectionResultProcessor<SwingMetawidget>().processInspectionResult( inspectionResult, null );
			assertTrue( false );
		}
		catch ( InspectionResultProcessorException e )
		{
			assertTrue( "Infinite loop detected when sorting comes-after".equals( e.getMessage() ) );
		}
	}

	public void testComesAfterItself()
	{
		try
		{
			String inputXml = "<?xml version=\"1.0\"?>";
			inputXml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">";
			inputXml += "<entity type=\"Foo\">";
			inputXml += "<property name=\"bar\" comes-after=\"bar\"/>";
			inputXml += "</entity></inspection-result>";

			Element inspectionResult = XmlUtils.documentFromString( inputXml ).getDocumentElement();

			// Run processor

			inspectionResult = new SortByComesAfterInspectionResultProcessor<SwingMetawidget>().processInspectionResult( inspectionResult, null );
			assertTrue( false );
		}
		catch ( InspectionResultProcessorException e )
		{
			assertTrue( "'bar' comes-after itself".equals( e.getMessage() ) );
		}
	}
}
