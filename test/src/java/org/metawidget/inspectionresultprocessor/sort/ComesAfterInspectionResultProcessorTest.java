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

package org.metawidget.inspectionresultprocessor.sort;

import junit.framework.TestCase;

import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessorException;
import org.metawidget.swing.SwingMetawidget;

/**
 * @author Richard Kennard
 */

public class ComesAfterInspectionResultProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testNoComesAfter()
		throws Exception {

		// Set up

		String inputXml = "<?xml version=\"1.0\"?>";
		inputXml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">";
		inputXml += "<entity type=\"Foo\">";
		inputXml += "<property name=\"foo\"/>";
		inputXml += "<property name=\"bar\"/>";
		inputXml += "<property name=\"baz\"/>";
		inputXml += "</entity></inspection-result>";

		// Run processor

		String outputXml = new ComesAfterInspectionResultProcessor<SwingMetawidget>().processInspectionResult( inputXml, null );

		// Test result

		String validateXml = "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" version=\"1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\">";
		validateXml += "<entity type=\"Foo\">";
		validateXml += "<property name=\"foo\"/>";
		validateXml += "<property name=\"bar\"/>";
		validateXml += "<property name=\"baz\"/>";
		validateXml += "</entity>";
		validateXml += "</inspection-result>";

		assertEquals( validateXml, outputXml );
	}

	public void testComesAfterAll()
		throws Exception {

		// Set up

		String inputXml = "<?xml version=\"1.0\"?>";
		inputXml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">";
		inputXml += "<entity type=\"Foo\">";
		inputXml += "<property name=\"foo\" comes-after=\"\"/>";
		inputXml += "<property name=\"bar\"/>";
		inputXml += "<property name=\"baz\"/>";
		inputXml += "</entity></inspection-result>";

		// Run processor

		String outputXml = new ComesAfterInspectionResultProcessor<SwingMetawidget>().processInspectionResult( inputXml, null );

		// Test result

		String validateXml = "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" version=\"1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\">";
		validateXml += "<entity type=\"Foo\">";
		validateXml += "<property name=\"bar\"/>";
		validateXml += "<property name=\"baz\"/>";
		validateXml += "<property name=\"foo\" comes-after=\"\"/>";
		validateXml += "</entity>";
		validateXml += "</inspection-result>";

		assertEquals( validateXml, outputXml );
	}

	public void testSingleComesAfter()
		throws Exception {

		// Set up

		String inputXml = "<?xml version=\"1.0\"?>";
		inputXml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">";
		inputXml += "<entity type=\"Foo\">";
		inputXml += "<property name=\"foo\" comes-after=\"bar\"/>";
		inputXml += "<property name=\"bar\"/>";
		inputXml += "<property name=\"baz\"/>";
		inputXml += "</entity></inspection-result>";

		// Run processor

		String outputXml = new ComesAfterInspectionResultProcessor<SwingMetawidget>().processInspectionResult( inputXml, null );

		// Test result

		String validateXml = "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" version=\"1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\">";
		validateXml += "<entity type=\"Foo\">";
		validateXml += "<property name=\"bar\"/>";
		validateXml += "<property name=\"foo\" comes-after=\"bar\"/>";
		validateXml += "<property name=\"baz\"/>";
		validateXml += "</entity>";
		validateXml += "</inspection-result>";

		assertEquals( validateXml, outputXml );
	}

	public void testMultipleComesAfter()
		throws Exception {

		// Set up

		String inputXml = "<?xml version=\"1.0\"?>";
		inputXml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">";
		inputXml += "<entity type=\"Foo\">";
		inputXml += "<property name=\"foo\" comes-after=\"bar,baz\"/>";
		inputXml += "<property name=\"bar\"/>";
		inputXml += "<property name=\"baz\"/>";
		inputXml += "<property name=\"abc\"/>";
		inputXml += "</entity></inspection-result>";

		// Run processor

		String outputXml = new ComesAfterInspectionResultProcessor<SwingMetawidget>().processInspectionResult( inputXml, null );

		// Test result

		String validateXml = "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" version=\"1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\">";
		validateXml += "<entity type=\"Foo\">";
		validateXml += "<property name=\"bar\"/>";
		validateXml += "<property name=\"baz\"/>";
		validateXml += "<property name=\"foo\" comes-after=\"bar,baz\"/>";
		validateXml += "<property name=\"abc\"/>";
		validateXml += "</entity>";
		validateXml += "</inspection-result>";

		assertEquals( validateXml, outputXml );
	}

	public void testNonDeterministicComesAfter()
		throws Exception {

		// Set up

		String inputXml = "<?xml version=\"1.0\"?>";
		inputXml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">";
		inputXml += "<entity type=\"Foo\">";
		inputXml += "<property name=\"foo\" comes-after=\"baz\"/>";
		inputXml += "<property name=\"bar\" comes-after=\"baz\"/>";
		inputXml += "<property name=\"baz\"/>";
		inputXml += "<property name=\"abc\"/>";
		inputXml += "</entity></inspection-result>";

		// Run processor

		String outputXml = new ComesAfterInspectionResultProcessor<SwingMetawidget>().processInspectionResult( inputXml, null );

		// Test result

		String validateXml = "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" version=\"1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\">";
		validateXml += "<entity type=\"Foo\">";
		validateXml += "<property name=\"baz\"/>";
		validateXml += "<property name=\"bar\" comes-after=\"baz\"/>";
		validateXml += "<property name=\"foo\" comes-after=\"baz\"/>";
		validateXml += "<property name=\"abc\"/>";
		validateXml += "</entity>";
		validateXml += "</inspection-result>";

		assertEquals( validateXml, outputXml );
	}

	public void testIteratedComesAfter()
		throws Exception {

		// Set up

		String inputXml = "<?xml version=\"1.0\"?>";
		inputXml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">";
		inputXml += "<entity type=\"Foo\">";
		inputXml += "<property name=\"foo\" comes-after=\"bar\"/>";
		inputXml += "<property name=\"bar\" comes-after=\"abc\"/>";
		inputXml += "<property name=\"baz\"/>";
		inputXml += "<property name=\"abc\"/>";
		inputXml += "</entity></inspection-result>";

		// Run processor

		String outputXml = new ComesAfterInspectionResultProcessor<SwingMetawidget>().processInspectionResult( inputXml, null );

		// Test result

		String validateXml = "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" version=\"1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\">";
		validateXml += "<entity type=\"Foo\">";
		validateXml += "<property name=\"baz\"/>";
		validateXml += "<property name=\"abc\"/>";
		validateXml += "<property name=\"bar\" comes-after=\"abc\"/>";
		validateXml += "<property name=\"foo\" comes-after=\"bar\"/>";
		validateXml += "</entity>";
		validateXml += "</inspection-result>";

		assertEquals( validateXml, outputXml );
	}

	public void testInfiniteLoop() {

		try {
			String inputXml = "<?xml version=\"1.0\"?>";
			inputXml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">";
			inputXml += "<entity type=\"InfiniteFoo\">";
			inputXml += "<property name=\"foo\" comes-after=\"bar\"/>";
			inputXml += "<property name=\"bar\" comes-after=\"foo,baz\"/>";
			inputXml += "<property name=\"baz\" comes-after=\"\"/>";
			inputXml += "</entity></inspection-result>";

			// Run processor

			new ComesAfterInspectionResultProcessor<SwingMetawidget>().processInspectionResult( inputXml, null );
			assertTrue( false );
		} catch ( InspectionResultProcessorException e ) {
			assertEquals( "Infinite loop detected when sorting comes-after: bar comes after foo and baz, but baz comes at the end, but foo comes after bar", e.getMessage() );
		}
	}

	public void testComesAfterItself() {

		try {
			String inputXml = "<?xml version=\"1.0\"?>";
			inputXml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" version=\"1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\">";
			inputXml += "<entity type=\"Foo\">";
			inputXml += "<property name=\"bar\" comes-after=\"bar\"/>";
			inputXml += "</entity></inspection-result>";

			// Run processor

			new ComesAfterInspectionResultProcessor<SwingMetawidget>().processInspectionResult( inputXml, null );
			assertTrue( false );
		} catch ( InspectionResultProcessorException e ) {
			assertEquals( "'bar' comes-after itself", e.getMessage() );
		}
	}
}
