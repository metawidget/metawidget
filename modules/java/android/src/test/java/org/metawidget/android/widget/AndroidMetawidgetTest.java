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

package org.metawidget.android.widget;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.android.AndroidMetawidgetTests.MockAttributeSet;
import org.metawidget.android.widget.layout.LinearLayout;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor;
import org.metawidget.inspectionresultprocessor.sort.ComesAfterInspectionResultProcessor;
import org.metawidget.util.CollectionUtils;

import android.widget.EditText;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class AndroidMetawidgetTest
	extends TestCase {

	//
	// Public methods
	//

	public void testGettersAndSetters() {

		// setToInspect and getPath

		AndroidMetawidget androidMetawidget = new AndroidMetawidget( null );
		androidMetawidget.setToInspect( new Date() );
		assertEquals( "java.util.Date", androidMetawidget.getPath() );
		androidMetawidget.setPath( "foo" );
		assertEquals( "foo", androidMetawidget.getPath() );

		// getLabelString and getLocalizedKey

		assertEquals( "", androidMetawidget.getLabelString( null ) );

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( LABEL, "camelCase" );
		assertEquals( "camelCase", androidMetawidget.getLabelString( attributes ) );
		attributes.put( NAME, "foo" );
		assertEquals( "camelCase", androidMetawidget.getLabelString( attributes ) );
		attributes.remove( LABEL );
		assertEquals( "Foo", androidMetawidget.getLabelString( attributes ) );
		attributes.put( NAME, "fooBar" );
		assertEquals( "Foo Bar", androidMetawidget.getLabelString( attributes ) );
		attributes.put( LABEL, "" );
		assertEquals( null, androidMetawidget.getLabelString( attributes ) );

		assertEquals( null, androidMetawidget.getLocalizedKey( null ) );
		assertEquals( null, androidMetawidget.getLocalizedKey( "" ) );

		// clientProperties

		assertEquals( null, androidMetawidget.getClientProperty( "foo" ) );
		androidMetawidget.putClientProperty( "foo", "bar" );
		assertEquals( "bar", androidMetawidget.getClientProperty( "foo" ) );

		// maximumInspectionDepth

		assertEquals( 10, androidMetawidget.getMaximumInspectionDepth() );
		androidMetawidget.setMaximumInspectionDepth( 2 );
		assertEquals( 2, androidMetawidget.getMaximumInspectionDepth() );

		// readOnly

		assertFalse( androidMetawidget.isReadOnly() );
		androidMetawidget.setReadOnly( true );
		assertTrue( androidMetawidget.isReadOnly() );

		MockAttributeSet mockAttributeSet = new MockAttributeSet();
		androidMetawidget = new AndroidMetawidget( null, mockAttributeSet );
		assertFalse( androidMetawidget.isReadOnly() );

		mockAttributeSet.setAttributeValue( "readOnly", "false" );
		androidMetawidget = new AndroidMetawidget( null, mockAttributeSet );
		assertFalse( androidMetawidget.isReadOnly() );

		mockAttributeSet.setAttributeValue( "readOnly", "true" );
		androidMetawidget = new AndroidMetawidget( null, mockAttributeSet );
		assertTrue( androidMetawidget.isReadOnly() );
	}

	public void testGetSetValue() {

		AndroidMetawidget androidMetawidget = new AndroidMetawidget( null );

		try {
			androidMetawidget.getValue();
			fail();
		} catch ( MetawidgetException e ) {
			assertEquals( "No names specified", e.getMessage() );
		}

		try {
			androidMetawidget.getValue( "foo" );
			fail();
		} catch ( MetawidgetException e ) {
			assertEquals( "No View with tag foo", e.getMessage() );
		}

		try {
			androidMetawidget.setValue( 2 );
			fail();
		} catch ( MetawidgetException e ) {
			assertEquals( "No names specified", e.getMessage() );
		}

		try {
			androidMetawidget.setValue( 2, "foo" );
			fail();
		} catch ( MetawidgetException e ) {
			assertEquals( "No View with tag foo", e.getMessage() );
		}

		EditText editText = new EditText( null );
		editText.setTag( "foo" );
		editText.setText( "Bar" );
		androidMetawidget.addView( editText );
		assertEquals( "Bar", androidMetawidget.getValue( "foo" ) );
		androidMetawidget.setValue( "Baz", "foo" );
		assertEquals( "Baz", androidMetawidget.getValue( "foo" ) );
	}

	public void testAutomaticBuildWidgets() {

		AndroidMetawidget androidMetawidget = new AndroidMetawidget( null );
		androidMetawidget.setLayout( new LinearLayout() );
		androidMetawidget.setToInspect( new Foo() );
		assertEquals( 2, androidMetawidget.getChildCount() );

		androidMetawidget = new AndroidMetawidget( null );
		androidMetawidget.setLayout( new LinearLayout() );
		androidMetawidget.setToInspect( new Foo() );
		assertTrue( androidMetawidget.getChildAt( 1 ) instanceof EditText );

		// Cannot override findViewWithTag - is marked final!
	}

	public void testGetInspectionResultProcessor() {

		MyInspectionResultProcessor myInspectionResultProcessor = new MyInspectionResultProcessor();

		@SuppressWarnings( "unchecked" )
		InspectionResultProcessor<AndroidMetawidget>[] inspectionResultProcessors = new InspectionResultProcessor[] {
				new ComesAfterInspectionResultProcessor<AndroidMetawidget>(),
				myInspectionResultProcessor
		};

		AndroidMetawidget androidMetawidget = new AndroidMetawidget( null );
		androidMetawidget.setInspectionResultProcessors( inspectionResultProcessors );

		assertEquals( myInspectionResultProcessor, androidMetawidget.getInspectionResultProcessor( MyInspectionResultProcessor.class ) );
	}

	public void testConfigureOnce() {

		final List<String> configuredDefaults = new ArrayList<String>();

		AndroidMetawidget androidMetawidget = new AndroidMetawidget( null ) {

			@Override
			protected void configureDefaults() {

				configuredDefaults.add( "called" );
				super.configureDefaults();
			}
		};

		androidMetawidget.setToInspect( new Foo() );
		androidMetawidget.buildWidgets();
		androidMetawidget.setToInspect( new Foo() );
		androidMetawidget.buildWidgets();

		assertEquals( 1, configuredDefaults.size() );
	}

	//
	// Inner class
	//

	public static class Foo {

		public String getName() {

			return null;
		}

		public void setName( @SuppressWarnings( "unused" ) String name ) {

			// Do nothing
		}
	}

	/* pacakge private */static class MyInspectionResultProcessor
		implements InspectionResultProcessor<AndroidMetawidget> {

		//
		// Public methods
		//

		public String processInspectionResult( String inspectionResult, AndroidMetawidget metawidget, Object toInspect, String type, String... names ) {

			return null;
		}
	}
}
