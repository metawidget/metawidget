// Metawidget (licensed under LGPL)
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

package org.metawidget.android.widget;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.android.AndroidMetawidgetTests.MockAttributeSet;
import org.metawidget.android.widget.layout.LinearLayout;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.util.CollectionUtils;

import android.widget.EditText;

/**
 * @author Richard Kennard
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
}
