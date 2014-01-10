// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.inspectionresultprocessor.sort;

import junit.framework.TestCase;

import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessorException;
import org.metawidget.util.XmlUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ComesAfterInspectionResultProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testBadInput()
		throws Exception {

		// Set up

		String inputXml = "<?xml version=\"1.0\"?>";
		inputXml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">";
		inputXml += "<form type=\"Foo\"/>";
		inputXml += "</inspection-result>";

		// Run processor

		try {
			new ComesAfterInspectionResultProcessor<Object>().processInspectionResult( inputXml, null, null, null );
			assertTrue( false );
		} catch ( InspectionResultProcessorException e ) {
			assertEquals( "Top-level element name should be entity, not form", e.getMessage() );
		}
	}

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

		String outputXml = new ComesAfterInspectionResultProcessor<Object>().processInspectionResult( inputXml, null, null, null );

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

		String outputXml = new ComesAfterInspectionResultProcessor<Object>().processInspectionResult( inputXml, null, null, null );

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

		String outputXml = new ComesAfterInspectionResultProcessor<Object>().processInspectionResult( inputXml, null, null, null );

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

		String outputXml = new ComesAfterInspectionResultProcessor<Object>().processInspectionResult( inputXml, null, null, null );

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

		String outputXml = new ComesAfterInspectionResultProcessor<Object>().processInspectionResult( inputXml, null, null, null );

		// Test result

		String validateXml = "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" version=\"1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\">";
		validateXml += "<entity type=\"Foo\">";
		validateXml += "<property name=\"baz\"/>";
		validateXml += "<property name=\"foo\" comes-after=\"baz\"/>";
		validateXml += "<property name=\"bar\" comes-after=\"baz\"/>";
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

		String outputXml = new ComesAfterInspectionResultProcessor<Object>().processInspectionResult( inputXml, null, null, null );

		// Test result

		String validateXml = "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" version=\"1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\">";
		validateXml += "<entity type=\"Foo\">";
		validateXml += "<property name=\"abc\"/>";
		validateXml += "<property name=\"bar\" comes-after=\"abc\"/>";
		validateXml += "<property name=\"foo\" comes-after=\"bar\"/>";
		validateXml += "<property name=\"baz\"/>";
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

			new ComesAfterInspectionResultProcessor<Object>().processInspectionResult( inputXml, null, null, null );
			fail();
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

			new ComesAfterInspectionResultProcessor<Object>().processInspectionResult( inputXml, null, null, null );
			fail();
		} catch ( InspectionResultProcessorException e ) {
			assertEquals( "'bar' comes-after itself", e.getMessage() );
		}
	}

	public void testPrettyXml()
		throws Exception {

		// Set up

		String inputXml = "<?xml version=\"1.0\"?>";
		inputXml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">";
		inputXml += "<entity type=\"Foo\">";
		inputXml += "<property name=\"foo\"/>";
		inputXml += "<property name=\"bar\"/>";
		inputXml += "<property name=\"baz\"/>";
		inputXml += "</entity></inspection-result>";

		String prettyXml = XmlUtils.documentToString( XmlUtils.documentFromString( inputXml ), true );

		// Run processor

		String outputXml = new ComesAfterInspectionResultProcessor<Object>().processInspectionResult( prettyXml, null, null, null );

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
}
